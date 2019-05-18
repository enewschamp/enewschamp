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
	public static class PageBuilderMapping {
		@NotNull
		private String pageName;
		@NotNull
		private String builderName;
	}
	
	private Map<String, String> pageBuilderConfig;
	
	private Map<String, Map<String, String>> pageNavigationConfig;
}
