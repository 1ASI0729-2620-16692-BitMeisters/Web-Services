package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionItemId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class InspectionItemIdAttributeConverter implements AttributeConverter<InspectionItemId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(InspectionItemId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public InspectionItemId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : new InspectionItemId(dbData);
    }
}
