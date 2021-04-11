package com.enewschamp.user.app.dto;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSearchData extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	private String orderSearchType;

	private String orderSearchStatus;

	private String merchantOrderId;

	private String payMode;

	private String isSort;

	private String fromDate;

	private String toDate;

	private String pageNumber;

	private String pageSize;
}
