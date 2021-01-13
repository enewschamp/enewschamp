package com.enewschamp.app.admin.otp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.otp.entity.OTP;

public interface OTPRepositoryCustom {
	public Page<OTP> findOTPs(Pageable pageable, AdminSearchRequest searchRequest);
}

