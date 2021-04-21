package com.enewschamp.app.admin.handler;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operatorId", "lastUpdate", "recordInUse" })
public class ListPageData extends PageData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Pagination pagination;
	private AdminSearchRequest filter;

}
