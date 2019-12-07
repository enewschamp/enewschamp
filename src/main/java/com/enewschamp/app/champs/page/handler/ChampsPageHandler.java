package com.enewschamp.app.champs.page.handler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.champs.page.data.ChampsPageData;
import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.service.StudentChampService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ChampsPageHandler")
public class ChampsPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	StudentChampService studentChampService;

	@Autowired
	StudentControlBusiness studentControlBusiness;
	@Autowired
	PreferenceBusiness preferenceBusiness;

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
		String eMailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		int pageNo = pageNavigationContext.getPageRequest().getHeader().getPageNo();

		ChampsSearchData searchData = new ChampsSearchData();

		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					ChampsSearchData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (PageAction.Champs.toString().equalsIgnoreCase(action)) {
			Long studentId = studentControlBusiness.getStudentId(eMailId);
			if (studentId == null || studentId == 0L) {
				throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
			}
			StudentPreferencesDTO studPref = preferenceBusiness.getPreferenceFromMaster(studentId);
			searchData.setReadingLevel(studPref.getReadingLevel());
			// set month year to last Month..
			LocalDate currdate = LocalDate.now();
			LocalDate prevMonth = currdate.minusMonths(1);
			int month = prevMonth.getMonthValue();
			String monthStr = (month < 10) ? "0" + month : "" + month;

			int year = prevMonth.getYear();
			String newYearMonth = year + monthStr;
			searchData.setMonthYear(newYearMonth);

		}
		if (PageAction.Level1.toString().equalsIgnoreCase(action)) {
			searchData.setReadingLevel("1");
			LocalDate currdate = LocalDate.now();
			int month = currdate.getMonthValue();
			String monthStr = (month < 10) ? "0" + month : "" + month;
			int year = currdate.getYear();
			String newYearMonth = year + monthStr;
			searchData.setMonthYear(newYearMonth);

		}
		if (PageAction.Level2.toString().equalsIgnoreCase(action)) {
			searchData.setReadingLevel("2");
			LocalDate currdate = LocalDate.now();
			int month = currdate.getMonthValue();
			String monthStr = (month < 10) ? "0" + month : "" + month;
			int year = currdate.getYear();
			String newYearMonth = year + monthStr;
			searchData.setMonthYear(newYearMonth);

		}
		if (PageAction.Level3.toString().equalsIgnoreCase(action)) {
			searchData.setReadingLevel("3");
			LocalDate currdate = LocalDate.now();
			int month = currdate.getMonthValue();
			String monthStr = (month < 10) ? "0" + month : "" + month;
			int year = currdate.getYear();
			String newYearMonth = year + monthStr;
			searchData.setMonthYear(newYearMonth);

		}
		if (PageAction.next.toString().equalsIgnoreCase(action)
				|| PageAction.LeftSwipe.toString().equalsIgnoreCase(action)) {
			pageNo++;
		}
		if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.RightSwipe.toString().equalsIgnoreCase(action)) {
			if (pageNo > 1)
				pageNo--;
		}

		List<ChampStudentDTO> champList = studentChampService.findChampStudents(searchData, pageNo);
		ChampsPageData pageData = new ChampsPageData();
		pageData.setChamps(champList);
		pageData.setMonthYear(searchData.getMonthYear());
		pageData.setReadingLevel(searchData.getReadingLevel());
		pageData.setPageNo(pageNo++);
		pageDto.setData(pageData);

		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
