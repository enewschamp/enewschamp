package com.enewschamp;


import java.time.LocalDate;
import java.time.LocalDateTime;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;

import com.enewschamp.domain.common.LocalDateDeserializer;
import com.enewschamp.domain.common.LocalDateSerializer;
import com.enewschamp.domain.common.LocalDateTimeDeserializer;
import com.enewschamp.domain.common.LocalDateTimeSerializer;
import com.enewschamp.publication.domain.service.PublicationSummaryRepositoryCustom;
import com.enewschamp.publication.domain.service.PublicationSummaryRepositoryImpl;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

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
	public PublicationSummaryRepositoryCustom publicationSummaryCustomRepository() {
		return new PublicationSummaryRepositoryImpl();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper()
			      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleModule module = new SimpleModule();
		module.addSerializer(LocalDate.class, new LocalDateSerializer());
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
		mapper.registerModule(module);
		mapper.registerModule(new ProblemModule());
		
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		return mapper;
	}
    
//	@Bean(name = "javers")
//	@ConditionalOnMissingBean
//	public Javers javers(JaversSqlRepository sqlRepository, PlatformTransactionManager transactionManager) {
//	    return TransactionalJaversBuilder
//	            .javers()
//	            .withTxManager(transactionManager)
//	            .registerJaversRepository(sqlRepository)
//	            .withObjectAccessHook(new HibernateUnproxyObjectAccessHook())
//	            .withNewObjectsSnapshot(false)
//	            .withPrettyPrint(true)
//	            .build();
//	}
	
//	@Bean
//    public AuthorProvider authorProvider() {
//        return new OperatorIdProvider();
//    }
}
