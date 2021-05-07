package com.enewschamp.app.admin.student.payment.failed.handler;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPaymentFailedPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private Long paymentId = 0L;
	private Long studentId = 0L;
	private String editionId;
	private String subscriptionType;
	private String subscriptionId;
	private String subscriptionPeriod;
	private LocalDate subscriptionEndDate;
	private String paymentCurrency;
	private Double paymentAmount;
	private String orderId;
	private String finalOrderStatus;
	private LocalDateTime paytmCallbackRespTime;
	private String paytmChecksumHash;
	private String paytmGatewayName;
	private String paytmRespCode;
	private String paytmRespMsg;
	private String paytmBankName;
	private String paytmPaymentMode;
	private String paytmTxnId;
	private String paytmTxnAmount;
	private String paytmStatus;
	private String paytmBankTxnId;
	private String paytmTxnDate;
	private String initTranApiRequest;
	private String initTranApiResponse;
	private String tranStatusApiRequest;
	private String tranStatusApiResponse;
}
