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
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
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
	StudentRegistrationService regService;

	@Autowired
	private PropertiesBackendService propertiesService;

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
		if (ruleStr.contains("pageName")) {
			dataMap = getIncompletePageName(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("isAccountLocked")) {
			dataMap = isAccountLocked(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("isAccountInactive")) {
			dataMap = isAccountInactive(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("forcePasswordChange")) {
			dataMap = forcePasswordChange(pageRequest, pageResponse, dataMap);
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
		if (ruleStr.contains("paymentSuccess")) {
			dataMap = paymentSuccess(pageRequest, pageResponse, dataMap);
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
		if (ruleStr.contains("quizAvailable")) {
			dataMap = quizAvailable(pageRequest, pageResponse, dataMap);
		}
		if (ruleStr.contains("autoRenewal")) {
			dataMap = autoRenewal(pageRequest, pageResponse, dataMap);
		}
		return dataMap;

	}

	private Map<String, String> autoRenewal(PageRequestDTO pageRequest, PageDTO pageResponse,
			Map<String, String> dataMap) {
		String autoRenewal = "N";
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControl studentEntity = studentControlService.get(studentId);
		if (studentEntity != null) {
			StudentSubscription studentSubscription = studentSubscriptionService.get(studentEntity.getStudentId(),
					editionId);
			if (studentSubscription != null) {
				autoRenewal = studentSubscription.getAutoRenewal();
			}
		}
		dataMap.put("autoRenewal", autoRenewal);
		return dataMap;
	}

	public Map<String, String> validateLoginCredentials(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String validateLogin = "Fail";
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControl studentEntity = studentControlService.get(studentId);
		if (studentEntity != null && "Y".equals(studentEntity.getEmailIdVerified())) {
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
		Long studentId = pageRequest.getHeader().getStudentId();
		String registrationCompletionGraceDaysLapsed = "";
		StudentControl studentEntity = studentControlService.get(studentId);
		if (studentEntity != null) {
			StudentRegistration studentRegistration = regService.getStudentReg(emailId);
			registrationCompletionGraceDaysLapsed = (studentRegistration.getCreationDateTime()
					.plusDays(Integer.valueOf(propertiesService.getValue(pageRequest.getHeader().getModule(),
							PropertyConstants.STUDENT_REGISTRATION_COMPLETION_GRACE_DAYS)))
					.toLocalDate().isBefore(LocalDate.now())) ? "Y" : "N";
		}
		dataMap.put("registrationCompletionGraceDaysLapsed", registrationCompletionGraceDaysLapsed);
		dataMap = getIncompleteOperation(pageRequest, page, dataMap);
		return dataMap;
	}

	public Map<String, String> studentDetails(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		Long studentId = pageRequest.getHeader().getStudentId();
		String studentDetails = "";
		StudentControl studentEntity = studentControlService.get(studentId);
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
		Long studentId = pageRequest.getHeader().getStudentId();
		String operation = "";
		if (studentId != null && studentId > 0) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
			if ((studentControlWorkDTO != null)) {
				operation = studentControlWorkDTO.getNextPageOperation();
			}
		}
		dataMap.put("operation", operation);
		return dataMap;
	}

	public Map<String, String> getIncompletePageName(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		Long studentId = pageRequest.getHeader().getStudentId();
		String pageName = "";
		if (studentId != null && studentId > 0) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
			if ((studentControlWorkDTO != null)) {
				pageName = studentControlWorkDTO.getNextPageName();
			}
		}
		dataMap.put("pageName", pageName);
		return dataMap;
	}

	public Map<String, String> getRegistrationNextPage(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControl studentEntity = studentControlService.get(studentId);
		StudentControlWork studentEntityWork = studentControlWorkService.get(studentId);
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
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControl studentEntity = studentControlService.get(studentId);
		if (studentEntity != null) {
			subscriptionType = studentEntity.getSubscriptionType();
		}
		dataMap.put("subscriptionType", subscriptionType);
		return dataMap;
	}

	public Map<String, String> subscriptionTypeW(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String subscriptionTypeW = "F";
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControlWork studentEntityWork = studentControlWorkService.get(studentId);
		if (studentEntityWork != null) {
			subscriptionTypeW = (studentEntityWork.getSubscriptionTypeW() != null
					? studentEntityWork.getSubscriptionTypeW()
					: "F");
		}
		dataMap.put("subscriptionTypeW", subscriptionTypeW);
		return dataMap;

	}

	public Map<String, String> evalAvailed(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		String evalAvailed = "N";
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControl studentEntity = studentControlService.get(studentId);
		if (studentEntity != null) {
			evalAvailed = studentEntity.getEvalAvailed() != null ? studentEntity.getEvalAvailed() : "N";
		}
		dataMap.put("evalAvailed", evalAvailed);
		return dataMap;
	}

	public Map<String, String> paymentSuccess(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		String paymentSuccess = "N";
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = pageRequest.getHeader().getStudentId();
		StudentControl studentEntity = studentControlService.get(studentId);
		if (studentEntity != null) {
			StudentPaymentWork studentPaymentWork = studentPaymentWorkService
					.getSuccessTransactionByStudentIdAndEdition(studentEntity.getStudentId(), editionId);
			if (studentPaymentWork != null) {
				paymentSuccess = "Y";
			}
		}
		dataMap.put("paymentSuccess", paymentSuccess);
		return dataMap;
	}

	public Map<String, String> isAccountLocked(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		StudentRegistration studentRegistration = regService.getStudentReg(emailId);
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
		StudentRegistration studentRegistration = regService.getStudentReg(emailId);
		String isAccountInactive = "N";
		if (studentRegistration != null && "N".equals(studentRegistration.getIsActive())) {
			isAccountInactive = "Y";
		}
		dataMap.put("isAccountInactive", isAccountInactive);
		return dataMap;
	}

	public Map<String, String> forcePasswordChange(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String emailId = pageRequest.getHeader().getEmailId();
		StudentRegistration studentRegistration = regService.getStudentReg(emailId);
		String forcePasswordChange = "N";
		if (studentRegistration != null && "Y".equals(studentRegistration.getForcePasswordChange())) {
			forcePasswordChange = "Y";
		}
		dataMap.put("forcePasswordChange", forcePasswordChange);
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
			String emailId = pageRequest.getHeader().getEmailId();
			String isTestUser = "";
			StudentRegistration studReg = regService.getStudentReg(emailId);
			if (studReg != null) {
				isTestUser = studReg.getIsTestUser();
			}
			LocalDate newDate = newsArticleService.getPreviousAvailablePublicationDate(publicationDate, isTestUser,
					editionId, readingLevel, articleType);
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
			String emailId = pageRequest.getHeader().getEmailId();
			String isTestUser = "";
			StudentRegistration studReg = regService.getStudentReg(emailId);
			if (studReg != null) {
				isTestUser = studReg.getIsTestUser();
			}
			LocalDate newDate = newsArticleService.getNextAvailablePublicationDate(publicationDate, isTestUser,
					editionId, readingLevel, articleType);
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
		String emailId = pageRequest.getHeader().getEmailId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
		Long nextNewsArticleId = newsArticleService.getNextNewsArticleAvailable(publicationDate, isTestUser, editionId,
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
		String emailId = pageRequest.getHeader().getEmailId();
		String isTestUser = "";
		StudentRegistration studReg = regService.getStudentReg(emailId);
		if (studReg != null) {
			isTestUser = studReg.getIsTestUser();
		}
		Long previousNewsArticleId = newsArticleService.getPreviousNewsArticleAvailable(publicationDate, isTestUser,
				editionId, readingLevel, articleType, newsArticleId);
		if (previousNewsArticleId != null && previousNewsArticleId > 0) {
			prevNewsArticleAvailable = "True";
		}
		dataMap.put("previousNewsArticleAvailable", prevNewsArticleAvailable);
		return dataMap;
	}

	public Map<String, String> quizAvailable(PageRequestDTO pageRequest, PageDTO pageResponse,
			Map<String, String> dataMap) {
		NewsArticlePageData pageData = new NewsArticlePageData();
		try {
			if (pageResponse.getData() != null) {
				pageData = modelMapper.map(pageResponse.getData(), NewsArticlePageData.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long newsArticleId = pageData.getNewsArticleId();
		String quizAvailable = newsArticleService.isQuizAvailable(newsArticleId);
		dataMap.put("quizAvailable", quizAvailable);
		return dataMap;
	}

	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		// TODO Auto-generated method stub
		return null;
	}

}