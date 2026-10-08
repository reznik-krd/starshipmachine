package com.reznik.krd.starshipmachine.repository;

import com.reznik.krd.starshipmachine.entity.ShipEventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShipEventLogRepository extends JpaRepository<ShipEventLog, Long> {

    List<ShipEventLog> findTop30BySpaceshipIdOrderByCreatedAtDesc(UUID spaceshipId);

    void deleteBySpaceshipId(UUID spaceshipId);
}
