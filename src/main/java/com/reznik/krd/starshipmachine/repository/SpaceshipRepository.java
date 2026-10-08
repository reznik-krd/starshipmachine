package com.reznik.krd.starshipmachine.repository;

import com.reznik.krd.starshipmachine.entity.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, UUID> {
}
