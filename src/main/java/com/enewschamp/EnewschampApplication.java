package com.enewschamp;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zalando.problem.ProblemModule;

@SpringBootApplication
public class EnewschampApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnewschampApplication.class, args);
	}

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
    
}
