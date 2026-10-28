package com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.converters;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.valueobjects.Plate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PlateAttributeConverter implements AttributeConverter<Plate, String> {

    @Override
    public String convertToDatabaseColumn(Plate attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Plate convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Plate(dbData);
    }
}
