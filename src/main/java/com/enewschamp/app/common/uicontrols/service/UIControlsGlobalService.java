package com.enewschamp.app.common.uicontrols.service;

import static org.hamcrest.CoreMatchers.nullValue;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.uicontrols.dto.UIControlsGlobalDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControlsGlobal;
import com.enewschamp.app.common.uicontrols.repository.UIControlsGlobalRepository;

@Service
public class UIControlsGlobalService {

	@Autowired
	UIControlsGlobalRepository repository;

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

}
