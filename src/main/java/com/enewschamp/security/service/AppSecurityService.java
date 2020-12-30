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

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.security.entity.AppSecurity;
import com.enewschamp.security.repository.AppSecurityRepository;
import com.enewschamp.security.repository.AppSecurityRepositoryCustom;

@Service
public class AppSecurityService {

	@Autowired
	private AppSecurityRepository appSecurityRepository;
	
	@Autowired
	private AppSecurityRepositoryCustom appSecurityRepositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public AppSecurity create(AppSecurity appSecurityEntity) {
		AppSecurity appSecurity = null;
		try {
			appSecurity = appSecurityRepository.save(appSecurityEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return appSecurity;
	}

	public AppSecurity update(AppSecurity appSecurityEntity) {
		Long appSecurityId = appSecurityEntity.getAppSecId();
		AppSecurity existingAppSecurity = get(appSecurityId);
		modelMapper.map(appSecurityEntity, existingAppSecurity);
		return appSecurityRepository.save(existingAppSecurity);
	}

	public AppSecurity patch(AppSecurity appSecurity) {
		Long appSecurityId = appSecurity.getAppSecId();
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

	@Cacheable("appSec")
	public boolean isValidKey(final String appName, final String appKey, final String module) {
		Optional<AppSecurity> existingEntity = appSecurityRepository.getAppSec(appName, appKey, module);
		if (existingEntity.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
	
	public AppSecurity read(AppSecurity appSecurity) {
		Long appSecId = appSecurity.getAppSecId();
		AppSecurity existingAppSecurity = get(appSecId);
		if (existingAppSecurity.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingAppSecurity;
		}
		existingAppSecurity.setRecordInUse(RecordInUseType.Y);
		existingAppSecurity.setOperationDateTime(null);
		return appSecurityRepository.save(existingAppSecurity);
	}

	public AppSecurity close(AppSecurity appSecurityEntity) {
		Long appSecId = appSecurityEntity.getAppSecId();
		AppSecurity existingAppSecurity = get(appSecId);
		if (existingAppSecurity.getRecordInUse().equals(RecordInUseType.N)) {
			return existingAppSecurity;
		}
		existingAppSecurity.setRecordInUse(RecordInUseType.N);
		existingAppSecurity.setOperationDateTime(null);
		return appSecurityRepository.save(existingAppSecurity);
	}
	
	public AppSecurity reinstate(AppSecurity appSecurityEntity) {
		Long appSecId = appSecurityEntity.getAppSecId();
		AppSecurity existingAppSec = get(appSecId);
		if (existingAppSec.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingAppSec;
		}
		existingAppSec.setRecordInUse(RecordInUseType.Y);
		existingAppSec.setOperationDateTime(null);
		return appSecurityRepository.save(existingAppSec);
	}

	public Page<AppSecurity> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<AppSecurity> appSecList = appSecurityRepositoryCustom.findAppSecurities(pageable);
		return appSecList;
	}

}
