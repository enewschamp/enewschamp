package com.enewschamp.app.common;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.common.app.dto.PropertiesDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PageDTO implements Serializable {

	@NotNull
	private HeaderDTO header;

	@JsonIgnore
	private String pageName;

	@JsonInclude
	private PageData data;

	private List<UIControlsDTO> screenProperties;

	private List<PropertiesDTO> globalProperties;

	private String errorMessage;
	private JsonNode filter;
	@JsonInclude
	private List<PageData> records;
}
