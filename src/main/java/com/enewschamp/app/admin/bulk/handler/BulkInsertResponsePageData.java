package com.enewschamp.app.admin.bulk.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BulkInsertResponsePageData extends PageData {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numberOfRecords;

}
