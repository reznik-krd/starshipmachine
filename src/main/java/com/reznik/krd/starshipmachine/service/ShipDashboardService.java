package com.reznik.krd.starshipmachine.service;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.repository.ShipEventLogRepository;
import com.reznik.krd.starshipmachine.repository.SpaceshipRepository;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import com.reznik.krd.starshipmachine.statemachine.service.StarshipStateMachineService;
import com.reznik.krd.starshipmachine.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.reznik.krd.starshipmachine.util.Constants.DAMAGE_REASON;
import static com.reznik.krd.starshipmachine.util.Constants.EVACUATION_MESSAGE;

@Service
@RequiredArgsConstructor
public class ShipDashboardService {

    private final SpaceshipRepository spaceshipRepository;
    private final ShipEventLogRepository logRepository;
    private final StarshipStateMachineService machineService;

    @Transactional(readOnly = true)
    public List<ShipSummaryResponse> summaries() {
        return spaceshipRepository.findAll().stream()
                .map(spaceship -> {
                    StateMachine<ShipState, ShipEvent> stateMachine = machineService.acquireByShipId(spaceship.getId());
                    String currentState = stateMachine.getState().getId().name();

                    return new ShipSummaryResponse(
                            spaceship.getId(),
                            spaceship.getName(),
                            currentState,
                            spaceship.getHull(),
                            spaceship.getCrew()
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public ShipDashboardResponse dashboard(UUID shipId) {
        Spaceship ship = spaceshipRepository.findById(shipId)
                .orElseThrow(() -> new IllegalArgumentException("Spaceship not found: " + shipId));

        StateMachine<ShipState, ShipEvent> machine = machineService.acquireByShipId(shipId);
        ShipState state = machine.getState().getId();
        Set<ShipEvent> events = getAvailableEventsForUI(machine);

        List<ShipActionResponse> actions = events.stream()
                .map(e -> new ShipActionResponse(
                                e.name(),
                                e.getTitle(),
                                e.getDescription(),
                                isIncident(e)
                        )
                )
                .toList();

        List<ShipEventLogResponse> logs = logRepository
                .findTop30BySpaceshipIdOrderByCreatedAtDesc(shipId)
                .stream()
                .map(l -> new ShipEventLogResponse(
                                l.getEvent(),
                                l.getSourceState(),
                                l.getTargetState(),
                                l.getMessage(),
                                l.getCreatedAt()
                        )
                )
                .toList();

        Object reason = machine.getExtendedState().getVariables().get(DAMAGE_REASON);
        Object evacuation = machine.getExtendedState().getVariables().get(EVACUATION_MESSAGE);

        return new ShipDashboardResponse(
                ship.getId(),
                ship.getName(),
                state.name(),
                state.getDescription(),
                ship.getHull(),
                ship.getCrew(),
                reason == null ? null : reason.toString(),
                evacuation == null ? null : evacuation.toString(),
                actions,
                logs
        );
    }

    private boolean isIncident(ShipEvent e) {
        return e == ShipEvent.METEOR_SHOWER
                || e == ShipEvent.ENGINE_FAILURE
                || e == ShipEvent.MARTIAN_ATTACK;
    }

    public Set<ShipEvent> getAvailableEventsForUI(StateMachine<ShipState, ShipEvent> machine) {
        ShipState state = machine.getState().getId();

        return switch (state) {
            case CREATED, LANDED -> Set.of(ShipEvent.LAUNCH);
            case FLYING -> Set.of(
                    ShipEvent.LAND,
                    ShipEvent.METEOR_SHOWER,
                    ShipEvent.ENGINE_FAILURE,
                    ShipEvent.MARTIAN_ATTACK
            );

            case DAMAGED -> Set.of(
                    ShipEvent.METEOR_SHOWER,
                    ShipEvent.ENGINE_FAILURE
            );
            case CRITICAL -> Set.of(
                    ShipEvent.METEOR_SHOWER,
                    ShipEvent.ENGINE_FAILURE
            );
            case CAPTURED -> Set.of(ShipEvent.SEND_CREW);
            case FIGHTING_MARTIANS -> Set.of(ShipEvent.MARTIANS_DEFEATED);
            case LANDING -> Set.of(ShipEvent.LANDING_COMPLETE);
            case DESTROYED -> Set.of();
            default -> Set.of();
        };
    }
}
