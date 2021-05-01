package com.enewschamp.subscription.domain.entity;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.DoubleCryptoConverter;
import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentPayment")
public class StudentPayment extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_payment_id_generator")
	@SequenceGenerator(name = "student_payment_id_generator", sequenceName = "student_payment_id_seq", allocationSize = 1)
	private Long paymentId = 0L;

	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId = 0L;
	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;
	@NotNull
	@Column(name = "subscriptionType", length = 1)
	private String subscriptionType;

	@Column(name = "subscriptionId", length = 100)
	private String subscriptionId;

	@NotNull
	@Column(name = "subscriptionPeriod", length = 100)
	private String subscriptionPeriod;

	@Column(name = "subscriptionEndDate")
	private LocalDate subscriptionEndDate;

	@Column(name = "paymentCurrency", length = 4)
	private String paymentCurrency;

	@Convert(converter = DoubleCryptoConverter.class)
	@Column(name = "paymentAmount")
	private Double paymentAmount;

	@Column(name = "orderId")
	private String orderId;

	@Column(name = "finalOrderStatus")
	private String finalOrderStatus;

	@Column(name = "paytmCallbackRespTime")
	private LocalDateTime paytmCallbackRespTime;

	@Column(name = "paytmChecksumHash")
	String paytmChecksumHash = null;

	@Column(name = "paytmGatewayName")
	String paytmGatewayName = null;

	@Column(name = "paytmRespCode")
	String paytmRespCode = null;

	@Column(name = "paytmRespMsg")
	String paytmRespMsg = null;

	@Column(name = "paytmBankName")
	String paytmBankName = null;

	@Column(name = "paytmPaymentMode")
	String paytmPaymentMode = null;

	@Column(name = "paytmTxnId")
	String paytmTxnId = null;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "paytmTxnAmount")
	String paytmTxnAmount = null;

	@Column(name = "paytmStatus")
	String paytmStatus = null;

	@Column(name = "paytmBankTxnId")
	String paytmBankTxnId = null;

	@Column(name = "paytmTxnDate")
	String paytmTxnDate = null;

	@Column(name = "initTranApiRequest")
	private Blob initTranApiRequest;

	@Column(name = "initTranApiResponse")
	private Blob initTranApiResponse;

	@Column(name = "tranStatusApiRequest")
	private Blob tranStatusApiRequest;

	@Column(name = "tranStatusApiResponse")
	private Blob tranStatusApiResponse;
}