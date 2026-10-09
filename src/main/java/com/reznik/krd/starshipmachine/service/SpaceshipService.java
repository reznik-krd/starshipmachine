package com.reznik.krd.starshipmachine.service;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import com.reznik.krd.starshipmachine.repository.SpaceshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpaceshipService {

    private final SpaceshipRepository spaceshipRepository;

    @Transactional
    public Spaceship create(String name, UUID stateMachineId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ship name is required");
        }
        Spaceship spaceship = new Spaceship(name);
        spaceship.setStateMachineId(stateMachineId);
        return spaceshipRepository.save(spaceship);
    }

    @Transactional
    public Spaceship findById(UUID spaceShipId) {
        return spaceshipRepository.findById(spaceShipId).orElseThrow();
    }

    @Transactional
    public void deleteById(UUID spaceShipId) {
        spaceshipRepository.deleteById(spaceShipId);
    }
}
