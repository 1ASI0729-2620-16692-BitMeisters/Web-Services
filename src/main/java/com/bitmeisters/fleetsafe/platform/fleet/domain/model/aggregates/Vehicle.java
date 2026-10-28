package com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.commands.RegisterVehicleCommand;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.events.VehicleRegisteredEvent;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.FleetId;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.Plate;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.VehicleStatus;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.VehicleType;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.converters.FleetIdAttributeConverter;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.converters.PlateAttributeConverter;
import com.bitmeisters.fleetsafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * A vehicle of a fleet.
 * Its status is derived from the latest operational authorization, so it is never provided when the
 * vehicle is registered: every vehicle starts enabled.
 */
@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"plate"}, name = Vehicle.PLATE_UNIQUE_CONSTRAINT)
})
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {

    public static final String PLATE_UNIQUE_CONSTRAINT = "uk_vehicle_plate";
    public static final VehicleStatus INITIAL_STATUS = VehicleStatus.ENABLED;

    @Column(nullable = false)
    @Convert(converter = FleetIdAttributeConverter.class)
    private FleetId fleetId;

    @Column(nullable = false, length = 10)
    @Convert(converter = PlateAttributeConverter.class)
    private Plate plate;

    @Column(nullable = false, length = RegisterVehicleCommand.TEXT_MAX_LENGTH)
    private String brand;

    @Column(nullable = false, length = RegisterVehicleCommand.TEXT_MAX_LENGTH)
    private String model;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal capacity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleStatus currentStatus;

    protected Vehicle() {}

    public Vehicle(RegisterVehicleCommand command) {
        this.fleetId = command.fleetId();
        this.plate = command.plate();
        this.brand = command.brand();
        this.model = command.model();
        this.year = command.year();
        this.type = command.type();
        this.capacity = command.capacity();
        this.currentStatus = INITIAL_STATUS;
        addDomainEvent(new VehicleRegisteredEvent(command.fleetId(), command.plate()));
    }
}
