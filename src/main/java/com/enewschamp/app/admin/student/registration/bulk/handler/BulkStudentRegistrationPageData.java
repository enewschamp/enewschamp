package com.enewschamp.app.admin.student.registration.bulk.handler;

import com.enewschamp.app.common.PageData;
import com.enewschamp.app.student.registration.dto.StudentRegistrationDTO;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentPaymentDTO;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.app.dto.StudentSchoolDTO;
import com.enewschamp.subscription.app.dto.StudentSubscriptionDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BulkStudentRegistrationPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private StudentControlDTO studentControl;
	private StudentRegistrationDTO studentRegistration;
	private StudentSubscriptionDTO studentSubscription;
	private StudentPaymentDTO studentPayment;
	private StudentDetailsDTO studentDetails;
	private StudentSchoolDTO studentSchool;
	private StudentPreferencesDTO studentPreferences;
}
