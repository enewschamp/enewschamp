package com.enewschamp.app.admin.student.refund.handler;

import com.enewschamp.app.common.PageData;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentRefundPageData extends PageData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long refundId;
	private Long studentId;
	private String orderId;
	private String refOrderId;
	private String refundAmount;
	private String paytmTxnId;
	private String refundPaytmTxnId;
	private String paytmStatus;
	private String finalStatus;
	private JsonNode initRefundApiRequest;
	private JsonNode initRefundApiResponse;
	private JsonNode refundStatusApiRequest;
	private JsonNode refundStatusApiResponse;

}
