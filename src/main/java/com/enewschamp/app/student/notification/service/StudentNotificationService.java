package com.enewschamp.app.student.notification.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.notification.page.data.NotificationsSearchRequest;
import com.enewschamp.app.student.notification.StudentNotification;
import com.enewschamp.app.student.notification.StudentNotificationDTO;
import com.enewschamp.app.student.notification.repository.StudentNotificationCustomRepository;
import com.enewschamp.app.student.notification.repository.StudentNotificationRepository;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentNotificationService extends AbstractDomainService {

	@Autowired
	StudentNotificationRepository repository;

	@Autowired
	StudentNotificationCustomRepository customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public Page<StudentNotificationDTO> getNotificationList(NotificationsSearchRequest searchRequest, int pageNo,
			int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		return customRepository.getNotificationList(searchRequest, pageable);
	}

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
			throw new BusinessException(ErrorCodeConstants.STUDENT_NOTIFICATION_NOT_FOUND,
					String.valueOf(studentNotificationId));
		}
	}

}
