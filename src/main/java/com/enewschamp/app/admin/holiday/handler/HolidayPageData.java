package com.enewschamp.app.admin.holiday.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long holidayId;
	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;
	@NotNull(message = MessageConstants.DATE_NOT_NULL)
	private LocalDate holidayDate;
	@NotNull(message = MessageConstants.HOLIDAY_NOT_NULL)
	@NotEmpty(message = MessageConstants.HOLIDAY_NOT_EMPTY)
	private String holiday;
}
