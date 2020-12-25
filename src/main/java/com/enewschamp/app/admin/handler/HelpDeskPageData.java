package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HelpDeskPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long id;
	private Long requestId;
	@NotNull
	private Long studentId;
	@NotNull
	private String editionId;
	@NotNull
	private String categoryId;
	@NotNull
	private String details;
	@NotNull
	private String callBackPhoneNumber;
	private String supportingComments;
	private String closeFlag;
	@NotNull
	private LocalDateTime callBackTime;
	private LocalDateTime createDateTime;
	@JsonInclude
	private String recordInUse;
	@JsonInclude
	private String operator;
	@JsonInclude
	private LocalDateTime lastUpdate;
}
