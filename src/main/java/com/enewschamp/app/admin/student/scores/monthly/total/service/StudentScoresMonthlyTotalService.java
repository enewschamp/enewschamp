package com.enewschamp.app.admin.student.scores.monthly.total.service;

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
import com.enewschamp.app.admin.student.scores.monthly.total.repository.StudentScoresMonthlyTotal;
import com.enewschamp.app.admin.student.scores.monthly.total.repository.StudentScoresMonthlyTotalRepository;
import com.enewschamp.app.admin.student.scores.monthly.total.repository.StudentScoresMonthlyTotalRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentScoresMonthlyTotalService {

	@Autowired
	private StudentScoresMonthlyTotalRepositoryCustom repositoryCustom;

	@Autowired
	private StudentScoresMonthlyTotalRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentScoresMonthlyTotal create(StudentScoresMonthlyTotal studentScoreMonthlyEntity) {
		StudentScoresMonthlyTotal studentScoreMonthly = null;
		try {
			studentScoreMonthly = repository.save(studentScoreMonthlyEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentScoreMonthly;
	}

	public StudentScoresMonthlyTotal update(StudentScoresMonthlyTotal studentScoreMonthly) {
		Long studentScoreMonthlyId = studentScoreMonthly.getScoresMonthlyTotalId();
		StudentScoresMonthlyTotal existingStudentScoresMonthlyTotal = get(studentScoreMonthlyId);
		if (existingStudentScoresMonthlyTotal.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(studentScoreMonthly, existingStudentScoresMonthlyTotal);
		return repository.save(existingStudentScoresMonthlyTotal);
	}

	public StudentScoresMonthlyTotal patch(StudentScoresMonthlyTotal studentScoreMonthly) {
		Long studentScoreMonthlyId = studentScoreMonthly.getScoresMonthlyTotalId();
		StudentScoresMonthlyTotal existingEntity = get(studentScoreMonthlyId);
		modelMapperForPatch.map(studentScoreMonthly, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long studentScoreMonthlyId) {
		repository.deleteById(studentScoreMonthlyId);
	}

	public StudentScoresMonthlyTotal get(Long studentScoreMonthlyId) {
		Optional<StudentScoresMonthlyTotal> existingEntity = repository.findById(studentScoreMonthlyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_SCORES_MONTHLY_NOT_FOUND,
					String.valueOf(studentScoreMonthlyId));
		}
	}

	public StudentScoresMonthlyTotal read(StudentScoresMonthlyTotal studentScoreMonthly) {
		Long studentScoreMonthlyId = studentScoreMonthly.getScoresMonthlyTotalId();
		StudentScoresMonthlyTotal studentScoreMonthlyEntity = get(studentScoreMonthlyId);
		return studentScoreMonthlyEntity;
	}

	public StudentScoresMonthlyTotal close(StudentScoresMonthlyTotal studentScoreMonthlyEntity) {
		Long studentScoreMonthlyId = studentScoreMonthlyEntity.getScoresMonthlyTotalId();
		StudentScoresMonthlyTotal existingEntity = get(studentScoreMonthlyId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public StudentScoresMonthlyTotal reinstate(StudentScoresMonthlyTotal studentScoreMonthlyEntity) {
		Long studentScoreMonthlyId = studentScoreMonthlyEntity.getScoresMonthlyTotalId();
		StudentScoresMonthlyTotal existingStudentScoresMonthlyTotal = get(studentScoreMonthlyId);
		if (existingStudentScoresMonthlyTotal.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentScoresMonthlyTotal.setRecordInUse(RecordInUseType.Y);
		existingStudentScoresMonthlyTotal.setOperationDateTime(null);
		return repository.save(existingStudentScoresMonthlyTotal);
	}

	public Page<StudentScoresMonthlyTotal> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentScoresMonthlyTotal> studentScoresMonthlyList = repositoryCustom
				.findStudentMonthlyScoresTotals(pageable, searchRequest);
		if (studentScoresMonthlyList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return studentScoresMonthlyList;
	}
}