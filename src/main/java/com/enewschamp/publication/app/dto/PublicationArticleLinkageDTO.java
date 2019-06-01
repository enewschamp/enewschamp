package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PublicationArticleLinkageDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -2930184093115840741L;


	private long id;
	
	private long publicationId = 0L;
	
	@NotNull
	private long newsArticleId = 0L; 
	
	@NotNull
	private int sequence;
	
}
