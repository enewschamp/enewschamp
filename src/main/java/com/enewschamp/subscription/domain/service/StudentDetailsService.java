package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.repository.StudentDetailsRepository;

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
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
	}
	
	public String getAudit(Long studentId) {
		StudentDetails studentDetails = new StudentDetails();
		studentDetails.setStudentID(studentId);
		return auditService.getEntityAudit(studentDetails);
	}
	

}
