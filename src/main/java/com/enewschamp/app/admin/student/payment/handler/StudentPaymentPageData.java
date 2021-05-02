package com.enewschamp.app.admin.student.payment.handler;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentPaymentPageData extends PageData {
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
	private String paytmChecksumHash = null;
	private String paytmGatewayName = null;
	private String paytmRespCode = null;
    private	String paytmRespMsg = null;
    private	String paytmBankName = null;
	private String paytmPaymentMode = null;
	private String paytmTxnId = null;
	private String paytmTxnAmount = null;
	private String paytmStatus = null;
	private String paytmBankTxnId = null;
	private String paytmTxnDate = null;
	private String initTranApiRequest;
	private String initTranApiResponse;
	private String tranStatusApiRequest;
	private String tranStatusApiResponse;

}
