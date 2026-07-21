package com.api.vdtcommsws.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDToStringConverter implements AttributeConverter<UUID, String> {
  @Override
  public String convertToDatabaseColumn(UUID attribute) {
    return (attribute == null) ? null : attribute.toString();
  }
  
  @Override
  public UUID convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.trim().isEmpty()) {
      return null;
    }
    return UUID.fromString(dbData);
  }
}