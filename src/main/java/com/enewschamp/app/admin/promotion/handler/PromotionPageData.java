package com.enewschamp.app.admin.promotion.handler;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PromotionPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long promotionId;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	@JsonInclude
	private String editionId;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EDITION_ID_NOT_EMPTY)
	@JsonInclude
	private String couponCode;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@JsonInclude
	private LocalDate dateFrom;

	@NotNull(message = MessageConstants.EDITION_ID_NOT_NULL)
	@JsonInclude
	private LocalDate dateTo;

	@JsonInclude
	private String countryId;

	@JsonInclude
	private String stateId;

	@JsonInclude
	private String cityId;

	@NotNull(message = MessageConstants.PROMOTION_DETAIL_NOT_NULL)
	@NotEmpty(message = MessageConstants.PROMOTION_DETAIL_NOT_EMPTY)
	@JsonInclude
	private String promotionDetails;
	
	@JsonInclude
	@NotNull(message = MessageConstants.DESCRIPTION_NOT_NULL)
	@NotEmpty(message = MessageConstants.DESCRIPTION_NOT_EMPTY)
	private String description;
}
