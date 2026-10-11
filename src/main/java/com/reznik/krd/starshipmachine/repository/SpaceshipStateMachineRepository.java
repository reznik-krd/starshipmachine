package com.reznik.krd.starshipmachine.repository;

import com.reznik.krd.starshipmachine.entity.SpaceshipStateMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpaceshipStateMachineRepository
        extends JpaRepository<SpaceshipStateMachine, UUID> {

    @Query("select sm.stateMachineId " +
            "from SpaceshipStateMachine sm " +
            "where sm.spaceship.id = :spaceshipId")
    Optional<UUID> getStateMachineIdBySpaceshipId(UUID spaceshipId);
}
