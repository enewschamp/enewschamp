package com.enewschamp.app.common;

import java.time.LocalDate;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.NewsArticlePageData;
import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.app.article.page.dto.PublicationPageData;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "BusinessRulesPlugin")
public class BusinessRulesPlugin implements IPageNavRuleDataPlugin {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UserLoginService loginService;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentSubscriptionService studentSubscriptionService;

	@Autowired
	StudentControlService studentControlService;

	@Autowired
	StudentControlWorkService studentControlWorkService;

	@Autowired
	StudentPaymentWorkService studentPaymentWorkService;

	@Autowired
	StudentPaymentService studentPaymentService;

	@Autowired
	StudentRegistrationService studentRegistrationService;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	private static final long serialVersionUID = 1L;

	public Map<String, String> initializeRuleVariables(PageRequestDTO pageRequest, PageDTO pageResponse, String ruleStr,
			Map<String, String> dataMap) {
		if (ruleStr.contains("validateLogin")) {
			dataMap = validateLoginCredentials(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("operation")) {
			dataMap = getIncompleteOperation(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("isAccountLocked")) {
			dataMap = isAccountLocked(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("isAccountInactive")) {
			dataMap = isAccountInactive(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("subscriptionType")) {
			dataMap = getSubscriptionType(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("evalAvailed")) {
			dataMap = evalAvailed(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("registrationCompletionGraceDaysLapsed")) {
			dataMap = registrationCompletionGraceDaysLapsed(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("studentDetails")) {
			dataMap = studentDetails(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("subscriptionTypeW")) {
			dataMap = subscriptionTypeW(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("nextDayPublicationAvailable")) {
			dataMap = nextDayPublicationAvailable(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("previousDayPublicationAvailable")) {
			dataMap = previousDayPublicationAvailable(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("nextNewsArticleAvailable")) {
			dataMap = nextNewsArticleAvailable(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("previousNewsArticleAvailable")) {
			dataMap = previousNewsArticleAvailable(pageRequest, pageResponse, dataMap);
		}
		return dataMap;

	}

	public Map<String, String> validateLoginCredentials(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String validateLogin = "Fail";
		String emailId = pageRequest.getHeader().getEmailId();
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		if (studentEntity != null && "Y".equals(studentEntity.getEmailVerified())) {
			String deviceId = pageRequest.getHeader().getDeviceId();
			String tokenId = pageRequest.getHeader().getLoginCredentials();
			UserLogin loggedIn = loginService.getUserLogin(emailId, deviceId, tokenId, UserType.S);
			if (loggedIn != null) {
				validateLogin = "Pass";
			}
		}
		dataMap.put("validateLogin", validateLogin);
		return dataMap;

	}

	public Map<String, String> registrationCompletionGraceDaysLapsed(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		String registrationCompletionGraceDaysLapsed = "";
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		if (studentEntity != null) {
			registrationCompletionGraceDaysLapsed = (studentEntity != null && studentEntity.getOperationDateTime()
					.plusDays(Integer
							.valueOf(propertiesService.getValue(PropertyConstants.REGISTRATION_COMPLETION_GRACE_DAYS)))
					.toLocalDate().isBefore(LocalDate.now())) ? "Y" : "N";
		}
		dataMap.put("registrationCompletionGraceDaysLapsed", registrationCompletionGraceDaysLapsed);
		dataMap = getIncompleteOperation(pageRequest, page, dataMap);
		return dataMap;
	}

	public Map<String, String> studentDetails(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		String studentDetails = "";
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		if (studentEntity != null) {
			studentDetails = (studentEntity != null && "Y".equalsIgnoreCase(studentEntity.getStudentDetails())) ? "Y"
					: "N";
		}
		dataMap.put("studentDetails", studentDetails);
		dataMap = getIncompleteOperation(pageRequest, page, dataMap);
		return dataMap;
	}

	public Map<String, String> getIncompleteOperation(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		String operation = "";
		if (emailId != "" && emailId != null) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if ((studentControlWorkDTO != null)) {
				operation = studentControlWorkDTO.getNextPageOperation();
			}
		}
		dataMap.put("operation", operation);
		return dataMap;
	}

	public Map<String, String> getRegistrationNextPage(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		StudentControlWork studentEntityWork = studentControlWorkService.getStudentByEmail(emailId);
		dataMap.put("subscriptionTypeW", studentEntityWork != null ? studentEntityWork.getSubscriptionTypeW() : "");
		dataMap.put("studentDetails",
				(studentEntity != null && "Y".equalsIgnoreCase(studentEntity.getStudentDetails())) ? "Y" : "N");
		dataMap.put("schoolDetails",
				(studentEntity != null && "Y".equalsIgnoreCase(studentEntity.getSchoolDetails())) ? "Y" : "N");
		dataMap.put("subscriptionType", studentEntity != null ? studentEntity.getSubscriptionType() : "");
		return dataMap;
	}

	public Map<String, String> getSubscriptionType(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String subscriptionType = "";
		String emailId = pageRequest.getHeader().getEmailId();
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		if (studentEntity != null) {
			subscriptionType = studentEntity.getSubscriptionType();
		}
		dataMap.put("subscriptionType", subscriptionType);
		return dataMap;
	}

	public Map<String, String> subscriptionTypeW(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String subscriptionTypeW = "";
		String emailId = pageRequest.getHeader().getEmailId();
		StudentControlWork studentEntityWork = studentControlWorkService.getStudentByEmail(emailId);
		if (studentEntityWork != null) {
			subscriptionTypeW = studentEntityWork.getSubscriptionTypeW();
		}
		dataMap.put("subscriptionTypeW", subscriptionTypeW);
		return dataMap;

	}

	public Map<String, String> evalAvailed(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		String evalAvailed = "N";
		String emailId = pageRequest.getHeader().getEmailId();
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		if (studentEntity != null) {
			evalAvailed = studentEntity.getEvalAvailed() != null ? studentEntity.getEvalAvailed() : "N";
		}
		dataMap.put("evalAvailed", evalAvailed);
		return dataMap;
	}

	public Map<String, String> isAccountLocked(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		StudentRegistration studentRegistration = studentRegistrationService.getStudentReg(emailId);
		String isAccountLocked = "N";
		if (studentRegistration != null && "Y".equals(studentRegistration.getIsAccountLocked())) {
			isAccountLocked = "Y";
		}
		dataMap.put("isAccountLocked", isAccountLocked);
		return dataMap;
	}

	public Map<String, String> isAccountInactive(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		StudentRegistration studentRegistration = studentRegistrationService.getStudentReg(emailId);
		String isAccountInactive = "N";
		if (studentRegistration != null && "N".equals(studentRegistration.getIsActive())) {
			isAccountInactive = "Y";
		}
		dataMap.put("isAccountInactive", isAccountInactive);
		return dataMap;
	}

	public Map<String, String> previousDayPublicationAvailable(PageRequestDTO pageRequest, PageDTO pageResponse,
			Map<String, String> dataMap) {
		String previousDayPublicationAvailable = "False";
		PublicationPageData pageData = new PublicationPageData();
		try {
			if (pageResponse.getData() != null) {
				pageData = modelMapper.map(pageResponse.getData(), PublicationPageData.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pageData.getNewsArticles() != null && pageData.getNewsArticles().size() > 0) {
			PublicationData article = pageData.getNewsArticles().get(0);
			LocalDate publicationDate = article.getPublicationDate();
			int readingLevel = article.getReadingLevel();
			ArticleType articleType = ArticleType.NEWSARTICLE;
			String editionId = pageRequest.getHeader().getEditionId();
			LocalDate newDate = newsArticleService.getPreviousAvailablePublicationDate(publicationDate, editionId,
					readingLevel, articleType);
			if (newDate != null) {
				previousDayPublicationAvailable = "True";
			}
		}
		dataMap.put("previousDayPublicationAvailable", previousDayPublicationAvailable);
		return dataMap;
	}

	public Map<String, String> nextDayPublicationAvailable(PageRequestDTO pageRequest, PageDTO pageResponse,
			Map<String, String> dataMap) {
		String nextDayPublicationAvailable = "False";
		PublicationPageData pageData = new PublicationPageData();
		try {
			if (pageResponse.getData() != null) {
				pageData = modelMapper.map(pageResponse.getData(), PublicationPageData.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pageData.getNewsArticles() != null && pageData.getNewsArticles().size() > 0) {
			PublicationData article = pageData.getNewsArticles().get(0);
			LocalDate publicationDate = article.getPublicationDate();
			int readingLevel = article.getReadingLevel();
			ArticleType articleType = ArticleType.NEWSARTICLE;
			String editionId = pageRequest.getHeader().getEditionId();
			LocalDate newDate = newsArticleService.getNextAvailablePublicationDate(publicationDate, editionId,
					readingLevel, articleType);
			if (newDate != null) {
				nextDayPublicationAvailable = "True";
			}
		}
		dataMap.put("nextDayPublicationAvailable", nextDayPublicationAvailable);
		return dataMap;
	}

	public Map<String, String> nextNewsArticleAvailable(PageRequestDTO pageRequest, PageDTO pageResponse,
			Map<String, String> dataMap) {
		String nextNewsArticleAvailable = "False";
		NewsArticlePageData pageData = new NewsArticlePageData();
		try {
			if (pageResponse.getData() != null) {
				pageData = modelMapper.map(pageResponse.getData(), NewsArticlePageData.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDate publicationDate = pageData.getPublicationDate();
		Long newsArticleId = pageData.getNewsArticleId();
		int readingLevel = pageData.getReadingLevel();
		ArticleType articleType = ArticleType.NEWSARTICLE;
		String editionId = pageRequest.getHeader().getEditionId();
		Long nextNewsArticleId = newsArticleService.getNextNewsArticleAvailable(publicationDate, editionId,
				readingLevel, articleType, newsArticleId);
		if (nextNewsArticleId != null && nextNewsArticleId > 0) {
			nextNewsArticleAvailable = "True";
		}
		dataMap.put("nextNewsArticleAvailable", nextNewsArticleAvailable);
		return dataMap;
	}

	public Map<String, String> previousNewsArticleAvailable(PageRequestDTO pageRequest, PageDTO pageResponse,
			Map<String, String> dataMap) {
		String prevNewsArticleAvailable = "False";
		NewsArticlePageData pageData = new NewsArticlePageData();
		try {
			if (pageResponse.getData() != null) {
				pageData = modelMapper.map(pageResponse.getData(), NewsArticlePageData.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDate publicationDate = pageData.getPublicationDate();
		Long newsArticleId = pageData.getNewsArticleId();
		int readingLevel = pageData.getReadingLevel();
		ArticleType articleType = ArticleType.NEWSARTICLE;
		String editionId = pageRequest.getHeader().getEditionId();
		Long previousNewsArticleId = newsArticleService.getPreviousNewsArticleAvailable(publicationDate, editionId,
				readingLevel, articleType, newsArticleId);
		if (previousNewsArticleId != null && previousNewsArticleId > 0) {
			prevNewsArticleAvailable = "True";
		}
		dataMap.put("previousNewsArticleAvailable", prevNewsArticleAvailable);
		return dataMap;
	}

	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
