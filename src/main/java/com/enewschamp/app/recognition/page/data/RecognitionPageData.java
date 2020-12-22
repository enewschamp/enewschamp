package com.enewschamp.app.recognition.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PaginationData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RecognitionPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonInclude
	private List<RecognitionData> recognitions;

	@JsonInclude
	private PaginationData pagination;

}
