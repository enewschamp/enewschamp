package com.enewschamp.app.common.uicontrols.rules.plugin;

import java.time.LocalDate;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.rules.plugin.IPageNavRuleDataPlugin;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "PublicationRulePlugin")
public class PublicationRulePlugin implements IPageNavRuleDataPlugin {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;

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
	private EnewschampApplicationProperties appConfig;

	/**
	 * != null ? studentControlWorkDTO.getSubscriptionType() : "");
	 */
	private static final long serialVersionUID = 1L;

	public Map<String, String> checkIfIncompleteRegistration(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
<<<<<<< Updated upstream
		String emailId = pageRequest.getHeader().getEmailID();
=======
		String emailId = pageRequest.getHeader().getEmailId();
>>>>>>> Stashed changes
		// String deviceId = pageRequest.getHeader().getDeviceId();
		dataMap.put("Fn_IncompleteRegistrationByStudent",
				isIncompleteUserRegistration(emailId, pageRequest) ? "True" : "False");
		return dataMap;
	}

	public Map<String, String> getNextPageIncompleteRegistration(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
<<<<<<< Updated upstream
		String emailId = pageRequest.getHeader().getEmailID();
		String editionId = pageRequest.getHeader().getEditionID();
=======
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
>>>>>>> Stashed changes
		StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
		StudentControlWork studentEntityWork = studentControlWorkService.getStudentByEmail(emailId);
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService
				.getByStudentIdAndEdition(studentEntityWork.getStudentID(), editionId);
		dataMap.put("unAuthOperation", studentEntityWork != null ? studentEntityWork.getOperation() : "");
		dataMap.put("unAuthSubscriptionType",
				studentEntityWork != null ? studentEntityWork.getSubscriptionTypeW() : "");
		dataMap.put("unAuthPayment", studentPaymentWork != null ? "Y" : "N");
		dataMap.put("unAuthStudentDetails",
				(studentEntityWork != null && "Y".equalsIgnoreCase(studentEntityWork.getStudentDetailsW())) ? "Y"
						: "N");
		dataMap.put("unAuthSchoolDetails",
				(studentEntityWork != null && "Y".equalsIgnoreCase(studentEntityWork.getSchoolDetailsW())) ? "Y" : "N");
		dataMap.put("authSubscriptionType", studentEntity != null ? studentEntity.getSubscriptionType() : "");
		dataMap.put("authStudentDetails",
				(studentEntity != null && "Y".equalsIgnoreCase(studentEntity.getStudentDetails())) ? "Y" : "N");
		dataMap.put("authSchoolDetails",
				(studentEntity != null && "Y".equalsIgnoreCase(studentEntity.getSchoolDetails())) ? "Y" : "N");
		dataMap.put("preferences",
				(studentEntity != null && "Y".equalsIgnoreCase(studentEntity.getPreferences())) ? "Y" : "N");
		dataMap.put("emailVerified",
				(studentEntity != null && "Y".equalsIgnoreCase(studentEntity.getEmailVerified())) ? "Y" : "N");
		dataMap.put("registrationCompletionGraceDaysLapsed",
				(studentEntity != null
						&& studentEntity.getOperationDateTime().plusDays(appConfig.getRegistrationCompletionGraceDays())
								.toLocalDate().isBefore(LocalDate.now())) ? "Y" : "N");
		return dataMap;
	}

	public boolean isIncompleteUserRegistration(String emailId, PageRequestDTO pageRequest) {
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		boolean flag = false;
		if ((studentControlWorkDTO != null)) {
			flag = true;
		} else {
			StudentControl studentEntity = studentControlService.getStudentByEmail(emailId);
			if ("E".equalsIgnoreCase((studentEntity.getSubscriptionType()))
					|| "F".equalsIgnoreCase(studentEntity.getSubscriptionType())) {
				if (studentEntity.getOperationDateTime().plusDays(appConfig.getRegistrationCompletionGraceDays())
						.toLocalDate().isBefore(LocalDate.now())) {
					if (studentEntity.getSchoolDetails() != "Y" || studentEntity.getSchoolDetails() != "Y"
							|| studentEntity.getEmailVerified() != "Y") {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	public Map<String, String> checkPrevDayPublications(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String previousDayPublicationsAvailable = "False";
<<<<<<< Updated upstream
		String editionId = pageRequest.getHeader().getEditionID();
		LocalDate publicationDate = pageRequest.getHeader().getPublicationdate();
=======
		String editionId = pageRequest.getHeader().getEditionId();
		LocalDate publicationDate = pageRequest.getHeader().getPublicationDate();
>>>>>>> Stashed changes
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		LocalDate newDate = publicationDate.minusDays(1);
		searchRequestData.setPublicationDate(newDate);
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
				pageRequest.getHeader());
		if (pageResult.getNumberOfElements() > 0) {
			previousDayPublicationsAvailable = "True";
		}
		dataMap.put("previousDayPublicationsAvailable", previousDayPublicationsAvailable);
		return dataMap;
	}

	public Map<String, String> checkNextDayPublications(PageRequestDTO pageRequest, PageDTO page,
			Map<String, String> dataMap) {
		String nextDayPublicationsAvailable = "False";
<<<<<<< Updated upstream
		String editionId = pageRequest.getHeader().getEditionID();
		LocalDate publicationDate = pageRequest.getHeader().getPublicationdate();
=======
		String editionId = pageRequest.getHeader().getEditionId();
		LocalDate publicationDate = pageRequest.getHeader().getPublicationDate();
>>>>>>> Stashed changes
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		LocalDate newDate = publicationDate.plusDays(1);
		searchRequestData.setPublicationDate(newDate);
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData,
				pageRequest.getHeader());
		if (pageResult.getNumberOfElements() > 0) {
			nextDayPublicationsAvailable = "True";
		}
		dataMap.put("nextDayPublicationsAvailable", nextDayPublicationsAvailable);
		return dataMap;
	}

	@Override
	public Map<String, String> loadPluginData(PageRequestDTO pageRequest, PageDTO page, Map<String, String> dataMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
