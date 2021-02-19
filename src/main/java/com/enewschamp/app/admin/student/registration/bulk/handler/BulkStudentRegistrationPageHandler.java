package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.handler.ListPageData;
import com.enewschamp.app.admin.student.school.nonlist.handler.StudentSchoolNilDTO;
import com.enewschamp.app.common.CommonConstants;
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
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPreferenceComm;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.enewschamp.subscription.domain.service.StudentPaymentService;
import com.enewschamp.subscription.domain.service.StudentPreferencesService;
import com.enewschamp.subscription.domain.service.StudentSchoolService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.fasterxml.jackson.core.type.TypeReference;
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

	@Autowired
	private StudentPreferencesService studentPreferencesService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private BulkStudentRegistrationCustomImpl bulkStudentRepository;

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
		case "List":
			pageDto = findAll(pageRequest);
			break;
		default:
			break;
		}
		return pageDto;
	}

	@SneakyThrows
	@Transactional
	private PageDTO insertBulkStudentRegistration(PageRequestDTO pageRequest) {
		List<BulkStudentRegistrationPageData> pageData = objectMapper.readValue(pageRequest.getData().toString(),
				new TypeReference<List<BulkStudentRegistrationPageData>>(){});
		pageData.forEach(pagedata -> { validateData(pagedata); });
		PageDTO dto = performInsertion(pageData, pageRequest);
		return dto;
	}

	@SneakyThrows
	private PageDTO findAll(PageRequestDTO pageRequest) {
		PageDTO dto = new PageDTO();
		AdminSearchRequest searchRequest = objectMapper
				.readValue(pageRequest.getData().get(CommonConstants.FILTER).toString(), AdminSearchRequest.class);
		int pageNo = pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_NO).asInt();
		int pageSize = pageRequest.getData().get(CommonConstants.PAGINATION).get(CommonConstants.PAGE_SIZE).asInt();
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<BulkStudentRegistrationPageData> list = bulkStudentRepository.findAll(pageable, searchRequest);
		List<PageData> records = list.stream().map(e -> (PageData) e).collect(Collectors.toList());
		ListPageData pageData = objectMapper.readValue(pageRequest.getData().toString(), ListPageData.class);
		pageData.getPagination().setIsLastPage(PageStatus.N);
		dto.setHeader(pageRequest.getHeader());
		if ((list.getNumber() + 1) == list.getTotalPages()) {
			pageData.getPagination().setIsLastPage(PageStatus.Y);
		}
		dto.setData(pageData);
		dto.setRecords(records);
		return dto;

	}

	public PageDTO performInsertion(List<BulkStudentRegistrationPageData> pageData, PageRequestDTO pageRequest) {
		PageDTO dto = new PageDTO();
		List<BulkStudentRegistrationPageData> records = new ArrayList<>();
		for (BulkStudentRegistrationPageData page : pageData) {
			try {
				StudentRegistration studentRegistration = createStudentRegistration(page, pageRequest);
				Long studentId = studentRegistration.getStudentId();
				StudentControl studentControl = createStudentControl(page, pageRequest, studentId);
				StudentSubscription studentSubscription = createStudentSubscription(page, pageRequest, studentId);
				StudentPayment studentPayment = createStudentPayment(page, pageRequest, studentId);
				StudentDetails studentDetails = createStudentDetails(page, pageRequest, studentId);
				StudentSchool studentSchool = createStudentSchool(page, pageRequest, studentId);
				StudentPreferences studentPreferences = createStudentPreferences(page, pageRequest, studentId);
				BulkStudentRegistrationPageData data = buildPageData(studentRegistration, studentControl,
						studentSubscription, studentPayment, studentDetails, studentSchool, studentPreferences, page);
				records.add(data);

			} catch (Exception e) {
				log.error(e.getMessage());
				throw new BusinessException(ErrorCodeConstants.STUDENT_SCHOOL_NOTLIST_UPDATE_ERROR);
			}
		}
		List<PageData> recordList = records.stream().map(e -> (PageData) e).collect(Collectors.toList());
		dto.setHeader(pageRequest.getHeader());
		dto.setRecords(recordList);
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
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
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
		StudentSchool studentSchool = modelMapper.map(pageData.getStudentSchool(), StudentSchool.class);
		studentSchool.setOperatorId(pageRequest.getHeader().getUserId());
		studentSchool.setRecordInUse(RecordInUseType.Y);
		studentSchool.setStudentId(studentId);
		studentSchool = studentSchoolService.create(studentSchool);
		return studentSchool;
	}

	private StudentPreferences createStudentPreferences(BulkStudentRegistrationPageData pageData,
			PageRequestDTO pageRequest, long studentId) {
		StudentPreferences studentPreferences = modelMapper.map(pageData.getStudentPreferences(), StudentPreferences.class);
		studentPreferences.setOperatorId(pageRequest.getHeader().getUserId());
		studentPreferences.setRecordInUse(RecordInUseType.Y);
		studentPreferences.setStudentId(studentId);
		StudentPreferenceComm commsOverEmail = new StudentPreferenceComm();
		commsOverEmail.setAlertsNotifications(pageData.getStudentPreferences().getAlertsNotifications());
		commsOverEmail.setCommsEmailId(pageData.getStudentPreferences().getCommsEmailId());
		commsOverEmail.setDailyPublication(pageData.getStudentPreferences().getDailyPublication());
		commsOverEmail.setScoresProgressReports(pageData.getStudentPreferences().getScoresProgressReports());
		studentPreferences.setCommsOverEmail(commsOverEmail);
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