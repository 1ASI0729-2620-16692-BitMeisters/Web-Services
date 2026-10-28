package com.bitmeisters.fleetsafe.platform.documentation.interfaces.rest.transform;

import com.bitmeisters.fleetsafe.platform.documentation.domain.model.aggregates.VehicleDocument;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public class ResponseEntityFromVehicleDocumentQueryResultAssembler {

    public static ResponseEntity<?> toResponseEntityFromList(List<VehicleDocument> vehicleDocuments, LocalDate today) {
        var vehicleDocumentResources = vehicleDocuments.stream()
                .map(vehicleDocument -> VehicleDocumentResourceFromEntityAssembler.toResourceFromEntity(vehicleDocument, today))
                .toList();
        return ResponseEntity.ok(vehicleDocumentResources);
    }
}
