package com.reznik.krd.starshipmachine.service;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.entity.SpaceshipStateMachine;
import com.reznik.krd.starshipmachine.repository.SpaceshipStateMachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipStateMachineContextHelper {

    private final SpaceshipStateMachineRepository spaceshipStateMachineRepository;

    @Transactional(readOnly = true)
    public UUID getShipIdByStateMachineId(UUID machineId) {
        return spaceshipStateMachineRepository.findById(machineId)
                .map(SpaceshipStateMachine::getSpaceship)
                .map(Spaceship::getId)
                .orElseThrow();
    }


}
