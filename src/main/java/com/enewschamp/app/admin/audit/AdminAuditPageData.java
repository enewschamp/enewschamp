package com.enewschamp.app.admin.audit;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminAuditPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private JsonNode records;
}
