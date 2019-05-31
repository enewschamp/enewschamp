package com.enewschamp.article.app.dto;

import com.enewschamp.app.dto.AuditDTO;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleQuizAuditDTO extends AuditDTO {
	
	private static final long serialVersionUID = -1715262515801610375L;

	private JsonNode objectDTO;

}
