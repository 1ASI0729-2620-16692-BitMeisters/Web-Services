package com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.FleetId;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.Plate;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.VehicleType;

import java.util.UUID;

public class FleetValueObjectFromStringAssembler {

    public static FleetId toFleetIdFromUuid(UUID value) {
        return new FleetId(value);
    }

    public static Plate toPlateFromString(String value) {
        return new Plate(value);
    }

    public static VehicleType toVehicleTypeFromString(String value) {
        return VehicleType.valueOf(value);
    }
}