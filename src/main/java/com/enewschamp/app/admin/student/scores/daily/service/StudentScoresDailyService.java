package com.enewschamp.app.admin.student.scores.daily.service;

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
import com.enewschamp.app.admin.student.scores.daily.repository.StudentScoresDaily;
import com.enewschamp.app.admin.student.scores.daily.repository.StudentScoresDailyRepository;
import com.enewschamp.app.admin.student.scores.daily.repository.StudentScoresDailyRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentScoresDailyService {

	@Autowired
	private StudentScoresDailyRepositoryCustomImpl repositoryCustom;

	@Autowired
	private StudentScoresDailyRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentScoresDaily create(StudentScoresDaily studentScoreDailyEntity) {
		StudentScoresDaily studentScoreDaily = null;
		try {
			studentScoreDaily = repository.save(studentScoreDailyEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentScoreDaily;
	}

	public StudentScoresDaily update(StudentScoresDaily studentScoreDaily) {
		Long studentScoreDailyId = studentScoreDaily.getScoresDailyId();
		StudentScoresDaily existingStudentScoresDaily = get(studentScoreDailyId);
		if (existingStudentScoresDaily.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(studentScoreDaily, existingStudentScoresDaily);
		return repository.save(existingStudentScoresDaily);
	}

	public StudentScoresDaily patch(StudentScoresDaily studentScoreDaily) {
		Long studentScoreDailyId = studentScoreDaily.getScoresDailyId();
		StudentScoresDaily existingEntity = get(studentScoreDailyId);
		modelMapperForPatch.map(studentScoreDaily, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long studentScoreDailyId) {
		repository.deleteById(studentScoreDailyId);
	}

	public StudentScoresDaily get(Long studentScoreDailyId) {
		Optional<StudentScoresDaily> existingEntity = repository.findById(studentScoreDailyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_SCORES_DAILY_NOT_FOUND,
					String.valueOf(studentScoreDailyId));
		}
	}

	public StudentScoresDaily read(StudentScoresDaily studentScoreDaily) {
		Long studentScoreDailyId = studentScoreDaily.getScoresDailyId();
		StudentScoresDaily studentScoreDailyEntity = get(studentScoreDailyId);
		return studentScoreDailyEntity;
	}

	public StudentScoresDaily close(StudentScoresDaily studentScoreDailyEntity) {
		Long studentScoreDailyId = studentScoreDailyEntity.getScoresDailyId();
		StudentScoresDaily existingEntity = get(studentScoreDailyId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public StudentScoresDaily reinstate(StudentScoresDaily studentScoreDailyEntity) {
		Long studentScoreDailyId = studentScoreDailyEntity.getScoresDailyId();
		StudentScoresDaily existingStudentScoresDaily = get(studentScoreDailyId);
		if (existingStudentScoresDaily.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentScoresDaily.setRecordInUse(RecordInUseType.Y);
		existingStudentScoresDaily.setOperationDateTime(null);
		return repository.save(existingStudentScoresDaily);
	}

	public Page<StudentScoresDaily> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentScoresDaily> studentScoresDailyList = repositoryCustom.findAll(pageable, searchRequest);
		return studentScoresDailyList;
	}
}
