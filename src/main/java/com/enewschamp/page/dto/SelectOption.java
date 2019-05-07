package com.enewschamp.page.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SelectOption extends MaintenanceDTO {
	
	private static final long serialVersionUID = 6756233935123723489L;	

	@NotNull
	private String name;
	
	@NotNull
	private String value;

}
