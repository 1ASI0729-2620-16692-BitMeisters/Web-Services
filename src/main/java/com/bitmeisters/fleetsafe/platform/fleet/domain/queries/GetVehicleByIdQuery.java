package com.bitmeisters.fleetsafe.platform.fleet.domain.queries;

import java.util.UUID;

public record GetVehicleByIdQuery(UUID vehicleId) {

    public GetVehicleByIdQuery {
        if (vehicleId == null) {
            throw new IllegalArgumentException("fleet.vehicle.error.id.required");
        }
    }
}