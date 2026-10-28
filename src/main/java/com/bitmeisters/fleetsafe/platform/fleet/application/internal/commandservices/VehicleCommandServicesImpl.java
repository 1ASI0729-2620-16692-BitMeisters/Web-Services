package com.bitmeisters.fleetsafe.platform.fleet.application.internal.commandservices;

import com.bitmeisters.fleetsafe.platform.fleet.application.commandservices.VehicleCommandFailure;
import com.bitmeisters.fleetsafe.platform.fleet.application.commandservices.VehicleCommandService;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Vehicle;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.commands.RegisterVehicleCommand;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.repositories.FleetRepository;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.repositories.VehicleRepository;
import com.bitmeisters.fleetsafe.platform.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final FleetRepository fleetRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository, FleetRepository fleetRepository) {
        this.vehicleRepository = vehicleRepository;
        this.fleetRepository = fleetRepository;
    }

    /**
     * Registers a vehicle in a fleet.
     * <p>
     * It is not wrapped in an outer transaction on purpose: when two requests race for the same
     * plate, the database rejects the second one inside {@code saveAndFlush}. A surrounding transaction
     * would be marked rollback-only by that failure and could no longer answer with a plain duplicate result.
     */
    @Override
    public Result<Vehicle, VehicleCommandFailure> handle(RegisterVehicleCommand command) {
        if (!fleetRepository.existsById(command.fleetId().value())) {
            log.warn("Fleet not found while registering a vehicle: fleetId={}", command.fleetId().value());
            return Result.failure(new VehicleCommandFailure.FleetNotFound());
        }
        if (vehicleRepository.existsByPlate(command.plate())) {
            log.warn("Duplicate plate detected (pre-check): plate={}", command.plate().value());
            return duplicatePlate();
        }
        try {
            var vehicle = vehicleRepository.saveAndFlush(new Vehicle(command));
            log.info("Vehicle registered: id={}, plate={}, fleetId={}",
                    vehicle.getId(), vehicle.getPlate().value(), vehicle.getFleetId().value());
            return Result.success(vehicle);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicatePlateViolation(exception)) {
                log.warn("Duplicate plate detected (post-check): plate={}", command.plate().value());
                return duplicatePlate();
            }
            log.error("Unexpected data integrity violation while registering a vehicle: plate={}",
                    command.plate().value(), exception);
            throw exception;
        }
    }

    private Result<Vehicle, VehicleCommandFailure> duplicatePlate() {
        return Result.failure(new VehicleCommandFailure.DuplicatePlate());
    }

    private boolean isDuplicatePlateViolation(DataIntegrityViolationException exception) {
        Throwable cause = exception;
        while (cause != null) {
            String message = cause.getMessage();
            if (message != null && message.contains(Vehicle.PLATE_UNIQUE_CONSTRAINT)) return true;
            cause = cause.getCause();
        }
        return false;
    }
}
