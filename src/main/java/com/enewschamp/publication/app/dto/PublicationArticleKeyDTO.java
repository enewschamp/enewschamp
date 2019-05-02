package com.enewschamp.publication.app.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PublicationArticleKeyDTO implements Serializable {
	
	private static final long serialVersionUID = -6932838078545636727L;

	@NotNull
	private long publicationId = 0L;
	
	@NotNull
	private long newsArticleId = 0L;
	
}
