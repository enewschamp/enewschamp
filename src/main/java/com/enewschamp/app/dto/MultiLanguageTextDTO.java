package com.enewschamp.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MultiLanguageTextDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 5709892402330863853L;

	private long multiLanguageTextId = 0L;

	@NotNull
	@Size(max = 5)
	private String locale;

	@NotNull
	@Size(max = 50)
	private String entityName;

	@NotNull
	@Size(max = 50)
	private String entityColumn;

	@NotNull
	@Size(max = 200)
	private String text;

}
