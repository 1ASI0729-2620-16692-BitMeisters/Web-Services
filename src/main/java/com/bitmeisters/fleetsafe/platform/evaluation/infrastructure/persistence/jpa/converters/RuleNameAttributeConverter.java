package com.bitmeisters.fleetsafe.platform.evaluation.infrastructure.persistence.jpa.converters;

import com.bitmeisters.fleetsafe.platform.evaluation.domain.model.valueobjects.RuleName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RuleNameAttributeConverter implements AttributeConverter<RuleName, String> {

    @Override
    public String convertToDatabaseColumn(RuleName attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public RuleName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new RuleName(dbData);
    }
}
