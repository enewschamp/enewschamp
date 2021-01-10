package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.school.repository.StudentSchoolRepositoryCustom;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.repository.StudentSchoolRepository;

@Service
public class StudentSchoolService {
	@Autowired
	private StudentSchoolRepository repository;
	
	@Autowired
	private StudentSchoolRepositoryCustom repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentSchool create(StudentSchool StudentSchool) {
		return repository.save(StudentSchool);
	}

	public StudentSchool update(StudentSchool StudentSchool) {
		Long studentId = StudentSchool.getStudentId();

		StudentSchool existingEntity = get(studentId);
		modelMapper.map(StudentSchool, existingEntity);
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
			return existingStudentSchool;
		}
		existingStudentSchool.setRecordInUse(RecordInUseType.N);
		existingStudentSchool.setOperationDateTime(null);
		return repository.save(existingStudentSchool);
	}

	public StudentSchool reinstate(StudentSchool studentSchoolEntity) {
		Long studentDetailsId = studentSchoolEntity.getStudentId();
		StudentSchool existingStudentSchool = get(studentDetailsId);
		if (existingStudentSchool.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingStudentSchool;
		}
		existingStudentSchool.setRecordInUse(RecordInUseType.Y);
		existingStudentSchool.setOperationDateTime(null);
		return repository.save(existingStudentSchool);
	}

	public Page<StudentSchool> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentSchool> stakeHolderList = repositoryCustom.findStudentSchools(pageable, searchRequest);
		return stakeHolderList;
	}
}
