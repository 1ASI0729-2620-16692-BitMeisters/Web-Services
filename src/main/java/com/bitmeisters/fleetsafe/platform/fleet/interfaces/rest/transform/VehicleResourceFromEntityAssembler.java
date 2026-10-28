package com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Vehicle;
import com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.resources.VehicleResource;

public class VehicleResourceFromEntityAssembler {

    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return new VehicleResource(
                entity.getId(),
                entity.getFleetId().value(),
                entity.getPlate().value(),
                entity.getBrand(),
                entity.getModel(),
                entity.getYear(),
                entity.getType().name(),
                entity.getCapacity(),
                entity.getCurrentStatus().name(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
