package com.enewschamp.app.user.login.service;

import java.time.LocalDateTime;
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
import com.enewschamp.app.admin.user.login.repository.UserLoginRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;
import com.enewschamp.app.user.login.repository.UserLoginRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class UserLoginService {

	@Autowired
	UserLoginRepository repository;

	@Autowired
	UserLoginRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public UserLogin create(UserLogin userLogin) {
		UserLogin userLoginEntity = null;
		try {
			userLoginEntity = repository.save(userLogin);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return userLoginEntity;
	}

	public UserLogin update(UserLogin userLogin) {
		Long UserLoginId = userLogin.getUserLoginId();
		UserLogin existingUserLogin = get(UserLoginId);
		if (existingUserLogin.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public UserLogin getOperatorLogin(final String userId, final String deviceId, final String tokenId,
			final UserType userType) {
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

	public UserLogin read(UserLogin userLogin) {
		Long UserLoginId = userLogin.getUserLoginId();
		UserLogin existingUserLogin = get(UserLoginId);
		return existingUserLogin;
	}

	public UserLogin close(UserLogin userLogin) {
		Long UserLoginId = userLogin.getUserLoginId();
		UserLogin existingUserLogin = get(UserLoginId);
		if (existingUserLogin.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingUserLogin.setRecordInUse(RecordInUseType.N);
		existingUserLogin.setOperationDateTime(null);
		return repository.save(existingUserLogin);
	}

	public UserLogin reInstate(UserLogin userLogin) {
		Long UserLoginId = userLogin.getUserLoginId();
		UserLogin existingUserLogin = get(UserLoginId);
		if (existingUserLogin.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingUserLogin.setRecordInUse(RecordInUseType.Y);
		existingUserLogin.setOperationDateTime(null);
		return repository.save(existingUserLogin);
	}

	public Page<UserLogin> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<UserLogin> userList = repositoryCustom.findAll(pageable, searchRequest);
		if (userList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return userList;
	}
}
