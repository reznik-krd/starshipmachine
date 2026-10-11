package com.reznik.krd.starshipmachine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "spaceship_state_machines")
public class SpaceshipStateMachine {

    @Id
    @Column(name = "state_machine_id")
    private UUID stateMachineId;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "spaceship_id", nullable = false, unique = true)
    private Spaceship spaceship;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
