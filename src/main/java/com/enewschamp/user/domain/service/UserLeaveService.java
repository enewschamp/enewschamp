package com.enewschamp.user.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.UserLeave;
import com.enewschamp.user.domain.entity.UserLeaveKey;

@Service
public class UserLeaveService extends AbstractDomainService {

	@Autowired
	UserLeaveRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public UserLeave create(UserLeave userLeave) {
		return repository.save(userLeave);
	}

	public UserLeave update(UserLeave userLeave) {
		UserLeaveKey userLeaveKey = userLeave.getUserLeaveKey();
		UserLeave existingUserLeave = get(userLeaveKey);
		modelMapper.map(userLeave, existingUserLeave);
		return repository.save(existingUserLeave);
	}

	public UserLeave patch(UserLeave userLeave) {
		UserLeaveKey userLeaveKey = userLeave.getUserLeaveKey();
		UserLeave existingEntity = get(userLeaveKey);
		modelMapperForPatch.map(userLeave, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(UserLeaveKey userLeaveKey) {
		repository.deleteById(userLeaveKey);
	}

	public UserLeave get(UserLeaveKey userLeaveKey) {
		Optional<UserLeave> existingEntity = repository.findById(userLeaveKey);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.USER_LEAVE_NOT_FOUND);
		}
	}

	public String getAudit(UserLeaveKey userLeaveKey) {
		UserLeave userLeave = new UserLeave();
		userLeave.setUserLeaveKey(userLeaveKey);
		return auditService.getEntityAudit(userLeaveKey);
	}

}
