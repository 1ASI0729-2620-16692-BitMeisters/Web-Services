package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class InspectionIdAttributeConverter implements AttributeConverter<InspectionId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(InspectionId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public InspectionId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : new InspectionId(dbData);
    }
}
