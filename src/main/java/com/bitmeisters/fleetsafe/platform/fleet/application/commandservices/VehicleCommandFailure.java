package com.bitmeisters.fleetsafe.platform.fleet.application.commandservices;

public sealed interface VehicleCommandFailure
        permits VehicleCommandFailure.FleetNotFound,
        VehicleCommandFailure.DuplicatePlate {

    String messageKey();

    record FleetNotFound() implements VehicleCommandFailure {
        @Override
        public String messageKey() {
            return "fleet.vehicle.error.fleet-not-found";
        }
    }

    record DuplicatePlate() implements VehicleCommandFailure {
        @Override
        public String messageKey() {
            return "fleet.vehicle.error.plate.duplicate";
        }
    }
}