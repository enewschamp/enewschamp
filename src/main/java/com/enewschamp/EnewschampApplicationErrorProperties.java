package com.enewschamp;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties
@PropertySource("classpath:error-messages.properties")
public class EnewschampApplicationErrorProperties {

	private Map<String, String> errorMessagesConfig;;
}
