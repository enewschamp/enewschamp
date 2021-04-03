package com.enewschamp.user.app.dto;

import com.enewschamp.app.common.MaintenanceDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RefundOrders extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	private String orderId;

	private String merchantRefundRequestTimeStamp;

	private String acceptRefundTimeStamp;

	private String txnTimeStamp;

	private String acceptRefundStatus;

	private String txnAmount;

	private String refundId;

	private String refundAmount;

	private String refId;
}
