package com.enewschamp.subscription.app.fw.page.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.dto.UIControlsGlobalDTO;
import com.enewschamp.app.common.uicontrols.rules.UIControlsRuleExecutionService;
import com.enewschamp.app.common.uicontrols.service.UIControlsGlobalService;
import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.rules.PageNavRuleExecutionService;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentPreferencesWorkService;
import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.enewschamp.user.domain.service.UserService;

import lombok.extern.java.Log;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ui/v1")
public class AppPageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UIControlsService uiControlService;

	@Autowired
	UIControlsGlobalService uiControlGlobalService;

	@Autowired
	PageNavigationService pageNavigationService;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentPreferencesWorkService studentPreferencesWorkService;

	@Autowired
	StudentSubscriptionWorkService studentSubscriptionWorkService;

	@Autowired
	StudentControlWorkService studentControlWorkService;

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService;

	@Autowired
	StudentSchoolWorkService studentSchoolWorkService;

	@Autowired
	StudentPaymentWorkService studentPaymentWorkService;

	@Autowired
	StudentRegistrationService regService;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	PageNavRuleExecutionService pageNavRuleExecutionService;

	@Autowired
	UIControlsRuleExecutionService uiControlsRuleExecutionService;

	@Autowired
	EditionService editionService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	UserService userService;

	@PostMapping(value = "/app")
	public ResponseEntity<PageDTO> processAppRequest(@RequestBody PageRequestDTO pageRequest) {

		ResponseEntity<PageDTO> response = null;
		try {
			String module = pageRequest.getHeader().getModule();
			String pageName = pageRequest.getHeader().getPageName();
			String actionName = pageRequest.getHeader().getAction();
			String loginCredentials = pageRequest.getHeader().getLoginCredentials();
			String emailId = pageRequest.getHeader().getEmailId();
			String deviceId = pageRequest.getHeader().getDeviceId();
			String operation = pageRequest.getHeader().getOperation();
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);
			if (module == null || pageName == null || actionName == null || deviceId == null || operation == null
					|| loginCredentials == null
					|| (!propertiesService.getValue(PropertyConstants.STUDENT_APP_MODULE_NAME).equals(module))
					|| pageName.trim().isEmpty() || actionName.trim().isEmpty() || operation.trim().isEmpty()
					|| deviceId.trim().isEmpty()) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);

			}
			UserLogin userLogin = null;
			String appStatus = propertiesService.getProperty(PropertyConstants.APP_AVAILABLE);
			if ("Y".equals(appStatus)) {
				if ((!pageName.equalsIgnoreCase("SignIn")) && (!pageName.equalsIgnoreCase("ResetPassword"))
						&& (deviceId != null && !"".equals(deviceId))
						&& (loginCredentials != null && !"".equals(loginCredentials))
						&& (emailId != null && !"".equals(emailId))) {
					StudentRegistration studReg = regService.getStudentReg(emailId);
					if (studReg != null && !"Y".equals(studReg.getIsActive())) {
						pageName = propertiesService.getProperty(PropertyConstants.ACCOUNT_INACTIVE_PAGE_NAME);
						actionName = propertiesService.getProperty(PropertyConstants.ACCOUNT_INACTIVE_PAGE_ACTION);
						pageRequest.getHeader().setAction(actionName);
						pageRequest.getHeader().setPageName(pageName);
						pageRequest.getHeader().setOperation(
								propertiesService.getProperty(PropertyConstants.ACCOUNT_INACTIVE_PAGE_OPERATION));
					} else {
						userLogin = userLoginBusiness.getLoginDetails(deviceId, loginCredentials, emailId, UserType.S);
						StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
						if (userLogin != null && "Y".equals(userLogin.getLoginFlag().toString())
								&& studentControlDTO != null && "Y".equals(studentControlDTO.getEmailVerified())) {
							LocalDateTime tokenRefershTime = userLogin.getTokenExpirationTime().minusSeconds(60);
							if (tokenRefershTime.isBefore(LocalDateTime.now())) {
								userLogin = userLoginBusiness.login(emailId, deviceId, loginCredentials, UserType.S);
								pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
							}
						} else if (userLogin == null || !"Y".equals(userLogin.getLoginFlag().toString())) {
							boolean incomepleteRegistration = false;
							if (studentControlDTO == null || !"Y".equals(studentControlDTO.getEmailVerified())) {
								incomepleteRegistration = true;
								userLogin = userLoginBusiness.getNonLoginDetails(emailId, deviceId, loginCredentials,
										UserType.S);
								if (userLogin == null) {
									userLogin = userLoginBusiness.newDeviceLogin(emailId, deviceId, UserType.S);
									pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
								} else {
									LocalDateTime tokenRefershTime = userLogin.getTokenExpirationTime()
											.minusSeconds(60);
									if (tokenRefershTime.isBefore(LocalDateTime.now())) {
										userLogin = userLoginBusiness.login(emailId, deviceId, loginCredentials,
												UserType.S);
										pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
									}
								}
							}
							if (userLogin == null) {
								userLogin = userLoginBusiness.newDeviceLogin(emailId, deviceId, UserType.S);
								pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
							}
							String unknownUserPageName = propertiesService
									.getProperty(PropertyConstants.UNKNOWN_USER_PAGE_NAME);
							String unknownUserPageOperation = propertiesService
									.getProperty(PropertyConstants.UNKNOWN_USER_PAGE_OPERATION);
							if (unknownUserPageName.equals(pageRequest.getHeader().getPageName())
									&& unknownUserPageOperation.equals(pageRequest.getHeader().getOperation())
									|| incomepleteRegistration) {
								// do nothing!!!
							} else {
								pageName = propertiesService.getProperty(PropertyConstants.LAUNCH_APP_PAGE_NAME);
								actionName = propertiesService.getProperty(PropertyConstants.LAUNCH_APP_PAGE_ACTION);
								pageRequest.getHeader().setPageName(pageName);
								pageRequest.getHeader().setOperation(
										propertiesService.getProperty(PropertyConstants.LAUNCH_APP_PAGE_OPERATION));
								pageRequest.getHeader().setAction(actionName);
							}
						}
					}
				} else if (emailId == null || "".equals(emailId)) {
					userLogin = userLoginBusiness.getStudentLoginByDeviceId(deviceId);
					if (userLogin != null && (userLogin.getUserId() == null || "".equals(userLogin.getUserId()))) {
						LocalDateTime tokenRefershTime = userLogin.getTokenExpirationTime().minusSeconds(60);
						if (tokenRefershTime.isBefore(LocalDateTime.now())) {
							userLogin = userLoginBusiness.login(emailId, deviceId, loginCredentials, UserType.S);
							pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
						}
					} else {
						userLogin = userLoginBusiness.login(emailId, deviceId, loginCredentials, UserType.S);
						pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
					}
				} else {
					userLogin = userLoginBusiness.login(emailId, deviceId, loginCredentials, UserType.S);
					pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
				}
			} else {
				pageName = propertiesService.getProperty(PropertyConstants.APP_UNAVAILABLE_PAGE_NAME);
				actionName = propertiesService.getProperty(PropertyConstants.APP_UNAVAILABLE_PAGE_ACTION);
				pageRequest.getHeader().setAction(actionName);
				pageRequest.getHeader().setPageName(pageName);
				pageRequest.getHeader()
						.setOperation(propertiesService.getProperty(PropertyConstants.APP_UNAVAILABLE_PAGE_OPERATION));
			}
			PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "app");
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
			if (userLogin != null && !loginCredentials.equalsIgnoreCase(userLogin.getTokenId())) {
				response.getBody().getHeader().setLoginCredentials(userLogin.getTokenId());
			}
		} catch (BusinessException e) {
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			throw new Fault(e, header);
		}

		return response;
	}

	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		String operation = pageRequest.getHeader().getOperation();
		String edition = pageRequest.getHeader().getEditionId();
		editionService.getEdition(edition);
		pageRequest.getHeader().setTodaysDate(LocalDate.now());
		// get the page navigation based for current page
		PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(actionName, operation, pageName);
		String saveIn = pageNavDto.getUpdationTable();
		System.out.println(">>>>>submission pageNavDto>>>>>>>>" + pageNavDto);
		// check of the page page has secured access....and if the student subscription
		// is valid...
		if (pageNavDto != null && AppConstants.YES.toString().equals(pageNavDto.getSecured())) {
			// validate of the user is already logged in..
			String emailId = pageRequest.getHeader().getEmailId();
			String deviceId = pageRequest.getHeader().getDeviceId();
			if ("".equals(emailId) || null == emailId) {
				throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID);
			} else {
				boolean isLogged = userLoginBusiness.isUserLoggedIn(deviceId, "", emailId, UserType.S);
				if (!isLogged) {
					boolean isSubscriptiionValid = subscriptionBusiness.isStudentSubscriptionValid(emailId, edition);
					if (!isSubscriptiionValid)
						throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
				}
			}
		}
		System.out.println(">>>>>submission pageName>>>>>>>>" + pageName);
		System.out.println(">>>>>submission operation>>>>>>>>" + operation);
		System.out.println(">>>>>submission actionName>>>>>>>>" + actionName);
		System.out.println(">>>>>submission submissionMethod>>>>>>>>" + pageNavDto.getSubmissionMethod());
		System.out.println(">>>>>submission data>>>>>>>>" + pageRequest.getData());
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAppAction(pageRequest,
				pageNavDto);
		System.out.println(">>>>>getErrorMessage>>>>>>>>" + pageResponse.getErrorMessage());
		if (pageResponse.getErrorMessage() == null) {
			// save data in master Tables (including the previous unsaved data
			String workToMaster = pageNavDto.getWorkToMaster();
			if (workToMaster != null && !"".equals(workToMaster)) {
				String pageArr[] = workToMaster.split(",");
				for (String workPageName : pageArr) {
					pageHandlerFactory.getPageHandler(workPageName, context).saveAsMaster(pageRequest);
				}
			}
			String nextPageName = "";
			String nextPageOperation = "";
			String nextPageLoadMethod = "";
			String controlWorkEntryOrExit = "";
			Map<String, String> nextPageData = pageNavRuleExecutionService.getNextPageBasedOnRule(pageRequest,
					pageResponse, pageNavDto);
			if (!nextPageData.isEmpty()) {
				nextPageName = nextPageData.get("nextPage");
				nextPageOperation = nextPageData.get("nextPageOperation");
				nextPageLoadMethod = nextPageData.get("nextPageLoadMethod");
				controlWorkEntryOrExit = nextPageData.get("controlWorkEntryOrExit");
			} else {
				nextPageName = pageNavDto.getNextPage();
				nextPageOperation = pageNavDto.getNextPageOperation();
				nextPageLoadMethod = pageNavDto.getNextPageLoadMethod();
				controlWorkEntryOrExit = pageNavDto.getControlWorkEntryOrExit();
			}
			if ("loadIncompleteOperationPage".equalsIgnoreCase(nextPageLoadMethod)) {
				String emailId = pageResponse.getHeader().getEmailId();
				StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
				nextPageName = studentControlWorkDTO.getNextPageName();
				nextPageOperation = studentControlWorkDTO.getNextPageOperation();
				nextPageLoadMethod = studentControlWorkDTO.getNextPageLoadMethod();
			}
			PageNavigationContext pageNavigationContext = new PageNavigationContext();
			pageRequest.getHeader().setOperation(nextPageOperation);
			pageRequest.getHeader().setPageName(nextPageName);

			pageNavigationContext.setPageRequest(pageRequest);
			pageNavigationContext.setPreviousPageResponse(pageResponse);
			pageNavigationContext.setLoadMethod(nextPageLoadMethod);
			System.out.println(">>>>>>nextPageName>>>>>>>" + nextPageName);
			System.out.println(">>>>>>nextPageOperation>>>>>>>" + nextPageOperation);
			System.out.println(">>>>>>>>nextPageLoadMethod>>>>>" + nextPageLoadMethod);
			// load data for the next page
			pageResponse = pageHandlerFactory.getPageHandler(nextPageName, context).loadPage(pageNavigationContext);
			System.out.println(">>>>>>pageResponse>>>>>>>" + pageResponse);
			if ("ENTRY".equalsIgnoreCase(controlWorkEntryOrExit)) {
				doStudentControlWorkEntry(nextPageLoadMethod, pageResponse);
			} else if ("EXITPREV".equalsIgnoreCase(controlWorkEntryOrExit)) {
				doStudentControlWorkExitPrev(pageResponse);
			} else if ("EXITNEXT".equalsIgnoreCase(controlWorkEntryOrExit)) {
				doStudentControlWorkExitNext(pageResponse);
			}
			if (PageSaveTable.W.toString().equals(saveIn)) {
				updateWorkTable(nextPageName, nextPageOperation, nextPageLoadMethod, pageResponse);
			}
			addSuccessHeader(nextPageName, nextPageOperation, pageResponse);
			// attach UI controls for the next page to the response
			addUIControls(nextPageName, nextPageOperation, pageRequest, pageResponse);
			if ("WelcomeUser".equalsIgnoreCase(nextPageOperation)
					|| "UnknownUser".equalsIgnoreCase(nextPageOperation)) {
				String lastPropChangeTime = propertiesService.getProperty(PropertyConstants.LAST_PROP_CHANGE_TIME);
				if (!lastPropChangeTime.equals(pageRequest.getHeader().getLastPropChangeTime())) {
					pageResponse.setGlobalProperties(propertiesService.getPropertyList());
				}
			}
		}
		return pageResponse;
	}

	private void updateWorkTable(String nextPageName, String nextPageOperation, String nextPageLoadMethod,
			PageDTO pageDTO) {
		String emailId = pageDTO.getHeader().getEmailId();
		String editionId = pageDTO.getHeader().getEditionId();
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(emailId, editionId);
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		if (studentControlWorkDTO != null) {
			studentControlWorkDTO.setNextPageName(nextPageName);
			studentControlWorkDTO.setNextPageOperation(nextPageOperation);
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		}
	}

	private void doStudentControlWorkExitNext(PageDTO pageDTO) {
		String emailId = pageDTO.getHeader().getEmailId();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		studentControlBusiness.workToMaster(studentControlWorkDTO.getStudentId());
		studentControlBusiness.deleteFromWork(studentControlWorkDTO);
	}

	private void doStudentControlWorkExitPrev(PageDTO pageDTO) {
		String emailId = pageDTO.getHeader().getEmailId();
		String editionId = pageDTO.getHeader().getEditionId();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		Long studentId = studentControlWorkDTO.getStudentId();
		if (studentPreferencesWorkService.get(studentId) != null) {
			studentPreferencesWorkService.delete(studentId);
		}
		if (studentSubscriptionWorkService.get(studentId, editionId) != null) {
			studentSubscriptionWorkService.delete(studentId);
		}
		if (studentDetailsWorkService.get(studentId) != null) {
			studentDetailsWorkService.delete(studentId);
		}
		if (studentSchoolWorkService.get(studentId) != null) {
			studentSchoolWorkService.delete(studentId);
		}
		StudentPaymentWork studentPaymentWork = studentPaymentWorkService.getByStudentIdAndEdition(studentId,
				editionId);
		if (studentPaymentWork != null) {
			studentPaymentWorkService.delete(studentPaymentWork.getPaymentId());
		}
		if (studentControlWorkService.get(studentId) != null) {
			studentControlWorkService.delete(studentId);
		}
	}

	private void doStudentControlWorkEntry(String nextPageLoadMethod, PageDTO pageDTO) {
		String emailId = pageDTO.getHeader().getEmailId();
		String editionId = pageDTO.getHeader().getEditionId();
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(emailId, editionId);
		Long studentId = 0L;
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		if (studentControlDTO != null) {
			studentControlWorkDTO = new StudentControlWorkDTO();
			studentControlWorkDTO.setStudentId(studentControlDTO.getStudentId());
			studentControlWorkDTO.setEmail(studentControlDTO.getEmail());
			studentControlWorkDTO.setSubscriptionType(studentControlDTO.getSubscriptionType());
			studentControlWorkDTO.setStudentDetails(studentControlDTO.getStudentDetails());
			studentControlWorkDTO.setSchoolDetails(studentControlDTO.getSchoolDetails());
			studentControlWorkDTO.setPreferences(studentControlDTO.getPreferences());
			studentControlWorkDTO.setEmailVerified(studentControlDTO.getEmailVerified());
			studentControlWorkDTO.setEvalAvailed(studentControlDTO.getEvalAvailed());
			studentControlWorkDTO.setNextPageName(pageDTO.getHeader().getPageName());
			studentControlWorkDTO.setNextPageOperation(pageDTO.getHeader().getOperation());
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		} else if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentId();
			studentControlWorkDTO.setNextPageName(pageDTO.getHeader().getPageName());
			studentControlWorkDTO.setNextPageOperation(pageDTO.getHeader().getOperation());
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlWorkDTO.setStudentId(studentId);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		} else {
			StudentRegistration studReg = regService.getStudentReg(emailId);
			if (studReg == null) {
				studReg = new StudentRegistration();
				studReg.setEmailId(emailId);
			}
			studReg.setRecordInUse(RecordInUseType.Y);
			studReg.setOperatorId("APP");
			studReg.setIsDeleted("N");
			studReg = regService.create(studReg);
			studentControlWorkDTO = new StudentControlWorkDTO();
			studentControlWorkDTO.setNextPageName(pageDTO.getHeader().getPageName());
			studentControlWorkDTO.setNextPageOperation(pageDTO.getHeader().getOperation());
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlWorkDTO.setEmail(emailId);
			studentControlWorkDTO.setStudentId(studReg.getStudentId());
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		}
	}

	private void addSuccessHeader(String nextPageName, String nextOperationName, PageDTO page) {
		if (page.getHeader() == null) {
			page.setHeader(new HeaderDTO());
		}
		page.getHeader().setRequestStatus(RequestStatusType.S);
		page.getHeader().setPageName(nextPageName);
		page.getHeader().setOperation(nextOperationName);
	}

	private void addUIControls(String nextPageName, String nextOperationName, PageRequestDTO pageRequest,
			PageDTO pageResponse) {
		List<UIControlsDTO> uiControlsTemp = uiControlService.get(nextPageName, nextOperationName);
		if (uiControlsTemp == null || uiControlsTemp.size() == 0) {
			uiControlsTemp = uiControlService.get(nextPageName, "All");
		}
		List<UIControlsDTO> uiControls = new ArrayList<UIControlsDTO>();
		for (UIControlsDTO uiControlsDTO : uiControlsTemp) {
			if (uiControlsDTO.getGlobalControlRef() != null && !"".equals(uiControlsDTO.getGlobalControlRef())) {
				UIControlsGlobalDTO uiControlsGlobalDTO = uiControlGlobalService
						.get(uiControlsDTO.getGlobalControlRef());
				if (uiControlsGlobalDTO != null) {
					uiControlsDTO.setControlName(
							uiControlsDTO.getControlName() != null && !"".equals(uiControlsDTO.getControlName())
									? uiControlsDTO.getControlName()
									: uiControlsGlobalDTO.getControlName());
					uiControlsDTO.setControlType(
							uiControlsDTO.getControlType() != null && !"".equals(uiControlsDTO.getControlType())
									? uiControlsDTO.getControlType()
									: uiControlsGlobalDTO.getControlType());
					uiControlsDTO.setControlLabel(
							uiControlsDTO.getControlLabel() != null && !"".equals(uiControlsDTO.getControlLabel())
									? uiControlsDTO.getControlLabel()
									: uiControlsGlobalDTO.getControlLabel());
					uiControlsDTO
							.setDataType(uiControlsDTO.getDataType() != null && !"".equals(uiControlsDTO.getDataType())
									? uiControlsDTO.getDataType()
									: uiControlsGlobalDTO.getDataType());
					uiControlsDTO.setDefaultValue(
							uiControlsDTO.getDefaultValue() != null && !"".equals(uiControlsDTO.getDefaultValue())
									? uiControlsDTO.getDefaultValue()
									: uiControlsGlobalDTO.getDefaultValue());
					uiControlsDTO.setPlaceHolder(
							uiControlsDTO.getPlaceHolder() != null && !"".equals(uiControlsDTO.getPlaceHolder())
									? uiControlsDTO.getPlaceHolder()
									: uiControlsGlobalDTO.getPlaceHolder());
					uiControlsDTO
							.setHelpText(uiControlsDTO.getHelpText() != null && !"".equals(uiControlsDTO.getHelpText())
									? uiControlsDTO.getHelpText()
									: uiControlsGlobalDTO.getHelpText());
					uiControlsDTO.setIcon(uiControlsDTO.getIcon() != null && !"".equals(uiControlsDTO.getIcon())
							? uiControlsDTO.getIcon()
							: uiControlsGlobalDTO.getIcon());
					uiControlsDTO
							.setIconType(uiControlsDTO.getIconType() != null && !"".equals(uiControlsDTO.getIconType())
									? uiControlsDTO.getIconType()
									: uiControlsGlobalDTO.getIconType());
					uiControlsDTO
							.setKeyboard(uiControlsDTO.getKeyboard() != null && !"".equals(uiControlsDTO.getKeyboard())
									? uiControlsDTO.getKeyboard()
									: uiControlsGlobalDTO.getKeyboard());
					uiControlsDTO.setMultiLine(
							uiControlsDTO.getMultiLine() != null && !"".equals(uiControlsDTO.getMultiLine())
									? uiControlsDTO.getMultiLine()
									: uiControlsGlobalDTO.getMultiLine());
					uiControlsDTO.setMinLength(
							uiControlsDTO.getMinLength() != null && !"".equals(uiControlsDTO.getMinLength())
									? uiControlsDTO.getMinLength()
									: uiControlsGlobalDTO.getMinLength());
					uiControlsDTO.setMaxLength(
							uiControlsDTO.getMaxLength() != null && !"".equals(uiControlsDTO.getMaxLength())
									? uiControlsDTO.getMaxLength()
									: uiControlsGlobalDTO.getMaxLength());
					uiControlsDTO.setHeight(uiControlsDTO.getHeight() != null && !"".equals(uiControlsDTO.getHeight())
							? uiControlsDTO.getHeight()
							: uiControlsGlobalDTO.getHeight());
					uiControlsDTO.setWidth(uiControlsDTO.getWidth() != null && !"".equals(uiControlsDTO.getWidth())
							? uiControlsDTO.getWidth()
							: uiControlsGlobalDTO.getWidth());
					uiControlsDTO.setAction(uiControlsDTO.getAction() != null && !"".equals(uiControlsDTO.getAction())
							? uiControlsDTO.getAction()
							: uiControlsGlobalDTO.getAction());
					uiControlsDTO.setVisibility(
							uiControlsDTO.getVisibility() != null && !"".equals(uiControlsDTO.getVisibility())
									? uiControlsDTO.getVisibility()
									: uiControlsGlobalDTO.getVisibility());
					uiControlsDTO.setMandatory(
							uiControlsDTO.getMandatory() != null && !"".equals(uiControlsDTO.getMandatory())
									? uiControlsDTO.getMandatory()
									: uiControlsGlobalDTO.getMandatory());
					uiControlsDTO.setRegex(uiControlsDTO.getRegex() != null && !"".equals(uiControlsDTO.getRegex())
							? uiControlsDTO.getRegex()
							: uiControlsGlobalDTO.getRegex());
					uiControlsDTO.setErrorMessage(
							uiControlsDTO.getErrorMessage() != null && !"".equals(uiControlsDTO.getErrorMessage())
									? uiControlsDTO.getErrorMessage()
									: uiControlsGlobalDTO.getErrorMessage());
					uiControlsDTO.setConfirmationMessage(uiControlsDTO.getConfirmationMessage() != null
							&& !"".equals(uiControlsDTO.getConfirmationMessage())
									? uiControlsDTO.getConfirmationMessage()
									: uiControlsGlobalDTO.getConfirmationMessage());
					uiControlsDTO.setIsPremiumFeature(uiControlsDTO.getIsPremiumFeature() != null
							&& !"".equals(uiControlsDTO.getIsPremiumFeature()) ? uiControlsDTO.getIsPremiumFeature()
									: uiControlsGlobalDTO.getIsPremiumFeature());
				}
			}
			uiControls.add(uiControlsRuleExecutionService.getControlParamsBasedOnRule(pageRequest, pageResponse,
					uiControlsDTO));
		}
		pageResponse.setScreenProperties(uiControls);
	}

	@RequestMapping(value = "/app/images", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public void getImage(HttpServletResponse response, @RequestParam String imageType, @RequestParam String imagePath,
			@RequestParam String appKey, @RequestParam String appName, @RequestParam String userId,
			@RequestParam String deviceId, @RequestParam String loginCredentials) throws IOException {
		try {
			if (imageType == null || imagePath == null || appKey == null || appName == null || deviceId == null
					|| loginCredentials == null) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
			} else if (imageType.trim().isEmpty() || imagePath.trim().isEmpty() || appKey.trim().isEmpty()
					|| appName.trim().isEmpty() || deviceId.trim().isEmpty() || loginCredentials.trim().isEmpty()) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
			}
			String imagesFolderPath = propertiesService.getProperty(imageType + "-image-config.imagesRootFolderPath");
			if ("STUDENT".equalsIgnoreCase(imageType)) {
				if (studentControlBusiness.getStudentId(userId) == 0L) {
					throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, userId);
				} else {
					userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, userId, UserType.S);
				}
			} else {
				UserLogin deviceLogin = userLoginBusiness.getDeviceLogin(deviceId, loginCredentials);
				if (deviceLogin == null) {
					throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
				}
			}
			if (imagePath.startsWith("/")) {
				imagePath = imagePath.substring(2, imagePath.length());
			}
			File imageFile = new File(imagesFolderPath + imagePath);
			if (imageFile != null && imageFile.exists()) {
				InputStream imageStream = new FileInputStream(imageFile);
				if (imagePath.toUpperCase().endsWith(".JPG") || imagePath.toUpperCase().endsWith(".JEPG")) {
					response.setContentType(MediaType.IMAGE_JPEG_VALUE);
				} else if (imagePath.toUpperCase().endsWith(".PNG")) {
					response.setContentType(MediaType.IMAGE_PNG_VALUE);
				} else if (imagePath.toUpperCase().endsWith(".GIF")) {
					response.setContentType(MediaType.IMAGE_GIF_VALUE);
				}
				StreamUtils.copy(imageStream, response.getOutputStream());
			}
		} catch (BusinessException e) {
			throw new Fault(e);
		}
	}
}
