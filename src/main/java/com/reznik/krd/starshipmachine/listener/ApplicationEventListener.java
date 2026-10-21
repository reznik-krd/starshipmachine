package com.reznik.krd.starshipmachine.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationEventListener {

    /**
     * Список событий:
     * - ApplicationStartingEvent (Начало запуска приложения)
     * - ContextRefreshedEvent (Контекст Spring обновлён)
     * - ApplicationReadyEvent (Приложение готово к работе)
     *
     * @param event - событие
     */
    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        log.info("Spring Boot готов к работе!");
    }
}
