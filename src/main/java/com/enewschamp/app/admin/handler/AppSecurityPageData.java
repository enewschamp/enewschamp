package com.enewschamp.app.admin.handler;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppSecurityPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long appSecId;
	@JsonInclude
	private String appName;
	@JsonInclude
	private String appKey;
	@JsonInclude
	private String module;
//	@JsonInclude
//	private String recordInUse;
//	@JsonInclude
//	private String operator;
//	@JsonInclude
//	private LocalDateTime lastUpdate;
}
