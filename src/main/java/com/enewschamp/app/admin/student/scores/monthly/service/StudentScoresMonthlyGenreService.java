package com.enewschamp.app.admin.student.scores.monthly.service;

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
import com.enewschamp.app.admin.student.scores.monthly.repository.StudentScoresMonthlyGenre;
import com.enewschamp.app.admin.student.scores.monthly.repository.StudentScoresMonthlyGenreRepository;
import com.enewschamp.app.admin.student.scores.monthly.repository.StudentScoresMonthlyGenreRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentScoresMonthlyGenreService {

	@Autowired
	private StudentScoresMonthlyGenreRepositoryCustomImpl repositoryCustom;
	
	@Autowired
	private StudentScoresMonthlyGenreRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public StudentScoresMonthlyGenre create(StudentScoresMonthlyGenre studentScoreMonthlyEntity) {
		StudentScoresMonthlyGenre studentScoreMonthly = null;
		try {
			studentScoreMonthly = repository.save(studentScoreMonthlyEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentScoreMonthly;
	}

	public StudentScoresMonthlyGenre update(StudentScoresMonthlyGenre studentScoreMonthly) {
		Long studentScoreMonthlyId = studentScoreMonthly.getScoresMonthlyGenreId();
		StudentScoresMonthlyGenre existingStudentScoresMonthlyGenre = get(studentScoreMonthlyId);
		if(existingStudentScoresMonthlyGenre.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(studentScoreMonthly, existingStudentScoresMonthlyGenre);
		return repository.save(existingStudentScoresMonthlyGenre);
	}

	public StudentScoresMonthlyGenre patch(StudentScoresMonthlyGenre studentScoreMonthly) {
		Long studentScoreMonthlyId = studentScoreMonthly.getScoresMonthlyGenreId();
		StudentScoresMonthlyGenre existingEntity = get(studentScoreMonthlyId);
		modelMapperForPatch.map(studentScoreMonthly, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long studentScoreMonthlyId) {
		repository.deleteById(studentScoreMonthlyId);
	}

	public StudentScoresMonthlyGenre get(Long studentScoreMonthlyId) {
		Optional<StudentScoresMonthlyGenre> existingEntity = repository.findById(studentScoreMonthlyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_SCORES_MONTHLY_NOT_FOUND, String.valueOf(studentScoreMonthlyId));
		}
	}


	public StudentScoresMonthlyGenre read(StudentScoresMonthlyGenre studentScoreMonthly) {
		Long studentScoreMonthlyId = studentScoreMonthly.getScoresMonthlyGenreId();
		StudentScoresMonthlyGenre studentScoreMonthlyEntity = get(studentScoreMonthlyId);
		return studentScoreMonthlyEntity;
	}

	public StudentScoresMonthlyGenre close(StudentScoresMonthlyGenre studentScoreMonthlyEntity) {
		Long studentScoreMonthlyId = studentScoreMonthlyEntity.getScoresMonthlyGenreId();
		StudentScoresMonthlyGenre existingEntity = get(studentScoreMonthlyId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public StudentScoresMonthlyGenre reinstate(StudentScoresMonthlyGenre studentScoreMonthlyEntity) {
		Long studentScoreMonthlyId = studentScoreMonthlyEntity.getScoresMonthlyGenreId();
		StudentScoresMonthlyGenre existingStudentScoresMonthlyGenre = get(studentScoreMonthlyId);
		if (existingStudentScoresMonthlyGenre.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentScoresMonthlyGenre.setRecordInUse(RecordInUseType.Y);
		existingStudentScoresMonthlyGenre.setOperationDateTime(null);
		return repository.save(existingStudentScoresMonthlyGenre);
	}
	

	public Page<StudentScoresMonthlyGenre> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentScoresMonthlyGenre> studentScoresMonthlyList = repositoryCustom.findAll(pageable, searchRequest);
		if (studentScoresMonthlyList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return studentScoresMonthlyList;
	}
}