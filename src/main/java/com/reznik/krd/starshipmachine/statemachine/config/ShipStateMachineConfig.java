package com.reznik.krd.starshipmachine.statemachine.config;

import com.reznik.krd.starshipmachine.statemachine.action.*;
import com.reznik.krd.starshipmachine.statemachine.guards.*;
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

    // actions
    private final CreateShipAction createShipAction;
    private final CalculateDamageMeteorAction calculateDamageMeteorAction;
    private final ApplyDamageAction applyDamageAction;
    private final EngineDamageAction engineDamageAction;
    private final AfterDamagingToDestroyedAction afterDamagingToDestroyedAction;
    private final MartianBattleAction martianBattleAction;
    private final EvacuationAction evacuationAction;
    private final RepairAction repairAction;

    // guards
    private final DestroyedGuard destroyedGuard;
    private final CriticalGuard criticalGuard;
    private final DamagedGuard damagedGuard;
    private final MartianCrewGuard martianCrewGuard;
    private final CrewAvailableGuard crewAvailableGuard;

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
                .state(ShipState.LANDED)
                .state(ShipState.DAMAGED)
                .state(ShipState.CRITICAL)
                .state(ShipState.DESTROYED)
                .state(ShipState.CAPTURED)
                .state(ShipState.FIGHTING_MARTIANS)
                .state(ShipState.REPAIRING)
                .state(ShipState.EVACUATING)
                .choice(ShipState.DAMAGE_CHOICE)
                .choice(ShipState.MARTIAN_RESULT_CHOICE)
                .choice(ShipState.REPAIR_RESULT_CHOICE);
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

        damaging(transitions);
        underMartiansAttack(transitions);
        repairing(transitions);
        evacuating(transitions);
    }

    private void damaging(StateMachineTransitionConfigurer<ShipState, ShipEvent>
                                  transitions) throws Exception {

        transitions.withExternal()
                .source(ShipState.FLYING)
                .target(ShipState.DAMAGE_CHOICE)
                .event(ShipEvent.METEOR_SHOWER)
                .action(calculateDamageMeteorAction)
                .action(applyDamageAction)

                .and()
                .withExternal()
                .source(ShipState.FLYING)
                .target(ShipState.DAMAGE_CHOICE)
                .event(ShipEvent.ENGINE_FAILURE)
                .action(engineDamageAction)

                .and()
                .withExternal()
                .source(ShipState.DAMAGED)
                .target(ShipState.DAMAGE_CHOICE)
                .event(ShipEvent.METEOR_SHOWER)
                .action(calculateDamageMeteorAction)
                .action(applyDamageAction)

                .and()
                .withExternal()
                .source(ShipState.CRITICAL)
                .target(ShipState.DAMAGE_CHOICE)
                .event(ShipEvent.METEOR_SHOWER)
                .action(calculateDamageMeteorAction)
                .action(applyDamageAction)

                .and()
                .withExternal()
                .source(ShipState.DAMAGED)
                .target(ShipState.DAMAGE_CHOICE)
                .event(ShipEvent.ENGINE_FAILURE)
                .action(engineDamageAction)

                .and()
                .withExternal()
                .source(ShipState.CRITICAL)
                .target(ShipState.DAMAGE_CHOICE)
                .event(ShipEvent.ENGINE_FAILURE)
                .action(engineDamageAction)

                .and()
                .withChoice()
                .source(ShipState.DAMAGE_CHOICE)
                .first(ShipState.DESTROYED, destroyedGuard, afterDamagingToDestroyedAction)
                .then(ShipState.CRITICAL, criticalGuard)
                .then(ShipState.DAMAGED, damagedGuard)
                .last(ShipState.FLYING);

    }

    public void underMartiansAttack(StateMachineTransitionConfigurer<ShipState, ShipEvent>
                                            transitions) throws Exception {
        transitions
                .withExternal()
                .source(ShipState.FLYING)
                .target(ShipState.CAPTURED)
                .event(ShipEvent.MARTIAN_ATTACK)

                .and()
                .withExternal()
                .source(ShipState.CAPTURED)
                .target(ShipState.FIGHTING_MARTIANS)
                .event(ShipEvent.SEND_CREW)
                .guard(martianCrewGuard)

                .and()
                .withExternal()
                .source(ShipState.FIGHTING_MARTIANS)
                .target(ShipState.MARTIAN_RESULT_CHOICE)
                .event(ShipEvent.MARTIANS_DEFEATED)
                .action(martianBattleAction)

                .and()
                .withChoice()
                .source(ShipState.MARTIAN_RESULT_CHOICE)
                .first(ShipState.DESTROYED, destroyedGuard, afterDamagingToDestroyedAction)
                .then(ShipState.CRITICAL, criticalGuard)
                .last(ShipState.DAMAGED);
    }

    public void repairing(StateMachineTransitionConfigurer<ShipState, ShipEvent>
                                  transitions) throws Exception {
        transitions
                .withExternal()
                .source(ShipState.DAMAGED)
                .target(ShipState.REPAIRING)
                .event(ShipEvent.START_REPAIR)
                .guard(crewAvailableGuard)

                .and()
                .withExternal()
                .source(ShipState.CRITICAL)
                .target(ShipState.REPAIRING)
                .event(ShipEvent.START_REPAIR)
                .guard(crewAvailableGuard)

                .and()
                .withExternal()
                .source(ShipState.REPAIRING)
                .target(ShipState.REPAIR_RESULT_CHOICE)
                .event(ShipEvent.REPAIR_COMPLETE)
                .action(repairAction)

                .and()
                .withChoice()
                .source(ShipState.REPAIR_RESULT_CHOICE)
                .first(ShipState.CRITICAL, criticalGuard)
                .then(ShipState.DAMAGED, damagedGuard)
                .last(ShipState.FLYING);
    }

    public void evacuating(StateMachineTransitionConfigurer<ShipState, ShipEvent>
                                   transitions) throws Exception {
        transitions
                .withExternal()
                .source(ShipState.CRITICAL)
                .target(ShipState.EVACUATING)
                .event(ShipEvent.EVACUATE)
                .action(evacuationAction)

                .and()
                .withExternal()
                .source(ShipState.EVACUATING)
                .target(ShipState.DESTROYED)
                .event(ShipEvent.EVACUATION_COMPLETE);
    }

}
