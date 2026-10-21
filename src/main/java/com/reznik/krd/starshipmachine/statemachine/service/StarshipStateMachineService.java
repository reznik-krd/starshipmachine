package com.reznik.krd.starshipmachine.statemachine.service;

import com.reznik.krd.starshipmachine.repository.SpaceshipStateMachineRepository;
import com.reznik.krd.starshipmachine.service.SpaceshipService;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.statemachine.config.ShipState;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StarshipStateMachineService {

    private final StateMachineService<ShipState, ShipEvent> stateMachineService;
    private final SpaceshipStateMachineRepository spaceshipStateMachineRepository;
    private final JpaStateMachineRepository jpaStateMachineRepository;
    private final SpaceshipService spaceshipService;

    public StateMachine<ShipState, ShipEvent> createStateMachine() {
        return stateMachineService.acquireStateMachine(
                UUID.randomUUID().toString()
        );
    }

    public StateMachine<ShipState, ShipEvent> acquireByShipId(UUID shipId) {
        UUID machineId = spaceshipStateMachineRepository.getStateMachineIdBySpaceshipId(shipId)
                .orElseThrow();
        return stateMachineService.acquireStateMachine(machineId.toString());
    }

    public void stopByShipId(UUID shipId) {
        UUID machineId = spaceshipStateMachineRepository.getStateMachineIdBySpaceshipId(shipId)
                .orElseThrow();
        stateMachineService.releaseStateMachine(machineId.toString());
    }

    public void deleteByShipId(UUID shipId) {
        spaceshipStateMachineRepository.getStateMachineIdBySpaceshipId(shipId)
                .ifPresent(stateMachineId -> {
            spaceshipStateMachineRepository.deleteById(stateMachineId);
            jpaStateMachineRepository.deleteById(stateMachineId.toString());
        });
    }

    public boolean sendEvent(StateMachine<ShipState, ShipEvent> machine,
                             ShipEvent event) {

        Message<ShipEvent> message = MessageBuilder
                .withPayload(event)
                .build();

        return machine.sendEventCollect(Mono.just(message))
                .block()
                .stream()
                .anyMatch(result ->
                        result.getResultType() == StateMachineEventResult.ResultType.ACCEPTED
                );

    }
}
