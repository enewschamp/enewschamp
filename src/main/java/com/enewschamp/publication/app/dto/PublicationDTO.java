package com.enewschamp.publication.app.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.AbstractDTO;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.enewschamp.publication.domain.common.PublicationStatusType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationDTO extends AbstractDTO {

	private static final long serialVersionUID = -1938738416579485109L;

	private long publicationId;

	@NotNull
	@Size(max = 6)
	private String editionId;

	@NotNull
	private int readingLevel;

	@NotNull
	private LocalDate publishDate;

	private PublicationStatusType status;

	@Size(max = ForeignKeyColumnLength.UserId)
	private String editorId;

	@Size(max = ForeignKeyColumnLength.UserId)
	private String publisherId;

	@Column(name = "Comments")
	@Lob
	private String comments;
	
	private Set<PublicationArticleDTO> articles = new HashSet<PublicationArticleDTO>();

}
