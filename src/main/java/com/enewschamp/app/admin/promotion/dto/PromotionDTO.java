package com.enewschamp.app.admin.promotion.dto;

import java.time.LocalDate;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PromotionDTO extends MaintenanceDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long promotionId;

	@NotNull
	private String editionId;

	@NotNull
	private String couponCode;

	@NotNull
	private LocalDate dateFrom;

	@NotNull
	private LocalDate dateTo;

	@NotNull
	private String countryId;

	@NotNull
	private String stateId;

	@NotNull
	private String cityId;

	@NotNull
	private String promotionDetails;

	@NotNull
	private String description;
}
