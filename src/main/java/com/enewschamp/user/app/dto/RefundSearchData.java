package com.enewschamp.user.app.dto;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RefundSearchData extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	private String isSort;

	private String startDate;

	private String endDate;

	private String pageNumber;

	private String pageSize;
}
