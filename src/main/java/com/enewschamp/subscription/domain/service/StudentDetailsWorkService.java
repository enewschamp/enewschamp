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
import com.enewschamp.subscription.domain.repository.StudentDetailsWorkRepository;
import com.enewschamp.subscription.domin.entity.StudentDetailsWork;

@Service
public class StudentDetailsWorkService {

	@Autowired
	StudentDetailsWorkRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public StudentDetailsWork create(StudentDetailsWork StudentDetailsWork)
	{
		return repository.save(StudentDetailsWork);
	}
	
	public StudentDetailsWork update(StudentDetailsWork StudentDetailsWork) {
		Long studentId = StudentDetailsWork.getStudentID();
		
		StudentDetailsWork existingEntity = get(studentId);
		modelMapper.map(StudentDetailsWork, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentDetailsWork patch(StudentDetailsWork StudentDetailsWork) {
		Long studentId = StudentDetailsWork.getStudentID();

		StudentDetailsWork existingEntity = get(studentId);
		modelMapperForPatch.map(StudentDetailsWork, existingEntity);
		return repository.save(existingEntity);
	}
	
	public StudentDetailsWork get(Long studentId) {
		Optional<StudentDetailsWork> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.STUDENT_DTLS_NOT_FOUND, "Publication not found!");
		}
	}
	
	public String getAudit(Long studentId) {
		StudentDetailsWork StudentDetailsWork = new StudentDetailsWork();
		StudentDetailsWork.setStudentID(studentId);
		return auditService.getEntityAudit(StudentDetailsWork);
	}
	

}
