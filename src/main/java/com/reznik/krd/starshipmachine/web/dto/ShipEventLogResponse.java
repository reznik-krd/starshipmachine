package com.reznik.krd.starshipmachine.web.dto;

import java.time.LocalDateTime;

public record ShipEventLogResponse(String event, String sourceState, String targetState, String message,
                                   LocalDateTime createdAt) {
}
