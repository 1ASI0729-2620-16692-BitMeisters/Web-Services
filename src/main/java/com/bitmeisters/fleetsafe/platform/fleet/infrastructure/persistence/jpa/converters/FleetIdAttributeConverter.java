package com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.converters;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.FleetId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class FleetIdAttributeConverter implements AttributeConverter<FleetId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(FleetId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public FleetId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : new FleetId(dbData);
    }
}