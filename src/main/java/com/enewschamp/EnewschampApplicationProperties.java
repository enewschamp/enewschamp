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
	
	private Map<String, Map<String, String>> pageHandlerConfig;
	
	private Map<String, Map<String, Map<String, String>>> pageNavigationConfig;
	
	
	private Map<String, String> subscriptionText;
	private Map<String, String> studentDetailsPageText;
	private int evalDays;
	private int pageSize;
	private int otpExpirySecs;
	private int workingDays;
	private String incompleteFormText;
	private String premiumSubsMsg;
	private String helpDeskText;
	private String helpDeskEmail;
	private String otpMessage;
	private String registrationMessage;
	private String accountDeletionMessage;
	private String pwdResetMessage;
	private String shareMyAchievementsText;
	private String fromEmailId;
	private String emailPwd;
	private String emailHost;
	private String emailPort;
	private String otpEmailBodyText;
	private String otpEmailSubject;
	private Long monthLov;
	private String monthLovFormat;
	
	@Data
	public static class AuditConfig {
		private boolean includeSnapShots;
	}
	
	private AuditConfig audit;
	
	private Map<String, Map<String, Map<String, String>>> statusTransitionConfig;
}
