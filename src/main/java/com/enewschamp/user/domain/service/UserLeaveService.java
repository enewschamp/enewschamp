package com.enewschamp.user.domain.service;

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
import com.enewschamp.app.admin.user.leave.repository.UserLeaveRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.UserLeave;
import com.enewschamp.user.domain.entity.UserLeaveKey;

@Service
public class UserLeaveService extends AbstractDomainService {

	@Autowired
	UserLeaveRepository repository;

	@Autowired
	UserLeaveRepositoryCustom customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public UserLeave create(UserLeave userLeave) {
		UserLeave userLeaveEntity = null;
		try {
			userLeaveEntity = repository.save(userLeave);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return userLeaveEntity;
	}

	public UserLeave update(UserLeave userLeave) {
		UserLeaveKey userLeaveKey = userLeave.getUserLeaveKey();
		UserLeave existingUserLeave = get(userLeaveKey);
		if (existingUserLeave.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public UserLeave read(UserLeave userLeaveEntity) {
		UserLeaveKey userLeaveId = userLeaveEntity.getUserLeaveKey();
		UserLeave userLeave = get(userLeaveId);
		return userLeave;
	}

	public UserLeave close(UserLeave userLeaveEntity) {
		UserLeaveKey userLeaveId = userLeaveEntity.getUserLeaveKey();
		UserLeave existingUserLeave = get(userLeaveId);
		if (existingUserLeave.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingUserLeave.setRecordInUse(RecordInUseType.N);
		existingUserLeave.setOperationDateTime(null);
		return repository.save(existingUserLeave);
	}

	public UserLeave reInstate(UserLeave userLeaveEntity) {
		UserLeaveKey userLeaveId = userLeaveEntity.getUserLeaveKey();
		UserLeave existingUserLeave = get(userLeaveId);
		if (existingUserLeave.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingUserLeave.setRecordInUse(RecordInUseType.Y);
		existingUserLeave.setOperationDateTime(null);
		return repository.save(existingUserLeave);
	}

	public Page<UserLeave> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<UserLeave> userLeaveList = customRepository.findUserLeaves(searchRequest, pageable);
		if (userLeaveList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return userLeaveList;
	}

}
