package com.reznik.krd.starshipmachine.web.dto;

import java.util.List;
import java.util.UUID;

public record ShipDashboardResponse(UUID id, String name, String state, String stateDescription, int hull, int crew,
                                    String damageReason, String evacuationMessage, List<ShipActionResponse> actions,
                                    List<ShipEventLogResponse> log) {
}
