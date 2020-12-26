package com.enewschamp.app.admin.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EditionPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@NotNull
	@JsonInclude
	private String id;
	@JsonInclude
	private String editionName;
    @NotNull
    @JsonInclude
	private Long languageId;
//    @JsonInclude
//	private String recordInUse;
//    @JsonInclude
//	private String operator;
//    @JsonInclude
//	private LocalDateTime lastUpdate;
}
