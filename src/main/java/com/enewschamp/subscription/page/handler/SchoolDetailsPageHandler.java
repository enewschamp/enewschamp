package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.CityLOVDTO;
import com.enewschamp.subscription.app.dto.CountryPageData;
import com.enewschamp.subscription.app.dto.SchoolData;
import com.enewschamp.subscription.app.dto.SchoolDetailsRequestData;
import com.enewschamp.subscription.app.dto.SchoolLovDTO;
import com.enewschamp.subscription.app.dto.StateLOVDTO;
import com.enewschamp.subscription.app.dto.StatePageData;
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
	private EnewschampApplicationProperties appConfig;

	
	
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
			List<CountryPageData> countries = countryService.getCountries();
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
				List<CountryPageData> countries = countryService.getCountries();
				studentSchoolPageData.setCountryLOV(countries);

				pageDTO.setData(studentSchoolPageData);
			}
			else if(PageSaveTable.W.toString().equalsIgnoreCase(pageNavDto.getUpdationTable()))
			{
				StudentSchoolWorkDTO studentSchoolWorkDTO = schoolDetailsBusiness.getStudentFromWork(studentId);
					StudentSchoolPageData studentSchoolPageData = modelMapper.map(studentSchoolWorkDTO, StudentSchoolPageData.class);
				// set the state, country and cities...
				List<CountryPageData> countries = countryService.getCountries();
				studentSchoolPageData.setCountryLOV(countries);

				pageDTO.setData(studentSchoolPageData);
			}
		}
		else if(PageAction.schooldetails.toString().equalsIgnoreCase(action))
		{
			StudentSchoolDTO studentSchoolDTO = schoolDetailsBusiness.getStudentFromMaster(studentId);
			Long schoolId = studentSchoolDTO.getSchoolId();
			School school= schoolService.get(schoolId);
			SchoolData schoolData = new SchoolData();
			schoolData.setId(school.getSchoolId());
			schoolData.setName(school.getName());
			String countryId = school.getCountryId();
			String stateId = school.getStateId();
			String cityId = school.getCityId();
			
			StudentSchoolPageData studentSchoolPageData = modelMapper.map(studentSchoolDTO, StudentSchoolPageData.class);
			studentSchoolPageData.setSchool(schoolData);
			
			// set the state, country and cities...
			List<CountryPageData> countries = countryService.getCountries();
			studentSchoolPageData.setCountryLOV(countries);

			CountryPageData schoolCountryData = countryService.getCountry(countryId);
			studentSchoolPageData.setCountry(schoolCountryData);
			
			StatePageData statePageData = stateService.getState(stateId);
			studentSchoolPageData.setState(statePageData);
			
			City city  = cityService.getCity(cityId);
			if(city!=null)
			studentSchoolPageData.setCityID(city.getDescription());
			
			studentSchoolPageData.setIncompeleteFormText(appConfig.getIncompleteFormText());
			
			pageDTO.setData(studentSchoolPageData);
		}
		else if(PageAction.stateLov.toString().equalsIgnoreCase(action))
		{
			String countryId="";
			try {
				SchoolDetailsRequestData	schoolDetailsRequestData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
						SchoolDetailsRequestData.class);
				countryId = schoolDetailsRequestData.getCountryID();
				
			} catch (IOException e) {
				throw new BusinessException(ErrorCodes.SREVER_ERROR);

			}
			
			StateLOVDTO stateLov = new StateLOVDTO();
			List<State> stateList = stateService.getStateForCountry(countryId);
			List<StatePageData> statePageDataList = new ArrayList<StatePageData>();
			for(State state:stateList)
			{
				StatePageData statePageData = new StatePageData();
				statePageData.setId(state.getNameId());
				statePageData.setName(state.getDescription());
				statePageDataList.add(statePageData);
			}
			stateLov.setStateLOV(statePageDataList);	
			stateLov.setCountryID(countryId);
			
			pageDTO.setData(stateLov);
		}
		else if(PageAction.cityLov.toString().equalsIgnoreCase(action))
		{
			String countryId="";
			String stateId="";
			
			try {
				SchoolDetailsRequestData	schoolDetailsRequestData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
						SchoolDetailsRequestData.class);
				countryId = schoolDetailsRequestData.getCountryID();
				stateId=schoolDetailsRequestData.getStateID();
				
			} catch (IOException e) {
				throw new BusinessException(ErrorCodes.SREVER_ERROR);
			}
			
			List<City> cityList = cityService.getCitiesForState(stateId );
			CityLOVDTO cityDto = new CityLOVDTO();
			List<String> cities = new ArrayList<String>();
			
			for(City city:cityList)
			{
				cities.add(city.getNameId());
			}
			cityDto.setCityLOV(cities);
			cityDto.setStateID(stateId);
			pageDTO.setData(cityDto);
		}
		else if(PageAction.schoolLov.toString().equalsIgnoreCase(action))
		{
			String countryId="";
			String stateId="";
			String cityId="";
			try {
				SchoolDetailsRequestData	schoolDetailsRequestData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
						SchoolDetailsRequestData.class);
				countryId = schoolDetailsRequestData.getCountryID();
				stateId=schoolDetailsRequestData.getStateID();
				cityId = schoolDetailsRequestData.getCityID();
				
			} catch (IOException e) {
				throw new BusinessException(ErrorCodes.SREVER_ERROR);
			}
			
			List<School> schools = schoolService.getSchools(cityId, stateId, countryId);
			SchoolLovDTO schoolLovData = new SchoolLovDTO();
			List<SchoolData> schoolDataList = new ArrayList<SchoolData>();
			
			for(School school:schools)
			{
				SchoolData schoolData = new SchoolData();
				schoolData.setId(school.getSchoolId());
				schoolData.setName(school.getName());
				schoolDataList.add(schoolData);
				
			}
			
			schoolLovData.setSchoolLOV(schoolDataList);
			schoolLovData.setCityID(cityId);
			schoolLovData.setCountryID(countryId);
			schoolLovData.setStateID(stateId);
			
			pageDTO.setData(schoolLovData);
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
