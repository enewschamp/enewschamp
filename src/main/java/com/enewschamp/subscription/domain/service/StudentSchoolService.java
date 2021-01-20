package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.school.repository.StudentSchoolRepositoryCustomImpl;
import com.enewschamp.app.admin.student.school.repository.StudentSchoolRepositoryCustomImplNotInTheList;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.repository.StudentSchoolRepository;

@Service
public class StudentSchoolService {
	@Autowired
	private StudentSchoolRepository repository;
	
	@Autowired
	private StudentSchoolRepositoryCustomImpl repositoryCustom;
	
	@Autowired
	private StudentSchoolRepositoryCustomImplNotInTheList nonListRepositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentSchool create(StudentSchool studentSchool) {
		StudentSchool studentSchoolEntity = null;
		try {
			studentSchoolEntity = repository.save(studentSchool);
		}
		catch(DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentSchoolEntity;
	}

	public StudentSchool update(StudentSchool studentSchool) {
		Long studentId = studentSchool.getStudentId();
		StudentSchool existingEntity = get(studentId);
		if(existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(studentSchool, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSchool patch(StudentSchool StudentSchool) {
		Long studentId = StudentSchool.getStudentId();

		StudentSchool existingEntity = get(studentId);
		modelMapperForPatch.map(StudentSchool, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentSchool get(Long studentId) {
		Optional<StudentSchool> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(Long studentId) {
		StudentSchool StudentSchool = new StudentSchool();
		StudentSchool.setStudentId(studentId);
		return auditService.getEntityAudit(StudentSchool);
	}

	public StudentSchool read(StudentSchool studentSchoolEntity) {
		Long studentDetailsId = studentSchoolEntity.getStudentId();
		StudentSchool stakeHolder = get(studentDetailsId);
		return stakeHolder;
	}

	public StudentSchool close(StudentSchool studentSchoolEntity) {
		Long studentDetailsId = studentSchoolEntity.getStudentId();
		StudentSchool existingStudentSchool = get(studentDetailsId);
		if (existingStudentSchool.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingStudentSchool.setRecordInUse(RecordInUseType.N);
		existingStudentSchool.setOperationDateTime(null);
		return repository.save(existingStudentSchool);
	}

	public StudentSchool reinstate(StudentSchool studentSchoolEntity) {
		Long studentDetailsId = studentSchoolEntity.getStudentId();
		StudentSchool existingStudentSchool = get(studentDetailsId);
		if (existingStudentSchool.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentSchool.setRecordInUse(RecordInUseType.Y);
		existingStudentSchool.setOperationDateTime(null);
		return repository.save(existingStudentSchool);
	}

	public Page<StudentSchool> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentSchool> studentSchoolList = repositoryCustom.findAll(pageable, searchRequest);
		if(studentSchoolList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return studentSchoolList;
	}
	
	public Page<StudentSchool> notInThelist(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentSchool> studentSchoolList = nonListRepositoryCustom.findAll(pageable, searchRequest);
		if(studentSchoolList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return studentSchoolList;
	}
}
