package com.enewschamp.subscription.page.handler;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.subscription.app.dto.MyProfilePageData;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsPageData;
import com.enewschamp.subscription.app.dto.StudentDetailsWorkDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencePageData;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.enewschamp.subscription.app.dto.StudentSchoolWorkDTO;
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
	ModelMapper modelMapper;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String action = pageNavigationContext.getActionName();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = 0L;
		MyProfilePageData myProfilePageData = new MyProfilePageData();

		if (PageAction.get.toString().equalsIgnoreCase(action)
				|| PageAction.MyProfile.toString().equalsIgnoreCase(action)) {
			studentId = studentControlBusiness.getStudentId(emailId);
			StudentSubscriptionDTO studentSubscriptionDTO = studentSubscriptionBusiness
					.getStudentSubscriptionFromMaster(studentId, editionId);
			StudentSubscriptionPageData studentSubscriptionPageData = modelMapper.map(studentSubscriptionDTO,
					StudentSubscriptionPageData.class);
			StudentDetailsPageData studentDetailsPageData = new StudentDetailsPageData();
			StudentDetailsDTO studentDetailsDTO = studentDetailsBusiness.getStudentDetailsFromMaster(studentId);
			if (studentDetailsDTO == null) {
				StudentDetailsWorkDTO studentDetailsWorkDTO = studentDetailsBusiness
						.getStudentDetailsFromWork(studentId);
				if (studentDetailsWorkDTO != null) {
					studentDetailsPageData = modelMapper.map(studentDetailsWorkDTO, StudentDetailsPageData.class);
				}
			} else {
				studentDetailsPageData = modelMapper.map(studentDetailsDTO, StudentDetailsPageData.class);
			}
			StudentSchoolPageData studentSchoolPageData = new StudentSchoolPageData();
			StudentSchoolDTO studentSchoolDTO = schoolDetailsBusiness.getStudentFromMaster(studentId);
			if (studentSchoolDTO == null) {
				StudentSchoolWorkDTO studentSchoolWorkDTO = schoolDetailsBusiness.getStudentFromWork(studentId);
				if (studentSchoolWorkDTO != null) {
					studentSchoolPageData = modelMapper.map(studentSchoolWorkDTO, StudentSchoolPageData.class);
				}
			} else {
				studentSchoolPageData = modelMapper.map(studentSchoolDTO, StudentSchoolPageData.class);
			}
			StudentPreferencePageData studentPreferencesPageData = new StudentPreferencePageData();
			StudentPreferencesDTO studentPreferencesDTO = preferenceBusiness.getPreferenceFromMaster(studentId);
			if (studentPreferencesDTO == null) {
				StudentPreferencesWorkDTO studentPreferencesWorkDTO = preferenceBusiness
						.getPreferenceFromWork(studentId);
				if (studentPreferencesWorkDTO != null) {
					studentPreferencesPageData = modelMapper.map(studentPreferencesWorkDTO,
							StudentPreferencePageData.class);
				}
			} else {
				studentPreferencesPageData = modelMapper.map(studentPreferencesDTO, StudentPreferencePageData.class);
			}
			StudentShareAchievementsPageData studentShareAchievementsPageData = new StudentShareAchievementsPageData();
			List<StudentShareAchievementsDTO> studentsharedAchievementsList = studentShareAchievementsBusiness
					.getStudentDetailsFromMaster(studentId);
			if (studentsharedAchievementsList != null) {
				studentShareAchievementsPageData = mapDTOtoPage(studentsharedAchievementsList,
						studentShareAchievementsPageData);
			}
			// add the objects in ProfilePageData..
			myProfilePageData.setPreferences(studentPreferencesPageData);
			myProfilePageData.setSchoolDetails(studentSchoolPageData);
			myProfilePageData.setStudentDetails(studentDetailsPageData);
			myProfilePageData.setShareAchievements(studentShareAchievementsPageData);
			myProfilePageData.setSubscription(studentSubscriptionPageData);
			pageDto.setData(myProfilePageData);
		}
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {

		PageDTO pageDto = new PageDTO();
		if (PageAction.next.toString().equalsIgnoreCase(actionName)) {
		}

		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public StudentShareAchievementsPageData mapDTOtoPage(List<StudentShareAchievementsDTO> studentShareAchievementsList,
			StudentShareAchievementsPageData StudentShareAchievementsPageData) {
		for (StudentShareAchievementsDTO ach : studentShareAchievementsList) {
			String contact1 = ach.getContact1();
			String contact2 = ach.getContact2();
			String contact3 = ach.getContact3();
			String contact4 = ach.getContact4();
			String contact5 = ach.getContact5();
			String contact6 = ach.getContact6();
			String contact7 = ach.getContact7();
			String contact8 = ach.getContact8();
			String contact9 = ach.getContact9();
			String contact10 = ach.getContact10();

			List<String> contactList = new ArrayList<String>();
			contactList.add(contact1);
			contactList.add(contact2);
			contactList.add(contact3);
			contactList.add(contact4);
			contactList.add(contact5);
			contactList.add(contact6);
			contactList.add(contact7);
			contactList.add(contact8);
			contactList.add(contact9);
			contactList.add(contact10);

			StudentShareAchievementsPageData.setContacts(contactList);

		}
		return StudentShareAchievementsPageData;
	}
}
