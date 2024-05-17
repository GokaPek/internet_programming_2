package com.example.demo.core.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.core.model.BaseEntity;

@Configuration
public class MapperConfiguration {
    @Bean
    ModelMapper modelMapper() {
        final ModelMapper mapper = new ModelMapper();
        mapper.addMappings(new PropertyMap<Object, BaseEntity>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        return mapper;
    }
}
