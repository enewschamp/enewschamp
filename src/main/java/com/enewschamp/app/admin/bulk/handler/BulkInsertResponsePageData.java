package com.enewschamp.app.admin.bulk.handler;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "operatorId", "lastUpdate", "recordInUse" })
public class BulkInsertResponsePageData extends PageData {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private int numberOfRecords;

}
