package com.enewschamp.app.admin.student.registration.bulk.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.app.student.registration.entity.StudentRegistration;
import com.enewschamp.domain.service.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSchool;
import com.enewschamp.subscription.domain.entity.StudentSubscription;

@Repository
public class BulkStudentCustomImpl extends RepositoryImpl {
	@PersistenceContext
	private EntityManager entityManager;

	public List<BulkStudentRegistrationPageData2> findArticles(AdminSearchRequest searchRequest) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = cb.createQuery(Tuple.class);
		Root<StudentControl> studentControlRoot = criteriaQuery.from(StudentControl.class);
		Root<StudentDetails> studentDetailsRoot = criteriaQuery.from(StudentDetails.class);
		Root<StudentSchool> studentSchoolRoot = criteriaQuery.from(StudentSchool.class);
		Root<StudentPreferences> studentPreferencesRoot = criteriaQuery.from(StudentPreferences.class);
		Root<StudentSubscription> studentSubscriptionRoot = criteriaQuery.from(StudentSubscription.class);
		Root<StudentRegistration> studentRegistrationRoot = criteriaQuery.from(StudentRegistration.class);
		Root<StudentActivity> studentActivityRoot = criteriaQuery.from(StudentActivity.class);
		
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates.add(cb.equal(studentRegistrationRoot.get("studentId"), studentActivityRoot.get("studentId")));
		filterPredicates
				.add(cb.equal(studentRegistrationRoot.get("studentId"), studentSubscriptionRoot.get("studentId")));
		filterPredicates
				.add(cb.equal(studentRegistrationRoot.get("studentId"), studentPreferencesRoot.get("studentId")));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get("studentId"), studentSchoolRoot.get("studentId")));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get("studentId"), studentDetailsRoot.get("studentId")));
		filterPredicates.add(cb.equal(studentRegistrationRoot.get("studentId"), studentControlRoot.get("studentId")));

		// Apply the filter Criterias
		mapStudentRegistrationPredicate(searchRequest, studentRegistrationRoot, cb, filterPredicates);
		mapStudentControlPredicate(searchRequest, studentControlRoot, cb, filterPredicates);
		mapStudentDetailsPredicate(searchRequest, studentDetailsRoot, cb, filterPredicates);
		mapStudentSchoolPredicate(searchRequest, studentSchoolRoot, cb, filterPredicates);
		mapStudentPreferencePredicate(searchRequest, studentPreferencesRoot, cb, filterPredicates);
		mapStudentSubscriptionPredicate(searchRequest, studentSubscriptionRoot, cb, filterPredicates);

		criteriaQuery.select(cb.tuple(studentRegistrationRoot, studentSubscriptionRoot, studentPreferencesRoot, studentSchoolRoot,
				studentDetailsRoot, studentControlRoot))
				.where((Predicate[]) filterPredicates.toArray(new Predicate[0]));
		List<Tuple> l = entityManager.createQuery(criteriaQuery).getResultList();
		List<BulkStudentRegistrationPageData2> bulkList = new ArrayList<BulkStudentRegistrationPageData2>();
		for (Tuple t : l) {
			BulkStudentRegistrationPageData2 pageData = new BulkStudentRegistrationPageData2();
			StudentRegistration s = (StudentRegistration) t.get(0);
			StudentSubscription sc = (StudentSubscription) t.get(1);
			StudentPreferences sf = (StudentPreferences) t.get(2);
			StudentSchool ss = (StudentSchool) t.get(3);
			StudentDetails sd = (StudentDetails) t.get(4);
			StudentControl scs = (StudentControl) t.get(5);
			pageData.setStudentRegistration(s);
			pageData.setStudentSubscription(sc);
			pageData.setStudentControl(scs);
			pageData.setStudentSchool(ss);
			pageData.setStudentPreferences(sf);
			pageData.setStudentDetails(sd);
			bulkList.add(pageData);
		}
		System.out.println("tuple list: " + l);
		return bulkList;
	}

	private void mapStudentRegistrationPredicate(AdminSearchRequest searchRequest,
			Root<StudentRegistration> studentRegistrationRoot, CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getEmailId()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("emailId"), searchRequest.getEmailId()));

		if (!StringUtils.isEmpty(searchRequest.getAvatar()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("avatarName"), searchRequest.getAvatar()));

		if (!StringUtils.isEmpty(searchRequest.getPhoto()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("photoName"), searchRequest.getPhoto()));

		if (!StringUtils.isEmpty(searchRequest.getTheme()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("theme"), searchRequest.getTheme()));

		if (!StringUtils.isEmpty(searchRequest.getIsAccountLocked()))
			filterPredicates
					.add(cb.equal(studentRegistrationRoot.get("isAccountLocked"), searchRequest.getIsAccountLocked()));

		if (!StringUtils.isEmpty(searchRequest.getIsActive()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("isActive"), searchRequest.getIsActive()));

		if (!StringUtils.isEmpty(searchRequest.getIsDeleted()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("isDeleted"), searchRequest.getIsDeleted()));
	
		if (!StringUtils.isEmpty(searchRequest.getLastLoginTime()))
			filterPredicates.add(cb.equal(studentRegistrationRoot.get("lastSuccessfulLoginAttempt"), searchRequest.getLastLoginTime()));

	}

	private void mapStudentControlPredicate(AdminSearchRequest searchRequest, Root<StudentControl> studentControlRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentControlRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getEmailId()))
			filterPredicates.add(cb.equal(studentControlRoot.get("emailId"), searchRequest.getEmailId()));

		if (!StringUtils.isEmpty(searchRequest.getSubscriptionType()))
			filterPredicates
					.add(cb.equal(studentControlRoot.get("subscriptionType"), searchRequest.getSubscriptionType()));

		if (!StringUtils.isEmpty(searchRequest.getEvalAvailed()))
			filterPredicates.add(cb.equal(studentControlRoot.get("evalAvailed"), searchRequest.getEvalAvailed()));

		if (!StringUtils.isEmpty(searchRequest.getSchoolDetails()))
			filterPredicates.add(cb.equal(studentControlRoot.get("schoolDetails"), searchRequest.getSchoolDetails()));

		if (!StringUtils.isEmpty(searchRequest.getStudentDetails()))
			filterPredicates.add(cb.equal(studentControlRoot.get("studentDetails"), searchRequest.getStudentDetails()));
	}

	private void mapStudentDetailsPredicate(AdminSearchRequest searchRequest, Root<StudentDetails> studentDetailsRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {

		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getName()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get("name"), searchRequest.getName()));

		if (!StringUtils.isEmpty(searchRequest.getSurname()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get("surname"), searchRequest.getSurname()));

		if (!StringUtils.isEmpty(searchRequest.getGender()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get("gender"), searchRequest.getGender()));

		if (!StringUtils.isEmpty(searchRequest.getDobFrom()) && !StringUtils.isEmpty(searchRequest.getDobTo()))
			filterPredicates.add(
					cb.between(studentDetailsRoot.get("doB"), searchRequest.getDobFrom(), searchRequest.getDobTo()));

		if (!StringUtils.isEmpty(searchRequest.getMobileNumber()))
			filterPredicates.add(cb.equal(studentDetailsRoot.get("mobileNumber"), searchRequest.getMobileNumber()));
	}

	private void mapStudentSchoolPredicate(AdminSearchRequest searchRequest, Root<StudentSchool> studentSchoolRoot,
			CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getCity()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get("city"), searchRequest.getCity()));

		if (!StringUtils.isEmpty(searchRequest.getCityNotInTheList()))
			filterPredicates
					.add(cb.equal(studentSchoolRoot.get("cityNotInTheList"), searchRequest.getCityNotInTheList()));

		if (!StringUtils.isEmpty(searchRequest.getCountry()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get("country"), searchRequest.getCountry()));

		if (!StringUtils.isEmpty(searchRequest.getCountryNotInTheList()))
			filterPredicates.add(
					cb.equal(studentSchoolRoot.get("countryNotInTheList"), searchRequest.getCountryNotInTheList()));
		if (!StringUtils.isEmpty(searchRequest.getGrade()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get("grade"), searchRequest.getGrade()));

		if (!StringUtils.isEmpty(searchRequest.getSchool()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get("school"), searchRequest.getSchool()));

		if (!StringUtils.isEmpty(searchRequest.getSchoolNotInTheList()))
			filterPredicates
					.add(cb.equal(studentSchoolRoot.get("schoolNotInTheList"), searchRequest.getSchoolNotInTheList()));

		if (!StringUtils.isEmpty(searchRequest.getState()))
			filterPredicates.add(cb.equal(studentSchoolRoot.get("state"), searchRequest.getState()));

		if (!StringUtils.isEmpty(searchRequest.getStateNotInTheList()))
			filterPredicates
					.add(cb.equal(studentSchoolRoot.get("stateNotInTheList"), searchRequest.getStateNotInTheList()));

	}

	private void mapStudentPreferencePredicate(AdminSearchRequest searchRequest,
			Root<StudentPreferences> studentPreferenceRoot, CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getReadingLevel()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get("readingLevel"), searchRequest.getReadingLevel()));

		if (!StringUtils.isEmpty(searchRequest.getDailyPublication()))
			filterPredicates
					.add(cb.equal(studentPreferenceRoot.get("dailyPublication"), searchRequest.getDailyPublication()));

		if (!StringUtils.isEmpty(searchRequest.getScoresProgressReports()))
			filterPredicates.add(cb.equal(studentPreferenceRoot.get("scoresProgressReports"),
					searchRequest.getScoresProgressReports()));

		if (!StringUtils.isEmpty(searchRequest.getAlertsNotifications()))
			filterPredicates.add(
					cb.equal(studentPreferenceRoot.get("alertsNotifications"), searchRequest.getAlertsNotifications()));
	}

	private void mapStudentSubscriptionPredicate(AdminSearchRequest searchRequest,
			Root<StudentSubscription> studentSubscriptionRoot, CriteriaBuilder cb, List<Predicate> filterPredicates) {
		if (!StringUtils.isEmpty(searchRequest.getStudentId()))
			filterPredicates.add(cb.equal(studentSubscriptionRoot.get("studentId"), searchRequest.getStudentId()));

		if (!StringUtils.isEmpty(searchRequest.getSubscriptionType()))
			filterPredicates.add(
					cb.equal(studentSubscriptionRoot.get("subscriptionType"), searchRequest.getSubscriptionType()));

		if (!StringUtils.isEmpty(searchRequest.getAutoRenewal()))
			filterPredicates.add(cb.equal(studentSubscriptionRoot.get("autoRenewal"), searchRequest.getAutoRenewal()));

		if (!StringUtils.isEmpty(searchRequest.getStartDateFrom())
				&& !StringUtils.isEmpty(searchRequest.getStartDateTo()))
			filterPredicates.add(cb.between(studentSubscriptionRoot.get("startDate"), searchRequest.getStartDateFrom(),
					searchRequest.getStartDateTo()));

		if (!StringUtils.isEmpty(searchRequest.getEndDateFrom()) && !StringUtils.isEmpty(searchRequest.getEndDateTo()))
			filterPredicates.add(cb.between(studentSubscriptionRoot.get("endDate"), searchRequest.getEndDateFrom(),
					searchRequest.getEndDateTo()));

	}

}