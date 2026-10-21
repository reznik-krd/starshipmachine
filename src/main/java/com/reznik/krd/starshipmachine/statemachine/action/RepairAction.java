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

import static com.reznik.krd.starshipmachine.util.Constants.REPAIR_AMOUNT;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepairAction implements Action<ShipState, ShipEvent> {

    private final ShipStateMachineContextHelper contextService;

    @Override
    @Transactional
    public void execute(StateContext<ShipState, ShipEvent> context) {
        Spaceship ship = contextService
                .getShipByStateMachineId(context.getStateMachine().getId());
        int old = ship.getHull();
        ship.setHull(Math.min(100, old + REPAIR_AMOUNT));

        log.info("Ship {} repaired: {} -> {}", ship.getName(), old, ship.getHull());
    }
}
