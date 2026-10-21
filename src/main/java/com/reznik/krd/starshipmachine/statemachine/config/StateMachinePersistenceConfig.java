package com.reznik.krd.starshipmachine.statemachine.config;

import com.reznik.krd.starshipmachine.entity.MeteorIntensity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class StateMachinePersistenceConfig {

    @Bean
    public StateMachineRuntimePersister<ShipState, ShipEvent, String> stateMachineRuntimePersister(
            JpaStateMachineRepository repository
    ) {
        return new JpaPersistingStateMachineInterceptor<>(repository, kryo -> {
            kryo.register(ShipState.class);
            kryo.register(ShipEvent.class);
            kryo.register(MeteorIntensity.class);
        });
    }

    @Bean
    public StateMachineService<ShipState, ShipEvent> stateMachineService(
            StateMachineFactory<ShipState, ShipEvent> factory,
            StateMachineRuntimePersister<ShipState, ShipEvent, String> persister
    ) {
        return new DefaultStateMachineService<>(factory, persister);
    }
}