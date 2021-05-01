package com.enewschamp.app.admin.student.school.nonlist.handler;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.student.school.handler.StudentSchoolPageHandler;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.city.service.CityService;
import com.enewschamp.app.common.state.entity.State;
import com.enewschamp.app.common.state.service.StateService;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.service.SchoolService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.service.StudentSchoolService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("NotInTheListPageHandler")
@Transactional
@Slf4j
public class NotInTheListPageHandler implements IPageHandler {

	@Autowired
	private StudentSchoolService studentSchoolService;

	@Autowired
	private StateService stateService;
	@Autowired
	private CityService cityService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private StudentSchoolPageHandler studentSchoolPageHandler;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ObjectMapper objectMapper;

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
			pageDto = studentSchoolPageHandler.listStudentSchool(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

	@SneakyThrows
	@Transactional
	private PageDTO updateStudentSchoolNotInTheList(PageRequestDTO pageRequest) {
		StudentSchoolNotInTheListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentSchoolNotInTheListPageData.class);
		validate(pageData, this.getClass().getName());
		PageDTO dto = performUpdation(pageData, pageRequest);
		return dto;
	}

	public PageDTO performUpdation(StudentSchoolNotInTheListPageData pageData, PageRequestDTO pageRequest) {
		PageDTO dto = new PageDTO();
		try {
			State state = createState(pageData, pageRequest);
			City city = createCity(pageData, pageRequest);
			School school = createSchool(pageData, pageRequest);
			StudentSchool studentSchool = updateStudentSchool(city.getNameId(), state.getNameId(),
					school.getSchoolId(), pageData);
			StudentSchoolNotInTheListPageData data = buildPageData(state, city, school, studentSchool, pageData);
			dto.setHeader(pageRequest.getHeader());
			dto.setData(data);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.STUDENT_SCHOOL_NOTLIST_UPDATE_ERROR);
		}
		return dto;
	}

	private StudentSchoolNotInTheListPageData buildPageData(State state, City city, School school,
			StudentSchool studentSchool, StudentSchoolNotInTheListPageData pageData) {
		StudentSchoolNotInTheListPageData page = new StudentSchoolNotInTheListPageData();

		StateNilDTO stateDto = modelMapper.map(state, StateNilDTO.class);
		stateDto.setLastUpdate(state.getOperationDateTime());
		page.setState(stateDto);

		CityNilDTO cityDto = modelMapper.map(city, CityNilDTO.class);
		cityDto.setLastUpdate(city.getOperationDateTime());
		page.setCity(cityDto);

		SchoolNilDTO schoolDto = modelMapper.map(school, SchoolNilDTO.class);
		schoolDto.setLastUpdate(school.getOperationDateTime());
		page.setSchool(schoolDto);

		StudentSchoolNilDTO studentSchoolDto = modelMapper.map(studentSchool, StudentSchoolNilDTO.class);
		studentSchoolDto.setLastUpdate(studentSchool.getOperationDateTime());
		page.setStudentSchool(studentSchoolDto);

		return page;
	}

	private State createState(StudentSchoolNotInTheListPageData pageData, PageRequestDTO pageRequest) {
		State state = stateService.getByNameAndCountryId(pageData.getState().getNameId(),
				pageData.getState().getCountryId());
		if (state == null) {
			state = modelMapper.map(pageData.getState(), State.class);
			state.setOperatorId(pageRequest.getHeader().getUserId());
			state.setRecordInUse(RecordInUseType.Y);
			state = stateService.create(state);
		}
		return state;
	}

	private City createCity(StudentSchoolNotInTheListPageData pageData, PageRequestDTO pageRequest) {
		City city = cityService.getByNameAndCountryId(pageData.getCity().getNameId(), pageData.getCity().getStateId(),
				pageData.getCity().getCountryId());
		if (city == null) {
			city = modelMapper.map(pageData.getCity(), City.class);
			city.setOperatorId(pageRequest.getHeader().getUserId());
			city.setRecordInUse(RecordInUseType.Y);
			city = cityService.create(city);
		}
		return city;
	}

	private School createSchool(StudentSchoolNotInTheListPageData pageData, PageRequestDTO pageRequest) {
		School school = schoolService.getByNameAndCityAndStateAndCountryId(pageData.getSchool().getName(),
				pageData.getSchool().getCityId(), pageData.getSchool().getStateId(),
				pageData.getSchool().getCountryId());
		if (school == null) {
			school = modelMapper.map(pageData.getSchool(), School.class);
			school.setOperatorId(pageRequest.getHeader().getUserId());
			school.setRecordInUse(RecordInUseType.Y);
			school = schoolService.create(school);
		}
		return school;
	}

	private StudentSchool updateStudentSchool(String cityName, String stateName, Long schoolId,
			StudentSchoolNotInTheListPageData pageData) {
		StudentSchool existingStuSchool = studentSchoolService.get(pageData.getStudentSchool().getStudentId());
		if (existingStuSchool != null) {
			StudentSchool studentSchool = modelMapper.map(pageData.getStudentSchool(), StudentSchool.class);
			studentSchool.setCity(String.valueOf(cityName));
			studentSchool.setCityNotInTheList("N");
			studentSchool.setState(stateName);
			studentSchool.setStateNotInTheList("N");
			studentSchool.setSchool(String.valueOf(schoolId));
			studentSchool.setSchoolNotInTheList("N");
			studentSchool.setRecordInUse(RecordInUseType.Y);
			studentSchool.setOperatorId(pageData.getOperatorId());
			studentSchool.setOperationDateTime(LocalDateTime.now());
			studentSchool.setApprovalRequired("N");
			Object existingStuSchool1 = studentSchoolService.update(studentSchool);
			System.out.println(existingStuSchool1);
		}
		return existingStuSchool;
	}

}
