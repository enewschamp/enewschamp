package com.enewschamp.app.user.login.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.repository.UserLoginRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class UserLoginService {

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

	public UserLogin get(Long userLoginId) {
		Optional<UserLogin> existingEntity = repository.findById(userLoginId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.STUD_LOGIN_NOT_FOUND, String.valueOf(userLoginId));
		}
	}

	public UserLogin getUserLogin(final String userId, final String deviceId, final String tokenId, UserType userType) {
		Optional<UserLogin> existingEntity = repository.getLogin(userId, deviceId, tokenId, userType);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
			// throw new BusinessException(ErrorCodes.STUD_LOGIN_NOT_FOUND, emailId);
		}
	}

	public UserLogin getUserNonLogin(final String userId, final String deviceId, final String tokenId,
			UserType userType) {
		Optional<UserLogin> existingEntity = repository.getNonLogin(userId, deviceId, tokenId, userType);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
			// throw new BusinessException(ErrorCodes.STUD_LOGIN_NOT_FOUND, emailId);
		}
	}

	public UserLogin getDeviceLogin(final String userId, final String deviceId, final String tokenId,
			final UserType userType) {
		Optional<UserLogin> existingEntity = repository.getDeviceLogin(userId, deviceId, tokenId, userType);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public LocalDateTime getUserLastLoginDetails(final String userId, final String deviceId, final UserType userType) {
		return repository.getLastLoginDate(userId, deviceId, userType);
	}

	public UserLogin getOperatorLogin(final String userId, final String deviceId, final String tokenId, UserType userType) {
		Optional<UserLogin> existingEntity = repository.getOperatorLogin(userId, deviceId, tokenId, userType);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public UserLogin getOperatorLogin(final String userId, UserType userType) {
		Optional<UserLogin> existingEntity = repository.getOperatorLogin(userId, userType);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

}
