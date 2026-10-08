package com.reznik.krd.starshipmachine.web.dto;

public record EventResultResponse(boolean accepted, String message, ShipDashboardResponse dashboard) {
}
