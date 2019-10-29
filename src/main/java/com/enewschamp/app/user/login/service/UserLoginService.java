package com.enewschamp.app.user.login.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.repository.UserLoginRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class UserLoginService  {

	@Autowired
	UserLoginRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public UserLogin create(UserLogin userLogin) {
		
		return repository.save(userLogin);
	}
	
	public UserLogin update(UserLogin userLogin) {
		Long UserLoginId = userLogin.getUserLoginId();
		UserLogin existingUserLogin = get(UserLoginId);
		modelMapper.map(userLogin, existingUserLogin);
		return repository.save(existingUserLogin);
	}
	
	public UserLogin patch(UserLogin userLogin) {
		Long UserLoginId = userLogin.getUserLoginId();
		UserLogin existingEntity = get(UserLoginId);
		modelMapperForPatch.map(userLogin, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long UserLoginId) {
		repository.deleteById(UserLoginId);
	}
	
	public UserLogin get(Long UserLoginId) {
		Optional<UserLogin> existingEntity = repository.findById(UserLoginId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUD_LOGIN_NOT_FOUND, String.valueOf(UserLoginId));
		}
	}
	
	public UserLogin getUserLogin(final String userId, final String deviceId, UserType userType) {
		Optional<UserLogin> existingEntity = repository.getLogin(userId, deviceId,userType);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
			//throw new BusinessException(ErrorCodes.STUD_LOGIN_NOT_FOUND, emailId);
		}
	}
	
	public UserLogin getOperatorLogin(final String userId) {
		Optional<UserLogin> existingEntity = repository.getOperatorLogin(userId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}
	
}
