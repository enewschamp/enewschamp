package com.enewschamp.publication.app.dto;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.enewschamp.publication.domain.common.PublicationActionType;
import com.enewschamp.publication.domain.common.PublicationStatusType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -1938738416579485109L;

	private long publicationId;
	
	@NotNull
	private long publicationGroupId;

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
	
	@NotNull
	private PublicationActionType currentAction;

	private List<PublicationArticleLinkageDTO> articleLinkages;
	
	public PublicationDTO() {
		super();
	}

	public PublicationDTO(long publicationId, long publicationGroupId, String editionId, int readingLevel, LocalDate publishDate,
			PublicationStatusType status, String editorId, String publisherId) {
		super();
		this.publicationId = publicationId;
		this.publicationGroupId = publicationGroupId;
		this.editionId = editionId;
		this.readingLevel = readingLevel;
		this.publishDate = publishDate;
		this.status = status;
		this.editorId = editorId;
		this.publisherId = publisherId;
	}

}
