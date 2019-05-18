package com.enewschamp.user.domin.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.enewschamp.user.domin.entity.UserRole;
import com.enewschamp.user.domin.entity.UserRoleKey;

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
		UserRole existingUserRole = get(userRoleKey);
		modelMapper.map(userRole, existingUserRole);
		return repository.save(existingUserRole);
	}
	
	public UserRole patch(UserRole userRole) {
		UserRoleKey userRoleKey = userRole.getUserRoleKey();
		UserRole existingEntity = get(userRoleKey);
		modelMapperForPatch.map(userRole, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(UserRoleKey userRoleKey) {
		repository.deleteById(userRoleKey);
	}
	
	public UserRole get(UserRoleKey userRoleKey) {
		Optional<UserRole> existingEntity = repository.findById(userRoleKey);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.USER_ROLE_NOT_FOUND, "User role not found!");
		}
	}
	
	public String getAudit(UserRoleKey userRoleKey) {
		UserRole userRole = new UserRole();
		userRole.setUserRoleKey(userRoleKey);
		return auditService.getEntityAudit(userRoleKey);
	}
	
}
