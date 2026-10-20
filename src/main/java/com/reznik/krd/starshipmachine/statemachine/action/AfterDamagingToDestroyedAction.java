package com.reznik.krd.starshipmachine.statemachine.action;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.service.ShipStateMachineContextHelper;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AfterDamagingToDestroyedAction implements Action<ShipState, ShipEvent> {

    private final ShipStateMachineContextHelper shipStateMachineContextHelper;

    @Override
    public void execute(StateContext<ShipState, ShipEvent> context) {

        Spaceship spaceship = shipStateMachineContextHelper
                .getShipByStateMachineId(context.getStateMachine().getId());

        log.info("Наш корабль {} уничтожен, здоровье {}",
                spaceship.getId(), spaceship.getHull());

    }
}
