package com.reznik.krd.starshipmachine.statemachine.action;

import com.reznik.krd.starshipmachine.entity.MeteorIntensity;
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

import static com.reznik.krd.starshipmachine.util.Constants.METEOR_INTENSITY;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplyDamageAction implements Action<ShipState, ShipEvent> {

    private final ShipStateMachineContextHelper shipStateMachineContextHelper;

    @Override
    @Transactional
    public void execute(StateContext<ShipState, ShipEvent> context) {

        MeteorIntensity meteorIntensity = context.getExtendedState()
                .get(METEOR_INTENSITY, MeteorIntensity.class);

        Spaceship spaceship = shipStateMachineContextHelper
                .getShipByStateMachineId(context.getStateMachine().getId());

        int oldHull = spaceship.getHull();
        int newHull = Math.max(0, oldHull - meteorIntensity.getDamage());
        spaceship.setHull(newHull);
    }
}
