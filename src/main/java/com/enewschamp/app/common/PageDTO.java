package com.enewschamp.app.common;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageDTO implements Serializable {
	
	@NotNull
	private HeaderDTO header;
	
	@JsonIgnore
	private String pageName;
	
	private PageData data;
	private List<UIControlsDTO> screenProperties;
} 
