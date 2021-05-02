package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.details.repository.StudentDetailsRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.repository.StudentDetailsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentDetailsService {
	private final StudentDetailsRepository repository;
	private final StudentDetailsRepositoryCustomImpl repositoryCustom;
    private final ModelMapper modelMapper;
    private final AuditService auditService;
    
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	

	public StudentDetails create(StudentDetails studentDetails) {
		StudentDetails studentDetailsEntity = null;
		try {
			studentDetailsEntity = repository.save(studentDetails);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		catch (ConstraintViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentDetailsEntity;
	}

	public StudentDetails update(StudentDetails StudentDetails) {
		Long studentId = StudentDetails.getStudentId();
		StudentDetails existingEntity = get(studentId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(StudentDetails, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentDetails patch(StudentDetails StudentDetails) {
		Long studentId = StudentDetails.getStudentId();

		StudentDetails existingEntity = get(studentId);
		modelMapperForPatch.map(StudentDetails, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentDetails get(Long studentId) {
		Optional<StudentDetails> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long studentId) {
		StudentDetails studentDetails = new StudentDetails();
		studentDetails.setStudentId(studentId);
		return auditService.getEntityAudit(studentDetails);
	}

	public StudentDetails read(StudentDetails studentDetailsEntity) {
		Long studentDetailsId = studentDetailsEntity.getStudentId();
		StudentDetails stakeHolder = get(studentDetailsId);
		return stakeHolder;
	}

	public StudentDetails close(StudentDetails studentDetailsEntity) {
		Long studentDetailsId = studentDetailsEntity.getStudentId();
		StudentDetails existingStudentDetails = get(studentDetailsId);
		if (existingStudentDetails.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingStudentDetails.setRecordInUse(RecordInUseType.N);
		existingStudentDetails.setOperationDateTime(null);
		return repository.save(existingStudentDetails);
	}

	public StudentDetails reinstate(StudentDetails studentDetailsEntity) {
		Long studentDetailsId = studentDetailsEntity.getStudentId();
		StudentDetails existingStudentDetails = get(studentDetailsId);
		if (existingStudentDetails.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentDetails.setRecordInUse(RecordInUseType.Y);
		existingStudentDetails.setOperationDateTime(null);
		return repository.save(existingStudentDetails);
	}

	public Page<StudentDetails> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentDetails> studentDetailsList = repositoryCustom.findAll(pageable, searchRequest);
		return studentDetailsList;
	}

}