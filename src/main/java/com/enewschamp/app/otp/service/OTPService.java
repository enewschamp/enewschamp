package com.enewschamp.app.otp.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.otp.repository.OTPRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.otp.dto.OTPDTO;
import com.enewschamp.app.otp.entity.OTP;
import com.enewschamp.app.otp.repository.OTPRepository;
import com.enewschamp.app.smtp.service.EmailService;
import com.enewschamp.app.user.login.entity.UserAction;
import com.enewschamp.app.user.login.entity.UserActivityTracker;
import com.enewschamp.app.user.login.service.UserLoginBusiness;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class OTPService {

	@Autowired
	OTPRepository repository;

	@Autowired
	OTPRepositoryCustom repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	EmailService emailService;

	@Autowired
	UserLoginBusiness userLoginBusiness;

	@Autowired
	PropertiesBackendService propertiesService;

	public OTPDTO genOTP(final String appName, final String emailId, UserActivityTracker userActivityTracker) {
		String uniqueNo = new DecimalFormat("000000").format(new Random().nextInt(999999));
		OTP otp = new OTP();
		otp.setEmailId(emailId);
		otp.setOtpGenTime(LocalDateTime.now());
		otp.setOtp(uniqueNo);
		otp.setRecordInUse(RecordInUseType.Y);
		otp.setOperatorId("SYSTEM");
		otp.setOperationDateTime(LocalDateTime.now());
		otp = repository.save(otp);
		OTPDTO otpDto = modelMapper.map(otp, OTPDTO.class);
		boolean sendSuccess = emailService.sendOTP(appName, "" + uniqueNo, emailId);
		if (!sendSuccess) {
			if (userActivityTracker != null) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
			}
			throw new BusinessException(ErrorCodeConstants.EMAIL_NOT_SENT, emailId);
		}
		return otpDto;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean validateOtp(final String appName, final String otp, final String emailId,
			UserActivityTracker userActivityTracker) {
		boolean validOtp = false;
		List<OTP> data = repository.getOtpForEmail(emailId, RecordInUseType.Y);
		if (data.size() == 0) {
			if (userActivityTracker != null) {
				userActivityTracker.setActionStatus(UserAction.FAILURE);
				userLoginBusiness.auditUserActivity(userActivityTracker);
			}
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		} else {
			OTP otpEntity = null;
			for (int i = 0; i < data.size(); i++) {
				otpEntity = data.get(i);
				if (Integer.valueOf(
						propertiesService.getValue(appName, PropertyConstants.OTP_VERIFY_MAX_ATTEMPTS)) <= otpEntity
								.getVerifyAttempts()) {
					if (userActivityTracker != null) {
						userActivityTracker.setActionStatus(UserAction.FAILURE);
						userLoginBusiness.auditUserActivity(userActivityTracker);
					}
					throw new BusinessException(ErrorCodeConstants.OTP_VERIFY_MAX_ATTEMPTS_EXHAUSTED);
				}
				String otpExisting = otpEntity.getOtp();
				Long otpId = otpEntity.getOtpId();
				if (otpExisting.equals(otp)) {
					LocalDateTime currentTime = LocalDateTime.now();
					LocalDateTime otpGenTime = otpEntity.getOtpGenTime();
					LocalDateTime otpExpiryTime = otpGenTime.plusSeconds(
							Integer.valueOf(propertiesService.getValue(appName, PropertyConstants.OTP_EXPIRY_SECS)));
					if (currentTime.isAfter(otpExpiryTime)) {
						if (userActivityTracker != null) {
							userActivityTracker.setActionStatus(UserAction.FAILURE);
							userLoginBusiness.auditUserActivity(userActivityTracker);
						}
						throw new BusinessException(ErrorCodeConstants.OTP_EXPIRED);
					}
					validOtp = true;
					otpEntity.setVerified("Y");
					otpEntity.setVerificationTime(LocalDateTime.now());
					for (int j = 0; j < data.size(); j++) {
						OTP otpEntityOld = data.get(j);
						if (otpId != otpEntityOld.getOtpId()) {
							otpEntityOld.setRecordInUse(RecordInUseType.N);
						}
					}
					break;
				} else {
					otpEntity.setVerifyAttempts((otpEntity.getVerifyAttempts() + 1));
				}
			}
		}
		return validOtp;
	}

	public OTP create(OTP otp) {
		OTP otpEntity = null;
		try {
			otpEntity = repository.save(otp);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return otpEntity;
	}

	public OTP update(OTP otp) {
		Long OTPId = otp.getOtpId();
		OTP existingOTP = get(OTPId);
		if (existingOTP.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(otp, existingOTP);
		return repository.save(existingOTP);
	}

	public OTP get(Long otpId) {
		Optional<OTP> existingEntity = repository.findById(otpId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.OTP_NOT_FOUND, String.valueOf(otpId));
		}
	}

	public OTP read(OTP otp) {
		Long OTPId = otp.getOtpId();
		OTP existingOTP = get(OTPId);
		return existingOTP;
	}

	public OTP close(OTP otp) {
		Long OTPId = otp.getOtpId();
		OTP existingOTP = get(OTPId);
		if (existingOTP.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingOTP.setRecordInUse(RecordInUseType.N);
		existingOTP.setOperationDateTime(null);
		return repository.save(existingOTP);
	}

	public OTP reInstate(OTP otp) {
		Long OTPId = otp.getOtpId();
		OTP existingOTP = get(OTPId);
		if (existingOTP.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingOTP.setRecordInUse(RecordInUseType.Y);
		existingOTP.setOperationDateTime(null);
		return repository.save(existingOTP);
	}

	public Page<OTP> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<OTP> userList = repositoryCustom.findOTPs(pageable, searchRequest);
		if (userList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return userList;
	}
}
