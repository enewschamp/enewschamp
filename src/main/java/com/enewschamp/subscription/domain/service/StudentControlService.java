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

import com.enewschamp.app.admin.student.control.repository.StudentControlRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.repository.StudentControlRepository;

@Service
public class StudentControlService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	StudentControlRepository repository;

	@Autowired
	private StudentControlRepositoryCustomImpl repositoryCustom;

	public StudentControl create(StudentControl studentControl) {
		Optional<StudentControl> existinStudentControl  = repository.findById(studentControl.getStudentId());
		if(existinStudentControl.isPresent()) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		StudentControl studentControlEntity = null;
		try {
			studentControlEntity = repository.save(studentControl);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentControlEntity;
	}

	public StudentControl update(StudentControl StudentControl) {
		Long studentId = StudentControl.getStudentId();
		StudentControl existingEntity = get(studentId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(StudentControl, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentControl patch(StudentControl StudentControl) {
		Long studentId = StudentControl.getStudentId();

		StudentControl existingEntity = get(studentId);
		modelMapperForPatch.map(StudentControl, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentControl get(Long studentId) {
		Optional<StudentControl> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
	}

	public boolean studentExist(Long studentId) {
		return repository.existsById(studentId);

	}

	public String getAudit(Long studentId) {
		StudentControl StudentControl = new StudentControl();
		StudentControl.setStudentId(studentId);
		return auditService.getEntityAudit(StudentControl);
	}

	public Page<StudentControl> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentControl> studentControlList = repositoryCustom.findAll(pageable, null);
		return studentControlList;
	}
	
	public StudentControl read(StudentControl studentControlEntity) {
		Long studentControlId = studentControlEntity.getStudentId();
		return get(studentControlId);
	}

	public StudentControl close(StudentControl studentControlEntity) {
		Long studentControlId = studentControlEntity.getStudentId();
		StudentControl existingStudentControls = get(studentControlId);
		if (!(existingStudentControls.getRecordInUse().equals(RecordInUseType.Y))) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingStudentControls.setRecordInUse(RecordInUseType.N);
		existingStudentControls.setOperationDateTime(null);
		return repository.save(existingStudentControls);
	}

	public StudentControl reinstate(StudentControl StudentControlEntity) {
		Long StudentControlId = StudentControlEntity.getStudentId();
		StudentControl existingStudentControls = get(StudentControlId);
		if (existingStudentControls.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentControls.setRecordInUse(RecordInUseType.Y);
		existingStudentControls.setOperationDateTime(null);
		return repository.save(existingStudentControls);
	}
}