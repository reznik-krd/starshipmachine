package com.reznik.krd.starshipmachine.statemachine.action;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.service.SpaceshipService;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.reznik.krd.starshipmachine.util.Constants.SHIP_ID;
import static com.reznik.krd.starshipmachine.util.Constants.SHIP_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateShipAction implements Action<ShipState, ShipEvent> {

    private final SpaceshipService spaceshipService;

    @Override
    public void execute(StateContext<ShipState, ShipEvent> context) {
        String name = context.getExtendedState().get(SHIP_NAME, String.class);

        UUID machineID = UUID.fromString(context.getStateMachine().getId());

        Spaceship spaceship = spaceshipService.create(name, machineID);

        context.getExtendedState()
                .getVariables()
                .put(SHIP_ID, spaceship.getId());
    }
}
