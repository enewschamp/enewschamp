package com.enewschamp.publication.app.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.enewschamp.publication.domain.common.PublicationGroupStatusType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationGroupDTO extends MaintenanceDTO {

	private static final long serialVersionUID = 3270060263339800778L;

	private Long publicationGroupId;

	@NotNull
	@Size(max = ForeignKeyColumnLength.EditionId)
	private String editionId;

	@NotNull
	private LocalDate publicationDate;

	@JsonInclude
	@Size(max = ForeignKeyColumnLength.UserId)
	private String editorId;

	@JsonInclude
	@Size(max = ForeignKeyColumnLength.UserId)
	private String publisherId;

	@JsonInclude
	private PublicationGroupStatusType status;

	@JsonInclude
	private List<PublicationDTO> publications;

}
