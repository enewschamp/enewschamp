package com.enewschamp.app.admin.institution.handler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InstitutionAddressPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long addressId;

	@NotNull(message = MessageConstants.INSTITUTION_ID_NOT_NULL)
	private Long institutionId;

	@NotNull(message = MessageConstants.INSTITUTION_TYPE_NOT_NULL)
	@NotEmpty(message = MessageConstants.INSTITUTION_TYPE_NOT_EMPTY)
	private String institutionType;

	@NotNull(message = MessageConstants.ADDRESS_TYPE_NOT_NULL)
	@NotEmpty(message = MessageConstants.ADDRESS_TYPE_NOT_EMPTY)
	private String addressType;

	@NotNull(message = MessageConstants.STATE_NOT_NULL)
	@NotEmpty(message = MessageConstants.STATE_NOT_EMPTY)
	private String stateId;

	@NotNull(message = MessageConstants.COUNTRY_NOT_NULL)
	@NotEmpty(message = MessageConstants.COUNTRY_NOT_EMPTY)
	private String countryId;

	@NotNull(message = MessageConstants.CITY_NOT_NULL)
	@NotEmpty(message = MessageConstants.CITY_NOT_EMPTY)
	private String cityId;

	private String address;
	private String pinCode;
	private String landLine1;
	private String landLine2;
	private String landLine3;
	private String mobile1;
	private String mobile2;
	private String mobile3;
	private String email1;
	private String email2;
	private String email3;
	private String comments;

}
