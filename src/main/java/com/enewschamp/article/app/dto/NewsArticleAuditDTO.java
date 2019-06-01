package com.enewschamp.article.app.dto;

import com.enewschamp.app.dto.AuditDTO;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class NewsArticleAuditDTO extends AuditDTO {
	
	private static final long serialVersionUID = 776328071980637866L;	

	private JsonNode objectDTO;
	
}
