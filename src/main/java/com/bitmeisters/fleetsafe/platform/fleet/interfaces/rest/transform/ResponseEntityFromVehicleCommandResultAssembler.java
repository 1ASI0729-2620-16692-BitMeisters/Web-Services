package com.bitmeisters.fleetsafe.platform.fleet.interfaces.rest.transform;

import com.bitmeisters.fleetsafe.platform.fleet.application.commandservices.VehicleCommandFailure;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Vehicle;
import com.bitmeisters.fleetsafe.platform.shared.application.patterns.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public class ResponseEntityFromVehicleCommandResultAssembler {

    private static HttpStatus statusFromFailure(VehicleCommandFailure failure) {
        return switch (failure) {
            case VehicleCommandFailure.FleetNotFound fleetNotFound -> HttpStatus.NOT_FOUND;
            case VehicleCommandFailure.DuplicatePlate duplicatePlate -> HttpStatus.CONFLICT;
        };
    }

    private static String localizedMessage(MessageSource messageSource, String messageKey) {
        return messageSource.getMessage(messageKey, null, messageKey, LocaleContextHolder.getLocale());
    }

    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<Vehicle, VehicleCommandFailure> result,
            MessageSource messageSource) {
        return result.fold(
                vehicle -> ResponseEntity
                        .created(URI.create("/api/v1/vehicles/" + vehicle.getId()))
                        .body(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle)),
                failure -> {
                    var status = statusFromFailure(failure);
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(
                            status,
                            localizedMessage(messageSource, failure.messageKey())));
                });
    }
}