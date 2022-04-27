package com.samuel.distributed.brute.force.model.helpers;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ",";
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute == null ? null : String.join(DELIMITER, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(final String dbData) {
        return dbData == null ? Collections.emptyList() : Arrays.asList(dbData.split(DELIMITER));
    }
}