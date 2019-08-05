package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.country.dto.CountryDTO;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.enewschamp.subscription.app.dto.StudentSchoolWorkDTO;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "SchoolDetailsPageHandler")
public class SchoolDetailsPageHandler implements IPageHandler {
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;
	
	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	SchoolService schoolService;

	@Autowired
	CountryService countryService;

	@Autowired
	StateService stateService;

	@Autowired
	CityService cityService;
	@Autowired
	PageNavigationService pageNavigationService;
	
	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;
		String action = pageNavigationContext.getActionName();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();
		String operation = pageNavigationContext.getPageRequest().getHeader().getOperation();
		String pageName = pageNavigationContext.getPreviousPage();
		
		//StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
	/*	if (studentControlDTO != null) {
			studentId = studentControlDTO.getStudentID();
		} else {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			if(studentControlWorkDTO!=null)
			studentId = studentControlWorkDTO.getStudentID();
		}*/
		studentId = studentControlBusiness.getStudentId(emailId);
		if(PageAction.next.toString().equalsIgnoreCase(action))
		{
			StudentSchoolPageData studentSchoolPageData = new StudentSchoolPageData();
			List<CountryDTO> countries = countryService.getAll();
			studentSchoolPageData.setCountryLOV(countries);
			pageDTO.setData(studentSchoolPageData);
		}
		else if(PageAction.previous.toString().equalsIgnoreCase(action))
		{
			PageNavigatorDTO pageNavDto = pageNavigationService.getNavPage(action, operation, pageName);
			if(PageSaveTable.M.toString().equalsIgnoreCase(pageNavDto.getUpdationTable()))
			{
				StudentSchoolDTO studentSchoolDTO = schoolDetailsBusiness.getStudentFromMaster(studentId);
				StudentSchoolPageData studentSchoolPageData = modelMapper.map(studentSchoolDTO, StudentSchoolPageData.class);

				// set the state, country and cities...
				List<CountryDTO> countries = countryService.getAll();
				studentSchoolPageData.setCountryLOV(countries);

				pageDTO.setData(studentSchoolPageData);
			}
			else if(PageSaveTable.W.toString().equalsIgnoreCase(pageNavDto.getUpdationTable()))
			{
				StudentSchoolWorkDTO studentSchoolWorkDTO = schoolDetailsBusiness.getStudentFromWork(studentId);
					StudentSchoolPageData studentSchoolPageData = modelMapper.map(studentSchoolWorkDTO, StudentSchoolPageData.class);
				// set the state, country and cities...
				List<CountryDTO> countries = countryService.getAll();
				studentSchoolPageData.setCountryLOV(countries);

				pageDTO.setData(studentSchoolPageData);
			}
		}
		// set the header as is...
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		Long studentId = 0L;
		StudentSchoolPageData studentSchoolPageData = null;

		studentSchoolPageData = mapPagedata(pageRequest);

		// get student ID from student control..
		String emailId = pageRequest.getHeader().getEmailID();
		//StudentControlWorkDTO StudentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		//studentId = StudentControlWorkDTO.getStudentID();
		studentId = studentControlBusiness.getStudentId(emailId);
		StudentSchoolDTO studSchool = modelMapper.map(studentSchoolPageData, StudentSchoolDTO.class);
		studSchool.setStudentID(studentId);

		// studentPreferencePageDTO.set
		schoolDetailsBusiness.saveAsMaster(studSchool);
		pageDto.setHeader(pageRequest.getHeader());

		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		String action = pageRequest.getHeader().getAction();

		if (PageAction.next.toString().equalsIgnoreCase(action)) {

			String saveIn = pageNavigatorDTO.getUpdationTable();
			if (PageSaveTable.M.toString().equals(saveIn)) {
				StudentSchoolPageData studentSchoolPageData = null;

				studentSchoolPageData = mapPagedata(pageRequest);

				// get student ID from student control..
				String emailId = pageRequest.getHeader().getEmailID();

				//StudentControlWorkDTO StudentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);

				//Long studentId = StudentControlWorkDTO.getStudentID();
				Long studentId = studentControlBusiness.getStudentId(emailId);
				StudentSchoolDTO studSchool = modelMapper.map(studentSchoolPageData, StudentSchoolDTO.class);
				studSchool.setStudentID(studentId);
				schoolDetailsBusiness.saveAsMaster(studSchool);

			} else if (PageSaveTable.W.toString().equals(saveIn)) {
				StudentSchoolPageData studentSchoolPageData = null;

				studentSchoolPageData = mapPagedata(pageRequest);

				// get student ID from student control..
				String emailId = pageRequest.getHeader().getEmailID();
				//StudentControlWorkDTO StudentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);

				//Long studentId = StudentControlWorkDTO.getStudentID();
				Long studentId = studentControlBusiness.getStudentId(emailId);

				StudentSchoolWorkDTO studenSchool = modelMapper.map(studentSchoolPageData, StudentSchoolWorkDTO.class);
				studenSchool.setStudentID(studentId);

				// studentPreferencePageDTO.set
				schoolDetailsBusiness.saveAsWork(studenSchool);
			}
		}

		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public StudentSchoolPageData mapPagedata(PageRequestDTO pageRequest) {
		StudentSchoolPageData studentSchoolPageData = null;
		try {
			studentSchoolPageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentSchoolPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodes.SREVER_ERROR);

		}
		return studentSchoolPageData;
	}
}