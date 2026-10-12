package com.reznik.krd.starshipmachine.statemachine.config;

import com.reznik.krd.starshipmachine.statemachine.action.CreateShipAction;
import com.reznik.krd.starshipmachine.statemachine.listener.ShipStateMachineListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import static com.reznik.krd.starshipmachine.util.Constants.LAUNCH_DELAY_MS;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(contextEvents = true)
public class ShipStateMachineConfig
        extends EnumStateMachineConfigurerAdapter<ShipState, ShipEvent> {

    private final StateMachineRuntimePersister<ShipState, ShipEvent, String> persister;
    private final ShipStateMachineListener listener;

    private final CreateShipAction createShipAction;

    @Override
    public void configure(StateMachineConfigurationConfigurer<ShipState, ShipEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(false)
                .listener(listener)
                .and()
                .withPersistence()
                .runtimePersister(persister);
    }

    @Override
    public void configure(StateMachineStateConfigurer<ShipState, ShipEvent> states) throws Exception {
        states.withStates()
                .initial(ShipState.INITIAL)
                .state(ShipState.CREATED)
                .state(ShipState.LAUNCHING)
                .state(ShipState.FLYING)
                .state(ShipState.LANDING)
                .state(ShipState.LANDED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ShipState, ShipEvent> transitions)
            throws Exception {
        transitions.withExternal()
                .source(ShipState.INITIAL)
                .target(ShipState.CREATED)
                .event(ShipEvent.CREATE)
                .action(createShipAction)

                .and()
                .withExternal()
                .source(ShipState.CREATED)
                .target(ShipState.LAUNCHING)
                .event(ShipEvent.LAUNCH)

                .and()
                .withExternal()
                .source(ShipState.LAUNCHING)
                .target(ShipState.FLYING)
                .action((ShipState) -> {
                    System.out.println("таймаут"); // для дебага
                })
                .timerOnce(LAUNCH_DELAY_MS)

                .and()
                .withExternal()
                .source(ShipState.FLYING)
                .target(ShipState.LANDING)
                .event(ShipEvent.LAND)

                .and()
                .withExternal()
                .source(ShipState.LANDING)
                .target(ShipState.LANDED)
                .event(ShipEvent.LANDING_COMPLETE)

                .and()
                .withExternal()
                .source(ShipState.LANDED)
                .target(ShipState.LAUNCHING)
                .event(ShipEvent.LAUNCH);
    }
}
