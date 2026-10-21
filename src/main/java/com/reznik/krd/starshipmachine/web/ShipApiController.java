package com.reznik.krd.starshipmachine.web;

import com.reznik.krd.starshipmachine.service.ShipDashboardService;
import com.reznik.krd.starshipmachine.service.SpaceshipProcessService;
import com.reznik.krd.starshipmachine.statemachine.config.ShipEvent;
import com.reznik.krd.starshipmachine.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ships")
@RequiredArgsConstructor
public class ShipApiController {

    private final SpaceshipProcessService spaceshipProcessService;
    private final ShipDashboardService dashboardService;

    @GetMapping
    public List<ShipSummaryResponse> ships() {
        return dashboardService.summaries();
    }

    @PostMapping
    public ResponseEntity<ShipDashboardResponse> create(@RequestBody CreateShipRequest request) {
        var ship = spaceshipProcessService.create(request.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dashboardService.dashboard(ship.getId()));
    }

    @GetMapping("/{id}")
    public ShipDashboardResponse dashboard(@PathVariable UUID id) {
        return dashboardService.dashboard(id);
    }

    @PostMapping("/{id}/events/{event}")
    public EventResultResponse event(@PathVariable UUID id,
                                     @PathVariable ShipEvent event,
                                     @RequestBody(required = false) EventRequest request) {
        boolean accepted = spaceshipProcessService.sendEvent(id, event, request);
        return new EventResultResponse(
                accepted,
                accepted ? "Event accepted" : "Event rejected by state machine",
                dashboardService.dashboard(id)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        spaceshipProcessService.delete(id);
    }
}
