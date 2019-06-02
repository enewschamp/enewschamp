package com.enewschamp;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties
@PropertySource("classpath:error-messages.properties")
public class EnewschampApplicationErrorProperties {


	@Data
	public static class ErrorMessageConfig {
		@NotNull
		private String errorCode;
		@NotNull
		private String errorMessage;
	}
	
	
	private Map<String, String> errorMessagesConfig;;
}
