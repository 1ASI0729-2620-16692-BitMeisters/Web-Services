package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.InspectionResultId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter
public class InspectionResultIdAttributeConverter implements AttributeConverter<InspectionResultId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(InspectionResultId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public InspectionResultId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : new InspectionResultId(dbData);
    }
}
