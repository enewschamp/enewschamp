package com.enewschamp.app.admin.promotion.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PromotionPageData extends PageData {
	private static final long serialVersionUID = 1L;
	@JsonInclude
	private Long id;

	@NotNull
	@JsonInclude
	private String editionId;

	@NotNull
	@JsonInclude
	private String couponCode;

	@NotNull
	@JsonInclude
	private LocalDate dateFrom;

	@NotNull
	@JsonInclude
	private LocalDate dateTo;

	@NotNull
	@JsonInclude
	private String countryId;

	@NotNull
	@JsonInclude
	private String stateId;

	@NotNull
	@JsonInclude
	private String cityId;

	@NotNull
	@JsonInclude
	private String promotionDetails;
	@JsonInclude
	private String description;
//	@JsonInclude
//	private String recordInUse;
//	@JsonInclude
//	private String operator;
//	@JsonInclude
//	private LocalDateTime lastUpdate;
}
