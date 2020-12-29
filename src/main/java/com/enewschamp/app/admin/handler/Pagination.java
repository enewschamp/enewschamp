package com.enewschamp.app.admin.handler;

import com.enewschamp.app.common.PageStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pagination {
	private int pageNumber;
	private int pageSize;
	private PageStatus isLastPage;
}
