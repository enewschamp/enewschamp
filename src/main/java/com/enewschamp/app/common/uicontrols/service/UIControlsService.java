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

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.uicontrols.repository.UIControlsRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControls;
import com.enewschamp.app.common.uicontrols.repository.UIControlsRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.google.common.reflect.TypeToken;

@Service
public class UIControlsService implements IUIControlsService {

	@Autowired
	UIControlsRepository repository;

	@Autowired
	UIControlsRepositoryCustom customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<UIControlsDTO> get(String pageName, String operation) {
		List<UIControls> existingEntity = repository.findByPageNameAndOperation(pageName, operation);
		java.lang.reflect.Type listType = new TypeToken<List<UIControlsDTO>>() {
		}.getType();
		List<UIControlsDTO> uicontrolList = modelMapper.map(existingEntity, listType);
		return uicontrolList;
	}

	@Override
	public UIControlsDTO create(UIControlsDTO uiDto) {
		UIControls entity = modelMapper.map(uiDto, UIControls.class);
		UIControls uicontrols = repository.save(entity);
		UIControlsDTO dto = modelMapper.map(uicontrols, UIControlsDTO.class);
		return dto;
	}

	public UIControls createOne(UIControls entity) {
		UIControls uiControlos = null;
		try {
			uiControlos = repository.save(entity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return uiControlos;
	}

	public UIControls update(UIControls uiControlEntity) {
		Long CityId = uiControlEntity.getUiControlId();
		UIControls existingEntity = get(CityId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(uiControlEntity, existingEntity);
		return repository.save(existingEntity);
	}

	public UIControls read(UIControls uiControlEntity) {
		Long uiControlId = uiControlEntity.getUiControlId();
		UIControls city = get(uiControlId);
		return city;

	}

	public UIControls close(UIControls uiControlEntity) {
		Long uiControlId = uiControlEntity.getUiControlId();
		UIControls existingUIControls = get(uiControlId);
		if (existingUIControls.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingUIControls.setRecordInUse(RecordInUseType.N);
		existingUIControls.setOperationDateTime(null);
		return repository.save(existingUIControls);
	}

	public UIControls reInstateUIControls(UIControls uiControlEntity) {
		Long uiControlId = uiControlEntity.getUiControlId();
		UIControls existingUIControls = get(uiControlId);
		if (existingUIControls.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingUIControls.setRecordInUse(RecordInUseType.Y);
		existingUIControls.setOperationDateTime(null);
		return repository.save(existingUIControls);
	}

	public Page<UIControls> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<UIControls> cityList = customRepository.findUIControls(pageable, searchRequest);
		if (cityList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return cityList;
	}

	public UIControls get(Long uiControlsId) {
		Optional<UIControls> existingEntity = repository.findById(uiControlsId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.UICONTROLS_NOT_FOUND);
		}
	}

}
