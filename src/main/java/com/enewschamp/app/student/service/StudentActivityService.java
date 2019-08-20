package com.enewschamp.app.student.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.repository.StudentActivityRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentActivityService {

	@Autowired
	StudentActivityRepository StudentActivityRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public StudentActivity create(StudentActivity StudentActivityEntity) {
		return StudentActivityRepository.save(StudentActivityEntity);
	}

	public StudentActivity update(StudentActivity StudentActivityEntity) {
		Long StudentActivityId = StudentActivityEntity.getStudentActivityId();
		StudentActivity existingStudentActivity = get(StudentActivityId);
		modelMapper.map(StudentActivityEntity, existingStudentActivity);
		return StudentActivityRepository.save(existingStudentActivity);
	}

	public StudentActivity patch(StudentActivity StudentActivity) {
		Long navId = StudentActivity.getStudentActivityId();
		StudentActivity existingEntity = get(navId);
		modelMapperForPatch.map(StudentActivity, existingEntity);
		return StudentActivityRepository.save(existingEntity);
	}

	public void delete(Long StudentActivityId) {
		StudentActivityRepository.deleteById(StudentActivityId);
	}

	public StudentActivity get(Long StudentActivityId) {
		Optional<StudentActivity> existingEntity = StudentActivityRepository.findById(StudentActivityId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException( ErrorCodes.STUDENT_ACTIVITY_NOT_FOUND,
					"Student Activity not found!");
		}
	}
	
	public StudentActivity getStudentActivity(Long studentId, Long newsArticleId)
	{
		Optional<StudentActivity> existingEntity = StudentActivityRepository.getStudentActivity(studentId, newsArticleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException( ErrorCodes.STUDENT_ACTIVITY_NOT_FOUND);
		}
	}
	public List<StudentActivity> getsavedArticles(Long studentId)
	{
		List<StudentActivity> existingEntity = StudentActivityRepository.getSavedArticles(studentId);
		if (existingEntity.size()>0) {
			return existingEntity;
		} else {
			throw new BusinessException( ErrorCodes.STUDENT_ACTIVITY_NOT_FOUND);
		}
	}
	
}
