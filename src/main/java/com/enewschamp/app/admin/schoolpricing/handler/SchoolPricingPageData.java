package com.enewschamp.app.admin.schoolpricing.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolPricingPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long schoolPricingId;

	@NotNull(message = MessageConstants.INSTITUTION_ID_NOT_NULL)
	private Long institutionId;

	@NotNull(message = MessageConstants.INSTITUTION_TYPE_NOT_NULL)
	@NotEmpty(message = MessageConstants.INSTITUTION_TYPE_NOT_EMPTY)
	private String institutionType;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	private String editionId;

	@NotNull(message = MessageConstants.EFFECTIVE_DATE_NOT_NULL)
	private LocalDate effectiveDate;

	@NotNull(message = MessageConstants.PRICING_DETAIL_NOT_NULL)
	@NotEmpty(message = MessageConstants.PRICING_DETAIL_NOT_EMPTY)
	private String pricingDetails;

}
