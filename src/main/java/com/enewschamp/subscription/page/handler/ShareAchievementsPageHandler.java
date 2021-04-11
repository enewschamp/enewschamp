package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.ReceipientsPageData;
import com.enewschamp.subscription.app.dto.StudentShareAchievementsDTO;
import com.enewschamp.subscription.app.dto.StudentShareAchievementsPageData;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.StudentShareAchievementsBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ShareAchievementsPageHandler")
public class ShareAchievementsPageHandler implements IPageHandler {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentShareAchievementsBusiness studentShareAchievementsBusiness;

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
		StudentShareAchievementsPageData studentShareAchievementsPageData = new StudentShareAchievementsPageData();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		List<StudentShareAchievementsDTO> studentShareAchievementsDTO = studentShareAchievementsBusiness
				.getStudentDetailsFromMaster(studentId);
		if (studentShareAchievementsDTO != null) {
			studentShareAchievementsPageData = mapDTOtoPage(studentShareAchievementsDTO.get(0),
					studentShareAchievementsPageData);
		}
		pageDto.setData(studentShareAchievementsPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		String methodName = pageNavigatorDTO.getSubmissionMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[2];
			params[0] = PageRequestDTO.class;
			params[1] = PageNavigatorDTO.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
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
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleSaveAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		StudentShareAchievementsPageData studentShareAchievementsPageData = mapPagedata(pageRequest);
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentShareAchievementsDTO studAch = mapPageToDTO(studentShareAchievementsPageData);
		studAch.setStudentId(studentId);
		studAch.setOperatorId("" + studentId);
		studAch.setRecordInUse(RecordInUseType.Y);
		studAch.setPersonalisedMessage(studentShareAchievementsPageData.getPersonalisedMessage());
		try {
			studentShareAchievementsBusiness.saveAsMaster(studAch);
		} catch (Exception e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
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
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		return StudentShareAchievementsPageData;
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

	public StudentShareAchievementsDTO mapPageToDTO(StudentShareAchievementsPageData studentShareAchievementsPageData) {
		StudentShareAchievementsDTO studentShareAchievementsDTO = new StudentShareAchievementsDTO();
		List<ReceipientsPageData> recipientList = studentShareAchievementsPageData.getRecipients();
		studentShareAchievementsDTO.setPersonalisedMessage(studentShareAchievementsPageData.getPersonalisedMessage());
		studentShareAchievementsDTO.setApprovalRequired(studentShareAchievementsPageData.getApprovalRequired());
		if (!recipientList.isEmpty()) {
			studentShareAchievementsDTO.setRecipientContact1(recipientList.get(0).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName1(recipientList.get(0).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting1(recipientList.get(0).getRecipientGreeting());
		}
		if (recipientList.size() > 1 && recipientList.get(1) != null) {
			studentShareAchievementsDTO.setRecipientContact2(recipientList.get(1).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName2(recipientList.get(1).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting2(recipientList.get(1).getRecipientGreeting());
		}

		if (recipientList.size() > 2 && recipientList.get(2) != null) {
			studentShareAchievementsDTO.setRecipientContact3(recipientList.get(2).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName3(recipientList.get(2).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting3(recipientList.get(2).getRecipientGreeting());
		}

		if (recipientList.size() > 3 && recipientList.get(3) != null) {
			studentShareAchievementsDTO.setRecipientContact4(recipientList.get(3).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName4(recipientList.get(3).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting4(recipientList.get(3).getRecipientGreeting());
		}

		if (recipientList.size() > 4 && recipientList.get(4) != null) {
			studentShareAchievementsDTO.setRecipientContact5(recipientList.get(4).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName5(recipientList.get(4).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting5(recipientList.get(4).getRecipientGreeting());
		}
		if (recipientList.size() > 5 && recipientList.get(5) != null) {
			studentShareAchievementsDTO.setRecipientContact6(recipientList.get(5).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName6(recipientList.get(5).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting6(recipientList.get(5).getRecipientGreeting());
		}
		if (recipientList.size() > 6 && recipientList.get(6) != null) {
			studentShareAchievementsDTO.setRecipientContact7(recipientList.get(6).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName7(recipientList.get(6).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting7(recipientList.get(6).getRecipientGreeting());
		}
		if (recipientList.size() > 7 && recipientList.get(7) != null) {
			studentShareAchievementsDTO.setRecipientContact8(recipientList.get(7).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName8(recipientList.get(7).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting8(recipientList.get(7).getRecipientGreeting());
		}
		if (recipientList.size() > 8 && recipientList.get(8) != null) {
			studentShareAchievementsDTO.setRecipientContact9(recipientList.get(8).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName9(recipientList.get(8).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting9(recipientList.get(8).getRecipientGreeting());
		}
		if (recipientList.size() > 9 && recipientList.get(9) != null) {
			studentShareAchievementsDTO.setRecipientContact10(recipientList.get(9).getRecipientContact());
			studentShareAchievementsDTO.setRecipientName10(recipientList.get(9).getRecipientName());
			studentShareAchievementsDTO.setRecipientGreeting10(recipientList.get(9).getRecipientGreeting());
		}
		return studentShareAchievementsDTO;
	}
}