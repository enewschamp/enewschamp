package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class HashTagDTO extends AbstractDTO {
	
	private static final long serialVersionUID = 2844284287599966841L;

	@NotNull
	@Size(min=2, max=25)
	private String hashTag;
	
	@NotNull
	@Size(min=1, max = ForeignKeyColumnLength.GenreId)
	private String genreId;
	
	@NotNull
	@Size(min=3, max=3)
	private String languageId;
	
	
}
