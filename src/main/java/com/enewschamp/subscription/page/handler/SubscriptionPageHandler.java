package com.enewschamp.subscription.page.handler;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationErrorProperties;
import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageData;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SubscriptionPageHandler")
public class SubscriptionPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	EnewschampApplicationErrorProperties errorProperties;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String action = pageNavigationContext.getActionName();
<<<<<<< Updated upstream
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionID();
=======
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
>>>>>>> Stashed changes
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(eMailId);
		StudentControlWorkDTO studentControlWorkDTO = null;
		Long studentId = 0L;

		if (PageAction.next.toString().equalsIgnoreCase(action)
				|| PageAction.GoPremium.toString().equalsIgnoreCase(action)
				|| PageAction.PicImage.toString().equalsIgnoreCase(action)
				|| PageAction.ClickArticleImage.toString().equalsIgnoreCase(action)
				|| PageAction.CreateAccount.toString().equalsIgnoreCase(action)) {
			// add static data from proerties
			StudentSubscriptionPageData subscripionPagedata = new StudentSubscriptionPageData();
			if (pageNavigationContext.getPageRequest().getData().get("emailId") != null) {
				subscripionPagedata
						.setEmailID(pageNavigationContext.getPageRequest().getData().get("emailId").asText());
			}
			subscripionPagedata.setTerms(appConfig.getSubscriptionText().get("TermsOfUseText"));
			subscripionPagedata.setIncompeleteFormText(appConfig.getSubscriptionText().get("incompeleteFormText"));
			subscripionPagedata.setPrivacyPolicy(appConfig.getSubscriptionText().get("privacyPolicy"));
			subscripionPagedata.setWhatYouGetTextPremium(appConfig.getSubscriptionText().get("whatYouGetTextPremium"));
			subscripionPagedata.setWhatYouGetTextSchool(appConfig.getSubscriptionText().get("whatYouGetTextSchool"));
			subscripionPagedata
					.setWhatYouGetTextStandard(appConfig.getSubscriptionText().get("whatYouGetTextStandard"));
			pageDto.setData(subscripionPagedata);

		} else if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.Subscription.toString().equalsIgnoreCase(action)) {
			// if the user is new then fetch from work table
			if (studentControlDTO == null) {
				studentControlWorkDTO = studentControlBusiness.getStudentFromWork(eMailId);
				if (studentControlWorkDTO != null) {
					studentId = studentControlWorkDTO.getStudentID();
					StudentSubscriptionWorkDTO studentSubscriptionWorkDTO = subscriptionBusiness
							.getStudentSubscriptionFromWork(studentId, editionId);

					StudentSubscriptionPageData subscripionPagedata = modelMapper.map(studentSubscriptionWorkDTO,
							StudentSubscriptionPageData.class);
					subscripionPagedata.setEmailID(studentControlWorkDTO.getEmailID());
					// add static data from proerties
					subscripionPagedata.setTerms(appConfig.getSubscriptionText().get("TermsOfUseText"));
					subscripionPagedata
							.setIncompeleteFormText(appConfig.getSubscriptionText().get("incompeleteFormText"));
					subscripionPagedata.setPrivacyPolicy(appConfig.getSubscriptionText().get("privacyPolicy"));
					subscripionPagedata
							.setWhatYouGetTextPremium(appConfig.getSubscriptionText().get("whatYouGetTextPremium"));
					subscripionPagedata
							.setWhatYouGetTextStandard(appConfig.getSubscriptionText().get("whatYouGetTextStandard"));
					pageDto.setData(subscripionPagedata);
				}

			} else {

				StudentSubscriptionDTO studentSubscriptionDTO = subscriptionBusiness
						.getStudentSubscriptionFromMaster(studentControlDTO.getStudentID(), editionId);

				StudentSubscriptionPageData subscripionPagedata = modelMapper.map(studentSubscriptionDTO,
						StudentSubscriptionPageData.class);
				subscripionPagedata.setEmailID(studentControlDTO.getEmailID());
				// add static data from proerties
				subscripionPagedata.setTerms(appConfig.getSubscriptionText().get("TermsOfUseText"));
				subscripionPagedata.setIncompeleteFormText(appConfig.getSubscriptionText().get("incompeleteFormText"));
				subscripionPagedata.setPrivacyPolicy(appConfig.getSubscriptionText().get("privacyPolicy"));
				subscripionPagedata
						.setWhatYouGetTextPremium(appConfig.getSubscriptionText().get("whatYouGetTextPremium"));
				subscripionPagedata
						.setWhatYouGetTextStandard(appConfig.getSubscriptionText().get("whatYouGetTextStandard"));
				pageDto.setData(subscripionPagedata);

			}
		}

		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String eidtionId = pageRequest.getHeader().getEditionId();

		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		Long studentId = 0L;
		if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentID();
		}

		subscriptionBusiness.saveWorkToMaster(studentId, eidtionId);
		studentControlBusiness.workToMaster(studentId);

		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String operation = pageRequest.getHeader().getOperation();
		if (PageAction.SubscriptionNext.toString().equals(actionName)) {
			String saveIn = pageNavigatorDTO.getUpdationTable();
			if (PageSaveTable.W.toString().equals(saveIn)) {
				StudentSubscriptionPageData subscripionPagedata = mapPagedata(pageRequest);
				String emailId = subscripionPagedata.getEmailID();
				if (studentControlBusiness.getStudentFromMaster(emailId) == null
						&& studentControlBusiness.getStudentFromWork(emailId) == null) {
					Long studentId = 0L;
					StudentControlWorkDTO studentControlWorkDto = new StudentControlWorkDTO();
					studentControlWorkDto.setOperation(operation);
					studentControlWorkDto.setEmailID(emailId);
					studentControlWorkDto.setSubscriptionTypeW(subscripionPagedata.getSubscriptionSelected());
					StudentControlWork studentControlWork = studentControlBusiness.saveAsWork(studentControlWorkDto);
					studentId = studentControlWork.getStudentID();
					subscriptionBusiness.saveAsWork(studentId, pageRequest);
				} else {
					pageDTO.setData(subscripionPagedata);
					pageDTO.setErrorMessage(
							errorProperties.getErrorMessagesConfig().get(ErrorCodes.STUD_ALREADY_REGISTERED));
				}
			}
			if (PageSaveTable.M.toString().equals(saveIn)) {

				Long studentId = 0L;
				StudentSubscriptionPageData subscripionPagedata = null;

				subscripionPagedata = mapPagedata(pageRequest);

				String emailId = subscripionPagedata.getEmailID();
				StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
				if (studentControlDTO == null) {
					// create student control in work
					StudentControlDTO studentControlDto = new StudentControlDTO();
					studentControlDto.setEmailID(subscripionPagedata.getEmailID());
					studentControlDto.setSubscriptionType(subscripionPagedata.getSubscriptionSelected());
					StudentControl studentControlEntity = studentControlBusiness.saveAsMaster(studentControlDto);

					studentId = studentControlEntity.getStudentID();
					subscriptionBusiness.saveAsMaster(studentId, pageRequest);
				}

			}
		} else if (PageAction.SubscriptionPrevious.toString().equals(actionName)) {
			// do nothing
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public StudentSubscriptionPageData mapPagedata(PageRequestDTO pageRequest) {
		StudentSubscriptionPageData studentSubscriptionPageData = null;
		try {
			studentSubscriptionPageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentSubscriptionPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);
		}
		return studentSubscriptionPageData;
	}
}
