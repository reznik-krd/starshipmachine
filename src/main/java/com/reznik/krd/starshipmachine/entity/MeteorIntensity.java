package com.reznik.krd.starshipmachine.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeteorIntensity {

    LIGHT(10),
    MEDIUM(30),
    EXTREME(60);

    private final int damage;

}
