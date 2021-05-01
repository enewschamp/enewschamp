package com.enewschamp.app.admin.student.registration.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PageStatus;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("StudentRegistrationPageHandler")
@Slf4j
public class StudentRegistrationPageHandler implements IPageHandler {
	@Autowired
	private StudentRegistrationService studentRegistrationService;

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private CommonService commonService;
	private Validator validator;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		PageDTO pageDto = null;
		switch (pageRequest.getHeader().getAction()) {
		case "Create":
			pageDto = createStudentRegistration(pageRequest);
			break;
		case "Update":
			pageDto = updateStudentRegistration(pageRequest);
			break;
		case "Read":
			pageDto = readStudentRegistration(pageRequest);
			break;
		case "Close":
			pageDto = closeStudentRegistration(pageRequest);
			break;
		case "Reinstate":
			pageDto = reinstateStudentRegistration(pageRequest);
			break;
		case "List":
			pageDto = listStudentRegistration(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

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

	@SneakyThrows
	private PageDTO createStudentRegistration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRegistrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRegistrationPageData.class);
		validate(pageData);
		if(!pageData.getPassword().equals(pageData.getConfirmPassword())) {
			throw new BusinessException(ErrorCodeConstants.PASSWORD_MISMATCH);
		}
	    StudentRegistration studentRegistration = saveImage(pageData);
		mapStudentRegistration(pageRequest, pageDto, studentRegistration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO updateStudentRegistration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRegistrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRegistrationPageData.class);
		validate(pageData);
		StudentRegistration studentRegistration = updateImage(pageData, pageData.getStudentId());
		mapStudentRegistration(pageRequest, pageDto, studentRegistration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO readStudentRegistration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRegistrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRegistrationPageData.class);
		StudentRegistration studentRegistration = modelMapper.map(pageData, StudentRegistration.class);
		studentRegistration = studentRegistrationService.read(studentRegistration);
		mapStudentRegistration(pageRequest, pageDto, studentRegistration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO closeStudentRegistration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRegistrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRegistrationPageData.class);
		StudentRegistration studentRegistration = modelMapper.map(pageData, StudentRegistration.class);
		studentRegistration = studentRegistrationService.close(studentRegistration);
		mapStudentRegistration(pageRequest, pageDto, studentRegistration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO reinstateStudentRegistration(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();
		StudentRegistrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				StudentRegistrationPageData.class);
		StudentRegistration studentRegistration = modelMapper.map(pageData, StudentRegistration.class);
		studentRegistration = studentRegistrationService.reInstate(studentRegistration);
		mapStudentRegistration(pageRequest, pageDto, studentRegistration);
		return pageDto;
	}

	@SneakyThrows
	private PageDTO listStudentRegistration(PageRequestDTO pageRequest) {
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		Page<StudentRegistration> studentRegistrationList = studentRegistrationService.list(searchRequest,
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt(),
				pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt());

		List<StudentRegistrationPageData> list = mapStudentRegistrationData(studentRegistrationList);
		List<PageData> variable = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		PageDTO dto = new PageDTO();
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		mapHeaderData(pageRequest, dto);
		if ((studentRegistrationList.getNumber() + 1) == studentRegistrationList.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(variable);
		return dto;
	}

	private StudentRegistrationPageData mapPageData(StudentRegistration studentRegistration) {
		StudentRegistrationPageData pageData = modelMapper.map(studentRegistration, StudentRegistrationPageData.class);
		pageData.setLastUpdate(studentRegistration.getOperationDateTime());
		return pageData;
	}

	private void mapStudentRegistration(PageRequestDTO pageRequest, PageDTO pageDto,
			StudentRegistration studentRegistration) {
		StudentRegistrationPageData pageData;
		mapHeaderData(pageRequest, pageDto);
		pageData = mapPageData(studentRegistration);
		pageDto.setData(pageData);
	}

	public List<StudentRegistrationPageData> mapStudentRegistrationData(Page<StudentRegistration> page) {
		List<StudentRegistrationPageData> studentRegistrationPageDataList = new ArrayList<StudentRegistrationPageData>();
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			List<StudentRegistration> pageDataList = page.getContent();
			for (StudentRegistration studentRegistration : pageDataList) {
				StudentRegistrationPageData studentRegistrationPageData = modelMapper.map(studentRegistration,
						StudentRegistrationPageData.class);
				studentRegistrationPageData.setLastUpdate(studentRegistration.getOperationDateTime());
				studentRegistrationPageDataList.add(studentRegistrationPageData);
			}
		}
		return studentRegistrationPageDataList;
	}

	private void validate(StudentRegistrationPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<StudentRegistrationPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error("Validation failed: " + e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST);
		}
	}
	
	private StudentRegistration saveImage(StudentRegistrationPageData studentRegistrationDto) {
		StudentRegistration studentRegistration = modelMapper.map(studentRegistrationDto, StudentRegistration.class);
		try {
			studentRegistration.setRecordInUse(RecordInUseType.Y);
			studentRegistration.setForcePasswordChange("Y");
			studentRegistration = studentRegistrationService.create(studentRegistration);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(studentRegistrationDto.getImageUpdate())) {
			String newImageName = studentRegistration.getStudentId() + "_IMG_" + System.currentTimeMillis();
			String imageType = studentRegistrationDto.getImageTypeExt();
			String currentImageName = studentRegistration.getImageName();
			boolean saveImageFlag = commonService.saveImages("Admin", "student", imageType, studentRegistrationDto.getImageBase64(),
					newImageName);
			if (saveImageFlag) {
				studentRegistration.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				studentRegistration.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "student", currentImageName);
				updateFlag = true;
			}
		}

		if (updateFlag) {
			studentRegistration = studentRegistrationService.updateOne(studentRegistration);
		}
		return studentRegistration;
	}

	private StudentRegistration updateImage(StudentRegistrationPageData studentRegistrationDto, Long studentId) {
		studentRegistrationDto.setStudentId(studentId);
		StudentRegistration studentRegistration = studentRegistrationService.get(studentId);
		if (studentRegistration.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		String currentImageName = studentRegistrationDto.getImageName();
		studentRegistrationDto.setImageName(currentImageName);
		LocalDateTime lastSuccessfulLoginAttempt = studentRegistrationDto.getLastSuccessfulLoginAttempt();
		LocalDateTime lastUnSuccessfulLoginAttempt = studentRegistrationDto.getLastUnsuccessfulLoginAttempt();
		String isActive = studentRegistrationDto.getIsActive();
		String isAccountLocked = studentRegistrationDto.getIsAccountLocked();
		String forcePasswordChange = studentRegistrationDto.getForcePasswordChange();
		studentRegistration = modelMapper.map(studentRegistrationDto, StudentRegistration.class);
		
		studentRegistration.setRecordInUse(RecordInUseType.Y);
		studentRegistration.setLastSuccessfulLoginAttempt(lastSuccessfulLoginAttempt);
		studentRegistration.setLastUnsuccessfulLoginAttempt(lastUnSuccessfulLoginAttempt);
		studentRegistration.setIsActive(isActive);
		studentRegistration.setIsAccountLocked(isAccountLocked);
		studentRegistration.setForcePasswordChange(forcePasswordChange);
		
		studentRegistration = studentRegistrationService.updateOne(studentRegistration);
		boolean updateFlag = false;
		if ("Y".equalsIgnoreCase(studentRegistrationDto.getImageUpdate())) {
			String newImageName = studentRegistration.getStudentId() + "_IMG_" + System.currentTimeMillis();
			String imageType = studentRegistrationDto.getImageTypeExt();
			boolean saveImageFlag = commonService.saveImages("Admin", "student", imageType, studentRegistrationDto.getImageBase64(),
					newImageName);
			if (saveImageFlag) {
				studentRegistration.setImageName(newImageName + "." + imageType);
				updateFlag = true;
			} else {
				studentRegistration.setImageName(null);
				updateFlag = true;
			}
			if (currentImageName != null && !"".equals(currentImageName)) {
				commonService.deleteImages("Admin", "student", currentImageName);
				updateFlag = true;
			}
		}

		if (updateFlag) {
			studentRegistration = studentRegistrationService.updateOne(studentRegistration);
		}
		return studentRegistration;
	}

}