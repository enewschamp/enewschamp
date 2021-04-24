package com.enewschamp.app.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaginationData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private int pageNumber;

	@JsonInclude
	private int pageSize;

	@JsonInclude
	private String isLastPage;
}
