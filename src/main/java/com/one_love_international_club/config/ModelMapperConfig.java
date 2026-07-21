package com.one_love_international_club.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.PropertyInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    modelMapper.getConfiguration()
      .setFieldMatchingEnabled(true)
      .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
      .setMatchingStrategy(MatchingStrategies.STANDARD)
      .setSkipNullEnabled(true)
      .setPropertyCondition(context -> {
        String destFieldName = context.getMapping()
          .getDestinationProperties()
          .stream()
          .findFirst()
          .map(PropertyInfo::getName)
          .orElse("");

        Class<?> sourceType = context.getSourceType();
        Class<?> destinationType = context.getDestinationType();

        boolean isDtoToEntity = sourceType.getSimpleName().endsWith("Dto")
          && destinationType.getSimpleName().endsWith("Entity");

        return !isDtoToEntity || !"id".equalsIgnoreCase(destFieldName);
      });

    return modelMapper;
  }
}
