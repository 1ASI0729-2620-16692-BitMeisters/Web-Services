package com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest;

import com.bitmeisters.fleetsafe.platform.fleet.application.queryservices.FleetQueryService;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetAllFleetsQuery;
import com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform.ResponseEntityFromFleetQueryResultAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/fleets", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Fleet Management", description = "Vehicles, fleets, drivers and their assignments")
public class FleetsController {

    private final FleetQueryService fleetQueryService;

    public FleetsController(FleetQueryService fleetQueryService) {
        this.fleetQueryService = fleetQueryService;
    }

    @GetMapping
    @Operation(summary = "List the fleets", description = "Returns the fleets a vehicle can be registered in.")
    @ApiResponse(responseCode = "200", description = "The fleets")
    public ResponseEntity<?> getAllFleets() {
        var fleets = fleetQueryService.handle(new GetAllFleetsQuery());
        return ResponseEntityFromFleetQueryResultAssembler.toResponseEntityFromList(fleets);
    }
}
