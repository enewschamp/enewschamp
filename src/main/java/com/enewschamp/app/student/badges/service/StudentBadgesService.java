package com.enewschamp.app.student.badges.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.badges.entity.StudentBadges;
import com.enewschamp.app.student.badges.repository.StudentBadgesRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentBadgesService {

	@Autowired
	StudentBadgesRepository studentBadgesRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	
	public StudentBadges create(StudentBadges studentBadgesEntity) {
		return studentBadgesRepository.save(studentBadgesEntity);
	}

	public StudentBadges update(StudentBadges studentBadgesEntity) {
		Long trendsDailyId = studentBadgesEntity.getStudentBadgesId();
		StudentBadges existingStudentBadges = get(trendsDailyId);
		modelMapper.map(studentBadgesEntity, existingStudentBadges);
		return studentBadgesRepository.save(existingStudentBadges);
	}

	public StudentBadges patch(StudentBadges trendsDaily) {
		Long navId = trendsDaily.getStudentBadgesId();
		StudentBadges existingEntity = get(navId);
		modelMapperForPatch.map(trendsDaily, existingEntity);
		return studentBadgesRepository.save(existingEntity);
	}

	public void delete(Long StudentBadgesId) {
		studentBadgesRepository.deleteById(StudentBadgesId);
	}

	public StudentBadges get(Long trendsDailyId) {
		Optional<StudentBadges> existingEntity = studentBadgesRepository.findById(trendsDailyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUD_BADGES_NOT_FOUND);

		}
	}
	public StudentBadges getStudentBadges(Long studentId, String editionId, Long monthYear) {
		Optional<StudentBadges> existingEntity = studentBadgesRepository.getStudentBadges(studentId, editionId, monthYear);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUD_BADGES_NOT_FOUND);
		}
	}
	
	public Page<StudentBadges> getStudetbadges(Long StudentId, String editionId, int pageNo)
	{
		
		Pageable pageable = PageRequest.of(pageNo, 10);
		Page<StudentBadges> studentPage = studentBadgesRepository.getStudentBadges(StudentId, editionId, pageable);
		//if(!studentPage.isEmpty())
		//{
			return studentPage;
		//}
		//else
		//{
			//throw new BusinessException(ErrorCodes.STUD_BADGES_NOT_FOUND);

		//}

				
	}
}
