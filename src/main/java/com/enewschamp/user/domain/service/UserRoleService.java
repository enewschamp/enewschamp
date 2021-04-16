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
import com.enewschamp.app.admin.user.role.repository.UserRoleRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.entity.UserRoleKey;

@Service
public class UserRoleService extends AbstractDomainService {

	@Autowired
	UserRoleRepository repository;
	

	@Autowired
	UserRoleRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public UserRole create(UserRole userRole) {
		UserRole userRoleEntity = null;
		try {
			userRoleEntity = repository.save(userRole);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return userRoleEntity;
	}

	public UserRole update(UserRole userRole) {
		UserRoleKey userRoleKey = userRole.getUserRoleKey();
		UserRole existingUserRole = load(userRoleKey);
		if (existingUserRole.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(userRole, existingUserRole);
		return repository.save(existingUserRole);
	}

	public UserRole patch(UserRole userRole) {
		UserRoleKey userRoleKey = userRole.getUserRoleKey();
		UserRole existingEntity = load(userRoleKey);
		modelMapperForPatch.map(userRole, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(UserRoleKey userRoleKey) {
		repository.deleteById(userRoleKey);
	}

	public UserRole load(UserRoleKey userRoleKey) {
		Optional<UserRole> existingEntity = repository.findById(userRoleKey);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.USER_ROLE_NOT_FOUND, userRoleKey.getRoleId(),
					userRoleKey.getUserId(), userRoleKey.getDayOfTheWeek().toString());
		}
	}

	public UserRole get(UserRoleKey userRoleKey) {
		Optional<UserRole> existingEntity = repository.findById(userRoleKey);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public UserRole getByUserIdAndRole(String userId, String roleId) {
		Optional<UserRole> existingEntity = repository.getByUserIdAndRole(userId, roleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public UserRole getByUserId(String userId) {
		Optional<UserRole> existingEntity = repository.getByUserId(userId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public String getAudit(UserRoleKey userRoleKey) {
		UserRole userRole = new UserRole();
		userRole.setUserRoleKey(userRoleKey);
		return auditService.getEntityAudit(userRoleKey);
	}


	public UserRole read(UserRole userRole) {
		UserRoleKey userRoleKey = userRole.getUserRoleKey();
		UserRole existingEntity = load(userRoleKey);
		return existingEntity;
	}

	public UserRole close(UserRole userRole) {
		UserRoleKey userRoleKey = userRole.getUserRoleKey();
		UserRole existingEntity = load(userRoleKey);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public UserRole reInstate(UserRole userRole) {
		UserRoleKey userRoleKey = userRole.getUserRoleKey();
		UserRole existingEntity = load(userRoleKey);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingEntity.setRecordInUse(RecordInUseType.Y);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public Page<UserRole> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<UserRole> userList = repositoryCustom.findAll(pageable, searchRequest);
		return userList;
	}
	
	public Boolean isValidRole(String userId, String roleId) {
		UserRole userRole = getByUserIdAndRole(userId, roleId);
		if (userRole != null)
			return true;
		else
			throw new BusinessException(ErrorCodeConstants.USER_ROLE_NOT_FOUND, roleId, userId);

	}
}