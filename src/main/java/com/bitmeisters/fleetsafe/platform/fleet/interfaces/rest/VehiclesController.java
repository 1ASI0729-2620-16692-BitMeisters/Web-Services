package com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest;

import com.bitmeisters.fleetsafe.platform.fleet.application.commandservices.VehicleCommandService;
import com.bitmeisters.fleetsafe.platform.fleet.application.queryservices.VehicleQueryService;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetAllVehiclesQuery;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.resources.RegisterVehicleResource;
import com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform.RegisterVehicleCommandFromResourceAssembler;
import com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform.ResponseEntityFromVehicleCommandResultAssembler;
import com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform.ResponseEntityFromVehicleQueryResultAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Fleet Management", description = "Vehicles, fleets, drivers and their assignments")
public class VehiclesController {

    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;
    private final MessageSource messageSource;

    public VehiclesController(
            VehicleCommandService vehicleCommandService,
            VehicleQueryService vehicleQueryService,
            MessageSource messageSource) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
        this.messageSource = messageSource;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a vehicle", description = "Adds a vehicle to a fleet. The vehicle starts enabled.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehicle registered"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "The fleet does not exist"),
            @ApiResponse(responseCode = "409", description = "The plate is already registered")
    })
    public ResponseEntity<?> registerVehicle(@Valid @RequestBody RegisterVehicleResource resource) {
        var command = RegisterVehicleCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = vehicleCommandService.handle(command);
        return ResponseEntityFromVehicleCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @GetMapping
    @Operation(summary = "List the vehicles", description = "Returns every vehicle with its current status.")
    @ApiResponse(responseCode = "200", description = "The vehicles")
    public ResponseEntity<?> getAllVehicles() {
        var vehicles = vehicleQueryService.handle(new GetAllVehiclesQuery());
        return ResponseEntityFromVehicleQueryResultAssembler.toResponseEntityFromList(vehicles);
    }

    @GetMapping("/{vehicleId}")
    @Operation(summary = "Get a vehicle", description = "Returns the information and current status of a vehicle.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The vehicle"),
            @ApiResponse(responseCode = "404", description = "The vehicle does not exist")
    })
    public ResponseEntity<?> getVehicleById(@PathVariable UUID vehicleId) {
        var vehicle = vehicleQueryService.handle(new GetVehicleByIdQuery(vehicleId));
        if (vehicle.isEmpty()) {
            return ResponseEntityFromVehicleQueryResultAssembler.notFound(messageSource, "fleet.vehicle.error.not-found");
        }
        return ResponseEntityFromVehicleQueryResultAssembler.toResponseEntityFromVehicle(vehicle.get());
    }
}

