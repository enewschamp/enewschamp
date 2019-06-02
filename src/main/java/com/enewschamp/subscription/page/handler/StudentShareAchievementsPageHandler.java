package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentShareAchievementsDTO;
import com.enewschamp.subscription.app.dto.StudentShareAchievementsPageData;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentShareAchievementsBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "StudentShareAchievementsPageHandler")
public class StudentShareAchievementsPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentShareAchievementsBusiness studentShareAchievementsBusiness;

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
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		Long studentId = 0L;

		StudentShareAchievementsPageData StudentShareAchievementsPageData = new StudentShareAchievementsPageData();

		if (PageAction.next.toString().equalsIgnoreCase(action)) {
			// load the static data..
			// StudentShareAchievementsPageData StudentShareAchievementsPageData = new
			// StudentShareAchievementsPageData();
			// StudentShareAchievementsPageData.setIncompleteFormText(appConfig.getIncompleteFormText());

		} else if (PageAction.back.toString().equalsIgnoreCase(action)) {
			studentId = studentControlBusiness.getStudentId(emailId);

			List<StudentShareAchievementsDTO> StudentShareAchievementsDTO = studentShareAchievementsBusiness
					.getStudentDetailsFromMaster(studentId);
			StudentShareAchievementsPageData = mapDTOtoPage(StudentShareAchievementsDTO,
					StudentShareAchievementsPageData);

			pageDto.setData(StudentShareAchievementsPageData);
		}
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		// String emailId = pageRequest.getHeader().getEmailID();
		// Long studentId = studentControlBusiness.getStudentId(emailId);

		// preferenceBusiness.workToMaster(studentId);
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {

		PageDTO pageDto = new PageDTO();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		String emailId = pageRequest.getHeader().getEmailID();

		if (PageAction.save.toString().equalsIgnoreCase(actionName)) {

			if (PageSaveTable.M.toString().equals(saveIn)) {

				StudentShareAchievementsPageData StudentShareAchievementsPageData = mapPagedata(pageRequest);

				Long studentId = studentControlBusiness.getStudentId(emailId);

				StudentShareAchievementsDTO studAch = mapPageToDTO(StudentShareAchievementsPageData);
				studAch.setStudentId(studentId);

				// studentPreferencePageDTO.set
				studentShareAchievementsBusiness.saveAsMaster(studAch);

			} else if (PageSaveTable.W.toString().equals(saveIn)) {
			}
		}

		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public StudentShareAchievementsPageData mapPagedata(PageRequestDTO pageRequest) {
		StudentShareAchievementsPageData StudentShareAchievementsPageData = null;
		try {
			StudentShareAchievementsPageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentShareAchievementsPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);
		}
		return StudentShareAchievementsPageData;
	}

	public StudentShareAchievementsPageData mapDTOtoPage(List<StudentShareAchievementsDTO> studentShareAchievements,
			StudentShareAchievementsPageData StudentShareAchievementsPageData) {
		for (StudentShareAchievementsDTO ach : studentShareAchievements) {
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
			;
		}
		return StudentShareAchievementsPageData;
	}

	public StudentShareAchievementsDTO mapPageToDTO(StudentShareAchievementsPageData studentShareAchievementsPageData) {
		StudentShareAchievementsDTO studentShareAchievementsDTO = new StudentShareAchievementsDTO();
		List<String> contactList = studentShareAchievementsPageData.getContacts();
		studentShareAchievementsDTO.setPersonalMessage(studentShareAchievementsPageData.getPersonalMsg());
		
		if (!contactList.isEmpty())
			studentShareAchievementsDTO.setContact1(contactList.get(0));

		if (contactList.size()>1 && contactList.get(1) != null)
			studentShareAchievementsDTO.setContact2(contactList.get(1));

		if (contactList.size()>2 && contactList.get(2) != null)
			studentShareAchievementsDTO.setContact3(contactList.get(2));

		if (contactList.size()>3 && contactList.get(3) != null)
			studentShareAchievementsDTO.setContact4(contactList.get(3));

		if (contactList.size()>4 && contactList.get(4) != null)
			studentShareAchievementsDTO.setContact5(contactList.get(4));
		if ( contactList.size()>5 && contactList.get(5) != null)
			studentShareAchievementsDTO.setContact6(contactList.get(5));
		if (contactList.size()>6 && contactList.get(6) != null)
			studentShareAchievementsDTO.setContact7(contactList.get(6));
		if (contactList.size()>7 && contactList.get(7) != null)
			studentShareAchievementsDTO.setContact8(contactList.get(7));
		if (contactList.size()>8 && contactList.get(8) != null)
			studentShareAchievementsDTO.setContact9(contactList.get(8));
		if (contactList.size()>9 && contactList.get(9) != null)
			studentShareAchievementsDTO.setContact10(contactList.get(9));

		return studentShareAchievementsDTO;
	}
}
