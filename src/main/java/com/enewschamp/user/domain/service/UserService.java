package com.enewschamp.user.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.entity.User;

@Service
public class UserService extends AbstractDomainService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public User create(User user) {
		return repository.save(user);
	}
	
	public User update(User user) {
		String userId = user.getUserId();
		User existingUser = get(userId);
		modelMapper.map(user, existingUser);
		return repository.save(existingUser);
	}
	
	public User patch(User user) {
		String userId = user.getUserId();
		User existingEntity = get(userId);
		modelMapperForPatch.map(user, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(String userId) {
		repository.deleteById(userId);
	}
	
	public User get(String userId) {
		Optional<User> existingEntity = repository.findById(userId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.USER_NOT_FOUND, userId);
		}
	}
	
	public List<ListOfValuesItem> getPublisherLOV() {
		return toListOfValuesItems(repository.getPublisherLOV());
	}
	
	public List<ListOfValuesItem> getAuthorLOV() {
		return toListOfValuesItems(repository.getAuthorLOV());
	}
	
	public List<ListOfValuesItem> getEditorLOV() {
		return toListOfValuesItems(repository.getEditorLOV());
	}
	
	public String getAudit(String userId) {
		User user = new User();
		user.setUserId(userId);
		return auditService.getEntityAudit(user);
	}
	
}
