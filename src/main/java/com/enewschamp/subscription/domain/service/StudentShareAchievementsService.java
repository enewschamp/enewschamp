package com.enewschamp.subscription.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.achievement.repository.StudentShareAchievementsRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.entity.StudentShareAchievements;
import com.enewschamp.subscription.domain.repository.StudentShareAchievementsRepository;

@Service
public class StudentShareAchievementsService {
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	StudentShareAchievementsRepository repository;
	
	@Autowired
	StudentShareAchievementsRepositoryCustomImpl repositoryCustom;

	public StudentShareAchievements create(StudentShareAchievements studentShareAchievements) {
		StudentShareAchievements studentShareAchievementsEntity = null;
		try {
			studentShareAchievementsEntity = repository.save(studentShareAchievements);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return studentShareAchievementsEntity;
	}

	public StudentShareAchievements update(StudentShareAchievements StudentShareAchievements) {
		Long studentId = StudentShareAchievements.getStudentId();
		StudentShareAchievements existingEntity = get(studentId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(StudentShareAchievements, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentShareAchievements patch(StudentShareAchievements StudentShareAchievements) {
		Long studentId = StudentShareAchievements.getStudentId();
		StudentShareAchievements existingEntity = get(studentId);
		modelMapperForPatch.map(StudentShareAchievements, existingEntity);
		return repository.save(existingEntity);
	}

	public StudentShareAchievements get(Long studentId) {
		Optional<StudentShareAchievements> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_SHARE_ACHIEVEMENTS_NOT_FOUND);
		}
	}

	public List<StudentShareAchievements> getStudentAchievements(Long studentId) {
		return repository.getStudentAchievements(studentId);
	}

	public String getAudit(Long studentId) {
		StudentShareAchievements StudentShareAchievements = new StudentShareAchievements();
		StudentShareAchievements.setStudentId(studentId);
		return auditService.getEntityAudit(StudentShareAchievements);
	}
	
	public StudentShareAchievements read(StudentShareAchievements achievementEntity) {
		Long achievementId = achievementEntity.getStudentId();
		StudentShareAchievements stakeHolder = get(achievementId);
		return stakeHolder;
	}

	public StudentShareAchievements close(StudentShareAchievements achievementEntity) {
		Long achievementId = achievementEntity.getStudentId();
		StudentShareAchievements existingStudentShareAchievements = get(achievementId);
		if (existingStudentShareAchievements.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingStudentShareAchievements.setRecordInUse(RecordInUseType.N);
		existingStudentShareAchievements.setOperationDateTime(null);
		return repository.save(existingStudentShareAchievements);
	}

	public StudentShareAchievements reInstate(StudentShareAchievements achievementEntity) {
		Long achievementId = achievementEntity.getStudentId();
		StudentShareAchievements existingStudentShareAchievements = get(achievementId);
		if (existingStudentShareAchievements.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingStudentShareAchievements.setRecordInUse(RecordInUseType.Y);
		existingStudentShareAchievements.setOperationDateTime(null);
		return repository.save(existingStudentShareAchievements);
	}

	public Page<StudentShareAchievements> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentShareAchievements> achievementList = repositoryCustom.findAll(pageable, searchRequest);
		if (achievementList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return achievementList;
	}
}