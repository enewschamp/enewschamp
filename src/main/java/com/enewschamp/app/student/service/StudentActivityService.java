package com.enewschamp.app.student.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.activity.repository.StudentActivityRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.repository.StudentActivityRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentActivityService {

	@Autowired
	StudentActivityRepository studentActivityRepository;

	@Autowired
	private StudentActivityRepositoryCustomImpl studentActivityRepositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public StudentActivity create(StudentActivity StudentActivityEntity) {
		return studentActivityRepository.save(StudentActivityEntity);
	}

	public StudentActivity update(StudentActivity StudentActivityEntity) {
		Long StudentActivityId = StudentActivityEntity.getStudentActivityId();
		StudentActivity existingStudentActivity = get(StudentActivityId);
		modelMapper.map(StudentActivityEntity, existingStudentActivity);
		return studentActivityRepository.save(existingStudentActivity);
	}

	public StudentActivity patch(StudentActivity StudentActivity) {
		Long navId = StudentActivity.getStudentActivityId();
		StudentActivity existingEntity = get(navId);
		modelMapperForPatch.map(StudentActivity, existingEntity);
		return studentActivityRepository.save(existingEntity);
	}

	public void delete(Long StudentActivityId) {
		studentActivityRepository.deleteById(StudentActivityId);
	}

	public StudentActivity get(Long StudentActivityId) {
		Optional<StudentActivity> existingEntity = studentActivityRepository.findById(StudentActivityId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_ACTIVITY_NOT_FOUND);
		}
	}

	public StudentActivity getStudentActivity(Long studentId, Long newsArticleId) {
		Optional<StudentActivity> existingEntity = studentActivityRepository.getStudentActivity(studentId,
				newsArticleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_ACTIVITY_NOT_FOUND);
		}
	}

	public List<StudentActivity> getSavedArticles(Long studentId) {
		List<StudentActivity> existingEntity = studentActivityRepository.getSavedArticles(studentId);
		if (existingEntity.size() > 0) {
			return existingEntity;
		} else {
			throw new BusinessException(ErrorCodeConstants.STUDENT_ACTIVITY_NOT_FOUND);
		}
	}

	public Page<StudentActivity> listStudentActivity(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<StudentActivity> studentActivityList = studentActivityRepositoryCustom.findAll(pageable, searchRequest);
		if (studentActivityList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return studentActivityList;
	}

}