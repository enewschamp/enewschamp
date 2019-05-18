package com.enewschamp;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class EnewschampApplicationConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}
	
	@Bean(name="modelPatcher")
	public ModelMapper modelMapperForPatch() {
		ModelMapper modelMapper = new ModelMapper();
		// Null attributes are ignored from the source object
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		return modelMapper;
	}
	
	@Bean
    public ProblemModule problemModule() {
        return new ProblemModule();
    }
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper()
			      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}
    
}
