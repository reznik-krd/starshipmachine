package com.reznik.krd.starshipmachine.statemachine.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShipEvent {

    CREATE("Create", "Создать корабль"),
    LAUNCH("Launch", "Запустить корабль");

    private final String title;
    private final String description;
}
