package com.enewschamp.app.notification.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.notification.NotificationAction;
import com.enewschamp.app.notification.repository.NotificationActionRepository;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;

@Service
public class NotificationActionService extends AbstractDomainService {

	@Autowired
	NotificationActionRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AuditService auditService;

	public NotificationAction create(NotificationAction notificationAction) {
		return repository.save(notificationAction);
	}

	public NotificationAction update(NotificationAction notificationAction) {
		Long notificationActionId = notificationAction.getNotificationActionId();
		NotificationAction existingNotificationAction = load(notificationActionId);
		modelMapper.map(notificationAction, existingNotificationAction);
		return repository.save(existingNotificationAction);
	}

	public void delete(Long notificationActionId) {
		repository.deleteById(notificationActionId);
	}

	public NotificationAction load(Long notificationActionId) {
		NotificationAction existingEntity = get(notificationActionId);
		if (existingEntity != null) {
			return existingEntity;
		} else {
			throw new BusinessException(ErrorCodeConstants.NOTIFICATION_ACTION_NOT_FOUND,
					String.valueOf(notificationActionId));
		}
	}

	public NotificationAction get(Long notificationActionId) {
		Optional<NotificationAction> existingEntity = repository.findById(notificationActionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

}
