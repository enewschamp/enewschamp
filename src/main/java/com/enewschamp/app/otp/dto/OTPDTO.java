package com.enewschamp.app.otp.dto;

import java.time.LocalDateTime;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OTPDTO extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long otpId;
	private String emailId;
	private String phoneNo;
	private Long otp;
	private LocalDateTime otpGenTime;

}
