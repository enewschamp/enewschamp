package com.enewschamp.app.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaginationData implements Serializable {

	@JsonInclude
	private int pageNumber;

	@JsonInclude
	private int pageSize;
}
