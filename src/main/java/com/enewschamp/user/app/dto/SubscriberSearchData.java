package com.enewschamp.user.app.dto;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SubscriberSearchData extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	private String custId;

	private String subscriptionId;

	private boolean detailRequired;

	private String frequencyUnit;

	private String subsStatusFilter;
}
