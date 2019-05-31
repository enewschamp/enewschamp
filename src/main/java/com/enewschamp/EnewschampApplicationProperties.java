package com.enewschamp;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("enewschamp.config")
public class EnewschampApplicationProperties {


	@Data
	public static class PageHandlerMapping {
		@NotNull
		private String pageName;
		@NotNull
		private String handlerName;
	}
	
	private Map<String, String> pageHandlerConfig;
	
	private Map<String, Map<String, String>> pageNavigationConfig;
	
	
	@Data
	public static class AuditConfig {
		private boolean includeSnapShots;
	}
	
	private AuditConfig audit;
}
