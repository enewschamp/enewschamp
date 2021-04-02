package com.enewschamp.security.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.security.entity.AppSecurity;
import com.enewschamp.security.repository.AppSecurityRepository;

@Service
public class AppSecurityService {

	@Autowired
	AppSecurityRepository appSecurityRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public AppSecurity create(AppSecurity appSecurityEntity) {
		return appSecurityRepository.save(appSecurityEntity);
	}

	public AppSecurity update(AppSecurity appSecurityEntity) {
		Long appSecurityId = appSecurityEntity.getAppSecurityId();
		AppSecurity existingAppSecurity = get(appSecurityId);
		modelMapper.map(appSecurityEntity, existingAppSecurity);
		return appSecurityRepository.save(existingAppSecurity);
	}

	public AppSecurity patch(AppSecurity appSecurity) {
		Long appSecurityId = appSecurity.getAppSecurityId();
		AppSecurity existingEntity = get(appSecurityId);
		modelMapperForPatch.map(appSecurity, existingEntity);
		return appSecurityRepository.save(existingEntity);
	}

	public void delete(Long appSecurityId) {
		appSecurityRepository.deleteById(appSecurityId);
	}

	public AppSecurity get(Long appSecurityId) {
		Optional<AppSecurity> existingEntity = appSecurityRepository.findById(appSecurityId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.APP_SEC_KEY_NOT_FOUND);
		}
	}

	public String getAppAvailability(final String module) {
		Optional<AppSecurity> existingEntity = appSecurityRepository.getAppSecurityByModule(module);
		if (existingEntity.isPresent()) {
			return existingEntity.get().getIsAppAvailable();
		} else {
			throw new BusinessException(ErrorCodeConstants.APP_SEC_KEY_NOT_FOUND);
		}
	}

	public String getCompatibleAppVersions(final String module) {
		Optional<AppSecurity> existingEntity = appSecurityRepository.getAppSecurityByModule(module);
		if (existingEntity.isPresent()) {
			return existingEntity.get().getCompatibleVersions();
		} else {
			throw new BusinessException(ErrorCodeConstants.APP_SEC_KEY_NOT_FOUND);
		}
	}

	@Cacheable
	public boolean isValidKey(final String appName, final String appKey, final String module) {
		Optional<AppSecurity> existingEntity = appSecurityRepository.getAppSec(appName, appKey, module);
		if (existingEntity.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

}
