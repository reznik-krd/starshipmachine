package com.reznik.krd.starshipmachine.statemachine.listener;

import com.reznik.krd.starshipmachine.entity.ShipEventLog;
import com.reznik.krd.starshipmachine.repository.ShipEventLogRepository;
import com.reznik.krd.starshipmachine.service.ShipStateMachineContextHelper;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShipStateMachineListener
        extends StateMachineListenerAdapter<ShipState, ShipEvent> {

    private final ShipEventLogRepository shipEventLogRepository;
    private final ShipStateMachineContextHelper shipStateMachineContextHelper;

    @Override
    public void stateContext(StateContext<ShipState, ShipEvent> stateContext) {
        if (stateContext.getStage() != StateContext.Stage.TRANSITION_END
                || stateContext.getStateMachine() == null
                || stateContext.getSource() == null
                || stateContext.getTarget() == null) {
            return;
        }
        try {
            ShipEvent shipEvent = stateContext.getEvent();
            ShipState source = stateContext.getSource().getId();
            ShipState target = stateContext.getTarget().getId();

            String machineId = stateContext.getStateMachine().getId();
            UUID spaceshipId = shipStateMachineContextHelper.getShipIdByStateMachineId(UUID.fromString(machineId));

            String message = (shipEvent == null ? "AUTO"
                    : shipEvent.name()) + ": " + source + " → " + target;

            shipEventLogRepository.save(
                    ShipEventLog.builder()
                            .spaceshipId(spaceshipId)
                            .event(shipEvent == null ? null : shipEvent.name())
                            .sourceState(source.name())
                            .targetState(target.name())
                            .message(message)
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        } catch (Exception e) {
            log.error("Ошибка в ShipStateMachineListener {}", e.getMessage());
        }
    }
}
