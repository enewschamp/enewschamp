package com.enewschamp.app.admin.entitlement.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntitlementPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long id;
	@JsonInclude
	@NotNull
	private String userId;
	@JsonInclude
	@NotNull
	private String role;
	@JsonInclude
	@NotNull
	private String pageName;
	@JsonInclude
	private String recordInUse;
	@JsonInclude
	private String operator;
	@JsonInclude
	protected LocalDateTime lastUpdate;
	
}
