package com.enewschamp.user.domain.entity;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "StudentRefunds")
public class StudentRefund extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7063853831579952336L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_refunds_id_generator")
	@SequenceGenerator(name = "student_refunds_id_generator", sequenceName = "student_refunds_id_seq", allocationSize = 1)
	@Column(name = "refundId", updatable = false, nullable = false)
	private Long refundId;

	@Column(name = "studentId")
	private Long studentId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@Column(name = "orderId")
	private String orderId;

	@Column(name = "refOrderId")
	private String refOrderId;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "refundAmount")
	private String refundAmount;

	@Column(name = "paytmTxnId")
	private String paytmTxnId;

	@Column(name = "refundPaytmTxnId")
	private String refundPaytmTxnId;

	@Column(name = "paytmStatus")
	private String paytmStatus;

	@Column(name = "finalOrderStatus")
	private String finalOrderStatus;

	@Lob
	@Column(name = "initRefundApiRequest")
	private Blob initRefundApiRequest;

	@Column(name = "initRefundApiResponse")
	private Blob initRefundApiResponse;

	@Column(name = "refundStatusApiRequest")
	private Blob refundStatusApiRequest;

	@Column(name = "refundStatusApiResponse")
	private Blob refundStatusApiResponse;
}
