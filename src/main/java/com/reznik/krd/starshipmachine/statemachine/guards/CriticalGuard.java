package com.reznik.krd.starshipmachine.statemachine.guards;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.service.ShipStateMachineContextHelper;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CriticalGuard implements Guard<ShipState, ShipEvent> {

    private final ShipStateMachineContextHelper shipStateMachineContextHelper;

    @Override
    public boolean evaluate(StateContext<ShipState, ShipEvent> context) {
        Spaceship spaceship = shipStateMachineContextHelper
                .getShipByStateMachineId(context.getStateMachine().getId());
        return spaceship.getHull() > 0 && spaceship.getHull() <= 30;
    }
}
