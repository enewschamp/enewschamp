package com.enewschamp.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LOVDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 922724808801362317L;

	private long lovId = 0L;

	@NotNull
	@Size(max = 20)
	private String type;

	@NotNull
	@Size(max = 50)
	private String nameId;

	@NotNull
	@Size(max = 50)
	private String description;

}
