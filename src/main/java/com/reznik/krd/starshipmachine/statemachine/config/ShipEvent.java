package com.reznik.krd.starshipmachine.statemachine.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShipEvent {

    CREATE("Create", "Создать корабль"),
    LAUNCH("Launch", "Запустить корабль"),
    LAND("Land", "Начать посадку корабля"),
    LANDING_COMPLETE("LandingComplete", "Посадка корабля завершена"),
    METEOR_SHOWER("Meteor shower", "Метеоритный дождь повреждает корпус корабля"),
    ENGINE_FAILURE("Engine failure", "Произошла поломка двигателя"),

    MARTIAN_ATTACK("Martian attack", "Марсиане атакуют и захватывают корабль"),
    SEND_CREW("Send crew to fight", "Отправить экипаж на борьбу с марсианами"),
    MARTIANS_DEFEATED("Finish battle", "Завершить бой с марсианами"),

    START_REPAIR("Start repair", "Начать ремонт корабля"),
    REPAIR_COMPLETE("Repair complete", "Завершить ремонт корабля"),

    EVACUATE("Evacuate", "Начать эвакуацию экипажа"),
    EVACUATION_COMPLETE("Evacuation complete", "Эвакуация экипажа завершена");

    private final String title;
    private final String description;
}
