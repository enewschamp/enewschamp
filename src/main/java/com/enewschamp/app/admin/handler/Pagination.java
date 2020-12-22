package com.enewschamp.app.admin.handler;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pagination {
	private int pageNumber;
	private int pageSize;
	private boolean isLastPage;
}
