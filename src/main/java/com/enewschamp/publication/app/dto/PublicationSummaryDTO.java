package com.enewschamp.publication.app.dto;

import java.time.LocalDate;

import com.enewschamp.app.common.AbstractDTO;
import com.enewschamp.publication.domain.common.PublicationStatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PublicationSummaryDTO extends AbstractDTO {

	private static final long serialVersionUID = -4590288574254659259L;

	private Long publicationId;
	private Integer readingLevel;
	private PublicationStatusType status;
	private LocalDate publicationDate;
	private Long publicationGroupId;
	private String editionId;
	private String editorId;
	private String publisherId;

}
