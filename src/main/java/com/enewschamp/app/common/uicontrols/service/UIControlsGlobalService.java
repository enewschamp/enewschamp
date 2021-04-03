package com.enewschamp.app.common.uicontrols.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.uicontrols.global.repository.UIControlsGlobalRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.uicontrols.dto.UIControlsGlobalDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControlsGlobal;
import com.enewschamp.app.common.uicontrols.repository.UIControlsGlobalRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
@Service
public class UIControlsGlobalService {

	@Autowired
	UIControlsGlobalRepository repository;
	
	@Autowired
	UIControlsGlobalRepositoryCustomImpl customRepository;

	@Autowired
	ModelMapper modelMapper;

	public UIControlsGlobalDTO get(String globalControlRef) {
		// System.out.println(">>>>>>globalControlRef>>>>>" + globalControlRef);
		UIControlsGlobal existingEntity = repository.findByGlobalControlRef(globalControlRef);
		UIControlsGlobalDTO uicontrolsGlobalDto = null;
		if (existingEntity != null) {
			uicontrolsGlobalDto = modelMapper.map(existingEntity, UIControlsGlobalDTO.class);
		}
		return uicontrolsGlobalDto;
	}

	public UIControlsGlobalDTO create(UIControlsGlobalDTO uiDto) {
		UIControlsGlobal entity = modelMapper.map(uiDto, UIControlsGlobal.class);
		UIControlsGlobal uicontrols = repository.save(entity);
		UIControlsGlobalDTO dto = modelMapper.map(uicontrols, UIControlsGlobalDTO.class);
		return dto;
	}

	public UIControlsGlobal createOne(UIControlsGlobal entity) {
		UIControlsGlobal uiControlosGlobal = null;
		try {
			uiControlosGlobal = repository.save(entity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return uiControlosGlobal;
	}

	public UIControlsGlobal update(UIControlsGlobal uiControlEntity) {
		Long CityId = uiControlEntity.getUiControlGlobalId();
		UIControlsGlobal existingEntity = get(CityId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(uiControlEntity, existingEntity);
		return repository.save(existingEntity);
	}

	public UIControlsGlobal read(UIControlsGlobal uiControlEntity) {
		Long uiControlId = uiControlEntity.getUiControlGlobalId();
		UIControlsGlobal city = get(uiControlId);
		return city;

	}

	public UIControlsGlobal close(UIControlsGlobal uiControlEntity) {
		Long uiControlId = uiControlEntity.getUiControlGlobalId();
		UIControlsGlobal existingUIControls = get(uiControlId);
		if (existingUIControls.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingUIControls.setRecordInUse(RecordInUseType.N);
		existingUIControls.setOperationDateTime(null);
		return repository.save(existingUIControls);
	}

	public UIControlsGlobal reinstate(UIControlsGlobal uiControlEntity) {
		Long uiControlId = uiControlEntity.getUiControlGlobalId();
		UIControlsGlobal existingUIControls = get(uiControlId);
		if (existingUIControls.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingUIControls.setRecordInUse(RecordInUseType.Y);
		existingUIControls.setOperationDateTime(null);
		return repository.save(existingUIControls);
	}

	public Page<UIControlsGlobal> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<UIControlsGlobal> cityList = customRepository.findAll(pageable, searchRequest);
		if (cityList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return cityList;
	}

	public UIControlsGlobal get(Long uiControlsId) {
		Optional<UIControlsGlobal> existingEntity = repository.findById(uiControlsId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.UICONTROLS_NOT_FOUND);
		}
	}

	public int createAll(List<UIControlsGlobal> uiControlsGlobals) {
		int noOfRecords = 0;
		try {
			noOfRecords = repository.saveAll(uiControlsGlobals).size();
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return noOfRecords;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void clean() {
		repository.truncate();
		repository.deleteSequences();
		repository.initializeSequence();
	}
}