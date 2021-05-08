package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.enewschamp.app.admin.AdminConstant;
import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.school.nonlist.handler.StudentSchoolNilDTO;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.domain.service.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.subscription.domain.entity.StudentPaymentFailed;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.entity.StudentSubscription;
import com.enewschamp.subscription.domain.entity.StudentSubscriptionHistory;
import com.enewschamp.user.domain.entity.StudentRefund;

import lombok.SneakyThrows;

@Repository
public class BulkStudentRegistrationRepositoryCustomImpl extends RepositoryImpl implements AdminConstant {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	ModelMapper modelMapper;

	public Page<BulkStudentRegistrationPageData> findAll(Pageable pageable, AdminSearchRequest searchRequest) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = cb.createQuery(Tuple.class);
		Root<StudentControl> studentControlRoot = criteriaQuery.from(StudentControl.class);
		Root<StudentDetails> studentDetailsRoot = criteriaQuery.from(StudentDetails.class);
		Root<StudentSchool> studentSchoolRoot = criteriaQuery.from(StudentSchool.class);
		Root<StudentPreferences> studentPreferencesRoot = criteriaQuery.from(StudentPreferences.class);
		Root<StudentSubscription> studentSubscriptionRoot = criteriaQuery.from(StudentSubscription.class);
		Root<StudentRegistration> studentRegistrationRoot = criteriaQuery.from(StudentRegistration.class);
		Root<StudentSubscriptionHistory> studentSubscriptionHistoryRoot = criteriaQuery
				.from(StudentSubscriptionHistory.class);
		Root<StudentPayment> studentPaymentRoot = criteriaQuery.from(StudentPayment.class);
		Root<StudentPaymentFailed> studentPaymentFailedRoot = criteriaQuery.from(StudentPaymentFailed.class);
		Root<StudentRefund> studentRefundRoot = criteriaQuery.from(StudentRefund.class);
		List<Predicate> filterPredicates = new ArrayList<>();
		filterPredicates
				.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentSubscriptionRoot.get(STUDENT_ID)));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentPreferencesRoot.get(STUDENT_ID)));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentSchoolRoot.get(STUDENT_ID)));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentDetailsRoot.get(STUDENT_ID)));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentControlRoot.get(STUDENT_ID)));
		filterPredicates
				.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentSubscriptionHistoryRoot.get(STUDENT_ID)));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentPaymentRoot.get(STUDENT_ID)));
		filterPredicates
				.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentPaymentFailedRoot.get(STUDENT_ID)));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), studentRefundRoot.get(STUDENT_ID)));

		// Apply the filter Criterias
		mapStudentRegistrationPredicate(searchRequest, studentRegistrationRoot, cb, filterPredicates);
		mapStudentControlPredicate(searchRequest, studentControlRoot, cb, filterPredicates);
		mapStudentDetailsPredicate(searchRequest, studentDetailsRoot, cb, filterPredicates);
		mapStudentSchoolPredicate(searchRequest, studentSchoolRoot, cb, filterPredicates);
		mapStudentPreferencePredicate(searchRequest, studentPreferencesRoot, cb, filterPredicates);
		mapStudentSubscriptionPredicate(searchRequest, studentSubscriptionRoot, cb, filterPredicates);
		mapStudentSubscriptionHistoryPredicate(searchRequest, studentSubscriptionHistoryRoot, cb, filterPredicates);
		mapStudentPaymentPredicate(searchRequest, studentPaymentRoot, cb, filterPredicates);
		mapStudentPaymentFailedPredicate(searchRequest, studentPaymentFailedRoot, cb, filterPredicates);
		mapStudentRefundPredicate(searchRequest, studentRefundRoot, cb, filterPredicates);

		criteriaQuery
				.select(cb.tuple(studentRegistrationRoot, studentControlRoot, studentDetailsRoot, studentSchoolRoot,
						studentPreferencesRoot, studentSubscriptionRoot, studentSubscriptionHistoryRoot,
						studentPaymentRoot, studentPaymentFailedRoot, studentRefundRoot))
				.where((Predicate[]) filterPredicates.toArray(new Predicate[0]));

		TypedQuery<Tuple> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		List<Tuple> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentRegistrationRoot);

		List<BulkStudentRegistrationPageData> bulkList = new ArrayList<BulkStudentRegistrationPageData>();
		for (Tuple t : list) {
			BulkStudentRegistrationPageData pageData = new BulkStudentRegistrationPageData();
			StudentRegistration studentRegistration = (StudentRegistration) t.get(0);
			StudentControl studentControl = (StudentControl) t.get(1);
			StudentDetails studentDetails = (StudentDetails) t.get(2);
			StudentSchool studentSchool = (StudentSchool) t.get(3);
			StudentPreferences studentPreference = (StudentPreferences) t.get(4);
			StudentSubscription studentSubscription = (StudentSubscription) t.get(5);
			StudentSubscriptionHistory studentSubscriptionHistory = (StudentSubscriptionHistory) t.get(6);
			StudentPayment studentPayment = (StudentPayment) t.get(7);
			StudentPaymentFailed studentPaymentFailed = (StudentPaymentFailed) t.get(8);
			StudentRefund studentRefund = (StudentRefund) t.get(9);

			pageData.setStudentRegistration(modelMapper.map(studentRegistration, StudentRegistrationNilDTO.class));
			pageData.getStudentRegistration().setLastUpdate(studentRegistration.getOperationDateTime());
			
			pageData.setStudentSubscription(modelMapper.map(studentSubscription, StudentSubscriptionNilDTO.class));
			pageData.getStudentSubscription().setLastUpdate(studentSubscription.getOperationDateTime());
			
			pageData.setStudentPreferences(modelMapper.map(studentPreference, StudentPreferencesNilDTO.class));
			mapStudentPreferencesPageData(pageData, studentPreference);
			
			pageData.setStudentSchool(modelMapper.map(studentSchool, StudentSchoolNilDTO.class));
			pageData.getStudentSchool().setLastUpdate(studentSchool.getOperationDateTime());
			
			pageData.setStudentDetails(modelMapper.map(studentDetails, StudentDetailsNilDTO.class));
			pageData.getStudentDetails().setLastUpdate(studentSchool.getOperationDateTime());
			
			pageData.setStudentControl(modelMapper.map(studentControl, StudentControlNilDTO.class));
			pageData.getStudentControl().setLastUpdate(studentControl.getOperationDateTime());
			
			pageData.setStudentSubscriptionHistory(
					modelMapper.map(studentSubscriptionHistory, StudentSubscriptionHistoryNilDTO.class));
			pageData.getStudentSubscriptionHistory().setLastUpdate(studentSubscriptionHistory.getOperationDateTime());
			
			pageData.setStudentPayment(modelMapper.map(studentPayment, StudentPaymentNilDTO.class));
			mapStudentPaymentPageData(pageData, studentPayment);
			
			pageData.setStudentPaymentFailed(modelMapper.map(studentPaymentFailed, StudentPaymentFailedNilDTO.class));
			mapStudentPaymentFailedPageData(pageData, studentPaymentFailed);
			
			pageData.setStudentRefund(modelMapper.map(studentRefund, StudentRefundNilDTO.class));
			mapStudentRefundPageData(pageData, studentRefund);
			
			bulkList.add(pageData);
		}
		return new PageImpl<>(bulkList, pageable, count);
	}

	private void mapStudentPreferencesPageData(BulkStudentRegistrationPageData pagedata,
			StudentPreferences studentPreference) {
		if (studentPreference.getCommsOverEmail() != null) {
			pagedata.getStudentPreferences()
					.setAlertsNotifications(studentPreference.getCommsOverEmail().getAlertsNotifications());
			pagedata.getStudentPreferences().setCommsEmailId(studentPreference.getCommsOverEmail().getCommsEmailId());
			pagedata.getStudentPreferences()
					.setDailyPublication(studentPreference.getCommsOverEmail().getDailyPublication());
			pagedata.getStudentPreferences()
					.setScoresProgressReports(studentPreference.getCommsOverEmail().getScoresProgressReports());
		}
		if (studentPreference.getChampPermissions() != null) {
			pagedata.getStudentPreferences().setChampCity(studentPreference.getChampPermissions().getChampCity());
			pagedata.getStudentPreferences()
					.setChampProfilePic(studentPreference.getChampPermissions().getChampProfilePic());
			pagedata.getStudentPreferences().setChampSchool(studentPreference.getChampPermissions().getChampSchool());
		}
		pagedata.getStudentPreferences().setLastUpdate(studentPreference.getOperationDateTime());
	}

	private void mapStudentRegistrationPredicate(AdminSearchRequest searchRequest,
			Root<StudentRegistration> studentRegistrationRoot, CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getEmailId()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get(EMAIL_ID), searchRequest.getEmailId()));

		if (!StringUtils.isEmpty(searchRequest.getAvatar()) && searchRequest.getAvatar().equals("Y")) {
			filterPredicates.add(cb.and(cb.isNotNull(studentRegistrationRoot.get(AVATAR_NAME)),
					cb.notEqual(cb.trim(studentRegistrationRoot.get(AVATAR_NAME)), "")));
		}
		if (!StringUtils.isEmpty(searchRequest.getAvatar()) && searchRequest.getAvatar().equals("N")) {
			filterPredicates.add(cb.or(cb.isNull(studentRegistrationRoot.get(AVATAR_NAME)),
					cb.equal(cb.trim(studentRegistrationRoot.get(AVATAR_NAME)), "")));
		}

		if (!StringUtils.isEmpty(searchRequest.getPhoto()) && searchRequest.getPhoto().equals("Y")) {
			filterPredicates.add(cb.and(cb.isNotNull(studentRegistrationRoot.get(PHOTO_NAME)),
					cb.notEqual(cb.trim(studentRegistrationRoot.get(PHOTO_NAME)), "")));
		}
		if (!StringUtils.isEmpty(searchRequest.getPhoto()) && searchRequest.getPhoto().equals("N")) {
			filterPredicates.add(cb.or(cb.isNull(studentRegistrationRoot.get(PHOTO_NAME)),
					cb.equal(cb.trim(studentRegistrationRoot.get(PHOTO_NAME)), "")));
		}

		if (!StringUtils.isEmpty(searchRequest.getTheme()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get(THEME), searchRequest.getTheme()));

		if (!StringUtils.isEmpty(searchRequest.getIsAccountLocked()))
			filterPredicates
					.add(cb.equal(studentRegistrationRoot.get(IS_ACCOUNT_LOCKED), searchRequest.getIsAccountLocked()));

		if (!StringUtils.isEmpty(searchRequest.getIsActive()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get(IS_ACTIVE), searchRequest.getIsActive()));

		if (!StringUtils.isEmpty(searchRequest.getIsDeleted()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get(IS_DELETED), searchRequest.getIsDeleted()));

		if (!StringUtils.isEmpty(searchRequest.getLastSuccessLoginTimeFrom())
				&& !StringUtils.isEmpty(searchRequest.getLastSuccessLoginTimeTo()))
			filterPredicates.add(cb.between(studentRegistrationRoot.get(LAST_SUCCESS_LOGIN_ATTEMPT),
					searchRequest.getLastSuccessLoginTimeFrom(), searchRequest.getLastSuccessLoginTimeTo()));

		if (!StringUtils.isEmpty(searchRequest.getLastUnSuccessLoginTimeFrom())
				&& !StringUtils.isEmpty(searchRequest.getLastUnSuccessLoginTimeTo()))
			filterPredicates.add(cb.between(studentRegistrationRoot.get(LAST_UNSUCCESS_LOGIN_ATTEMPT),
					searchRequest.getLastUnSuccessLoginTimeFrom(), searchRequest.getLastUnSuccessLoginTimeTo()));

		if (!StringUtils.isEmpty(searchRequest.getCreateDateFrom())
				&& !StringUtils.isEmpty(searchRequest.getCreateDateTo()))
			filterPredicates.add(cb.between(studentRegistrationRoot.get(CREATION_DATE_TIME),
					searchRequest.getCreateDateFrom(), searchRequest.getCreateDateTo()));

	}

	private void mapStudentControlPredicate(AdminSearchRequest searchRequest, Root<StudentControl> studentControlRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentControlRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getEmailId()))
			filterPredicates.add(cb.equal(studentControlRoot.get(EMAIL_ID), searchRequest.getEmailId()));

		if (!StringUtils.isEmpty(searchRequest.getSubscriptionType()))
			filterPredicates
					.add(cb.equal(studentControlRoot.get(SUBSCRIPTION_TYPE), searchRequest.getSubscriptionType()));

		if (!StringUtils.isEmpty(searchRequest.getEvalAvailed()))
			filterPredicates.add(cb.equal(studentControlRoot.get(EVAL_AVAILED), searchRequest.getEvalAvailed()));

		if (!StringUtils.isEmpty(searchRequest.getSchoolDetails()))
			filterPredicates.add(cb.equal(studentControlRoot.get(SCHOOL_DETAILS), searchRequest.getSchoolDetails()));

	}

	private void mapStudentDetailsPredicate(AdminSearchRequest searchRequest, Root<StudentDetails> studentDetailsRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getName()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get(NAME), searchRequest.getName()));

		if (!StringUtils.isEmpty(searchRequest.getSurname()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get(SURNAME), searchRequest.getSurname()));

		if (!StringUtils.isEmpty(searchRequest.getGender()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get(GENDER), searchRequest.getGender()));

		if (!StringUtils.isEmpty(searchRequest.getDobFrom()) && !StringUtils.isEmpty(searchRequest.getDobTo()))
			filterPredicates
					.add(cb.between(studentDetailsRoot.get(DOB), searchRequest.getDobFrom(), searchRequest.getDobTo()));

		if (!StringUtils.isEmpty(searchRequest.getMobileNumber()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get(MOBILE_NUMBER), searchRequest.getMobileNumber()));
	}

	private void mapStudentSchoolPredicate(AdminSearchRequest searchRequest, Root<StudentSchool> studentSchoolRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getCity()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get(CITY), searchRequest.getCity()));

		if (!StringUtils.isEmpty(searchRequest.getCityNotInTheList()))
			filterPredicates
					.add(cb.equal(studentSchoolRoot.get(CITY_NOT_IN_THE_LIST), searchRequest.getCityNotInTheList()));

		if (!StringUtils.isEmpty(searchRequest.getCountry()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get(COUNTRY), searchRequest.getCountry()));

		if (!StringUtils.isEmpty(searchRequest.getCountryNotInTheList()))
			filterPredicates.add(
					cb.equal(studentSchoolRoot.get(COUNTRY_NOT_IN_THE_LIST), searchRequest.getCountryNotInTheList()));
		if (!StringUtils.isEmpty(searchRequest.getGrade()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get(GRADE), searchRequest.getGrade()));

		if (!StringUtils.isEmpty(searchRequest.getSchool()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get(SCHOOL), searchRequest.getSchool()));

		if (!StringUtils.isEmpty(searchRequest.getSchoolNotInTheList()))
			filterPredicates.add(
					cb.equal(studentSchoolRoot.get(SCHOOL_NOT_IN_THE_LIST), searchRequest.getSchoolNotInTheList()));

		if (!StringUtils.isEmpty(searchRequest.getState()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get(STATE), searchRequest.getState()));

		if (!StringUtils.isEmpty(searchRequest.getStateNotInTheList()))
			filterPredicates
					.add(cb.equal(studentSchoolRoot.get(STATE_NOT_IN_THE_LIST), searchRequest.getStateNotInTheList()));

	}

	private void mapStudentPreferencePredicate(AdminSearchRequest searchRequest,
			Root<StudentPreferences> studentPreferenceRoot, CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getReadingLevel()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get(READING_LEVEL), searchRequest.getReadingLevel()));

		if (!StringUtils.isEmpty(searchRequest.getDailyPublication()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get(COMMS_OVER_MAIL).get(DAILY_PUBLICATION),
					searchRequest.getDailyPublication()));

		if (!StringUtils.isEmpty(searchRequest.getScoresProgressReports()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get(COMMS_OVER_MAIL).get(SCHOOL_PROGRESS_REPORT),
					searchRequest.getScoresProgressReports()));

		if (!StringUtils.isEmpty(searchRequest.getAlertsNotifications()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get(COMMS_OVER_MAIL).get(ALERT_NOTIFICATION),
					searchRequest.getAlertsNotifications()));
	}

	private void mapStudentSubscriptionPredicate(AdminSearchRequest searchRequest,
			Root<StudentSubscription> studentSubscriptionRoot, CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentSubscriptionRoot.get(STUDENT_ID), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getSubscriptionType()))
			filterPredicates.add(
					cb.equal(studentSubscriptionRoot.get(SUBSCRIPTION_SELECTED), searchRequest.getSubscriptionType()));

		if (!StringUtils.isEmpty(searchRequest.getAutoRenewal()))
			filterPredicates.add(cb.equal(studentSubscriptionRoot.get(AUTO_RENEWAL), searchRequest.getAutoRenewal()));

		if (!StringUtils.isEmpty(searchRequest.getStartDateFrom())
				&& !StringUtils.isEmpty(searchRequest.getStartDateTo()))
			filterPredicates.add(cb.between(studentSubscriptionRoot.get(START_DATE), searchRequest.getStartDateFrom(),
					searchRequest.getStartDateTo()));

		if (!StringUtils.isEmpty(searchRequest.getEndDateFrom()) && !StringUtils.isEmpty(searchRequest.getEndDateTo()))
			filterPredicates.add(cb.between(studentSubscriptionRoot.get(END_DATE), searchRequest.getEndDateFrom(),
					searchRequest.getEndDateTo()));

	}

	private void mapStudentSubscriptionHistoryPredicate(AdminSearchRequest searchRequest,
			Root<StudentSubscriptionHistory> studentSubscriptionHistoryRoot, CriteriaBuilder cb,
			List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates
					.add(cb.equal(studentSubscriptionHistoryRoot.get(STUDENT_ID), searchRequest.getStudentId()));
	}

	private void mapStudentPaymentPredicate(AdminSearchRequest searchRequest, Root<StudentPayment> studentPaymentRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentPaymentRoot.get(STUDENT_ID), searchRequest.getStudentId()));
	}

	private void mapStudentPaymentFailedPredicate(AdminSearchRequest searchRequest,
			Root<StudentPaymentFailed> studentPaymentFailedRoot, CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentPaymentFailedRoot.get(STUDENT_ID), searchRequest.getStudentId()));
	}

	private void mapStudentRefundPredicate(AdminSearchRequest searchRequest, Root<StudentRefund> studentRefundRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentRefundRoot.get(STUDENT_ID), searchRequest.getStudentId()));
	}

	@SneakyThrows
	private void mapStudentRefundPageData(BulkStudentRegistrationPageData pagedata, StudentRefund studentRefund) {
		pagedata.getStudentRefund().setInitRefundApiRequest(blobToString(studentRefund.getInitRefundApiRequest()));
		pagedata.getStudentRefund().setInitRefundApiResponse(blobToString(studentRefund.getInitRefundApiResponse()));
		pagedata.getStudentRefund().setRefundStatusApiRequest(blobToString(studentRefund.getRefundStatusApiRequest()));
		pagedata.getStudentRefund()
				.setRefundStatusApiResponse(blobToString(studentRefund.getRefundStatusApiResponse()));
		pagedata.getStudentRefund().setLastUpdate(studentRefund.getOperationDateTime());
	}

	@SneakyThrows
	private void mapStudentPaymentPageData(BulkStudentRegistrationPageData pagedata, StudentPayment studentPayment) {
		pagedata.getStudentPayment().setInitTranApiRequest(blobToString(studentPayment.getInitTranApiRequest()));
		pagedata.getStudentPayment().setInitTranApiResponse(blobToString(studentPayment.getInitTranApiResponse()));
		pagedata.getStudentPayment().setTranStatusApiRequest((blobToString(studentPayment.getTranStatusApiRequest())));
		pagedata.getStudentPayment().setTranStatusApiResponse(blobToString(studentPayment.getTranStatusApiResponse()));
		pagedata.getStudentPayment().setLastUpdate(studentPayment.getOperationDateTime());
	}

	@SneakyThrows
	private void mapStudentPaymentFailedPageData(BulkStudentRegistrationPageData pagedata,
			StudentPaymentFailed studentPaymentFailed) {
		pagedata.getStudentPaymentFailed()
				.setInitTranApiRequest(blobToString(studentPaymentFailed.getInitTranApiRequest()));
		pagedata.getStudentPaymentFailed()
				.setInitTranApiResponse(blobToString(studentPaymentFailed.getInitTranApiResponse()));
		pagedata.getStudentPaymentFailed()
				.setTranStatusApiRequest((blobToString(studentPaymentFailed.getTranStatusApiRequest())));
		pagedata.getStudentPaymentFailed()
				.setTranStatusApiResponse(blobToString(studentPaymentFailed.getTranStatusApiResponse()));
		pagedata.getStudentPaymentFailed().setLastUpdate(studentPaymentFailed.getOperationDateTime());
	}

	@SneakyThrows
	private String blobToString(Blob data) throws SQLException, IOException {
		if (data == null) {
			return null;
		}
		InputStream bstream = data.getBinaryStream();
		return IOUtils.toString(bstream, "UTF-8");
	}

}