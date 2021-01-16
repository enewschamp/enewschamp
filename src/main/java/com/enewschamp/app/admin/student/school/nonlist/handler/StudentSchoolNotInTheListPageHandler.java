package com.enewschamp.app.admin.student.school.nonlist.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.common.RequestStatusType;
import com.enewschamp.app.common.city.dto.CityDTO;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.country.entity.Country;
import com.enewschamp.app.common.country.service.CountryService;
import com.enewschamp.app.common.state.dto.StateDTO;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.school.dto.SchoolDTO;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.app.dto.CountryPageData;
import com.enewschamp.subscription.app.dto.StatePageData;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.service.StudentSchoolService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentSchoolNotInTheListPageHandler")
@Slf4j
public class StudentSchoolNotInTheListPageHandler implements IPageHandler {

	@Autowired
	private StudentSchoolService studentSchoolService;

	@Autowired
	private CountryService countryService;
	@Autowired
	private StateService stateService;
	@Autowired
	private CityService cityService;
	@Autowired
	private SchoolService schoolService;
	

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;
	private Validator validator;

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Update":
			pageDto = updateStudentSchoolNotInTheList(pageRequest);
			break;
		case "List":
			pageDto = listStudentSchoolNotInTheList(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

	private StudentSchool mapStudentSchoolData(PageRequestDTO pageRequest, StudentSchoolNotInTheListPageData pageData) {
		StudentSchool studentSchool = modelMapper.map(pageData, StudentSchool.class);
		studentSchool.setRecordInUse(RecordInUseType.Y);
		return studentSchool;
	}

	public List<StudentSchoolNotInTheListPageData> mapStudentSchoolData(Page<StudentSchool> page) {
		List<StudentSchoolNotInTheListPageData> StudentSchoolNotInTheListPageDataList = new ArrayList<StudentSchoolNotInTheListPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentSchool> pageDataList = page.getContent();
			for (StudentSchool studentSchool : pageDataList) {
				StudentSchoolNotInTheListPageData pageData = modelMapper.map(studentSchool,
						StudentSchoolNotInTheListPageData.class);
				pageData.setLastUpdate(studentSchool.getOperationDateTime());
				StudentSchoolNotInTheListPageDataList.add(pageData);
			}
		}
		return StudentSchoolNotInTheListPageDataList;
	}

	@SneakyThrows
	private PageDTO listStudentSchoolNotInTheList(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);

		Page<StudentSchool> studentSchoolList = studentSchoolService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentSchoolNotInTheListPageData> list = mapStudentSchoolData(studentSchoolList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((studentSchoolList.getNumber() + 1) == studentSchoolList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	@SneakyThrows
	@Transactional
	private PageDTO updateStudentSchoolNotInTheList(PageRequestDTO pageRequest) {
		StudentSchoolNotInTheListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolNotInTheListPageData.class);
		validateData(pageData);
		PageDTO dto = performUpdation(pageData, pageRequest);
		return dto;
	}
	

	private PageDTO performUpdation(StudentSchoolNotInTheListPageData pageData, PageRequestDTO pageRequest) {
		PageDTO dto = new PageDTO();
		State state = createState(pageData);
		City city = createCity(pageData);
		School school = createSchool(pageData);
		StudentSchool studentSchool = updateStudentSchool(city.getCityId(), state.getStateId(), school.getSchoolId(), pageData);
		StudentSchoolNotInTheListPageData data = buildPageData(state, city, school, studentSchool, pageData);
        dto.setHeader(pageRequest.getHeader());
        dto.setData(data);
		return dto;
	}
	
	private StudentSchoolNotInTheListPageData buildPageData(State state, City city, School school,
			StudentSchool studentSchool, StudentSchoolNotInTheListPageData pageData) {
		StudentSchoolNotInTheListPageData page = new StudentSchoolNotInTheListPageData();
		page.setState(modelMapper.map(state, StateDTO.class));
		page.setCity(modelMapper.map(city, CityDTO.class));
		page.setSchool(modelMapper.map(school, SchoolDTO.class));
		page.setStudentSchool(modelMapper.map(studentSchool, StudentSchoolDTO.class));
		return page;
	}

	private State createState(StudentSchoolNotInTheListPageData pageData) {
        State state = stateService.getByNameAndCountryId(pageData.getState().getNameId(), pageData.getState().getCountryId());
        if(state == null) {
			state = modelMapper.map(pageData.getState(), State.class);
			state = stateService.create(state);
        }
        return state;
	}

	private City createCity(StudentSchoolNotInTheListPageData pageData) {
        City city = cityService.getByNameAndCountryId(pageData.getCity().getNameId(), pageData.getCity().getStateId(), pageData.getCity().getCountryId());
        if(city == null) {
			city = modelMapper.map(pageData.getCity(), City.class);
			city = cityService.create(city);
        }	
        return city;
	}
	
	private School createSchool(StudentSchoolNotInTheListPageData pageData) {
        School school = schoolService.getByNameAndCityAndStateAndCountryId(pageData.getSchool().getName(), pageData.getSchool().getCityId(), pageData.getSchool().getStateId(), pageData.getSchool().getCountryId());
        if(school == null) {
        	school = modelMapper.map(pageData.getSchool(), School.class);
        	school = schoolService.create(school);
        }	
        return school;
	}
	
	private StudentSchool updateStudentSchool(Long cityId, Long stateId, Long schoolId, StudentSchoolNotInTheListPageData pageData) {
		StudentSchool existingStuSchool = studentSchoolService.get(pageData.getStudentSchool().getStudentId());
		if(existingStuSchool != null) {
			StudentSchool studentSchool = modelMapper.map(pageData.getStudentSchool(), StudentSchool.class);
			studentSchool.setCity(String.valueOf(cityId));
			studentSchool.setCityNotInTheList("N");
			studentSchool.setState(String.valueOf(stateId));
			studentSchool.setStateNotInTheList("N");
			studentSchool.setSchool(String.valueOf(schoolId));
			studentSchool.setSchoolNotInTheList("N");
			existingStuSchool = studentSchoolService.update(studentSchool);
		}
		return existingStuSchool;
	}
	
	private State createCity(StudentSchoolNotInTheListPageData pageData, String stateId, String cityId) {
		City city = null;
		StateDTO stateDto = pageData.getState();
		String countryId = pageData.getState().getCountryId();
		CountryPageData country = countryService.getCountry(countryId);
		if(country != null) {
			State stateEntity = modelMapper.map(stateDto, State.class);
			//state = stateService.create(stateEntity);
		}
		return null;
	}
	
	private void mapStudentSchool(PageRequestDTO pageRequest, PageDTO pageDto, StudentSchool studentSchool) {
		StudentSchoolNotInTheListPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentSchool);
		pageDto.setData(pageData);
	}

	private void mapHeaderData(PageRequestDTO pageRequest, PageDTO pageDto) {
		pageDto.setHeader(pageRequest.getHeader());
		pageDto.getHeader().setRequestStatus(RequestStatusType.S);
		pageDto.getHeader().setTodaysDate(LocalDate.now());
		pageDto.getHeader().setLoginCredentials(null);
		pageDto.getHeader().setUserId(null);
		pageDto.getHeader().setDeviceId(null);
	}

	private StudentSchoolNotInTheListPageData mapPageData(StudentSchool studentSchool) {
		StudentSchoolNotInTheListPageData pageData = modelMapper.map(studentSchool,
				StudentSchoolNotInTheListPageData.class);
		pageData.setLastUpdate(studentSchool.getOperationDateTime());
		return pageData;
	}

	private void validateData(StudentSchoolNotInTheListPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentSchoolNotInTheListPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}

}