package com.enewschamp.subscription.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.common.PageSaveTable;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.fw.page.navigation.service.PageNavigationService;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.app.school.service.SchoolPricingService;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.SchoolData;
import com.enewschamp.subscription.app.dto.SchoolDetailsRequestData;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolPageData;
import com.enewschamp.subscription.app.dto.StudentSchoolWorkDTO;
import com.enewschamp.subscription.common.SubscriptionType;
import com.enewschamp.subscription.domain.business.SchoolDetailsBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.service.StudentSchoolWorkService;
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
	SchoolPricingService schoolPricingService;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	StudentSchoolWorkService studentSchoolWorkService;

	@Autowired
	SchoolService schoolService;

	@Autowired
	CountryService countryService;

	@Autowired
	StateService stateService;

	@Autowired
	CityService cityService;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	PageNavigationService pageNavigationService;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
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
		return pageDTO;
	}

	public PageDTO loadNextPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		StudentSchoolPageData studentSchoolPageData = new StudentSchoolPageData();
		studentSchoolPageData.setCountryLOV(countryService.getCountries());
		studentSchoolPageData.setCountry(propertiesService.getProperty(PropertyConstants.DEFAULT_COUNTRY));
		studentSchoolPageData.setStateLOV(stateService.getStatesForCountry(studentSchoolPageData.getCountry()));
		pageDTO.setData(studentSchoolPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadPreviousPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		studentId = studentControlBusiness.getStudentId(emailId);
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
		studentSchoolPageData.setCountryLOV(countryService.getCountries());
		pageDTO.setData(studentSchoolPageData);
		pageDTO.setData(studentSchoolPageData);

		// set the header as is...
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadWorkDataPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		studentId = studentControlBusiness.getStudentId(emailId);
		String subscriptionType = "";
		StudentSchoolPageData studentSchoolPageData = new StudentSchoolPageData();
		StudentSchoolWorkDTO studentSchoolWorkDTO = schoolDetailsBusiness.getStudentFromWork(studentId);
		if (studentSchoolWorkDTO != null) {
			studentSchoolPageData = modelMapper.map(studentSchoolWorkDTO, StudentSchoolPageData.class);

		} else {
			StudentSchoolDTO studentSchoolDTO = schoolDetailsBusiness.getStudentFromMaster(studentId);
			if (studentSchoolDTO != null) {
				studentSchoolPageData = modelMapper.map(studentSchoolDTO, StudentSchoolPageData.class);
			}
		}
		StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
		if (studentControlWorkDTO != null) {
			subscriptionType = studentControlWorkDTO.getSubscriptionTypeW();
		} else {
			StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
			if (studentControlDTO != null) {
				subscriptionType = studentControlDTO.getSubscriptionType();
			}
		}

		if ("S".equals(subscriptionType)) {
			studentSchoolPageData.setSchoolProgramLOV(schoolService.getSchoolProgramDetails());
		} else {
			studentSchoolPageData.setCountryLOV(countryService.getCountries());
			if (studentSchoolPageData.getCountry() == null || "".equals(studentSchoolPageData.getCountry())) {
				studentSchoolPageData.setCountry(propertiesService.getProperty(PropertyConstants.DEFAULT_COUNTRY));
			}
			studentSchoolPageData.setStateLOV(stateService.getStatesForCountry(studentSchoolPageData.getCountry()));
			studentSchoolPageData.setCityLOV(cityService.getCitiesForStateCountry(studentSchoolPageData.getState(),
					studentSchoolPageData.getCountry()));
			studentSchoolPageData
					.setSchoolLOV(schoolService.getSchoolsForCityStateCountry(studentSchoolPageData.getCity(),
							studentSchoolPageData.getState(), studentSchoolPageData.getCountry()));
		}
		pageDTO.setData(studentSchoolPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadExistingSchoolDetailsPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		studentId = studentControlBusiness.getStudentId(emailId);
		StudentSchoolDTO studentSchoolDTO = schoolDetailsBusiness.getStudentFromMaster(studentId);
		StudentSchoolPageData studentSchoolPageData = new StudentSchoolPageData();
		if (studentSchoolDTO != null) {
			studentSchoolPageData = modelMapper.map(studentSchoolDTO, StudentSchoolPageData.class);
		} else {
			studentSchoolDTO = new StudentSchoolDTO();
		}
		studentSchoolPageData.setCountryLOV(countryService.getCountries());
		studentSchoolPageData.setStateLOV(stateService.getStatesForCountry(studentSchoolDTO.getCountry()));
		studentSchoolPageData.setCityLOV(
				cityService.getCitiesForStateCountry(studentSchoolDTO.getState(), studentSchoolDTO.getCountry()));
		studentSchoolPageData.setSchoolLOV(schoolService.getSchoolsForCityStateCountry(studentSchoolDTO.getCity(),
				studentSchoolDTO.getState(), studentSchoolDTO.getCountry()));
		pageDTO.setData(studentSchoolPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadStateLovPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String country = "";
		try {
			SchoolDetailsRequestData schoolDetailsRequestData = objectMapper.readValue(
					pageNavigationContext.getPageRequest().getData().toString(), SchoolDetailsRequestData.class);
			country = schoolDetailsRequestData.getCountry();
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		StudentSchoolPageData schoolPageData = new StudentSchoolPageData();
		schoolPageData.setStateLOV(stateService.getStatesForCountry(country));
		schoolPageData.setCountry(country);
		pageDTO.setData(schoolPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadCityLovPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String country = "";
		String state = "";
		try {
			SchoolDetailsRequestData schoolDetailsRequestData = objectMapper.readValue(
					pageNavigationContext.getPageRequest().getData().toString(), SchoolDetailsRequestData.class);
			country = schoolDetailsRequestData.getCountry();
			state = schoolDetailsRequestData.getState();
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		StudentSchoolPageData schoolPageData = new StudentSchoolPageData();
		schoolPageData.setCityLOV(cityService.getCitiesForStateCountry(state, country));
		schoolPageData.setState(state);
		schoolPageData.setCountry(country);
		pageDTO.setData(schoolPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadSchoolLovPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String country = "";
		String state = "";
		String city = "";
		try {
			SchoolDetailsRequestData schoolDetailsRequestData = objectMapper.readValue(
					pageNavigationContext.getPageRequest().getData().toString(), SchoolDetailsRequestData.class);
			country = schoolDetailsRequestData.getCountry();
			state = schoolDetailsRequestData.getState();
			city = schoolDetailsRequestData.getCity();
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		StudentSchoolPageData schoolPageData = new StudentSchoolPageData();
		schoolPageData.setSchoolLOV(schoolService.getSchoolsForCityStateCountry(city, state, country));
		schoolPageData.setCity(city);
		schoolPageData.setCountry(country);
		schoolPageData.setState(state);
		pageDTO.setData(schoolPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadSchoolProgramCodePage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDTO = new PageDTO();
		String schoolProgramCode = "";
		try {
			SchoolDetailsRequestData schoolDetailsRequestData = objectMapper.readValue(
					pageNavigationContext.getPageRequest().getData().toString(), SchoolDetailsRequestData.class);
			schoolProgramCode = schoolDetailsRequestData.getSchoolProgramCode();
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);
		}
		StudentSchoolPageData schoolPageData = new StudentSchoolPageData();
		List<School> school = schoolService.getSchoolFromProgramCode(schoolProgramCode);
		List<SchoolData> schoolLOV = new ArrayList<SchoolData>();
		if (school != null && school.size() > 0) {
			School schoolDetails = school.get(0);
			SchoolData schoolData = new SchoolData();
			schoolData.setId(schoolDetails.getSchoolId());
			schoolData.setName(schoolDetails.getName());
			schoolData.setSchoolProgramCode(schoolDetails.getSchoolProgramCode());
			schoolLOV.add(schoolData);
			schoolPageData.setCity(schoolDetails.getCityId());
			schoolPageData.setCountry(schoolDetails.getCountryId());
			schoolPageData.setState(schoolDetails.getStateId());
		}
		schoolPageData.setSchoolLOV(schoolLOV);
		schoolPageData.setCountryLOV(countryService.getCountries());
		schoolPageData.setStateLOV(stateService.getStatesForCountry(schoolPageData.getCountry()));
		schoolPageData.setCityLOV(
				cityService.getCitiesForStateCountry(schoolPageData.getState(), schoolPageData.getCountry()));
		pageDTO.setData(schoolPageData);
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;

	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		schoolDetailsBusiness.workToMaster(studentId);
		studentSchoolWorkService.delete(studentId);
		pageDto.setHeader(pageRequest.getHeader());
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
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
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
		return pageDTO;
	}

	public PageDTO handleSchoolDetailsAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		String operation = pageRequest.getHeader().getOperation();
		String editionId = pageRequest.getHeader().getEditionId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		if (PageSaveTable.M.toString().equals(saveIn)) {
			StudentSchoolPageData studentSchoolPageData = null;
			studentSchoolPageData = mapPagedata(pageRequest);
			String emailId = pageRequest.getHeader().getEmailId();
			Long studentId = studentControlBusiness.getStudentId(emailId);
			StudentSchoolDTO studSchool = modelMapper.map(studentSchoolPageData, StudentSchoolDTO.class);
			studSchool.setStudentId(studentId);
			schoolDetailsBusiness.saveAsMaster(studSchool);
			StudentControlDTO studentControlDTO = studentControlBusiness.getStudentFromMaster(emailId);
			studentControlDTO.setSchoolDetails("Y");
			studentControlBusiness.saveAsMaster(studentControlDTO);
		} else if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentSchoolPageData studentSchoolPageData = null;
			studentSchoolPageData = mapPagedata(pageRequest);
			String emailId = pageRequest.getHeader().getEmailId();
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			Long studentId = studentControlBusiness.getStudentId(emailId);
			StudentSchoolWorkDTO studenSchool = modelMapper.map(studentSchoolPageData, StudentSchoolWorkDTO.class);
			studenSchool.setStudentId(studentId);
			schoolDetailsBusiness.saveAsWork(studenSchool);
			studentControlWorkDTO.setSchoolDetailsW("Y");
			studentControlWorkDTO.setNextPageOperation(operation);
			studentControlBusiness.saveAsWork(studentControlWorkDTO);
			Long schoolId = 0L;
			if (SubscriptionType.S.toString().equalsIgnoreCase(studentControlWorkDTO.getSubscriptionTypeW())) {
				StudentSchoolWorkDTO studentSchoolWorkDTO = schoolDetailsBusiness.getStudentFromWork(studentId);
				if (studentSchoolWorkDTO != null) {
					if ("N".equalsIgnoreCase(studentSchoolWorkDTO.getSchoolNotInTheList())) {
						schoolId = Long.valueOf(studentSchoolWorkDTO.getSchool());
					}
				}
				SchoolPricing schoolPricing = schoolPricingService.getPricingForInstitution(schoolId, editionId);
			}
		}
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

	public PageDTO handlePreviousWorkDataPage(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		Long studentId = 0L;
		String emailId = pageRequest.getHeader().getEmailId();
		String saveIn = pageNavigatorDTO.getUpdationTable();
		StudentSchoolPageData studentSchoolPageData = mapPagedata(pageRequest);
		if (PageSaveTable.W.toString().equals(saveIn)) {
			StudentControlWorkDTO studentControlWorkDTO = studentControlBusiness.getStudentFromWork(emailId);
			studentId = studentControlWorkDTO.getStudentId();
			StudentSchoolWorkDTO studentSchoolWorkDTO = schoolDetailsBusiness.getStudentFromWork(studentId);
			if (studentSchoolWorkDTO == null) {
				studentSchoolWorkDTO = new StudentSchoolWorkDTO();
			}
			studentSchoolWorkDTO = modelMapper.map(studentSchoolPageData, StudentSchoolWorkDTO.class);
			studentSchoolWorkDTO.setStudentId(studentId);
			schoolDetailsBusiness.saveAsWork(studentSchoolWorkDTO);
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public StudentSchoolPageData mapPagedata(PageRequestDTO pageRequest) {
		StudentSchoolPageData studentSchoolPageData = null;
		try {
			studentSchoolPageData = objectMapper.readValue(pageRequest.getData().toString(),
					StudentSchoolPageData.class);
		} catch (IOException e) {
			throw new BusinessException(ErrorCodeConstants.SREVER_ERROR);

		}
		return studentSchoolPageData;
	}
}
