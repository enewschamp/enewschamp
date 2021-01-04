package com.enewschamp.app.student.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import com.enewschamp.app.student.dto.ChampStudentMYDTO;
import com.enewschamp.app.student.scores.entity.ScoresMonthlyTotal;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.service.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPreferences;
import com.enewschamp.subscription.domain.entity.StudentSchool;

@Repository
public class ChampRepositoryImpl extends RepositoryImpl implements ChampStudentRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PropertiesBackendService propertiesService;

	@Override
	public Page<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChampStudentDTO> criteriaQuery = cb.createQuery(ChampStudentDTO.class);

		Root<StudentSchool> studentSchoolRoot = criteriaQuery.from(StudentSchool.class);
		Root<StudentDetails> studentDetailsRoot = criteriaQuery.from(StudentDetails.class);
		Root<School> schoolRoot = criteriaQuery.from(School.class);
		Root<City> cityRoot = criteriaQuery.from(City.class);

		Root<ScoresMonthlyTotal> scoresMonthlyRoot = criteriaQuery.from(ScoresMonthlyTotal.class);
		Root<StudentPreferences> studentPrefRoot = criteriaQuery.from(StudentPreferences.class);

		criteriaQuery = criteriaQuery.select(cb.construct(ChampStudentDTO.class, studentDetailsRoot.get("name"),
				studentDetailsRoot.get("surname"), studentSchoolRoot.get("grade"), schoolRoot.get("name"),
				cityRoot.get("nameId"), scoresMonthlyRoot.get("quizQCorrect")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates.add(cb.equal((studentDetailsRoot.get("studentId")), (studentSchoolRoot.get("studentId"))));
		filterPredicates.add(cb.equal(schoolRoot.get("schoolId"), studentSchoolRoot.get("schoolId")));
		filterPredicates.add(cb.equal(schoolRoot.get("cityId"), cityRoot.get("cityId")));
		filterPredicates.add(cb.equal(studentDetailsRoot.get("studentId"), scoresMonthlyRoot.get("studentId")));
		filterPredicates.add(cb.equal(studentDetailsRoot.get("studentId"), studentPrefRoot.get("studentId")));

		if (searchRequest.getReadingLevel() != null) {
			filterPredicates.add(cb.equal(studentPrefRoot.get("readingLevel"), searchRequest.getReadingLevel()));
		}
		if (searchRequest.getYearMonth() != null) {
			filterPredicates.add(cb.equal(scoresMonthlyRoot.get("yearMonth"), searchRequest.getYearMonth()));
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
		criteriaQuery = criteriaQuery.select(cb.construct(ChampStudentDTO.class, champRoot.get("studentId"),
				champRoot.get("studentName"), champRoot.get("surname"), champRoot.get("grade"),
				champRoot.get("schoolName"), champRoot.get("cityName"), champRoot.get("score"), champRoot.get("rank"),
				champRoot.get("avatarName"), champRoot.get("photoName")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();
		if (searchRequest.getReadingLevel() != null) {
			filterPredicates.add(cb.equal(champRoot.get("readingLevel"), searchRequest.getReadingLevel()));
		}
		if (searchRequest.getYearMonth() != null) {
			filterPredicates.add(cb.equal(champRoot.get("yearMonth"), searchRequest.getYearMonth()));
		}
		filterPredicates.add(cb.lessThanOrEqualTo(champRoot.get("rank"),
				Integer.valueOf(propertiesService.getValue("StudentApp", PropertyConstants.CHAMPS_LIMIT))));
		criteriaQuery.orderBy(cb.desc(champRoot.get("score")));
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

	@Override
	public Page<ChampStudentDTO> findQuarterlyChampions(ChampsSearchData searchRequest, Pageable pageable) {
		String readingLevel = searchRequest.getReadingLevel();
		String yearMonth = searchRequest.getYearMonth();
		Query query = entityManager.createNativeQuery(
				"SELECT student_id,reading_level,avatar_name,photo_name,student_name as name,surname,grade,school_name as school,city_name as city,SUM(score) AS score,RANK() OVER (PARTITION BY reading_level ORDER BY score DESC) AS `rank` FROM champs_vw WHERE `year_month` IN (?,?,?) AND reading_level=? GROUP BY student_id,reading_level",
				ChampStudentMYDTO.class);
		String yearMonth1 = "";
		String yearMonth2 = "";
		String yearMonth3 = "";
		if (yearMonth != null && !"".equals(yearMonth)) {
			String year = yearMonth.substring(0, 4);
			String qtr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
			if ("Q1".equalsIgnoreCase(qtr)) {
				yearMonth1 = year + "01";
				yearMonth2 = year + "02";
				yearMonth3 = year + "03";
			} else if ("Q2".equalsIgnoreCase(qtr)) {
				yearMonth1 = year + "04";
				yearMonth2 = year + "05";
				yearMonth3 = year + "06";
			} else if ("Q3".equalsIgnoreCase(qtr)) {
				yearMonth1 = year + "07";
				yearMonth2 = year + "08";
				yearMonth3 = year + "09";
			} else if ("Q4".equalsIgnoreCase(qtr)) {
				yearMonth1 = year + "10";
				yearMonth2 = year + "11";
				yearMonth3 = year + "12";
			}
		}
		query.setParameter(1, yearMonth1);
		query.setParameter(2, yearMonth2);
		query.setParameter(3, yearMonth3);
		query.setParameter(4, readingLevel);
		long count = query.getResultList().size();
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			query.setFirstResult(pageNumber * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());

		}
		List<ChampStudentDTO> list = query.getResultList();
		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<ChampStudentDTO> findYearlyChampions(ChampsSearchData searchRequest, Pageable pageable) {
		String readingLevel = searchRequest.getReadingLevel();
		String yearMonth = searchRequest.getYearMonth();
		Query query = entityManager.createNativeQuery(
				"SELECT student_id,reading_level,avatar_name,photo_name,student_name as name,surname,grade,school_name as school,city_name as city,SUM(score) AS score,RANK() OVER (PARTITION BY reading_level ORDER BY score DESC) AS `rank` FROM champs_vw WHERE SUBSTRING(`year_month`,1,4)=? AND reading_level=? GROUP BY student_id,reading_level",
				ChampStudentMYDTO.class);
		query.setParameter(1, yearMonth);
		query.setParameter(2, readingLevel);
		long count = query.getResultList().size();
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			query.setFirstResult(pageNumber * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());

		}
		List<ChampStudentDTO> list = query.getResultList();
		return new PageImpl<>(list, pageable, count);
	}
}
