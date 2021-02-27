package com.enewschamp;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("enewschamp.admin.audit")
public class EnewschampEntityConfig {
	private Map<String,  String> entityConfig;

}
