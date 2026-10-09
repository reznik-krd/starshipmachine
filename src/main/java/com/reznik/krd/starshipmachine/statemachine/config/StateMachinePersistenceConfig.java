package com.reznik.krd.starshipmachine.statemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class StateMachinePersistenceConfig {

    @Bean
    public StateMachineService<ShipState, ShipEvent> stateMachineService(
            StateMachineFactory<ShipState, ShipEvent> factory
    ) {
        return new DefaultStateMachineService<>(factory);
    }
}