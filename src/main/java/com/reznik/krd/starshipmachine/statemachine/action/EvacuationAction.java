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

import static com.reznik.krd.starshipmachine.util.Constants.EVACUATION_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class EvacuationAction implements Action<ShipState, ShipEvent> {

    private final ShipStateMachineContextHelper contextService;

    @Override
    public void execute(StateContext<ShipState, ShipEvent> context) {
        Spaceship ship = contextService
                .getShipByStateMachineId(context.getStateMachine().getId());
        String message = context.getExtendedState().get(EVACUATION_MESSAGE, String.class);
        log.warn("Evacuation on {}: {}", ship.getName(), message);
    }
}
