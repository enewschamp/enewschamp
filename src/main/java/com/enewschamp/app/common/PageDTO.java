package com.enewschamp.app.common;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.enewschamp.app.common.uicontrols.dto.UIControlsDTO;
import com.enewschamp.common.app.dto.PropertiesFrontendDTO;
import com.enewschamp.publication.domain.common.BOUserList;
import com.enewschamp.publication.domain.common.BadgeList;
import com.enewschamp.publication.domain.common.GenreList;
import com.enewschamp.publication.domain.common.HolidayList;
import com.fasterxml.jackson.annotation.JsonInclude;

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

	private List<PropertiesFrontendDTO> globalProperties;

	private List<GenreList> genres;

	private List<BadgeList> badges;

	private List<HolidayList> holidays;

	private List<BOUserList> boUsers;

	private String errorMessage;

	private String exitPrev;

	private String exitNext;
}
