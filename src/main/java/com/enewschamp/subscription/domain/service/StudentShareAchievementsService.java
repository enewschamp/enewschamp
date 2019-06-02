package com.enewschamp.subscription.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
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
	
	public StudentShareAchievements create(StudentShareAchievements StudentShareAchievements)
	{
		return repository.save(StudentShareAchievements);
	}
	
	public StudentShareAchievements update(StudentShareAchievements StudentShareAchievements) {
		Long studentId = StudentShareAchievements.getStudentShareAchievementsId();
		
		StudentShareAchievements existingEntity = get(studentId);
		modelMapper.map(StudentShareAchievements, existingEntity);
		return repository.save(existingEntity);
	}
	public StudentShareAchievements patch(StudentShareAchievements StudentShareAchievements) {
		Long studentId = StudentShareAchievements.getStudentShareAchievementsId();

		StudentShareAchievements existingEntity = get(studentId);
		modelMapperForPatch.map(StudentShareAchievements, existingEntity);
		return repository.save(existingEntity);
	}
	
	public StudentShareAchievements get(Long studentId) {
		Optional<StudentShareAchievements> existingEntity = repository.findById(studentId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUDENT_SHARE_ACHIEVEMENTS_NOT_FOUND);
		}
	}
	
	public List<StudentShareAchievements> getStudentAchievements(Long studentId)
	{
		return repository.getStudentAchievements(studentId);
	}
	
	
	
	public String getAudit(Long studentId) {
		StudentShareAchievements StudentShareAchievements = new StudentShareAchievements();
		StudentShareAchievements.setStudentId(studentId);
		return auditService.getEntityAudit(StudentShareAchievements);
	}
}
