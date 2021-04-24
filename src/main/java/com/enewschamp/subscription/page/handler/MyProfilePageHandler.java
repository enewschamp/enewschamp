package com.enewschamp.subscription.page.handler;

import java.time.LocalDate;
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
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.subscription.app.dto.MyPicturePageData;
import com.enewschamp.subscription.app.dto.MyProfilePageData;
import com.enewschamp.subscription.app.dto.ReceipientsPageData;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesPageData;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.enewschamp.subscription.app.dto.StudentShareAchievementsDTO;
import com.enewschamp.subscription.app.dto.StudentShareAchievementsPageData;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionPageData;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentShareAchievementsBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "MyProfilePageHandler")
public class MyProfilePageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentRegistrationService studentRegistrationService;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	StudentDetailsBusiness studentDetailsBusiness;

	@Autowired
	StudentShareAchievementsBusiness studentShareAchievementsBusiness;

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
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = pageNavigationContext.getPageRequest().getHeader().getStudentId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		MyProfilePageData myProfilePageData = new MyProfilePageData();
		StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(studentId);
		StudentSubscriptionDTO studentSubscriptionDTO = studentSubscriptionBusiness
				.getStudentSubscriptionFromMaster(studentId, editionId);
		StudentSubscriptionPageData studentSubscriptionPageData = modelMapper.map(studentSubscriptionDTO,
				StudentSubscriptionPageData.class);
		if ("F".equals(studentSubscriptionDTO.getSubscriptionSelected())) {
			studentSubscriptionPageData
					.setSubscriptionSelected(("Y".equals(studentControlDTO.getEvalAvailed()) ? "F" : "E"));
			studentSubscriptionPageData.setValidity(studentSubscriptionDTO.getEndDate().toString());
		} else if (studentSubscriptionDTO.getEndDate().isBefore(LocalDate.now())) {
			studentSubscriptionPageData.setValidity("");
		} else {
			studentSubscriptionPageData.setValidity(studentSubscriptionDTO.getEndDate().toString());
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
			myPicturePageData.setImageApprovalRequired(studentRegistration.getImageApprovalRequired());
		} else {
			myPicturePageData.setAvatarName("");
			myPicturePageData.setPhotoName("");
			myPicturePageData.setImageApprovalRequired("");
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
		StudentShareAchievementsPageData studentShareAchievementsPageData = new StudentShareAchievementsPageData();
		List<StudentShareAchievementsDTO> studentsharedAchievementsList = studentShareAchievementsBusiness
				.getStudentDetailsFromMaster(studentId);
		if (studentsharedAchievementsList != null) {
			studentShareAchievementsPageData = mapDTOtoPage(studentsharedAchievementsList.get(0),
					studentShareAchievementsPageData);
		}
		myProfilePageData.setMyPicture(myPicturePageData);
		myProfilePageData.setPreferences(studentPreferencesPageData);
		myProfilePageData.setSchoolDetails(studentSchoolPageData);
		myProfilePageData.setStudentDetails(studentDetailsPageData);
		myProfilePageData.setShareAchievements(studentShareAchievementsPageData);
		myProfilePageData.setSubscription(studentSubscriptionPageData);
		pageDto.setData(myProfilePageData);
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

	public StudentShareAchievementsPageData mapDTOtoPage(StudentShareAchievementsDTO ach,
			StudentShareAchievementsPageData studentShareAchievementsPageData) {
		String recipientName1 = ach.getRecipientName1();
		String recipientName2 = ach.getRecipientName2();
		String recipientName3 = ach.getRecipientName3();
		String recipientName4 = ach.getRecipientName4();
		String recipientName5 = ach.getRecipientName5();
		String recipientName6 = ach.getRecipientName6();
		String recipientName7 = ach.getRecipientName7();
		String recipientName8 = ach.getRecipientName8();
		String recipientName9 = ach.getRecipientName9();
		String recipientName10 = ach.getRecipientName10();
		String recipientGreeting1 = ach.getRecipientGreeting1();
		String recipientGreeting2 = ach.getRecipientGreeting2();
		String recipientGreeting3 = ach.getRecipientGreeting3();
		String recipientGreeting4 = ach.getRecipientGreeting4();
		String recipientGreeting5 = ach.getRecipientGreeting5();
		String recipientGreeting6 = ach.getRecipientGreeting6();
		String recipientGreeting7 = ach.getRecipientGreeting7();
		String recipientGreeting8 = ach.getRecipientGreeting8();
		String recipientGreeting9 = ach.getRecipientGreeting9();
		String recipientGreeting10 = ach.getRecipientGreeting10();
		String recipientContact1 = ach.getRecipientContact1();
		String recipientContact2 = ach.getRecipientContact2();
		String recipientContact3 = ach.getRecipientContact3();
		String recipientContact4 = ach.getRecipientContact4();
		String recipientContact5 = ach.getRecipientContact5();
		String recipientContact6 = ach.getRecipientContact6();
		String recipientContact7 = ach.getRecipientContact7();
		String recipientContact8 = ach.getRecipientContact8();
		String recipientContact9 = ach.getRecipientContact9();
		String recipientContact10 = ach.getRecipientContact10();

		List<ReceipientsPageData> recipientList = new ArrayList<ReceipientsPageData>();
		recipientList = addRecipient(recipientContact1, recipientName1, recipientGreeting1, recipientList);
		recipientList = addRecipient(recipientContact2, recipientName2, recipientGreeting2, recipientList);
		recipientList = addRecipient(recipientContact3, recipientName3, recipientGreeting3, recipientList);
		recipientList = addRecipient(recipientContact4, recipientName4, recipientGreeting4, recipientList);
		recipientList = addRecipient(recipientContact5, recipientName5, recipientGreeting5, recipientList);
		recipientList = addRecipient(recipientContact6, recipientName6, recipientGreeting6, recipientList);
		recipientList = addRecipient(recipientContact7, recipientName7, recipientGreeting7, recipientList);
		recipientList = addRecipient(recipientContact8, recipientName8, recipientGreeting8, recipientList);
		recipientList = addRecipient(recipientContact9, recipientName9, recipientGreeting9, recipientList);
		recipientList = addRecipient(recipientContact10, recipientName10, recipientGreeting10, recipientList);

		studentShareAchievementsPageData.setRecipients(recipientList);
		studentShareAchievementsPageData.setPersonalisedMessage(ach.getPersonalisedMessage());
		studentShareAchievementsPageData.setApprovalRequired(ach.getApprovalRequired());
		return studentShareAchievementsPageData;

	}

	private List<ReceipientsPageData> addRecipient(String recipientContact, String recipientName,
			String recipientGreeting, List<ReceipientsPageData> list) {
		if (recipientContact != null && !"".equals(recipientContact.trim())) {
			ReceipientsPageData receipientsPageData = new ReceipientsPageData();
			receipientsPageData.setRecipientContact(recipientContact);
			receipientsPageData.setRecipientName(recipientName);
			receipientsPageData.setRecipientGreeting(recipientGreeting);
			list.add(receipientsPageData);
		}
		return list;
	}
}