package com.reznik.krd.starshipmachine.statemachine.guards;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.service.ShipStateMachineContextHelper;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MartianCrewGuard implements Guard<ShipState, ShipEvent> {


    private final ShipStateMachineContextHelper contextService;

    @Override
    public boolean evaluate(StateContext<ShipState, ShipEvent> context) {
        Spaceship spaceship = contextService
                .getShipByStateMachineId(context.getStateMachine().getId());
        return spaceship.getCrew() >= 1;
    }
}
