package com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.commands.RegisterVehicleCommand;
import com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.resources.RegisterVehicleResource;

public class RegisterVehicleCommandFromResourceAssembler {

    public static RegisterVehicleCommand toCommandFromResource(RegisterVehicleResource resource) {
        return new RegisterVehicleCommand(
                FleetValueObjectFromStringAssembler.toFleetIdFromUuid(resource.fleetId()),
                FleetValueObjectFromStringAssembler.toPlateFromString(resource.plate()),
                resource.brand(),
                resource.model(),
                resource.year(),
                FleetValueObjectFromStringAssembler.toVehicleTypeFromString(resource.type()),
                resource.capacity());
    }
}

