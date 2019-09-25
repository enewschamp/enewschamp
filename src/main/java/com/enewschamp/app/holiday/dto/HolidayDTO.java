package com.enewschamp.app.holiday.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HolidayDTO extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long holidayId;
	private String editionId;
	private LocalDate holdidayDate;
	private String holiday;
	
}
