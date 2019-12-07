package com.enewschamp.subscription.domain.business;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;
import com.enewschamp.subscription.domain.service.StudentSubscriptionService;
import com.enewschamp.subscription.domain.service.StudentSubscriptionWorkService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SubscriptionBusiness {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StudentSubscriptionService studentSubscription;

	@Autowired
	StudentSubscriptionWorkService studentWorkSubscription;

	@Autowired
	StudentControlBusiness studentControlBusiness;
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	public void createNewStudentScription(Long studentId, PageRequestDTO PageRequestDTO) {

		// studentSubscription.create(subscripionDto);
	}

	public StudentSubscriptionWork updateSubscriptionPeriodInWork(
			StudentSubscriptionWorkDTO studentSubscpritionWorkDTO) {
		StudentSubscriptionWork studentSubscriptionWork = modelMapper.map(studentSubscpritionWorkDTO,
				StudentSubscriptionWork.class);
		// TO Do // change the operator id..

		studentSubscriptionWork.setOperatorId("APP");
		studentSubscriptionWork.setRecordInUse(RecordInUseType.Y);

		studentSubscriptionWork = studentWorkSubscription.create(studentSubscriptionWork);
		return studentSubscriptionWork;
	}

	public void saveAsWork(Long studentId, PageRequestDTO pageRequestDTO) {
		HeaderDTO header = pageRequestDTO.getHeader();
		StudentSubscription subscripionDto = null;
		try {
			subscripionDto = objectMapper.readValue(pageRequestDTO.getData().toString(), StudentSubscription.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String subscriptionType = subscripionDto.getSubscriptionSelected();
<<<<<<< Updated upstream
		String editionId = header.getEditionID();
=======
		String editionId = header.getEditionId();
>>>>>>> Stashed changes
		if ("E".equals(subscriptionType)) {
			subscripionDto.setEditionID(editionId);
			subscripionDto.setStudentID(studentId);
			int evalDays = appConfig.getEvalDays();
			// set the evaluation type as E
			subscripionDto.setSubscriptionSelected("E");
			Date startDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.add(Calendar.DATE, evalDays);

			Date endDate = c.getTime();
			subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			subscripionDto.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			subscripionDto.setRecordInUse(RecordInUseType.Y);
			subscripionDto.setAutoRenewal("Y");

			// to be corrected with actual value
			subscripionDto.setOperatorId("APP");
			StudentSubscriptionWork subsWork = modelMapper.map(subscripionDto, StudentSubscriptionWork.class);
			subsWork.setStudentID(studentId);
			studentWorkSubscription.create(subsWork);
		} else if ("P".equals(subscriptionType)) {
			subscripionDto.setEditionID(editionId);
			subscripionDto.setStudentID(studentId);
			Date startDate = new Date();

			subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			subscripionDto.setRecordInUse(RecordInUseType.Y);

			// to be corrected with actual value
			subscripionDto.setOperatorId("APP");
			StudentSubscriptionWork subsWork = modelMapper.map(subscripionDto, StudentSubscriptionWork.class);
			subsWork.setStudentID(studentId);
			studentWorkSubscription.create(subsWork);
		} else if ("S".equals(subscriptionType)) {
			subscripionDto.setEditionID(editionId);
			subscripionDto.setStudentID(studentId);
			Date startDate = new Date();
<<<<<<< Updated upstream

			subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			subscripionDto.setRecordInUse(RecordInUseType.Y);

=======

			subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			subscripionDto.setRecordInUse(RecordInUseType.Y);

>>>>>>> Stashed changes
			// to be corrected with actual value
			subscripionDto.setOperatorId("APP");
			StudentSubscriptionWork subsWork = modelMapper.map(subscripionDto, StudentSubscriptionWork.class);
			subsWork.setStudentID(studentId);
			studentWorkSubscription.create(subsWork);
		}

	}

	public void saveAsMaster(Long studentId, PageRequestDTO pageRequestDTO) {

		HeaderDTO header = pageRequestDTO.getHeader();

		StudentSubscription subscripionDto = null;
		try {
			subscripionDto = objectMapper.readValue(pageRequestDTO.getData().toString(), StudentSubscription.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String subscriptionType = subscripionDto.getSubscriptionSelected();
		String operation = header.getOperation();
		String editionId = header.getEditionId();
		boolean studentExist = studentControlBusiness.isStudentExist(studentId);

		// if student exists already
		if (studentExist) {
			if ("S".equals(subscriptionType)) {
				subscripionDto.setEditionID(editionId);

				int evalDays = 10;
				// set the evaluation type as E
				subscripionDto.setSubscriptionSelected("E");
				Date startDate = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, evalDays);

				Date endDate = c.getTime();
				subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				subscripionDto.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				subscripionDto.setRecordInUse(RecordInUseType.Y);
				subscripionDto.setAutoRenewal("Y");
				// to be corrected with actual value
				subscripionDto.setOperatorId("APP");
				StudentSubscription subsmast = modelMapper.map(subscripionDto, StudentSubscription.class);
				subsmast.setStudentID(studentId);

				studentSubscription.create(subsmast);
			}
		} else {
			if ("S".equals(subscriptionType)) {
				subscripionDto.setEditionID(editionId);
				int evalDays = appConfig.getEvalDays();
				// set the evaluation type as E
				subscripionDto.setSubscriptionSelected("E");
				Date startDate = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, evalDays);

				Date endDate = c.getTime();
				subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				subscripionDto.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				subscripionDto.setRecordInUse(RecordInUseType.Y);
				subscripionDto.setAutoRenewal("Y");

				// to be corrected with actual value
				subscripionDto.setOperatorId("APP");
				StudentSubscription subsmast = modelMapper.map(subscripionDto, StudentSubscription.class);
				subsmast.setStudentID(studentId);
				studentSubscription.create(subsmast);
			} else if ("P".equals(subscriptionType)) {
				subscripionDto.setEditionID(editionId);
				Date startDate = new Date();

				subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				subscripionDto.setRecordInUse(RecordInUseType.Y);

				// to be corrected with actual value
				subscripionDto.setOperatorId("APP");
				StudentSubscription subsmast = modelMapper.map(subscripionDto, StudentSubscription.class);
				subsmast.setStudentID(studentId);
				studentSubscription.create(subsmast);

			}

		}

	}

	public StudentSubscription saveAsMaster(StudentSubscriptionDTO StudentSubscriptionDTO) {
		StudentSubscription studentSubscriptionentity = modelMapper.map(StudentSubscriptionDTO,
				StudentSubscription.class);

		StudentSubscription updatedEntity = studentSubscription.create(studentSubscriptionentity);
		return updatedEntity;
	}

	public void saveWorkToMaster(Long studentId, String editionId) {
		StudentSubscriptionWork subsWork = studentWorkSubscription.get(studentId, editionId);
		StudentSubscription studSubsDto = modelMapper.map(subsWork, StudentSubscription.class);
		studentSubscription.create(studSubsDto);
	}

	public StudentSubscriptionDTO getStudentSubscriptionFromMaster(Long studentId, String eidtionId) {
		StudentSubscription studentSubscriptionEntity = studentSubscription.get(studentId, eidtionId);
		StudentSubscriptionDTO studentSubscriptionDTO = modelMapper.map(studentSubscriptionEntity,
				StudentSubscriptionDTO.class);
		return studentSubscriptionDTO;
	}

	public boolean isStudentSubscriptionValid(String emailId, String eidtionId) {
		boolean validSubcription = false;
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentSubscription studentSubscriptionEntity = studentSubscription.get(studentId, eidtionId);
		LocalDate startDate = studentSubscriptionEntity.getStartDate();
		LocalDate endDate = studentSubscriptionEntity.getEndDate();
		long diffDays = ChronoUnit.DAYS.between(startDate, endDate);
		long evalDays = appConfig.getEvalDays();
		if (evalDays >= diffDays) {
			validSubcription = true;
		}
		return validSubcription;

	}

	public StudentSubscriptionWorkDTO getStudentSubscriptionFromWork(Long studentId, String eidtionId) {
		StudentSubscriptionWork studentSubscriptionEntity = studentWorkSubscription.get(studentId, eidtionId);
		StudentSubscriptionWorkDTO studentSubscriptionWorkDTO = modelMapper.map(studentSubscriptionEntity,
				StudentSubscriptionWorkDTO.class);
		return studentSubscriptionWorkDTO;
	}
}
