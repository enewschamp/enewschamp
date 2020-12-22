package com.enewschamp.app.admin.handler;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListPageData extends PageData {
	private Pagination pagination;
	private Filter filter;

}
