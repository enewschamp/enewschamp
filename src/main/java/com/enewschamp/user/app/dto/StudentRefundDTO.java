package com.enewschamp.user.app.dto;

import javax.persistence.Convert;
import javax.persistence.Id;

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

	private String editionId;

	private String orderId;

	private String refOrderId;

	@Convert(converter = StringCryptoConverter.class)
	private String refundAmount;

	private String paytmTxnId;

	private String refundPaytmTxnId;

	private String paytmStatus;

	private String finalOrderStatus;

	private String message;
}
