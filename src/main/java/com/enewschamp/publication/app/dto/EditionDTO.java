package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EditionDTO extends AbstractDTO {
	
	private static final long serialVersionUID = 1201220065078913195L;

	@NotNull
	@Size(max=6)
	private String editionId;
	
	@NotNull
	@Size(min=3, max=3)
	private String languageId;
	
	@NotNull
	@Size(min=6, max=20)
	private String editionName;
}
