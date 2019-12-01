package com.enewschamp.subscription.app.fw.page.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.rules.UIControlsRuleExecutionService;
import com.enewschamp.app.common.uicontrols.service.UIControlsService;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.rules.PageNavRuleExecutionService;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.PageHandlerFactory;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;

import lombok.extern.java.Log;

@Log
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ui/v1")
public class AppPageController {

	@Autowired
	private PageHandlerFactory pageHandlerFactory;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UIControlsService uiControlService;

	@Autowired
	PageNavigationService pageNavigationService;
	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	PageNavRuleExecutionService pageNavRuleExecutionService;

	@Autowired
	UIControlsRuleExecutionService uiControlsRuleExecutionService;

	@Autowired
	EditionService editionService;
	@Autowired
	EnewschampApplicationProperties appConfig;

	@Autowired
	UserLoginBusiness studentLoginBusiness;

	@PostMapping(value = "/app")
	public ResponseEntity<PageDTO> processAppRequest(@RequestBody PageRequestDTO pageRequest) {

		ResponseEntity<PageDTO> response = null;
		try {
			String pageName = pageRequest.getHeader().getPageName();
			String actionName = pageRequest.getHeader().getAction();
			pageRequest.getHeader().setPageName(pageName);
			pageRequest.getHeader().setAction(actionName);

			PageDTO pageResponse = processRequest(pageName, actionName, pageRequest, "app");
			response = new ResponseEntity<PageDTO>(pageResponse, HttpStatus.OK);
		} catch (BusinessException e) {
			HeaderDTO header = pageRequest.getHeader();
			if (header == null) {
				header = new HeaderDTO();
			}
			header.setRequestStatus(RequestStatusType.F);
			throw new Fault(new HttpStatusAdapter(HttpStatus.INTERNAL_SERVER_ERROR), e, header);
		}

		return response;
	}

	private PageDTO processRequest(String pageName, String actionName, PageRequestDTO pageRequest, String context) {
		String operation = pageRequest.getHeader().getOperation();
		String edition = pageRequest.getHeader().getEditionID();
		// check if the edition exist..
		editionService.getEdition(edition);

		// set default page size from app..
		pageRequest.getHeader().setPageSize(appConfig.getPageSize());

		// get the page navigation based for current page
		PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(actionName, operation, pageName);

		// check of the page page has secured access....and if the student subscription
		// is valid...
		if (pageNavDto != null && AppConstants.YES.toString().equals(pageNavDto.getSecured())) {
			// validate of the user is already logged in..
			String emailId = pageRequest.getHeader().getEmailID();
			String deviceId = pageRequest.getHeader().getDeviceId();

			if ("".equals(emailId) || null == emailId) {
				throw new BusinessException(ErrorCodes.INVALID_EMAIL_ID, "Invalid Email Id");

			} else {
				boolean isLogged = studentLoginBusiness.isUserLoggedIn(deviceId, emailId, UserType.S);

				if (!isLogged) {
					boolean isSubscriptiionValid = subscriptionBusiness.isStudentSubscriptionValid(emailId, edition);
					if (!isSubscriptiionValid)
						throw new BusinessException(ErrorCodes.UNAUTH_ACCESS, "UnAuthorised Access");

				}
			}
		}
		// Process current page
		// PageDTO pageResponse =
		// pageHandlerFactory.getPageHandler(pageName).handleAppAction(actionName,
		// pageRequest,pageNavDto);
		PageDTO pageResponse = pageHandlerFactory.getPageHandler(pageName, context).handleAppAction(actionName,
				pageRequest, pageNavDto);
		System.out.println(">>>>>pageName>>>>>>>>" + pageName);
		System.out.println(">>>>>operation>>>>>>>>" + operation);
		System.out.println(">>>>>actionName>>>>>>>>" + actionName);
		System.out.println(">>>>>getErrorMessage>>>>>>>>" + pageResponse.getErrorMessage());
		if (pageResponse.getErrorMessage() == null) {
			// save data in master Tables (including the previous unsaved data
			String commitMasterData = pageNavDto.getCommitMasterData();
			if ("Y".equals(commitMasterData)) {
				List<PageNavigatorDTO> pagenNavList = pageNavigationService.getNavList(actionName, operation, pageName);
				for (PageNavigatorDTO item : pagenNavList) {
					pageHandlerFactory.getPageHandler(item.getCurrentPage(), context).saveAsMaster(actionName,
							pageRequest);
				}
			}
			String nextPageName = "";
			String nextPageOperation = "";
			// Load next page.. If action is next load the next page.. If action is previous
			// load the previous page.
			if (PageAction.next.toString().equalsIgnoreCase(actionName)
					|| PageAction.save.toString().equalsIgnoreCase(actionName)
					|| PageAction.home.toString().equalsIgnoreCase(actionName)
					|| PageAction.savedarticles.toString().equalsIgnoreCase(actionName)
					|| PageAction.GoPremium.toString().equalsIgnoreCase(actionName)
					|| PageAction.PicImage.toString().equalsIgnoreCase(actionName)
					|| PageAction.opinions.toString().equalsIgnoreCase(actionName)
					|| PageAction.Subscription.toString().equalsIgnoreCase(actionName)
					|| PageAction.savednewsarticle.toString().equalsIgnoreCase(actionName)
					|| PageAction.MyActivity.toString().equalsIgnoreCase(actionName)
					|| PageAction.Champs.toString().equalsIgnoreCase(actionName)
					|| PageAction.Menu.toString().equalsIgnoreCase(actionName)
					|| PageAction.HelpDesk.toString().equalsIgnoreCase(actionName)
					|| PageAction.Month.toString().equalsIgnoreCase(actionName)
					|| PageAction.Year.toString().equalsIgnoreCase(actionName)
					|| PageAction.Cancel.toString().equalsIgnoreCase(actionName)
					|| PageAction.MyProfile.toString().equalsIgnoreCase(actionName)
					|| PageAction.FilterSavedArticles.toString().equalsIgnoreCase(actionName)
					|| PageAction.ClearFilterSavedArticles.toString().equalsIgnoreCase(actionName)
					|| PageAction.Scores.toString().equalsIgnoreCase(actionName)
					|| PageAction.Trends.toString().equalsIgnoreCase(actionName)
					|| PageAction.recognitions.toString().equalsIgnoreCase(actionName)
					|| PageAction.LeftSwipe.toString().equalsIgnoreCase(actionName)
					|| PageAction.RightSwipe.toString().equalsIgnoreCase(actionName)
					|| PageAction.DeleteAccount.toString().equalsIgnoreCase(actionName)
					|| PageAction.LoadResetPassword.toString().equalsIgnoreCase(actionName)
					|| PageAction.LoginStudent.toString().equalsIgnoreCase(actionName)
					|| PageAction.CreateAccount.toString().equalsIgnoreCase(actionName)
					|| PageAction.ResendSecurityCode.toString().equalsIgnoreCase(actionName)
					|| PageAction.ResetPassword.toString().equalsIgnoreCase(actionName)
					|| PageAction.SubscriptionPrevious.toString().equalsIgnoreCase(actionName)
					|| PageAction.PreferencesSave.toString().equalsIgnoreCase(actionName)
					|| PageAction.CreateAccount.toString().equalsIgnoreCase(actionName)
					|| PageAction.PreferencesBack.toString().equalsIgnoreCase(actionName)
					|| PageAction.SubscriptionPeriodPrevious.toString().equalsIgnoreCase(actionName)
					|| PageAction.SomeUIErrorOccurred.toString().equals(actionName)
					|| PageAction.NetworkNotAvailable.toString().equals(actionName)
					|| PageAction.ClientAuthenticationFailed.toString().equals(actionName)
					|| PageAction.OnErrorLoadingWebPage.toString().equals(actionName)
					|| PageAction.OnBackPressedCancelTransaction.toString().equals(actionName)
					|| PageAction.OnTransactionCancel.toString().equals(actionName)
					|| PageAction.MyProfileBack.toString().equalsIgnoreCase(actionName)
					|| PageAction.MyPicture.toString().equalsIgnoreCase(actionName)
					|| PageAction.StudentDetails.toString().equalsIgnoreCase(actionName)
					|| PageAction.Preferences.toString().equalsIgnoreCase(actionName)
					|| PageAction.SchoolDetails.toString().equalsIgnoreCase(actionName)
					|| PageAction.Info.toString().equalsIgnoreCase(actionName)
					|| PageAction.Password.toString().equalsIgnoreCase(actionName)
					|| PageAction.More.toString().equalsIgnoreCase(actionName)
					|| PageAction.NewsEvents.toString().equalsIgnoreCase(actionName)
					|| PageAction.Notifications.toString().equalsIgnoreCase(actionName)) {
				nextPageName = pageNavDto.getNextPage();
				nextPageOperation = pageNavDto.getNextPageOperation();
			} else if (PageAction.previous.toString().equalsIgnoreCase(actionName)
					|| PageAction.back.toString().equalsIgnoreCase(actionName)) {
				nextPageName = pageNavDto.getPreviousPage();
				nextPageOperation = operation;

			} else if (PageAction.LaunchApp.toString().equalsIgnoreCase(actionName)
					|| PageAction.LoadPublication.toString().equalsIgnoreCase(actionName)
					|| PageAction.SubscriptionNext.toString().equalsIgnoreCase(actionName)
					|| PageAction.SubscriptionPeriodNext.toString().equalsIgnoreCase(actionName)
					|| PageAction.ClickArticleImage.toString().equalsIgnoreCase(actionName)
					|| PageAction.StudentDetailsNext.toString().equalsIgnoreCase(actionName)
					|| PageAction.StudentDetailsPrevious.toString().equalsIgnoreCase(actionName)
					|| PageAction.SchoolDetailsNext.toString().equalsIgnoreCase(actionName)
					|| PageAction.SchoolDetailsPrevious.toString().equalsIgnoreCase(actionName)
					|| PageAction.OnTransactionResponse.toString().equalsIgnoreCase(actionName)
					|| PageAction.ShareAchievementsSave.toString().equalsIgnoreCase(actionName)
					|| PageAction.Like.toString().equalsIgnoreCase(actionName)
					|| PageAction.SaveOpinion.toString().equalsIgnoreCase(actionName)
					|| PageAction.SaveArticle.toString().equalsIgnoreCase(actionName)
					|| PageAction.SaveMyPicture.toString().equalsIgnoreCase(actionName)) {
				Map<String, String> nextPageData = pageNavRuleExecutionService.getNextPageBasedOnRule(pageRequest,
						pageResponse, pageNavDto);
				System.out.println("nextPageData>>>>>>>>>>>>>" + nextPageData);
				if (!nextPageData.isEmpty()) {
					nextPageName = nextPageData.get("nextPage");
					nextPageOperation = nextPageData.get("nextPageOperation");
				} else {
					nextPageName = pageNavDto.getNextPage();
					nextPageOperation = pageNavDto.getNextPageOperation();
				}
			} else {
				nextPageName = pageName;
				nextPageOperation = operation;
			}
			if (!pageName.equals(nextPageName)) {
				PageNavigationContext pageNavigationContext = new PageNavigationContext();
				pageNavigationContext.setActionName(actionName);
				pageNavigationContext.setPageRequest(pageRequest);
				pageNavigationContext.setPreviousPageResponse(pageResponse);
				pageNavigationContext.setPreviousPage(nextPageName);
				// load data for the next page
				pageResponse = pageHandlerFactory.getPageHandler(nextPageName, context).loadPage(pageNavigationContext);
			} else {
				PageNavigationContext pageNavigationContext = new PageNavigationContext();
				pageNavigationContext.setActionName(actionName);
				pageNavigationContext.setPageRequest(pageRequest);
				pageNavigationContext.setPreviousPageResponse(pageResponse);
				pageNavigationContext.setPreviousPage(pageName);
				// load data for the same page
				pageResponse = pageHandlerFactory.getPageHandler(pageName, context).loadPage(pageNavigationContext);
			}
			System.out.println(">>>>>>>>>>>>>" + nextPageName);
			System.out.println(">>>>>>>>>>>>>" + nextPageOperation);
			System.out.println(">>>>>>>>>>>>>" + actionName);
			addSuccessHeader(nextPageName, actionName, nextPageOperation, pageResponse);
			// attach UI controls for the next page to the response
			addUIControls(nextPageName, nextPageOperation, pageRequest, pageResponse);
		}
		return pageResponse;
	}

	private void addSuccessHeader(String nextPageName, String actionName, String nextOperationName, PageDTO page) {

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
		List<UIControlsDTO> uiControls = new ArrayList<UIControlsDTO>();
		for (UIControlsDTO uiControlsDTO : uiControlsTemp) {
			uiControls.add(uiControlsRuleExecutionService.getControlParamsBasedOnRule(pageRequest, pageResponse,
					uiControlsDTO));
		}
		pageResponse.setScreenProperties(uiControls);
	}

}
