package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.subscription.domain.repository.StudentDetailsRepository;
import com.enewschamp.subscription.domin.entity.StudentDetails;

@Service
public class StudentDetailsService {

	@Autowired
	StudentDetailsRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public StudentDetails create(StudentDetails studentDetails)
	{
		return repository.save(studentDetails);
	}
	
	public StudentDetails update(StudentDetails StudentDetails) {
		Long studentId = StudentDetails.getStudentID();
		
		StudentDetails existingEntity = get(studentId);
		modelMapper.map(StudentDetails, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentDetails patch(StudentDetails StudentDetails) {
		Long studentId = StudentDetails.getStudentID();

		StudentDetails existingEntity = get(studentId);
		modelMapperForPatch.map(StudentDetails, existingEntity);
		return repository.save(existingEntity);
	}
	
	public StudentDetails get(Long studentId) {
		Optional<StudentDetails> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.STUDENT_DTLS_NOT_FOUND, "Publication not found!");
		}
	}
	
	public String getAudit(Long studentId) {
		StudentDetails studentDetails = new StudentDetails();
		studentDetails.setStudentID(studentId);
		return auditService.getEntityAudit(studentDetails);
	}
	

}
