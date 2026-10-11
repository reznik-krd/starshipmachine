package com.reznik.krd.starshipmachine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "spaceships")
@NoArgsConstructor
public class Spaceship {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int hull;

    @Column(nullable = false)
    private int crew;

    public Spaceship(String name) {
        this.name = name;
        this.hull = 100;
        this.crew = 5;
    }
}
