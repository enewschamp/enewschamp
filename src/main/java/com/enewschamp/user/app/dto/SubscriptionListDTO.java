package com.enewschamp.user.app.dto;

import org.json.simple.JSONArray;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SubscriptionListDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	private JSONArray subscriptionDetails;

	private SubscriberSearchData searchData;

	private String message;
}
