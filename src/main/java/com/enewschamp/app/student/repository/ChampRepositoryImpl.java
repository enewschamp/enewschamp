package com.enewschamp.app.student.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.enewschamp.app.champs.entity.Champ;
import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyTotal;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.service.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSchool;

@Repository
public class ChampRepositoryImpl extends RepositoryImpl implements ChampStudentRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PropertiesService propertiesService;

	@Override
	public Page<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChampStudentDTO> criteriaQuery = cb.createQuery(ChampStudentDTO.class);

		Root<StudentSchool> studentSchoolRoot = criteriaQuery.from(StudentSchool.class);
		Root<StudentDetails> studentDetailsRoot = criteriaQuery.from(StudentDetails.class);
		Root<School> schoolRoot = criteriaQuery.from(School.class);
		Root<City> cityRoot = criteriaQuery.from(City.class);

		Root<TrendsMonthlyTotal> trendsMonthlyRoot = criteriaQuery.from(TrendsMonthlyTotal.class);
		Root<StudentPreferences> studentPrefRoot = criteriaQuery.from(StudentPreferences.class);

		criteriaQuery = criteriaQuery.select(cb.construct(ChampStudentDTO.class, studentDetailsRoot.get("name"),
				studentDetailsRoot.get("surname"), studentSchoolRoot.get("grade"), schoolRoot.get("name"),
				cityRoot.get("nameId"), trendsMonthlyRoot.get("quizQCorrect")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates.add(cb.equal((studentDetailsRoot.get("studentId")), (studentSchoolRoot.get("studentId"))));
		filterPredicates.add(cb.equal(schoolRoot.get("schoolId"), studentSchoolRoot.get("schoolId")));
		filterPredicates.add(cb.equal(schoolRoot.get("cityId"), cityRoot.get("cityId")));
		filterPredicates.add(cb.equal(studentDetailsRoot.get("studentId"), trendsMonthlyRoot.get("studentId")));
		filterPredicates.add(cb.equal(studentDetailsRoot.get("studentId"), studentPrefRoot.get("studentId")));

		if (searchRequest.getReadingLevel() != null) {
			filterPredicates.add(cb.equal(studentPrefRoot.get("readingLevel"), searchRequest.getReadingLevel()));
		}
		if (searchRequest.getMonth() != null) {
			filterPredicates.add(cb.equal(trendsMonthlyRoot.get("yearMonth"), searchRequest.getMonth()));
		}

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));

		// Build query
		TypedQuery<ChampStudentDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<ChampStudentDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, studentDetailsRoot);

		return new PageImpl<>(list, pageable, count);

	}

	@Override
	public Page<ChampStudentDTO> findChampions(ChampsSearchData searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChampStudentDTO> criteriaQuery = cb.createQuery(ChampStudentDTO.class);
		Root<Champ> champRoot = criteriaQuery.from(Champ.class);
		criteriaQuery = criteriaQuery.select(cb.construct(ChampStudentDTO.class, champRoot.get("studentName"),
				champRoot.get("surname"), champRoot.get("grade"), champRoot.get("schoolName"),
				champRoot.get("cityName"), champRoot.get("monthlyScore"), champRoot.get("studentRank"),
				champRoot.get("avatarName"), champRoot.get("photoName")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();
		if (searchRequest.getReadingLevel() != null) {
			filterPredicates.add(cb.equal(champRoot.get("readingLevel"), searchRequest.getReadingLevel()));
		}
		if (searchRequest.getMonth() != null) {
			filterPredicates.add(cb.equal(champRoot.get("trendyearMonth"), searchRequest.getMonth()));
		}
		filterPredicates.add(cb.lessThanOrEqualTo(champRoot.get("studentRank"),
				Integer.valueOf(propertiesService.getProperty(PropertyConstants.CHAMPS_LIMIT))));
		criteriaQuery.orderBy(cb.desc(champRoot.get("monthlyScore")));
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		// Build query
		TypedQuery<ChampStudentDTO> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<ChampStudentDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, champRoot);
		return new PageImpl<>(list, pageable, count);
	}
}
