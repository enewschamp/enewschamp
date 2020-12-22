package com.enewschamp.app.otp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.domain.common.RecordInUseType;

public interface OTPRepository extends JpaRepository<OTP, Long> {

	@Query("Select o from OTP o where o.recordInUse=:recordInUse and o.emailId= :emailId and o.verified <> 'Y'")
	public List<OTP> getOtpForEmail(@Param("emailId") String emailId,
			@Param("recordInUse") RecordInUseType recordInUse);

	@Query("Select o from OTP o where o.recordInUse=:recordInUse and o.emailId= :emailId and o.verified <> 'Y' and date(o.otpGenTime)=current_date")
	public List<OTP> getOtpGenListForEmail(@Param("emailId") String emailId,
			@Param("recordInUse") RecordInUseType recordInUse);
}
