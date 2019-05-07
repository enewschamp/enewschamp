package com.enewschamp.publication.app.dto;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PublicationArticleDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -2930184093115840741L;

	private PublicationArticleKeyDTO publicationArticleLinkageKey; 
	
	@NotNull
	private int sequence;
	
}
