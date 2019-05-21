package com.enewschamp.app.common.uicontrols.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControls;
import com.enewschamp.app.common.uicontrols.repository.UIControlsRepository;
import com.google.common.reflect.TypeToken;

@Service
public class UIControlsService implements IUIControlsService{

	@Autowired
	UIControlsRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<UIControlsDTO> get(String screenName) {
		
		List<UIControls> existingEntity = repository.findByScreenName(screenName);
		java.lang.reflect.Type listType = new TypeToken<List<UIControlsDTO>>(){}.getType();

		List<UIControlsDTO> uicontrolList = modelMapper.map(existingEntity,listType);
	
		return uicontrolList;
	}

	@Override
	public List<UIControlsDTO> getByScreenAndAction(String screenName,String action) {
		
		List<UIControls> existingEntity = repository.findByScreenName(screenName);
		java.lang.reflect.Type listType = new TypeToken<List<UIControlsDTO>>(){}.getType();

		List<UIControlsDTO> uicontrolList = modelMapper.map(existingEntity,listType);
	
		return uicontrolList;
	}
	@Override
	public UIControlsDTO create(UIControlsDTO uiDto) {
		UIControls entity  = modelMapper.map(uiDto, UIControls.class);
		UIControls uicontrols = repository.save(entity);
		UIControlsDTO dto  = modelMapper.map(uicontrols, UIControlsDTO.class);
		return dto;
	}

}
