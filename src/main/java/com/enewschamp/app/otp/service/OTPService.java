package com.enewschamp.app.otp.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.otp.dto.OTPDTO;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.app.otp.repository.OTPRepository;
import com.enewschamp.app.smtp.service.EmailService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class OTPService {

	@Autowired
	OTPRepository repository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	EmailService emailService;
	@Autowired
	EnewschampApplicationProperties appConfig;

	public OTPDTO genOTP(final String emailId) {
		// generate a unique number..

		// Long uniqueNo = 123456L;
		Long uniqueNo = Math.round(Math.random() * 1000000);

		OTP otp = new OTP();
		otp.setEmailId(emailId);
		otp.setOtpGenTime(LocalDateTime.now());
		otp.setOtp(uniqueNo);
		otp.setRecordInUse(RecordInUseType.Y);
		otp.setOperatorId(emailId);

		otp = repository.save(otp);

		OTPDTO otpDto = modelMapper.map(otp, OTPDTO.class);
		boolean sendSuccess = emailService.sendOTP("" + uniqueNo, emailId);
		if (!sendSuccess) {
			throw new BusinessException(ErrorCodes.EMAIL_NOT_SENT, emailId);
		}

		return otpDto;

	}

	public boolean validateOtp(final Long otp, final String emailId) {
		boolean validOtp = false;
		Optional<OTP> data = repository.getOtpForEmail(emailId);
		OTP otpEntity=null;
		if (!data.isPresent()) {
			throw new BusinessException(ErrorCodes.NO_RECORD_FOUND, "No Record Found");

		} else {
			otpEntity = data.get();
			
			Long otpExisting = otpEntity.getOtp();
			if (otpExisting == otp) {
				LocalDateTime currentTime = LocalDateTime.now();
				LocalDateTime otpDateTime = otpEntity.getOtpGenTime();
				otpDateTime.plusSeconds(appConfig.getOtpExpirySecs());
				if (otpDateTime.isAfter(currentTime)) {
					throw new BusinessException(ErrorCodes.OTP_EXPIRED, "OTP Has Expired");

				}
				validOtp = true;
				otpEntity.setVerified("Y");
				otpEntity.setVerificationTime(LocalDateTime.now());
				
			}
		}
		return validOtp;
	}
}
