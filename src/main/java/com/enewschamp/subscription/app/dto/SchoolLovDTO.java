package com.enewschamp.subscription.app.dto;

import java.util.List;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SchoolLovDTO extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String countryId;
	private String stateId;
	private String cityId;
	private List<CountryPageData> countryLOV;
	private List<StatePageData> stateLOV;
	private List<CityPageData> cityLOV;
	private List<SchoolData> schoolLOV;

}
