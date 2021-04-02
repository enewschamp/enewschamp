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

import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolWorkDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
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
	StudentSubscriptionService studentSubscriptionService;

	@Autowired
	StudentControlWorkService studentControlWorkService;

	@Autowired
	StudentSubscriptionWorkService studentWorkSubscription;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	SchoolDetailsBusiness schoolDetailsBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private PropertiesBackendService propertiesService;

	public void createNewStudentScription(Long studentId, PageRequestDTO PageRequestDTO) {

		// studentSubscription.create(subscripionDto);
	}

	public StudentSubscriptionWork updateSubscriptionPeriodInWork(
			StudentSubscriptionWorkDTO studentSubscpritionWorkDTO) {
		StudentSubscriptionWork studentSubscriptionWork = modelMapper.map(studentSubscpritionWorkDTO,
				StudentSubscriptionWork.class);
		studentSubscriptionWork = studentWorkSubscription.create(studentSubscriptionWork);
		return studentSubscriptionWork;
	}

	public void saveAsWork(Long studentId, String evalAvailed, PageRequestDTO pageRequestDTO) {
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
		String editionId = header.getEditionId();
		String module = header.getModule();
		String emailId = header.getEmailId();
		subscripionDto.setEditionId(editionId);
		subscripionDto.setStudentId(studentId);
		Date startDate = new Date();
		if ("F".equals(subscriptionType)) {
			if (!"Y".equalsIgnoreCase(evalAvailed)) {
				int evalDays = Integer
						.valueOf(propertiesService.getValue(module, PropertyConstants.STUDENT_REGISTRATION_EVAL_DAYS));
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, evalDays);
				Date endDate = c.getTime();
				subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				subscripionDto.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
		} else {
			subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		subscripionDto.setRecordInUse(RecordInUseType.Y);
		subscripionDto.setOperatorId(""+studentId);
		StudentSubscriptionWork subsWork = modelMapper.map(subscripionDto, StudentSubscriptionWork.class);
		subsWork.setStudentId(studentId);
		studentWorkSubscription.create(subsWork);
	}

	public void saveAsMaster(Long studentId, String evalAvailed, PageRequestDTO pageRequestDTO) {
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
		String editionId = header.getEditionId();
		String module = header.getModule();
		String emailId = header.getEmailId();
		subscripionDto.setEditionId(editionId);
		boolean studentExist = studentControlBusiness.isStudentExist(studentId);

		// if student exists already
		if (studentExist) {
			if ("F".equals(subscriptionType)) {
				if (!"Y".equalsIgnoreCase(evalAvailed)) {
					int evalDays = Integer.valueOf(
							propertiesService.getValue(module, PropertyConstants.STUDENT_REGISTRATION_EVAL_DAYS));
					Date startDate = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					c.add(Calendar.DATE, evalDays);
					Date endDate = c.getTime();
					subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					subscripionDto.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		} else {
			Date startDate = new Date();
			subscripionDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		subscripionDto.setRecordInUse(RecordInUseType.Y);
		subscripionDto.setOperatorId(""+studentId);
		StudentSubscription subsmast = modelMapper.map(subscripionDto, StudentSubscription.class);
		subsmast.setStudentId(studentId);
		studentSubscriptionService.create(subsmast);

	}

	public StudentSubscription saveAsMaster(StudentSubscriptionDTO StudentSubscriptionDTO) {
		StudentSubscription studentSubscriptionentity = modelMapper.map(StudentSubscriptionDTO,
				StudentSubscription.class);
		StudentSubscription updatedEntity = studentSubscriptionService.create(studentSubscriptionentity);
		return updatedEntity;
	}

	public void workToMaster(String appName, Long studentId, String editionId) {
		StudentSubscriptionWork subsWork = studentWorkSubscription.get(studentId, editionId);
		if (subsWork != null) {
			StudentSubscription studSubsDto = modelMapper.map(subsWork, StudentSubscription.class);
			String subscriptionSelected = subsWork.getSubscriptionSelected();
			if ("F".equalsIgnoreCase(subscriptionSelected)) {
				int evalDays = Integer
						.valueOf(propertiesService.getValue(appName, PropertyConstants.STUDENT_REGISTRATION_EVAL_DAYS));
				Date startDate = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, evalDays);
				Date endDate = c.getTime();
				studSubsDto.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				studSubsDto.setEndDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			} else {
				String subscriptionPeriod = subsWork.getSubscriptionPeriod();
				int days = 0;
				int months = 0;
				int years = 0;
				if (subscriptionPeriod.endsWith("D")) {
					days = Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1)));
				} else if (subscriptionPeriod.endsWith("M")) {
					months = Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1)));
				} else if (subscriptionPeriod.endsWith("Y")) {
					years = Integer.valueOf(subscriptionPeriod.substring(0, (subscriptionPeriod.length() - 1)));
				}
				String currentSubscription = "";
				LocalDate startDate = LocalDate.now();
				LocalDate currentEndDate = null;
				StudentSubscription studentSubscription = studentSubscriptionService.get(studentId, editionId);
				if (studentSubscription != null) {
					currentSubscription = studentSubscription.getSubscriptionSelected();
					startDate = studentSubscription.getStartDate();
				}
				if (!"".equalsIgnoreCase(currentSubscription)) {
					if (("S".equalsIgnoreCase(currentSubscription) && "P".equalsIgnoreCase(subscriptionSelected))
							|| ("P".equalsIgnoreCase(currentSubscription)
									&& "S".equalsIgnoreCase(subscriptionSelected))) {
						currentEndDate = LocalDate.now();
					} else if (("S".equalsIgnoreCase(currentSubscription)
							&& "S".equalsIgnoreCase(subscriptionSelected))) {
						StudentSchoolWorkDTO studentSchoolWork = schoolDetailsBusiness.getStudentFromWork(studentId);
						StudentSchoolDTO studentSchoolMaster = schoolDetailsBusiness.getStudentFromMaster(studentId);
						currentEndDate = studentSubscription.getEndDate();
						if (!studentSchoolMaster.getSchool().toString()
								.equals(studentSchoolWork.getSchool().toString())) {
							currentEndDate = LocalDate.now();
						}
						if (currentEndDate.isBefore(LocalDate.now())) {
							currentEndDate = LocalDate.now();
						}
					} else {
						currentEndDate = studentSubscription.getEndDate();
						if (currentEndDate.isBefore(LocalDate.now())) {
							currentEndDate = LocalDate.now();
						}
					}
					if (subscriptionPeriod.endsWith("D")) {
						currentEndDate = currentEndDate.plusDays(days);
					} else if (subscriptionPeriod.endsWith("M")) {
						currentEndDate = currentEndDate.plusMonths(months);
					} else if (subscriptionPeriod.endsWith("Y")) {
						currentEndDate = currentEndDate.plusYears(years);
					}
					studSubsDto.setStartDate(startDate);
					studSubsDto.setEndDate(currentEndDate);
				} else {
					currentEndDate = startDate;
					int evalDays = Integer.valueOf(
							propertiesService.getValue(appName, PropertyConstants.STUDENT_REGISTRATION_EVAL_DAYS));
					currentEndDate = currentEndDate.plusDays(evalDays);
					if (subscriptionPeriod.endsWith("D")) {
						currentEndDate = currentEndDate.plusDays(days);
					} else if (subscriptionPeriod.endsWith("M")) {
						currentEndDate = currentEndDate.plusMonths(months);
					} else if (subscriptionPeriod.endsWith("Y")) {
						currentEndDate = currentEndDate.plusYears(years);
					}
					studSubsDto.setStartDate(startDate);
					studSubsDto.setEndDate(currentEndDate);
					StudentControlWork studentControlWork = studentControlWorkService.get(studentId);
					if (studentControlWork != null) {
						studentControlWork.setEvalAvailed("Y");
						studentControlWorkService.update(studentControlWork);
					}
				}
			}
			studentSubscriptionService.create(studSubsDto);
		}

	}

	public StudentSubscriptionDTO getStudentSubscriptionFromMaster(Long studentId, String editionId) {
		StudentSubscription studentSubscriptionEntity = studentSubscriptionService.get(studentId, editionId);
		if (studentSubscriptionEntity != null) {
			StudentSubscriptionDTO studentSubscriptionDTO = modelMapper.map(studentSubscriptionEntity,
					StudentSubscriptionDTO.class);
			return studentSubscriptionDTO;
		} else {
			return null;
		}
	}

	public boolean isStudentSubscriptionValid(String appName, String emailId, String editionId) {
		boolean validSubcription = false;
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentSubscription studentSubscriptionEntity = studentSubscriptionService.get(studentId, editionId);
		LocalDate startDate = studentSubscriptionEntity.getStartDate();
		LocalDate endDate = studentSubscriptionEntity.getEndDate();
		long diffDays = ChronoUnit.DAYS.between(startDate, endDate);
		long evalDays = Integer
				.valueOf(propertiesService.getValue(appName, PropertyConstants.STUDENT_REGISTRATION_EVAL_DAYS));
		if (evalDays >= diffDays) {
			validSubcription = true;
		}
		return validSubcription;

	}

	public StudentSubscriptionWorkDTO getStudentSubscriptionFromWork(Long studentId, String editionId) {
		StudentSubscriptionWork studentSubscriptionEntity = studentWorkSubscription.get(studentId, editionId);
		StudentSubscriptionWorkDTO studentSubscriptionWorkDTO = modelMapper.map(studentSubscriptionEntity,
				StudentSubscriptionWorkDTO.class);
		return studentSubscriptionWorkDTO;
	}
}
