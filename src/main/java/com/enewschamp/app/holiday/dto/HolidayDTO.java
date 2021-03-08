package com.enewschamp.app.holiday.dto;

import java.time.LocalDate;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long holidayId;
	private String editionId;
	private LocalDate holidayDate;
	private String holiday;
	private String publication;
	private String helpdesk;

}
