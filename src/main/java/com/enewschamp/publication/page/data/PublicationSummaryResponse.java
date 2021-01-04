package com.enewschamp.publication.page.data;

import java.util.List;

import com.enewschamp.app.common.PageData;
import com.enewschamp.publication.app.dto.PublicationDailySummaryDTO;
import com.enewschamp.publication.app.dto.PublicationMonthlySummaryDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PublicationSummaryResponse extends PageData {

	private static final long serialVersionUID = -4120218181123020107L;

	private Integer pageNumber;
	private Integer pageSize;
	private Integer pageCount;
	private Integer recordCount;
	private Boolean isLastPage;

	private List<PublicationDailySummaryDTO> dailySummary;
	private List<PublicationMonthlySummaryDTO> monthlySummary;

}
