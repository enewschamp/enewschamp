package com.enewschamp.user.app.dto;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.enewschamp.app.common.MaintenanceDTO;
import com.enewschamp.app.common.StringCryptoConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentRefundDTO extends MaintenanceDTO {

	private static final long serialVersionUID = -2818257501052075083L;

	@Id
	private Long refundId;

	private Long studentId;

	private String orderId;

	private String refOrderId;

	@Convert(converter = StringCryptoConverter.class)
	private String refundAmount;

	private String paytmTxnId;

	private String refundPaytmTxnId;

	private String paytmStatus;

	private String finalStatus;

	private String message;
}
