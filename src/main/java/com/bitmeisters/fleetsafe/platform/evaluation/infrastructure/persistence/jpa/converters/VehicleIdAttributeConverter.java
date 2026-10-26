package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.VehicleId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class VehicleIdAttributeConverter implements AttributeConverter<VehicleId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(VehicleId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public VehicleId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : new VehicleId(dbData);
    }
}
