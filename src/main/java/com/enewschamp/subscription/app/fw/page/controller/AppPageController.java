package com.enewschamp.subscription.app.fw.page.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

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
import com.enewschamp.app.holiday.service.HolidayService;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.common.domain.service.ErrorCodesService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.common.domain.service.PropertiesFrontendService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.security.service.AppSecurityService;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPaymentWork;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPaymentWorkService;
import com.enewschamp.subscription.domain.service.StudentPreferencesWorkService;
import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ui/v1")
public class AppPageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	AppSecurityService appSecService;

	@Autowired
	ErrorCodesService errorCodeService;

	@Autowired
	private PropertiesBackendService propertiesService;

	@Autowired
	private PropertiesFrontendService propertiesFrontendService;

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
	StudentPaymentService studentPaymentService;

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

	@Autowired
	GenreService genreService;

	@Autowired
	BadgeService badgeService;

	@Autowired
	HolidayService holidayService;

	@Autowired
	ObjectMapper objectMapper;

	@PostMapping(value = "/app")
	public ResponseEntity<PageDTO> processAppRequest(@RequestBody PageRequestDTO pageRequest) {

		ResponseEntity<PageDTO> response = null;
		String module = "";
		String emailId = "";
		String pageName = "";
		String deviceId = "";
		String actionName = "";
		String operation = "";
		Long studentId = 0L;
		try {
			module = pageRequest.getHeader().getModule();
			deviceId = pageRequest.getHeader().getDeviceId();
			pageName = pageRequest.getHeader().getPageName();
			actionName = pageRequest.getHeader().getAction();
			operation = pageRequest.getHeader().getOperation();
			String loginCredentials = pageRequest.getHeader().getLoginCredentials();
			String studentKey = pageRequest.getHeader().getStudentKey();
			String appVersion = pageRequest.getHeader().getAppVersion();
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);
			if (module == null || pageName == null || actionName == null || deviceId == null || operation == null
					|| loginCredentials == null || appVersion == null
					|| (!propertiesService.getValue(module, PropertyConstants.STUDENT_APP_MODULE_NAME).equals(module))
					|| pageName.trim().isEmpty() || actionName.trim().isEmpty() || operation.trim().isEmpty()
					|| deviceId.trim().isEmpty() || appVersion.trim().isEmpty()) {
				throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
			}
			if (studentKey != null && !"".equals(studentKey)) {
				emailId = regService.getStudentEmailByKey(studentKey);
				if (emailId != null && !"".equals(emailId)) {
					pageRequest.getHeader().setEmailId(emailId);
					StudentRegistration student = regService.getStudentReg(emailId);
					if (student != null) {
						studentId = student.getStudentId();
						pageRequest.getHeader().setStudentId(studentId);
					}
				}
			}
			UserLogin userLogin = null;
			String appStatus = appSecService.getAppAvailability(module);
			if ("Y".equals(appStatus)) {
				String compatibleAppVersions = appSecService.getCompatibleAppVersions(module);
				String[] compatibleAppVersionsArr = compatibleAppVersions.split("\\|");
				boolean appVersionCompatibility = false;
				for (int i = 0; i < compatibleAppVersionsArr.length; i++) {
					if (compatibleAppVersionsArr[i].equalsIgnoreCase(appVersion)) {
						appVersionCompatibility = true;
					}
				}
				if (!appVersionCompatibility) {
					throw new BusinessException(ErrorCodeConstants.APP_VERSION_NOT_SUPPORTED);
				}
				if ((!pageName.equalsIgnoreCase("SignIn")) && (!pageName.equalsIgnoreCase("ResetPassword"))
						&& (deviceId != null && !"".equals(deviceId))
						&& (loginCredentials != null && !"".equals(loginCredentials))
						&& (emailId != null && !"".equals(emailId))) {
					StudentRegistration studReg = regService.getStudentReg(emailId);
					if (studReg != null && !"Y".equals(studReg.getIsActive())) {
						pageName = propertiesService.getValue(module, PropertyConstants.ACCOUNT_INACTIVE_PAGE_NAME);
						actionName = propertiesService.getValue(module, PropertyConstants.ACCOUNT_INACTIVE_ACTION);
						pageRequest.getHeader().setAction(actionName);
						pageRequest.getHeader().setPageName(pageName);
						pageRequest.getHeader().setOperation(
								propertiesService.getValue(module, PropertyConstants.ACCOUNT_INACTIVE_OPERATION));
					} else {
						userLogin = userLoginBusiness.getLoginDetails(deviceId, loginCredentials, emailId, UserType.S);
						StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(studentId);
						if (userLogin != null && "Y".equals(userLogin.getLoginFlag().toString())
								&& studentControlDTO != null && "Y".equals(studentControlDTO.getEmailIdVerified())) {
							LocalDateTime tokenRefershTime = userLogin.getTokenExpirationTime().minusSeconds(60);
							if (tokenRefershTime.isBefore(LocalDateTime.now())) {
								userLogin = userLoginBusiness.login(emailId, "" + studentId, deviceId, loginCredentials,
										module, appVersion, UserType.S);
								pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
							}
						} else if (userLogin == null || !"Y".equals(userLogin.getLoginFlag().toString())) {
							boolean incomepleteRegistration = false;
							if (studentControlDTO == null || !"Y".equals(studentControlDTO.getEmailIdVerified())) {
								incomepleteRegistration = true;
								userLogin = userLoginBusiness.getNonLoginDetails(emailId, deviceId, loginCredentials,
										UserType.S);
								if (userLogin == null) {
									userLogin = userLoginBusiness.newDeviceLogin(emailId, "" + studentId, deviceId,
											UserType.S);
									pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
								} else {
									LocalDateTime tokenRefershTime = userLogin.getTokenExpirationTime()
											.minusSeconds(60);
									if (tokenRefershTime.isBefore(LocalDateTime.now())) {
										userLogin = userLoginBusiness.login(emailId, "" + studentId, deviceId,
												loginCredentials, module, appVersion, UserType.S);
										pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
									}
								}
							}
							if (userLogin == null) {
								userLogin = userLoginBusiness.newDeviceLogin(emailId, "" + studentId, deviceId,
										UserType.S);
								pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
							}
							String unknownUserPageName = propertiesService.getValue(module,
									PropertyConstants.UNKNOWN_USER_PAGE_NAME);
							String unknownUserPageOperation = propertiesService.getValue(module,
									PropertyConstants.UNKNOWN_USER_OPERATION);
							if (unknownUserPageName.equals(pageRequest.getHeader().getPageName())
									&& unknownUserPageOperation.equals(pageRequest.getHeader().getOperation())
									|| incomepleteRegistration) {
								// do nothing!!!
							} else {
								pageName = propertiesService.getValue(module, PropertyConstants.LAUNCH_APP_PAGE_NAME);
								actionName = propertiesService.getValue(module, PropertyConstants.LAUNCH_APP_ACTION);
								pageRequest.getHeader().setPageName(pageName);
								pageRequest.getHeader().setOperation(
										propertiesService.getValue(module, PropertyConstants.LAUNCH_APP_OPERATION));
								pageRequest.getHeader().setAction(actionName);
							}
						}
					}
				} else if (emailId == null || "".equals(emailId)) {
					userLogin = userLoginBusiness.getStudentLoginByDeviceId(deviceId);
					if (userLogin != null && (userLogin.getUserId() == null || "".equals(userLogin.getUserId()))) {
						LocalDateTime tokenRefershTime = userLogin.getTokenExpirationTime().minusSeconds(60);
						if (tokenRefershTime.isBefore(LocalDateTime.now())) {
							userLogin = userLoginBusiness.login(emailId, "" + studentId, deviceId, loginCredentials,
									module, appVersion, UserType.S);
							pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
						}
					} else {
						userLogin = userLoginBusiness.login(emailId, "" + studentId, deviceId, loginCredentials, module,
								appVersion, UserType.S);
						pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
					}
				} else {
					userLogin = userLoginBusiness.login(emailId, "" + studentId, deviceId, loginCredentials, module,
							appVersion, UserType.S);
					pageRequest.getHeader().setLoginCredentials(userLogin.getTokenId());
				}
			} else {
				pageName = propertiesService.getValue(module, PropertyConstants.APP_UNAVAILABLE_PAGE_NAME);
				actionName = propertiesService.getValue(module, PropertyConstants.APP_UNAVAILABLE_ACTION);
				pageRequest.getHeader().setAction(actionName);
				pageRequest.getHeader().setPageName(pageName);
				pageRequest.getHeader()
						.setOperation(propertiesService.getValue(module, PropertyConstants.APP_UNAVAILABLE_OPERATION));
			}
			PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "app");
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
			if (userLogin != null && !loginCredentials.equalsIgnoreCase(userLogin.getTokenId())) {
				response.getBody().getHeader().setLoginCredentials(userLogin.getTokenId());
			}
		} catch (BusinessException e) {
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setOperation(operation);
			pageRequest.getHeader().setAction(actionName);
			if (!"SignIn".equalsIgnoreCase(pageName) && !"ResetPassword".equalsIgnoreCase(pageName)) {
				String userId = emailId;
				if (userId == null || "".equals(userId)) {
					userId = "unknown";
				}
				UserActivityTracker userActivityTracker = new UserActivityTracker();
				userActivityTracker.setOperatorId(userId);
				userActivityTracker.setRecordInUse(RecordInUseType.Y);
				userActivityTracker.setActionPerformed(pageRequest.getHeader().getPageName() + "-"
						+ pageRequest.getHeader().getOperation() + "-" + actionName);
				userActivityTracker.setDeviceId(deviceId);
				userActivityTracker.setUserId(userId);
				userActivityTracker.setUserType(UserType.S);
				userActivityTracker.setActionTime(LocalDateTime.now());
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userActivityTracker.setErrorCode(e.getErrorCode());
				userActivityTracker.setErrorDescription(errorCodeService.getValue(e.getErrorCode()));
				if (e.getErrorCode() != null && e.getErrorCode().equals(ErrorCodeConstants.RUNTIME_EXCEPTION)) {
					if (e.getErrorMessageParams() != null) {
						try {
							userActivityTracker.setErrorText(new SerialBlob(e.getErrorMessageParams()[0].getBytes()));
						} catch (SerialException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				Blob payload = null;
				String dataPayLoad = "Payload is not valid";
				try {
					try {
						String json = objectMapper.writeValueAsString(pageRequest);
						JsonNode jsonNode = objectMapper.readTree(json);
						dataPayLoad = jsonNode.toString();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					payload = new SerialBlob(dataPayLoad.getBytes());
				} catch (SerialException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				userActivityTracker.setRequestData(payload);
				userLoginBusiness.auditUserActivity(userActivityTracker);
			}
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			header.setEmailId(null);
			header.setStudentId(null);
			throw new Fault(e, header);
		}
		response.getBody().getHeader().setEmailId(null);
		response.getBody().getHeader().setStudentId(null);
		return response;
	}

	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		String operation = pageRequest.getHeader().getOperation();
		String edition = pageRequest.getHeader().getEditionId();
		String module = pageRequest.getHeader().getModule();
		editionService.getEdition(edition);
		pageRequest.getHeader().setTodaysDate(LocalDate.now());
		// get the page navigation based for current page
		PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(actionName, operation, pageName);
		String saveIn = pageNavDto.getUpdationTable();
		// System.out.println(">>>>>submission pageNavDto>>>>>>>>" + pageNavDto);
		// check of the page page has secured access....and if the student subscription
		// is valid...
		if (pageNavDto != null && AppConstants.YES.toString().equals(pageNavDto.getSecured())) {
			// validate of the user is already logged in..
			String emailId = pageRequest.getHeader().getEmailId();
			String deviceId = pageRequest.getHeader().getDeviceId();
			Long studentId = pageRequest.getHeader().getStudentId();
			if ("".equals(emailId) || null == emailId) {
				throw new BusinessException(ErrorCodeConstants.INVALID_EMAIL_ID);
			} else {
				boolean isLogged = userLoginBusiness.isUserLoggedIn(deviceId, "", emailId, UserType.S);
				if (!isLogged) {
					boolean isSubscriptiionValid = subscriptionBusiness.isStudentSubscriptionValid(module, studentId,
							edition);
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
		// System.out.println(">>>>>getErrorMessage>>>>>>>>" +
		// pageResponse.getErrorMessage());
		if (pageResponse.getErrorMessage() == null) {
			// save data in master Tables (including the previous unsaved data
			String workToMaster = pageNavDto.getWorkToMaster();
			if (workToMaster != null && !"".equals(workToMaster)) {
				String pageArr[] = workToMaster.trim().split(",");
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
				Long studentId = pageResponse.getHeader().getStudentId();
				StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
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
				pageResponse = doStudentControlWorkEntry(nextPageLoadMethod, pageResponse);
			} else if ("EXITPREV".equalsIgnoreCase(controlWorkEntryOrExit)) {
				doStudentControlWorkExitPrev(pageResponse);
				pageResponse.setExitPrev("Y");
			} else if ("EXITNEXT".equalsIgnoreCase(controlWorkEntryOrExit)) {
				doStudentControlWorkExitNext(pageResponse);
				pageResponse.setExitNext("Y");
			}
			if (PageSaveTable.W.toString().equals(saveIn)) {
				updateWorkTable(nextPageName, nextPageOperation, nextPageLoadMethod, pageResponse);
			}
			addSuccessHeader(nextPageName, nextPageOperation, pageResponse);
			// attach UI controls for the next page to the response
			addUIControls(nextPageName, nextPageOperation, pageRequest, pageResponse);
			if ("WelcomeUser".equalsIgnoreCase(nextPageOperation)
					|| "UnknownUser".equalsIgnoreCase(nextPageOperation)) {
				String propertiesLabel = propertiesFrontendService.getValue(
						pageNavigationContext.getPageRequest().getHeader().getModule(),
						PropertyConstants.PROPERTIES_LABEL);
				if (!propertiesLabel.equals(pageRequest.getHeader().getPropertiesLabel())) {
					pageResponse.setGlobalProperties(propertiesFrontendService.getPropertyList(module));
				}
				pageResponse.setGenres(genreService.getGenreList());
				pageResponse.setBadges(badgeService.getBadgeList());
				pageResponse.setHolidays(holidayService.getHolidayList());
				pageResponse.setBoUsers(userService.getBOUserList());
			}
		}
		return pageResponse;
	}

	private void updateWorkTable(String nextPageName, String nextPageOperation, String nextPageLoadMethod,
			PageDTO pageDTO) {
		String emailId = pageDTO.getHeader().getEmailId();
		Long studentId = pageDTO.getHeader().getStudentId();
		String editionId = pageDTO.getHeader().getEditionId();
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(studentId, editionId);
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
		if (studentControlWorkDTO != null) {
			studentControlWorkDTO.setNextPageName(nextPageName);
			studentControlWorkDTO.setNextPageOperation(nextPageOperation);
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlWorkDTO.setOperatorId("" + studentControlWorkDTO.getStudentId());
			studentControlWorkDTO.setRecordInUse(RecordInUseType.Y);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		}
	}

	private void doStudentControlWorkExitNext(PageDTO pageDTO) {
		Long studentId = pageDTO.getHeader().getStudentId();
		String editionId = pageDTO.getHeader().getEditionId();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
		studentControlBusiness.workToMaster(studentId);
		studentControlBusiness.deleteFromWork(studentControlWorkDTO);
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(studentId);
		if (studentControlDTO != null) {
			StudentPayment studentPayment = studentPaymentService.getByStudentIdAndEdition(studentId, editionId);
			if (studentPayment != null && studentPayment.getSubscriptionEndDate() == null) {
				StudentSubscriptionDTO studentSubscriptionDTO = subscriptionBusiness
						.getStudentSubscriptionFromMaster(studentId, editionId);
				if (studentSubscriptionDTO != null && studentSubscriptionDTO.getEndDate() != null) {
					studentPayment.setSubscriptionEndDate(studentSubscriptionDTO.getEndDate());
					studentPaymentService.update(studentPayment);
				}
			}
		}
	}

	private void doStudentControlWorkExitPrev(PageDTO pageDTO) {
		Long studentId = pageDTO.getHeader().getStudentId();
		String editionId = pageDTO.getHeader().getEditionId();
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
		if (studentControlWorkDTO != null) {
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
	}

	private PageDTO doStudentControlWorkEntry(String nextPageLoadMethod, PageDTO pageDTO) {
		String emailId = pageDTO.getHeader().getEmailId();
		Long studentId = pageDTO.getHeader().getStudentId();
		String editionId = pageDTO.getHeader().getEditionId();
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(studentId, editionId);
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(studentId);
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(studentId);
		if (studentControlDTO != null) {
			studentControlWorkDTO = new StudentControlWorkDTO();
			studentControlWorkDTO.setStudentId(studentControlDTO.getStudentId());
			studentControlWorkDTO.setSubscriptionType(studentControlDTO.getSubscriptionType());
			studentControlWorkDTO.setStudentDetails(studentControlDTO.getStudentDetails());
			studentControlWorkDTO.setSchoolDetails(studentControlDTO.getSchoolDetails());
			studentControlWorkDTO.setPreferences(studentControlDTO.getPreferences());
			studentControlWorkDTO.setEmailIdVerified(studentControlDTO.getEmailIdVerified());
			studentControlWorkDTO.setEvalAvailed(studentControlDTO.getEvalAvailed());
			studentControlWorkDTO.setNextPageName(pageDTO.getHeader().getPageName());
			studentControlWorkDTO.setNextPageOperation(pageDTO.getHeader().getOperation());
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlWorkDTO.setOperatorId("" + studentId);
			studentControlWorkDTO.setRecordInUse(RecordInUseType.Y);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		} else if (studentControlWorkDTO != null) {
			studentId = studentControlWorkDTO.getStudentId();
			studentControlWorkDTO.setNextPageName(pageDTO.getHeader().getPageName());
			studentControlWorkDTO.setNextPageOperation(pageDTO.getHeader().getOperation());
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlWorkDTO.setStudentId(studentId);
			studentControlWorkDTO.setOperatorId("" + studentId);
			studentControlWorkDTO.setRecordInUse(RecordInUseType.Y);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
		} else {
			StudentRegistration studReg = regService.getStudentReg(emailId);
			if (studReg == null) {
				studReg = new StudentRegistration();
				studReg.setEmailId(emailId);
			}
			studReg.setRecordInUse(RecordInUseType.Y);
			studReg.setOperatorId("" + studentId);
			studReg.setIsDeleted("N");
			studReg = regService.create(studReg);
			studentControlWorkDTO = new StudentControlWorkDTO();
			studentControlWorkDTO.setNextPageName(pageDTO.getHeader().getPageName());
			studentControlWorkDTO.setNextPageOperation(pageDTO.getHeader().getOperation());
			studentControlWorkDTO.setNextPageLoadMethod(nextPageLoadMethod);
			studentControlWorkDTO.setStudentId(studReg.getStudentId());
			studentControlWorkDTO.setOperatorId("" + studentId);
			studentControlWorkDTO.setRecordInUse(RecordInUseType.Y);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
			pageDTO.getHeader().setStudentKey(studReg.getStudentKey());
		}
		return pageDTO;
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
					uiControlsDTO.setSuccessMessage(
							uiControlsDTO.getSuccessMessage() != null && !"".equals(uiControlsDTO.getSuccessMessage())
									? uiControlsDTO.getSuccessMessage()
									: uiControlsGlobalDTO.getSuccessMessage());
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
					uiControlsDTO.setUnavailableMessage(uiControlsDTO.getUnavailableMessage() != null
							&& !"".equals(uiControlsDTO.getUnavailableMessage()) ? uiControlsDTO.getUnavailableMessage()
									: uiControlsGlobalDTO.getUnavailableMessage());
				}
			}
			uiControls.add(uiControlsRuleExecutionService.getControlParamsBasedOnRule(pageRequest, pageResponse,
					uiControlsDTO));
		}
		pageResponse.setScreenProperties(uiControls);
	}

	@RequestMapping(value = "/app/images", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public void getImage(HttpServletResponse response, @RequestParam String imageType, @RequestParam String imagePath,
			@RequestParam String appKey, @RequestParam String appName, @RequestParam String module,
			@RequestParam String studentKey, @RequestParam String deviceId, @RequestParam String loginCredentials)
			throws IOException {
		if (imageType == null || imagePath == null || appKey == null || appName == null || deviceId == null
				|| loginCredentials == null || imageType.trim().isEmpty() || imagePath.trim().isEmpty()
				|| appKey.trim().isEmpty() || appName.trim().isEmpty() || deviceId.trim().isEmpty()
				|| loginCredentials.trim().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
		} else {
			boolean loginCheckFlag = false;
			String validateLogin = propertiesService.getValue(module, PropertyConstants.VALIDATE_LOGIN);
			String loginRequired[] = validateLogin.split("\\|");
			for (int i = 0; i < loginRequired.length; i++) {
				if (imageType.equalsIgnoreCase(loginRequired[i])) {
					loginCheckFlag = true;
					break;
				}
			}
			try {
				if (loginCheckFlag) {
					if (studentKey == null || studentKey.trim().isEmpty()) {
						throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
					}
					String emailId = regService.getStudentEmailByKey(studentKey);
					if (emailId == null || "".equals(emailId)) {
						throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, studentKey);
					} else {
						userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, emailId, UserType.S);
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
				String imageFolderPath = propertiesService.getValue(module, "imageRootFolderPath." + imageType);
				File imageFile = new File(imageFolderPath + imagePath);
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
					imageStream.close();
				}
			} catch (BusinessException e) {
				throw new Fault(e);
			}
		}

	}

	@RequestMapping(value = "/app/audios", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public void getAudio(HttpServletResponse response, @RequestParam String audioType, @RequestParam String audioPath,
			@RequestParam String appKey, @RequestParam String appName, @RequestParam String module,
			@RequestParam String studentKey, @RequestParam String deviceId, @RequestParam String loginCredentials)
			throws IOException {
		if (audioType == null || audioPath == null || appKey == null || appName == null || deviceId == null
				|| loginCredentials == null || audioType.trim().isEmpty() || audioPath.trim().isEmpty()
				|| appKey.trim().isEmpty() || appName.trim().isEmpty() || deviceId.trim().isEmpty()
				|| loginCredentials.trim().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
		} else {
			boolean loginCheckFlag = false;
			String validateLogin = propertiesService.getValue(module, PropertyConstants.VALIDATE_LOGIN);
			String loginRequired[] = validateLogin.split("\\|");
			for (int i = 0; i < loginRequired.length; i++) {
				if (audioType.equalsIgnoreCase(loginRequired[i])) {
					loginCheckFlag = true;
					break;
				}
			}
			try {
				if (loginCheckFlag) {
					if (studentKey == null || studentKey.trim().isEmpty()) {
						throw new BusinessException(ErrorCodeConstants.MISSING_REQUEST_PARAMS);
					}
					String emailId = regService.getStudentEmailByKey(studentKey);
					if (emailId == null || "".equals(emailId)) {
						throw new BusinessException(ErrorCodeConstants.INVALID_USER_ID, studentKey);
					} else {
						userLoginBusiness.isUserLoggedIn(deviceId, loginCredentials, emailId, UserType.S);
					}
				} else {
					UserLogin deviceLogin = userLoginBusiness.getDeviceLogin(deviceId, loginCredentials);
					if (deviceLogin == null) {
						throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS);
					}
				}
				if (audioPath.startsWith("/")) {
					audioPath = audioPath.substring(2, audioPath.length());
				}
				String audioFolderPath = propertiesService.getValue(module, "audioRootFolderPath." + audioType);
				File audioFile = new File(audioFolderPath + audioPath);
				if (audioFile != null && audioFile.exists()) {
					InputStream imageStream = new FileInputStream(audioFile);
					response.setContentType(MediaType.ALL_VALUE);
					StreamUtils.copy(imageStream, response.getOutputStream());
					imageStream.close();
				}
			} catch (BusinessException e) {
				throw new Fault(e);
			}
		}
	}
}