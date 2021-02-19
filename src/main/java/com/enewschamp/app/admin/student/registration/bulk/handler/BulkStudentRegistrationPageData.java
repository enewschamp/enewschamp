package com.enewschamp.app.admin.student.registration.bulk.handler;

import com.enewschamp.app.admin.student.school.nonlist.handler.StudentSchoolNilDTO;
import com.enewschamp.app.common.PageData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BulkStudentRegistrationPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private StudentControlNilDTO studentControl;
	private StudentRegistrationNilDTO studentRegistration;
	private StudentSubscriptionNilDTO studentSubscription;
	private StudentPaymentNilDTO studentPayment;
	private StudentDetailsNilDTO studentDetails;
	private StudentSchoolNilDTO studentSchool;
	private StudentPreferencesNilDTO studentPreferences;
}
