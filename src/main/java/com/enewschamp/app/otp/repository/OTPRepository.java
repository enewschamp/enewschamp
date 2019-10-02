package com.enewschamp.app.otp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.otp.entity.OTP;

public interface OTPRepository extends JpaRepository<OTP, Long>{

	@Query("Select o from OTP where o.otpId= (select max(otpId) from OTP oo where oo.emailId= :emailId and oo.verified <> 'Y'")
	public Optional<OTP> getOtpForEmail(@Param("emailId") String emailId);
	
}
