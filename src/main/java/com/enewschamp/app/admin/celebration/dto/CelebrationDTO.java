package com.enewschamp.app.admin.celebration.dto;

import java.time.LocalDate;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CelebrationDTO extends MaintenanceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long celebrationId = 0L;

	@NotNull
	private String editionId;

	private LocalDate date;

	@NotNull
	private int readingLevel;

	private String gender;

	@NotNull
	private String nameId;

	private String occasion;

	private String imageName;

	private String audioFileName;

}