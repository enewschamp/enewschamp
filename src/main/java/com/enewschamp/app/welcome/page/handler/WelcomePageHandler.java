package com.enewschamp.app.welcome.page.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.celebration.dto.CelebrationDTO;
import com.enewschamp.app.admin.celebration.entity.Celebration;
import com.enewschamp.app.admin.celebration.service.CelebrationService;
import com.enewschamp.app.admin.promotion.dto.PromotionDTO;
import com.enewschamp.app.admin.promotion.repository.Promotion;
import com.enewschamp.app.admin.promotion.service.PromotionService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.recognition.page.data.RecognitionData;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.app.scores.dto.StudentScoresMonthlyDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.scores.service.ScoreService;
import com.enewschamp.app.student.badges.business.StudentBadgesBusiness;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.student.scores.business.ScoresMonthlyGenreBusiness;
import com.enewschamp.app.student.scores.business.ScoresMonthlyTotalBusiness;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.app.welcome.page.data.WelcomePageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Edition;
import com.enewschamp.publication.domain.service.EditionService;
import com.enewschamp.subscription.app.dto.AppearancePageData;
import com.enewschamp.subscription.app.dto.MyPicturePageData;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesPageData;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageData;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.enewschamp.user.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "WelcomePageHandler")
public class WelcomePageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	UserLoginService loginService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	EditionService editionService;

	@Autowired
	UserLoginBusiness studentLoginBusiness;

	@Autowired
	StudentBadgesBusiness studentBadgesBusiness;

	@Autowired
	StudentRegistrationService studentRegistrationService;

	@Autowired
	BadgeService badgeService;

	@Autowired
	CelebrationService celebrationService;

	@Autowired
	PromotionService promotionService;

	@Autowired
	ScoresMonthlyTotalBusiness scoresMonthlyTotalBusiness;

	@Autowired
	ScoresMonthlyGenreBusiness scoresMonthlyGenreBusiness;

	@Autowired
	StudentRegistrationService regService;

	@Autowired
	StudentRegistrationBusiness studentRegBusiness;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	SubscriptionBusiness studentSubscriptionBusiness;

	@Autowired
	CityService cityService;

	@Autowired
	CountryService countryService;

	@Autowired
	StateService stateService;

	@Autowired
	SchoolService schoolService;

	@Autowired
	ScoreService scoreService;

	@Autowired
	UserService userService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		String methodName = pageNavigationContext.getLoadMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[1];
			params[0] = PageNavigationContext.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadWelcomePage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(studentId, editionId);
		studentRegBusiness.checkAndUpdateIfSubscriptionExpired(studentId, editionId);
		String editionName = "";
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		WelcomePageData pageData = new WelcomePageData();
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(studentId);
		StudentSubscriptionDTO studentSubscriptionDTO = studentSubscriptionBusiness
				.getStudentSubscriptionFromMaster(studentId, editionId);

		StudentSubscriptionPageData studentSubscriptionPageData = new StudentSubscriptionPageData();
		if (studentSubscriptionDTO != null) {
			studentSubscriptionPageData = modelMapper.map(studentSubscriptionDTO, StudentSubscriptionPageData.class);
			if ("F".equals(studentSubscriptionDTO.getSubscriptionSelected())) {
				studentSubscriptionPageData
						.setSubscriptionSelected(("Y".equals(studentControlDTO.getEvalAvailed()) ? "F" : "E"));
				studentSubscriptionPageData.setValidity(studentSubscriptionDTO.getEndDate().toString());
				studentSubscriptionPageData.setAutoRenewal(studentSubscriptionDTO.getAutoRenewal());
			} else if (studentSubscriptionDTO.getEndDate().isBefore(LocalDate.now())) {
				studentSubscriptionPageData.setValidity("");
			} else {
				studentSubscriptionPageData.setValidity(studentSubscriptionDTO.getEndDate().toString());
			}
		} else {
			studentSubscriptionPageData.setValidity("");
		}
		StudentDetailsPageData studentDetailsPageData = new StudentDetailsPageData();
		StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
		if (studentDetailsDTO != null) {
			studentDetailsPageData = modelMapper.map(studentDetailsDTO, StudentDetailsPageData.class);
		}
		List<Celebration> celebrationList = celebrationService.getCelebrationListForToday(editionId);
		List<CelebrationDTO> celebrationDTOList = new ArrayList<CelebrationDTO>();
		for (int i = 0; i < celebrationList.size(); i++) {
			CelebrationDTO celebrationDTO = modelMapper.map(celebrationList.get(i), CelebrationDTO.class);
			celebrationDTOList.add(celebrationDTO);
		}
		List<Promotion> promotionList = promotionService.getActivePromotionList(editionId);
		List<PromotionDTO> promotionDTOList = new ArrayList<PromotionDTO>();
		for (int i = 0; i < promotionList.size(); i++) {
			PromotionDTO promotionDTO = modelMapper.map(promotionList.get(i), PromotionDTO.class);
			promotionDTOList.add(promotionDTO);
		}
		MyPicturePageData myPicturePageData = new MyPicturePageData();
		AppearancePageData appearancePageData = new AppearancePageData();
		StudentRegistration studentRegistration = studentRegistrationService.getStudentReg(emailId);
		if (studentRegistration != null) {
			myPicturePageData.setAvatarName(studentRegistration.getAvatarName());
			myPicturePageData.setPhotoName(studentRegistration.getPhotoName());
			myPicturePageData.setImageApprovalRequired(studentRegistration.getImageApprovalRequired());
			if ("Y".equalsIgnoreCase(studentRegistration.getIsTestUser())) {
				pageData.setTestUser("Y");
			} else {
				pageData.setTestUser("N");
			}
			pageData.setCreationDatetime(studentRegistration.getCreationDateTime());
			appearancePageData.setTheme(studentRegistration.getTheme());
			appearancePageData.setFontHeight(studentRegistration.getFontHeight());
		} else {
			myPicturePageData.setAvatarName("");
			myPicturePageData.setPhotoName("");
			myPicturePageData.setImageApprovalRequired("");
			pageData.setTestUser("N");
			pageData.setCreationDatetime(null);
			appearancePageData.setTheme("");
			appearancePageData.setFontHeight("");
		}
		StudentSchoolPageData studentSchoolPageData = new StudentSchoolPageData();
		StudentSchoolDTO studentSchoolDTO = schoolDetailsBusiness.getStudentFromMaster(studentId);
		if (studentSchoolDTO != null) {
			studentSchoolPageData = modelMapper.map(studentSchoolDTO, StudentSchoolPageData.class);
			if (studentSchoolPageData.getCity() != null && !"Y".equals(studentSchoolPageData.getCityNotInTheList())) {
				studentSchoolPageData.setCity(cityService.getCity(studentSchoolPageData.getCity()).getDescription());
			}
			if (studentSchoolPageData.getState() != null && !"Y".equals(studentSchoolPageData.getStateNotInTheList())) {
				studentSchoolPageData.setState(stateService.getState(studentSchoolPageData.getState()).getName());
			}
			if (studentSchoolPageData.getCountry() != null
					&& !"Y".equals(studentSchoolPageData.getCountryNotInTheList())) {
				studentSchoolPageData
						.setCountry(countryService.getCountry(studentSchoolPageData.getCountry()).getName());
			}
			if (studentSchoolPageData.getSchool() != null
					&& !"Y".equals(studentSchoolPageData.getSchoolNotInTheList())) {
				studentSchoolPageData
						.setSchool(schoolService.get(Long.valueOf(studentSchoolPageData.getSchool())).getName());
			}
		}
		StudentPreferencesPageData studentPreferencesPageData = new StudentPreferencesPageData();
		StudentPreferencesDTO studentPreferencesDTO = preferenceBusiness.getPreferenceFromMaster(studentId);
		if (studentPreferencesDTO != null) {
			studentPreferencesPageData = modelMapper.map(studentPreferencesDTO, StudentPreferencesPageData.class);
		}
		Edition edition = editionService.getEdition(editionId);
		editionName = edition.getEditionName();
		pageData.setEditionName(editionName);
		pageData.setStudentId(studentId);
		int readingLevel = 3;
		if (studentPreferencesDTO != null) {
			readingLevel = Integer.valueOf(studentPreferencesDTO.getReadingLevel());
		}
		LocalDateTime lastLogin = loginService.getUserLastLoginDetails(emailId, deviceId, UserType.S);
		LocalDate fromDate = LocalDate.now().minusMonths(1);
		int year = fromDate.getYear();
		int month = fromDate.getMonthValue();
		Long yearMonth = Long.valueOf(year + "" + (month > 9 ? month : "0" + month));
		List<RecognitionData> studentbadges = studentBadgesBusiness.getBadgeDetails(studentId, editionId, readingLevel,
				yearMonth);

		List<StudentScoresMonthlyDTO> scoresMonthly = new ArrayList<StudentScoresMonthlyDTO>();
		ScoresSearchData searchData = new ScoresSearchData();
		searchData.setStudentId(studentId);
		searchData.setReadingLevel(readingLevel);
		searchData.setYearMonth("" + yearMonth);
		List<StudentScoresMonthlyTotalDTO> monthlyTotalScoresDTO = scoreService.findMonthWiseTotalScores(searchData);
		List<StudentScoresMonthlyGenreDTO> monthlyGenreScoresDTO = scoreService.findMonthWiseGenreScores(searchData);
		StudentScoresMonthlyDTO prevMonth = new StudentScoresMonthlyDTO();
		prevMonth.setMonth("" + yearMonth);
		prevMonth.setTotal(monthlyTotalScoresDTO);
		prevMonth.setByGenre(monthlyGenreScoresDTO);
		scoresMonthly.add(prevMonth);
		LocalDate todaysDate = LocalDate.now();
		year = todaysDate.getYear();
		month = todaysDate.getMonthValue();
		yearMonth = Long.valueOf(year + "" + (month > 9 ? month : "0" + month));
		searchData.setYearMonth("" + yearMonth);
		monthlyTotalScoresDTO = scoreService.findMonthWiseTotalScores(searchData);
		monthlyGenreScoresDTO = scoreService.findMonthWiseGenreScores(searchData);
		StudentScoresMonthlyDTO currMonth = new StudentScoresMonthlyDTO();
		currMonth.setMonth("" + yearMonth);
		currMonth.setTotal(monthlyTotalScoresDTO);
		currMonth.setByGenre(monthlyGenreScoresDTO);
		scoresMonthly.add(currMonth);
		pageData.setScoresMonthly(scoresMonthly);
		pageData.setAppearance(appearancePageData);
		pageData.setMyPicture(myPicturePageData);
		pageData.setPreferences(studentPreferencesPageData);
		pageData.setSchoolDetails(studentSchoolPageData);
		pageData.setStudentDetails(studentDetailsPageData);
		pageData.setSubscription(studentSubscriptionPageData);
		pageData.setBadgeDetails(studentbadges);
		pageData.setCelebrations(celebrationDTOList);
		pageData.setPromotions(promotionDTOList);
		pageData.setLastActivityDatetime(lastLogin);
		pageDto.setData(pageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

}