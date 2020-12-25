package com.enewschamp.app.admin.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long id;
	@NotNull
	private String editionId;
	@NotNull
	private LocalDate date;
	@NotNull
	private String holiday;
	@JsonInclude
	private String recordInUse;
	@JsonInclude
	private String operator;
	@JsonInclude
	private LocalDateTime lastUpdate;
}
