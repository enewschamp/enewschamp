package com.enewschamp.app.student.registration.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.otp.service.OTPService;
import com.enewschamp.app.student.registration.dto.StudentRegistrationDTO;
import com.enewschamp.app.student.registration.entity.RegistrationStatus;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.app.student.registration.service.StudentRegistrationService;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentRegistrationBusiness {

	@Autowired
	StudentRegistrationService regService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	OTPService otpService;
	
	public StudentRegistrationDTO register(final String emailId, final String password) {
		
		StudentRegistrationDTO studentDTO = new StudentRegistrationDTO();
		studentDTO.setEmailId(emailId);
		studentDTO.setPassword(password);
		studentDTO.setOperatorId("SYSTEM");
		studentDTO.setStatus(RegistrationStatus.A);
		
		
		// check if Email ID already exists..
		if (regService.userExists(studentDTO.getEmailId())) {
			throw new BusinessException(ErrorCodes.STUD_ALREADY_REGISTERED, "Student Already Registered");
		}
		// check password length..
		if (studentDTO.getPassword().length() > 20 || studentDTO.getPassword().length() < 8) {
			throw new BusinessException(ErrorCodes.INVALID_PWD_LEN, "Password Length must be between 8 and 20");

		}

		StudentRegistration student = modelMapper.map(studentDTO, StudentRegistration.class);
		student.setRecordInUse(RecordInUseType.Y);
		
		student = regService.create(student);
		studentDTO = modelMapper.map(student, StudentRegistrationDTO.class);
		return studentDTO;

	}

	public void logout(final String emailId) {
		StudentRegistration student = regService.getStudentReg(emailId);
		if (!student.getStatus().toString().equalsIgnoreCase("A")) {
			throw new BusinessException(ErrorCodes.STUD_IS_INACTIVE, "User Is is inactive");
		}
		student.setLoginFlag(AppConstants.NO);
		regService.update(student);

	}

	public void login(final String emailId) {
		StudentRegistration student = regService.getStudentReg(emailId);
		if (!student.getStatus().toString().equalsIgnoreCase("A")) {
			throw new BusinessException(ErrorCodes.STUD_IS_INACTIVE, "User Is is inactive");
		}
		student.setLoginFlag(AppConstants.YES);
		regService.update(student);

	}

	public boolean validatePassword(final String emailId, final String password) {
		StudentRegistration student = regService.getStudentReg(emailId);
		if (!student.getStatus().toString().equalsIgnoreCase("A")) {
			throw new BusinessException(ErrorCodes.STUD_IS_INACTIVE, "User Is is inactive");
		}
		boolean isValid = false;
		String studPass = student.getPassword();
		if (!studPass.equals(password)) {
			isValid = false;
			throw new BusinessException(ErrorCodes.INVALID_EMAILID_OR_PASSWORD, "Invalid User Id Or Password");
		} else {
			isValid = true;
		}
		return isValid;
	}

	public void deleteAccount(final String emailId) {
		StudentRegistration student = regService.getStudentReg(emailId);
		if (!student.getStatus().toString().equalsIgnoreCase("A")) {
			throw new BusinessException(ErrorCodes.STUD_IS_INACTIVE, "User Is is inactive");
		}
		student.setStatus(RegistrationStatus.D);
		regService.update(student);

	}

	public void resetPassword(final String emailId, final String password) {
		// check password length..
		if (password.length() > 20 || password.length() < 8) {
			throw new BusinessException(ErrorCodes.INVALID_PWD_LEN, "Password Length must be between 8 and 20");

		}

		StudentRegistration student = regService.getStudentReg(emailId);
		if (!student.getStatus().toString().equalsIgnoreCase("A")) {
			throw new BusinessException(ErrorCodes.STUD_IS_INACTIVE, "User Is is inactive");
		}
	
		student.setPassword(password);
		regService.update(student);

	}
	
	public void sendOtp(final String emailId)
	{
		//StudentRegistration student = regService.getStudentReg(emailId);
		//if (!student.getStatus().toString().equalsIgnoreCase("A")) {
		//	throw new BusinessException(ErrorCodes.STUD_IS_INACTIVE, "User Is is inactive");
		//}
		
		otpService.genOTP(emailId);
		
	}
	
	public boolean validateOtp(final String emailId, final Long otp)
	{
		/*StudentRegistration student = regService.getStudentReg(emailId);
		if (!student.getStatus().toString().equalsIgnoreCase("A")) {
			throw new BusinessException(ErrorCodes.STUD_IS_INACTIVE, "User Is is inactive");
		}*/
		
		return otpService.validateOtp(otp, emailId);
	}

}
