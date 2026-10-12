package com.reznik.krd.starshipmachine.statemachine.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShipState {

    INITIAL("Техническое начальное состояние"),
    CREATED("Корабль создан, но ещё не запускался"),
    LAUNCHING("Идёт последовательность запуска корабля"),
    FLYING("Корабль находится в штатном полёте"),
    LANDING("Корабль выполняет посадку"),
    LANDED("Посадка корабля выполнена успешно");

    private final String description;

}
