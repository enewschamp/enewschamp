package com.enewschamp.app.champs.page.data;

import java.util.List;

import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.PageData;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChampsPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private CommonFilterData filter;
	@JsonInclude
	private List<ChampStudentDTO> champs;
	@JsonInclude
	private PaginationData pagination;
}
