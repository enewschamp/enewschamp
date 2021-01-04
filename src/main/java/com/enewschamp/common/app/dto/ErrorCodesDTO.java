package com.enewschamp.common.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorCodesDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 1201220065078913195L;

	@NotNull
	private Long errorCodeId;

	private String errorCategory;

	private String errorDescription;

	@NotNull
	private String errorCode;

	@NotNull
	private String errorMessage;
}
