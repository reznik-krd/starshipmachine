package com.reznik.krd.starshipmachine.web.dto;

import java.util.UUID;

public record ShipSummaryResponse(UUID id, String name, String state, int hull, int crew) {
}
