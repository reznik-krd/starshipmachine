package com.reznik.krd.starshipmachine.service;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.repository.ShipEventLogRepository;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import com.reznik.krd.starshipmachine.statemachine.service.StarshipStateMachineService;
import com.reznik.krd.starshipmachine.web.dto.EventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static com.reznik.krd.starshipmachine.util.Constants.*;

@Service
@RequiredArgsConstructor
public class SpaceshipProcessService {

    private final SpaceshipService spaceshipService;
    private final ShipEventLogRepository logRepository;
    private final StarshipStateMachineService machineService;

    @Transactional
    public Spaceship create(String name) {
        StateMachine<ShipState, ShipEvent> machine = machineService.createStateMachine();

        machine.getExtendedState()
                .getVariables()
                .put(SHIP_NAME, name);

        machineService.sendEvent(machine, ShipEvent.CREATE);

        UUID shipId = machine.getExtendedState().get(SHIP_ID, UUID.class);
        return spaceshipService.findById(shipId);
    }

    public boolean sendEvent(UUID shipId, ShipEvent event, EventRequest request) {
        StateMachine<ShipState, ShipEvent> machine = machineService.acquireByShipId(shipId);

        if (request.meteorIntensity() != null) {
            machine.getExtendedState()
                    .getVariables()
                    .put(METEOR_INTENSITY, request.meteorIntensity());
        }
        if (StringUtils.hasText(request.evacuationMessage())) {
            machine.getExtendedState()
                    .getVariables()
                    .put(EVACUATION_MESSAGE, request.evacuationMessage());
        }

        return machineService.sendEvent(machine, event);
    }

    @Transactional
    public void delete(UUID shipId) {
        logRepository.deleteBySpaceshipId(shipId);
        machineService.stopByShipId(shipId);
        machineService.deleteByShipId(shipId);
        spaceshipService.deleteById(shipId);
    }

}
