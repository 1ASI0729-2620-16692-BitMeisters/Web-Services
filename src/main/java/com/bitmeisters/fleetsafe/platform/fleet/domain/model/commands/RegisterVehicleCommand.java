package com.bitmeisters.fleetsafe.platform.fleet.domain.model.commands;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.FleetId;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.Plate;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.VehicleType;

import java.math.BigDecimal;
import java.time.Year;

public record RegisterVehicleCommand(
        FleetId fleetId,
        Plate plate,
        String brand,
        String model,
        int year,
        VehicleType type,
        BigDecimal capacity) {

    public static final int MIN_YEAR = 1950;
    public static final int TEXT_MAX_LENGTH = 60;

    public RegisterVehicleCommand {
        if (fleetId == null) throw new IllegalArgumentException("fleet.error.fleet-id.required");
        if (plate == null) throw new IllegalArgumentException("fleet.vehicle.error.plate.required");
        if (brand == null || brand.isBlank()) throw new IllegalArgumentException("fleet.vehicle.error.brand.required");
        brand = brand.trim();
        if (brand.length() > TEXT_MAX_LENGTH) throw new IllegalArgumentException("fleet.vehicle.error.brand.size");
        if (model == null || model.isBlank()) throw new IllegalArgumentException("fleet.vehicle.error.model.required");
        model = model.trim();
        if (model.length() > TEXT_MAX_LENGTH) throw new IllegalArgumentException("fleet.vehicle.error.model.size");
        if (year < MIN_YEAR || year > Year.now().getValue() + 1) {
            throw new IllegalArgumentException("fleet.vehicle.error.year.range");
        }
        if (type == null) throw new IllegalArgumentException("fleet.vehicle.error.type.required");
        if (capacity == null || capacity.signum() <= 0) {
            throw new IllegalArgumentException("fleet.vehicle.error.capacity.positive");
        }
    }
}
