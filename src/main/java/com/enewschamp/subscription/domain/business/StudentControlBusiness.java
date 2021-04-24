package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;

@Service
public class StudentControlBusiness {

	@Autowired
	StudentControlWorkService studentControlWorkService;

	@Autowired
	StudentControlService studentControlService;

	@Autowired
	ModelMapper modelMapper;

	public StudentControlWork saveAsWork(StudentControlWorkDTO studenControlWorkDTO) {
		StudentControlWork studentControlWork = modelMapper.map(studenControlWorkDTO, StudentControlWork.class);
		studentControlWork = studentControlWorkService.create(studentControlWork);
		return studentControlWork;
	}

	public StudentControlWork updateAsWork(StudentControlWorkDTO studenControlWorkDto) {
		StudentControlWork studentControlWork = modelMapper.map(studenControlWorkDto, StudentControlWork.class);
		studentControlWork = studentControlWorkService.update(studentControlWork);
		return studentControlWork;
	}

	public StudentControl saveAsMaster(StudentControlDTO studenControlDto) {
		StudentControl studentControl = modelMapper.map(studenControlDto, StudentControl.class);
		studentControl = studentControlService.create(studentControl);
		return studentControl;
	}

	public void workToMaster(Long studentId) {
		StudentControlWork workEntity = studentControlWorkService.get(studentId);
		if (workEntity != null) {
			StudentControl masterEntity = modelMapper.map(workEntity, StudentControl.class);
			if (workEntity.getSubscriptionTypeW() != null && !"".equals(workEntity.getSubscriptionTypeW())) {
				masterEntity.setSubscriptionType(workEntity.getSubscriptionTypeW());
			}
			if (workEntity.getStudentDetailsW() != null && !"".equals(workEntity.getStudentDetailsW())) {
				masterEntity.setStudentDetails(workEntity.getStudentDetailsW());
			}
			if (workEntity.getSchoolDetailsW() != null && !"".equals(workEntity.getSchoolDetailsW())) {
				masterEntity.setSchoolDetails(workEntity.getSchoolDetailsW());
			}
			if (workEntity.getPreferencesW() != null && !"".equals(workEntity.getPreferencesW())) {
				masterEntity.setPreferences(workEntity.getPreferencesW());
			}
			if (workEntity.getEmailIdVerified() != null && !"".equals(workEntity.getEmailIdVerified())) {
				masterEntity.setEmailIdVerified(workEntity.getEmailIdVerified());
			}
			masterEntity.setStudentId(studentId);
			studentControlService.create(masterEntity);
		}
	}

	public void deleteFromWork(StudentControlWorkDTO studenControlWorkDto) {
		studentControlWorkService.delete(studenControlWorkDto.getStudentId());
	}

	public boolean isStudentExist(Long studentId) {
		return studentControlService.studentExist(studentId);
	}

	public StudentControlDTO getStudentFromMaster(Long studentId) {
		StudentControl studentEntity = studentControlService.get(studentId);
		if (studentEntity != null) {
			StudentControlDTO studentDto = modelMapper.map(studentEntity, StudentControlDTO.class);
			return studentDto;
		} else
			return null;

	}

	public StudentControlWorkDTO getStudentFromWork(Long studentId) {
		StudentControlWork studentEntityWork = studentControlWorkService.get(studentId);
		if (studentEntityWork != null) {
			StudentControlWorkDTO studentDto = modelMapper.map(studentEntityWork, StudentControlWorkDTO.class);
			return studentDto;
		} else
			return null;

	}

	/*
	 * public Long getStudentId(String emailId) { Long studentId = 0L;
	 * StudentControlDTO studentControlDTO = getStudentFromMaster(emailId); if
	 * (studentControlDTO != null) { studentId = studentControlDTO.getStudentId(); }
	 * else { StudentControlWorkDTO studentControlWorkDTO =
	 * getStudentFromWork(emailId); if (studentControlWorkDTO != null) studentId =
	 * studentControlWorkDTO.getStudentId(); } return studentId; }
	 */
}