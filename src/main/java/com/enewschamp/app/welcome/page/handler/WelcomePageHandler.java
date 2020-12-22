package com.enewschamp.app.welcome.page.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.recognition.page.data.RecognitionData;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.app.student.badges.business.StudentBadgesBusiness;
import com.enewschamp.app.student.monthlytrends.business.TrendsMonthlyByGenreBusiness;
import com.enewschamp.app.student.monthlytrends.business.TrendsMonthlyTotalBusiness;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyByGenreDTO;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyTotalDTO;
import com.enewschamp.app.student.registration.business.StudentRegistrationBusiness;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.app.trends.service.TrendsService;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.app.user.login.service.UserLoginService;
import com.enewschamp.app.welcome.page.data.BadgeDetailsDTO;
import com.enewschamp.app.welcome.page.data.WelcomePageData;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.master.badge.service.BadgeService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Badge;
import com.enewschamp.publication.domain.entity.Edition;
import com.enewschamp.publication.domain.service.EditionService;
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
	TrendsService trendsService;
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
	TrendsMonthlyTotalBusiness trendsMonthlyTotalBusiness;

	@Autowired
	TrendsMonthlyByGenreBusiness trendsMonthlyByGenreBusiness;

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
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
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
		studentRegBusiness.checkAndUpdateIfEvalPeriodExpired(emailId, editionId);
		studentRegBusiness.checkAndUpdateIfSubscriptionExpired(emailId, editionId);
		String editionName = "";
		String deviceId = pageNavigationContext.getPageRequest().getHeader().getDeviceId();
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();
		int month = todaysDate.getMonthValue();
		Long monthYear = Long.valueOf(year + "" + (month > 9 ? month : "0" + month));
		Long pointsToNextBadge = 0L;
		WelcomePageData pageData = new WelcomePageData();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
		StudentSubscriptionDTO studentSubscriptionDTO = studentSubscriptionBusiness
				.getStudentSubscriptionFromMaster(studentId, editionId);

		StudentSubscriptionPageData studentSubscriptionPageData = new StudentSubscriptionPageData();
		if (studentSubscriptionDTO != null) {
			studentSubscriptionPageData = modelMapper.map(studentSubscriptionDTO, StudentSubscriptionPageData.class);
			if ("F".equals(studentSubscriptionDTO.getSubscriptionSelected())) {
				studentSubscriptionPageData
						.setSubscriptionSelected(("Y".equals(studentControlDTO.getEvalAvailed()) ? "F" : "E"));
				studentSubscriptionPageData.setValidity(studentSubscriptionDTO.getEndDate().toString());
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
		MyPicturePageData myPicturePageData = new MyPicturePageData();
		StudentRegistration studentRegistration = studentRegistrationService.getStudentReg(emailId);
		if (studentRegistration != null) {
			myPicturePageData.setAvatarName(studentRegistration.getAvatarName());
			myPicturePageData.setPhotoName(studentRegistration.getPhotoName());
		} else {
			myPicturePageData.setAvatarName("");
			myPicturePageData.setPhotoName("");
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
		pageData.setMyPicture(myPicturePageData);
		pageData.setPreferences(studentPreferencesPageData);
		pageData.setSchoolDetails(studentSchoolPageData);
		pageData.setStudentDetails(studentDetailsPageData);
		pageData.setSubscription(studentSubscriptionPageData);
		Edition edition = editionService.getEdition(editionId);
		editionName = edition.getEditionName();
		pageData.setEditionName(editionName);
		pageData.setStudentId(studentId);
		int readingLevel = 3;
		if (studentPreferencesDTO != null) {
			readingLevel = Integer.valueOf(studentPreferencesDTO.getReadingLevel());
		}
		LocalDateTime lastLogin = loginService.getUserLastLoginDetails(emailId, deviceId, UserType.S);
		pageData.setLastActivityDatetime(lastLogin);
		List<RecognitionData> studentbadges = studentBadgesBusiness.getBadgeDetails(studentId, editionId, readingLevel,
				monthYear);
		List<BadgeDetailsDTO> badgeDetails = new ArrayList<BadgeDetailsDTO>();
		if (studentbadges != null && studentbadges.size() > 0) {
			for (RecognitionData studentBadge : studentbadges) {
				String message = "";
				if (studentBadge.getBadgeGenre().equalsIgnoreCase("MONTHLY")) {
					TrendsMonthlyTotalDTO trendsMonthlyTotalDTO = trendsMonthlyTotalBusiness.getMonthlyTrend(studentId,
							editionId, readingLevel, "" + monthYear);
					if (trendsMonthlyTotalDTO != null) {
						Long points = trendsMonthlyTotalDTO.getQuizCorrect();
						Badge nextbadge = badgeService.getNextBadgeForGenre(editionId,
								trendsMonthlyTotalDTO.getGenreId(), readingLevel, points);
						if (nextbadge != null) {
							Long monthlyPointsToScore = nextbadge.getMonthlyPointsToScore();
							pointsToNextBadge = monthlyPointsToScore - points;
							message = "Just " + pointsToNextBadge + " away from winning a " + nextbadge.getNameId();
						}
					}
				} else {
					TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO = trendsMonthlyByGenreBusiness.getMonthlyTrend(
							studentId, editionId, readingLevel, "" + monthYear, studentBadge.getBadgeGenre());
					if (trendsMonthlyByGenreDTO != null) {
						Long points = trendsMonthlyByGenreDTO.getQuizCorrect();
						Badge nextbadge = badgeService.getNextBadgeForGenre(editionId,
								trendsMonthlyByGenreDTO.getGenreId(), readingLevel, points);
						if (nextbadge != null) {
							Long monthlyPointsToScore = nextbadge.getMonthlyPointsToScore();
							pointsToNextBadge = monthlyPointsToScore - points;
							message = "Just " + pointsToNextBadge + " away from winning a " + nextbadge.getNameId();
						}
					}
				}
				Badge badge = badgeService.get(studentBadge.getBadgeId());
				BadgeDetailsDTO badgeDetailsDTO = new BadgeDetailsDTO();
				badgeDetailsDTO.setBadgeId(badge.getBadgeId());
				badgeDetailsDTO.setBadgeName(badge.getNameId());
				badgeDetailsDTO.setImageName(badge.getImageName());
				badgeDetailsDTO.setBadgeGenre(badge.getGenreId());
				// modelMapper.map(badge, BadgeDetailsDTO.class);
				badgeDetailsDTO.setBadgeAwayPointsMessage(message);
				badgeDetails.add(badgeDetailsDTO);
			}
		}
		pageData.setBadgeDetails(badgeDetails);
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
