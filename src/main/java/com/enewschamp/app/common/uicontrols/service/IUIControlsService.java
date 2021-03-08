package com.enewschamp.app.common.uicontrols.service;

import java.util.List;

import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;

public interface IUIControlsService {

	public List<UIControlsDTO> get(String pageName, String operation);

	public UIControlsDTO create(UIControlsDTO uiDto);
}
