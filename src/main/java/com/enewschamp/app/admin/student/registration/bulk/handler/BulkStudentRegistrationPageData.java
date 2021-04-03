package com.enewschamp.app.admin.student.registration.bulk.handler;

import javax.validation.constraints.NotNull;

import com.enewschamp.app.admin.student.school.nonlist.handler.StudentSchoolNilDTO;
import com.enewschamp.app.common.MessageConstants;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BulkStudentRegistrationPageData extends PageData {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = MessageConstants.STUDENT_CONTROL_NOT_NULL)
	private StudentControlNilDTO studentControl;
	@NotNull(message = MessageConstants.STUDENT_REGISTRATION_NOT_NULL)
	private StudentRegistrationNilDTO studentRegistration;
	@NotNull(message = MessageConstants.STUDENT_SUBSCRIPTION_NOT_NULL)
	private StudentSubscriptionNilDTO studentSubscription;
	@NotNull(message = MessageConstants.STUDENT_PAYMENT_NOT_NULL)
	private StudentPaymentNilDTO studentPayment;
	@NotNull(message = MessageConstants.STUDENT_DETAILS_NOT_NULL)
	private StudentDetailsNilDTO studentDetails;
	@NotNull(message = MessageConstants.STUDENT_SCHOOL_NOT_NULL)
	private StudentSchoolNilDTO studentSchool;
	@NotNull(message = MessageConstants.STUDENT_PREFERENCES_NOT_NULL)
	private StudentPreferencesNilDTO studentPreferences;
}
