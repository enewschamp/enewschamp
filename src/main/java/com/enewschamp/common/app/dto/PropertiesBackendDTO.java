package com.enewschamp.common.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PropertiesBackendDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -3428291258057090659L;

	private Long propertyId;

	@NotNull
	private String name;

	@NotNull
	private String value;

	@NotNull
	private String appName;
}
