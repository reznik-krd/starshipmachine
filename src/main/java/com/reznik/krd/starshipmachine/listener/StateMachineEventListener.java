package com.reznik.krd.starshipmachine.listener;

import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.event.OnStateChangedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.reznik.krd.starshipmachine.util.Constants.SHIP_ID;

@Slf4j
@Component
public class StateMachineEventListener {

    @EventListener
    @Async
    public void onStateChangedEvent(OnStateChangedEvent event) {

        StateMachine<ShipState, ShipEvent> stateMachine = (StateMachine<ShipState, ShipEvent>) event.getSource();
        UUID shipId = (UUID) stateMachine.getExtendedState()
                .getVariables()
                .get(SHIP_ID);
        log.info("Корабль {} изменил состояние {}", shipId, event.getTargetState().getId());
    }

}
