package com.enewschamp.app.student.notification.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.notification.StudentNotification;
import com.enewschamp.app.student.notification.repository.StudentNotificationRepository;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentNotificationService extends AbstractDomainService {

	@Autowired
	StudentNotificationRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public StudentNotification create(StudentNotification studentNotification) {
		return repository.save(studentNotification);
	}
	
	public StudentNotification update(StudentNotification studentNotification) {
		Long studentNotificationId = studentNotification.getStudentNotificationId();
		StudentNotification existingStudentNotification = get(studentNotificationId);
		modelMapper.map(studentNotification, existingStudentNotification);
		return repository.save(existingStudentNotification);
	}
	
	public StudentNotification patch(StudentNotification studentNotification) {
		Long studentNotificationId = studentNotification.getStudentNotificationId();
		StudentNotification existingEntity = get(studentNotificationId);
		modelMapperForPatch.map(studentNotification, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long studentNotificationId) {
		repository.deleteById(studentNotificationId);
	}
	
	public StudentNotification get(Long studentNotificationId) {
		Optional<StudentNotification> existingEntity = repository.findById(studentNotificationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUDENT_NOTIFICATION_NOT_FOUND, String.valueOf(studentNotificationId));
		}
	}
	
}
