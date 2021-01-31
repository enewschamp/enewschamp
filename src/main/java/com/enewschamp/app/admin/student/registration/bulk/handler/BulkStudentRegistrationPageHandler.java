package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.student.school.nonlist.handler.StudentSchoolNilDTO;
import com.enewschamp.app.admin.student.school.nonlist.handler.StudentSchoolNotInTheListPageData;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPreferencesService;
import com.enewschamp.subscription.domain.service.StudentSchoolService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component("BulkStudentRegistrationPageHandler")
@Transactional
@Slf4j
public class BulkStudentRegistrationPageHandler implements IPageHandler {

	@Autowired
	private StudentControlService studentControlService;

	@Autowired
	private StudentRegistrationService studentRegistrationService;

	@Autowired
	private StudentSubscriptionService studentSubscriptionService;

	@Autowired
	private StudentPaymentService studentPaymentService;

	@Autowired
	private StudentDetailsService studentDetailsService;

	@Autowired
	private StudentSchoolService studentSchoolService;

	private StudentPreferencesService studentPreferencesService;

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
		case "Create":
			pageDto = insertBulkStudentRegistration(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

	@SneakyThrows
	@Transactional
	private PageDTO insertBulkStudentRegistration(PageRequestDTO pageRequest) {
		BulkStudentRegistrationPageData pageData = objectMapper.readValue(pageRequest.getData().toString(),
				BulkStudentRegistrationPageData.class);
		validateData(pageData);
		PageDTO dto = performInsertion(pageData, pageRequest);
		return dto;
	}

	public PageDTO performInsertion(BulkStudentRegistrationPageData pageData, PageRequestDTO pageRequest) {
		PageDTO dto = new PageDTO();
		try {
			StudentRegistration studentRegistration = createStudentRegistration(pageData, pageRequest);
			
			Long studentId = studentRegistration.getStudentId();
			
			StudentControl studentControl = createStudentControl(pageData, pageRequest, studentId);
			StudentSubscription studentSubscription = createStudentSubscription(pageData, pageRequest, studentId);
			StudentPayment studentPayment = createStudentPayment(pageData, pageRequest, studentId);
			StudentDetails studentDetails = createStudentDetails(pageData, pageRequest, studentId);
			StudentSchool studentSchool = createStudentSchool(pageData, pageRequest, studentId);
			StudentPreferences studentPreferences = createStudentPreferences(pageData, pageRequest, studentId);
			BulkStudentRegistrationPageData data = buildPageData(studentRegistration, studentControl,
					studentSubscription, studentPayment, studentDetails, studentSchool, studentPreferences, pageData);
			dto.setHeader(pageRequest.getHeader());
			dto.setData(data);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.STUDENT_SCHOOL_NOTLIST_UPDATE_ERROR);
		}
		return dto;
	}

	private BulkStudentRegistrationPageData buildPageData(StudentRegistration studentRegistration,
			StudentControl studentControl, StudentSubscription studentSubscription, StudentPayment studentPayment,
			StudentDetails studentDetails, StudentSchool studentSchool, StudentPreferences studentPreferences,
			BulkStudentRegistrationPageData pageData) {
		BulkStudentRegistrationPageData page = new BulkStudentRegistrationPageData();

		StudentRegistrationNilDTO studentRegistrationDto = modelMapper.map(studentRegistration,
				StudentRegistrationNilDTO.class);
		studentRegistrationDto.setLastUpdate(studentRegistration.getOperationDateTime());
		page.setStudentRegistration(studentRegistrationDto);

		StudentControlNilDTO studentControlDto = modelMapper.map(studentControl, StudentControlNilDTO.class);
		studentControlDto.setLastUpdate(studentControl.getOperationDateTime());
		page.setStudentControl(studentControlDto);

		StudentSubscriptionNilDTO studentSubscriptionDTO = modelMapper.map(studentSubscription,
				StudentSubscriptionNilDTO.class);
		studentSubscriptionDTO.setLastUpdate(studentSubscription.getOperationDateTime());
		page.setStudentSubscription(studentSubscriptionDTO);

		StudentPaymentNilDTO studentPaymentDTO = modelMapper.map(studentPayment, StudentPaymentNilDTO.class);
		studentPaymentDTO.setLastUpdate(studentPayment.getOperationDateTime());
		page.setStudentPayment(studentPaymentDTO);

		StudentDetailsNilDTO studentDetailsDTO = modelMapper.map(studentDetails, StudentDetailsNilDTO.class);
		studentDetailsDTO.setLastUpdate(studentDetails.getOperationDateTime());
		page.setStudentDetails(studentDetailsDTO);

		StudentSchoolNilDTO studentSchoolDto = modelMapper.map(studentSchool, StudentSchoolNilDTO.class);
		studentSchoolDto.setLastUpdate(studentSchool.getOperationDateTime());
		page.setStudentSchool(studentSchoolDto);

		StudentPreferencesNilDTO studentPreferencesDTO = modelMapper.map(studentDetails,
				StudentPreferencesNilDTO.class);
		studentPreferencesDTO.setLastUpdate(studentDetails.getOperationDateTime());
		page.setStudentPreferences(studentPreferencesDTO);
		return page;
	}

	private StudentRegistration createStudentRegistration(BulkStudentRegistrationPageData pageData,
			PageRequestDTO pageRequest) {
		StudentRegistration studentRegistration = modelMapper.map(pageData.getStudentRegistration(),
				StudentRegistration.class);
		studentRegistration.setOperatorId(pageRequest.getHeader().getUserId());
		studentRegistration.setRecordInUse(RecordInUseType.Y);
		studentRegistration = studentRegistrationService.create(studentRegistration);
		return studentRegistration;
	}

	private StudentControl createStudentControl(BulkStudentRegistrationPageData pageData, PageRequestDTO pageRequest,
			long studentId) {
		StudentControl studentControl = modelMapper.map(pageData.getStudentControl(), StudentControl.class);
		studentControl.setOperatorId(pageRequest.getHeader().getUserId());
		studentControl.setRecordInUse(RecordInUseType.Y);
		studentControl.setStudentId(studentId);
		studentControl = studentControlService.create(studentControl);
		return studentControl;
	}

	private StudentSubscription createStudentSubscription(BulkStudentRegistrationPageData pageData,
			PageRequestDTO pageRequest, long studentId) {
		StudentSubscription studentSubscription = modelMapper.map(pageData.getStudentSubscription(),
				StudentSubscription.class);
		studentSubscription.setOperatorId(pageRequest.getHeader().getUserId());
		studentSubscription.setRecordInUse(RecordInUseType.Y);
		studentSubscription.setStudentId(studentId);
		studentSubscription = studentSubscriptionService.create(studentSubscription);
		return studentSubscription;
	}

	private StudentPayment createStudentPayment(BulkStudentRegistrationPageData pageData, PageRequestDTO pageRequest,
			long studentId) {
		StudentPayment studentPayment = modelMapper.map(pageData.getStudentPayment(), StudentPayment.class);
		studentPayment.setOperatorId(pageRequest.getHeader().getUserId());
		studentPayment.setRecordInUse(RecordInUseType.Y);
		studentPayment.setStudentId(studentId);
		studentPayment = studentPaymentService.create(studentPayment);
		return studentPayment;
	}

	private StudentDetails createStudentDetails(BulkStudentRegistrationPageData pageData, PageRequestDTO pageRequest,
			long studentId) {
		StudentDetails studentDetails = modelMapper.map(pageData.getStudentDetails(), StudentDetails.class);
		studentDetails.setOperatorId(pageRequest.getHeader().getUserId());
		studentDetails.setRecordInUse(RecordInUseType.Y);
		studentDetails.setStudentId(studentId);
		studentDetails = studentDetailsService.create(studentDetails);
		return studentDetails;
	}

	private StudentSchool createStudentSchool(BulkStudentRegistrationPageData pageData, PageRequestDTO pageRequest,
			long studentId) {
		StudentSchool studentSchool = modelMapper.map(pageData.getStudentDetails(), StudentSchool.class);
		studentSchool.setOperatorId(pageRequest.getHeader().getUserId());
		studentSchool.setRecordInUse(RecordInUseType.Y);
		studentSchool.setStudentId(studentId);
		studentSchool = studentSchoolService.create(studentSchool);
		return studentSchool;
	}

	private StudentPreferences createStudentPreferences(BulkStudentRegistrationPageData pageData,
			PageRequestDTO pageRequest, long studentId) {
		StudentPreferences studentPreferences = modelMapper.map(pageData.getStudentDetails(), StudentPreferences.class);
		studentPreferences.setOperatorId(pageRequest.getHeader().getUserId());
		studentPreferences.setRecordInUse(RecordInUseType.Y);
		studentPreferences.setStudentId(studentId);
		studentPreferences = studentPreferencesService.create(studentPreferences);
		return studentPreferences;
	}

	private void validateData(BulkStudentRegistrationPageData pageData) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		Set<ConstraintViolation<BulkStudentRegistrationPageData>> violations = validator.validate(pageData);
		if (!violations.isEmpty()) {
			violations.forEach(e -> {
				log.error(e.getMessage());
			});
			throw new BusinessException(ErrorCodeConstants.INVALID_REQUEST, CommonConstants.DATA);
		}
	}

}