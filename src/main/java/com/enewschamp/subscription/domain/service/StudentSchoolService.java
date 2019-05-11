package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.subscription.domain.repository.StudentSchoolRepository;
import com.enewschamp.subscription.domin.entity.StudentSchool;

public class StudentSchoolService {
	@Autowired
	StudentSchoolRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public StudentSchool create(StudentSchool StudentSchool)
	{
		return repository.save(StudentSchool);
	}
	
	public StudentSchool update(StudentSchool StudentSchool) {
		Long studentId = StudentSchool.getStudentID();
		
		StudentSchool existingEntity = get(studentId);
		modelMapper.map(StudentSchool, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentSchool patch(StudentSchool StudentSchool) {
		Long studentId = StudentSchool.getStudentID();

		StudentSchool existingEntity = get(studentId);
		modelMapperForPatch.map(StudentSchool, existingEntity);
		return repository.save(existingEntity);
	}
	
	public StudentSchool get(Long studentId) {
		Optional<StudentSchool> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.STUDENT_DTLS_NOT_FOUND, "Publication not found!");
		}
	}
	
	public String getAudit(Long studentId) {
		StudentSchool StudentSchool = new StudentSchool();
		StudentSchool.setStudentID(studentId);
		return auditService.getEntityAudit(StudentSchool);
	}
	
}
