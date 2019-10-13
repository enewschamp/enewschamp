package com.enewschamp.user.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.UserRole;
import com.enewschamp.user.domain.entity.UserRoleKey;

@Service
public class UserRoleService extends AbstractDomainService {

	@Autowired
	UserRoleRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public UserRole create(UserRole userRole) {
		return repository.save(userRole);
	}
	
	public UserRole update(UserRole userRole) {
		UserRoleKey userRoleKey = userRole.getUserRoleKey();
		UserRole existingUserRole = load(userRoleKey);
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
			throw new BusinessException(ErrorCodes.USER_ROLE_NOT_FOUND, userRoleKey.getRoleId(), userRoleKey.getUserId(), userRoleKey.getDayOfTheWeek().toString());
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
	
	
	public String getAudit(UserRoleKey userRoleKey) {
		UserRole userRole = new UserRole();
		userRole.setUserRoleKey(userRoleKey);
		return auditService.getEntityAudit(userRoleKey);
	}
	
}
