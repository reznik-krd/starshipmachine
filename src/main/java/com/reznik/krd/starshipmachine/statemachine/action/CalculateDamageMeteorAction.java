package com.reznik.krd.starshipmachine.statemachine.action;

import com.reznik.krd.starshipmachine.entity.MeteorIntensity;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.reznik.krd.starshipmachine.util.Constants.DAMAGE_REASON;
import static com.reznik.krd.starshipmachine.util.Constants.METEOR_INTENSITY;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculateDamageMeteorAction implements Action<ShipState, ShipEvent> {

    @Override
    @Transactional
    public void execute(StateContext<ShipState, ShipEvent> context) {

        MeteorIntensity meteorIntensity = (MeteorIntensity) context.getExtendedState()
                .getVariables()
                .get(METEOR_INTENSITY);

        if (meteorIntensity == null) {
            meteorIntensity = MeteorIntensity.MEDIUM;
        }

        context.getExtendedState().getVariables()
                .put(METEOR_INTENSITY, meteorIntensity);

        context.getExtendedState().getVariables()
                .put(DAMAGE_REASON, ShipEvent.METEOR_SHOWER.getDescription());
    }
}
