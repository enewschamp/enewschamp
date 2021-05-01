package com.enewschamp.app.admin.otp.handler;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OTPPageData extends PageData {
	private static final long serialVersionUID = 1L;

	private Long otpId;

	@NotNull(message = MessageConstants.EMAIL_ID_NOT_NULL)
	@NotEmpty(message = MessageConstants.EMAIL_ID_NOT_EMPTY)
	private String emailId;

	private String phoneNumber;

	@NotNull(message = MessageConstants.OTP_NOT_NULL)
	@NotEmpty(message = MessageConstants.OTP_NOT_EMPTY)
	private String otp;

	private LocalDateTime otpGenTime;

	private String verified;

	private LocalDateTime verificationTime;

	@NotNull(message = MessageConstants.VERIFY_ATTEMPTS_NOT_NULL)
	private int verifyAttempts;
}
