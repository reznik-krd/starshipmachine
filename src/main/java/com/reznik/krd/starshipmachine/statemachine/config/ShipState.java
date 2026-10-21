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
    LANDED("Посадка корабля выполнена успешно"),

    DAMAGED("Корабль повреждён, но ситуация остаётся контролируемой"),
    CRITICAL("Корабль находится в критическом состоянии"),
    DESTROYED("Корабль потерян. Терминальное состояние"),
    DAMAGE_CHOICE("Техническое состояние для определения результата повреждения"),
    REPAIRING("Экипаж выполняет ремонт корабля"),
    EVACUATING("Экипаж эвакуируется с корабля"),

    CAPTURED("Корабль захвачен марсианами"),
    FIGHTING_MARTIANS("Экипаж сражается с марсианами"),
    MARTIAN_RESULT_CHOICE("Техническое состояние для определения результата боя с марсианами"),
    REPAIR_RESULT_CHOICE("Техническое состояние для определения результата ремонта");

    private final String description;

}
