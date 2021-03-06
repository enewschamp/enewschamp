package com.enewschamp.user.app.dto;

import org.json.simple.JSONArray;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderListDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	private JSONArray orders;

	private OrderSearchData searchData;

	private String message;
}
