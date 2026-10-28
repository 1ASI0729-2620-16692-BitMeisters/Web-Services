package com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Vehicle;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityFromVehicleQueryResultAssembler {

    private static ResponseEntity<ProblemDetail> problemDetail(
            HttpStatus status,
            MessageSource messageSource,
            String messageKey, Object... args) {
        var detail = messageSource.getMessage(messageKey, args, messageKey, LocaleContextHolder.getLocale());
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
    }

    public static ResponseEntity<?> toResponseEntityFromVehicle(Vehicle vehicle) {
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    public static ResponseEntity<?> toResponseEntityFromList(List<Vehicle> vehicles) {
        var vehicleResources = vehicles.stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(vehicleResources);
    }

    public static ResponseEntity<ProblemDetail> notFound(MessageSource messageSource, String messageKey, Object... args) {
        return problemDetail(HttpStatus.NOT_FOUND, messageSource, messageKey, args);
    }
}