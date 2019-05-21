package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EditionDTO extends MaintenanceDTO {
	
	private static final long serialVersionUID = 1201220065078913195L;

	@NotNull
	@Size(max=ForeignKeyColumnLength.EditionId)
	private String editionId;
	
	@NotNull
	@Size(min=3, max=3)
	private String languageId;
	
	@NotNull
	@Size(min=6, max=255)
	private String editionName;
}
