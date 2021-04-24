package com.enewschamp.security.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.security.repository.AppSecurityRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.security.entity.AppSecurity;
import com.enewschamp.security.repository.AppSecurityRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppSecurityService {

	@Autowired
	AppSecurityRepository appSecurityRepository;

	@Autowired
	private AppSecurityRepositoryCustomImpl appSecurityRepositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public AppSecurity create(AppSecurity appSecurityEntity) {
		AppSecurity appSecurity = null;
		try {
			appSecurity = appSecurityRepository.save(appSecurityEntity);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return appSecurity;
	}

	public AppSecurity update(AppSecurity appSecurityEntity) {
		Long appSecurityId = appSecurityEntity.getAppSecurityId();
		AppSecurity existingAppSecurity = get(appSecurityId);
		if (existingAppSecurity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public AppSecurity read(AppSecurity appSecurity) {
		Long appSecId = appSecurity.getAppSecurityId();
		AppSecurity existingAppSecurity = get(appSecId);
		if (existingAppSecurity.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingAppSecurity;
		}
		existingAppSecurity.setRecordInUse(RecordInUseType.Y);
		existingAppSecurity.setOperationDateTime(null);
		return appSecurityRepository.save(existingAppSecurity);
	}

	public AppSecurity close(AppSecurity appSecurityEntity) {
		Long appSecId = appSecurityEntity.getAppSecurityId();
		AppSecurity existingAppSecurity = get(appSecId);
		if (existingAppSecurity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingAppSecurity.setRecordInUse(RecordInUseType.N);
		existingAppSecurity.setOperationDateTime(null);
		return appSecurityRepository.save(existingAppSecurity);
	}

	public AppSecurity reinstate(AppSecurity appSecurityEntity) {
		Long appSecId = appSecurityEntity.getAppSecurityId();
		AppSecurity existingAppSec = get(appSecId);
		if (existingAppSec.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingAppSec.setRecordInUse(RecordInUseType.Y);
		existingAppSec.setOperationDateTime(null);
		return appSecurityRepository.save(existingAppSec);
	}

	public Page<AppSecurity> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<AppSecurity> appSecList = appSecurityRepositoryCustom.findAll(pageable, null);
		return appSecList;
	}

}