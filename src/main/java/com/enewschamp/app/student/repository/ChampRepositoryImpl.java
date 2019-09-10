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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.enewschamp.app.champs.entity.Champ;
import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.common.city.entity.City;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyTotal;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentPreference;
import com.enewschamp.subscription.domain.entity.StudentSchool;

@Repository
public class ChampRepositoryImpl extends RepositoryImpl  implements ChampStudentRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Page<ChampStudentDTO> findChampStudents(ChampsSearchData searchRequest, Pageable pageable) {

		System.out.println("Year month : "+searchRequest.getMonthYear());
		System.out.println("Reading level month : "+searchRequest.getReadingLevel());
		

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChampStudentDTO> criteriaQuery = cb.createQuery(ChampStudentDTO.class);

		Root<StudentSchool> studentSchoolRoot = criteriaQuery.from(StudentSchool.class);
		Root<StudentDetails> studentDetailsRoot = criteriaQuery.from(StudentDetails.class);
		Root<School> schoolRoot = criteriaQuery.from(School.class);
		Root<City> cityRoot = criteriaQuery.from(City.class);

		Root<TrendsMonthlyTotal> trendsMonthlyRoot = criteriaQuery.from(TrendsMonthlyTotal.class);
		Root<StudentPreference> studentPrefRoot = criteriaQuery.from(StudentPreference.class);

		criteriaQuery = criteriaQuery.select(cb.construct(ChampStudentDTO.class, 
				studentDetailsRoot.get("photo"),
				studentDetailsRoot.get("name"), 
				studentDetailsRoot.get("surname"),
				studentSchoolRoot.get("grade"),
				schoolRoot.get("name"),
				//schoolRoot.get("cityId"),
				cityRoot.get("nameId"),
				trendsMonthlyRoot.get("quizQCorrect")
				//studentDetailsRoot.get("studentID")
				));
		
		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

	filterPredicates.add(cb.equal((studentDetailsRoot.get("studentID")), (studentSchoolRoot.get("studentID")) ));
	filterPredicates.add(cb.equal(schoolRoot.get("schoolId"), studentSchoolRoot.get("schoolId") ));
	filterPredicates.add(cb.equal(schoolRoot.get("cityId"), cityRoot.get("cityId") ));
	filterPredicates.add(cb.equal(studentDetailsRoot.get("studentID"), trendsMonthlyRoot.get("studentId") ));
	filterPredicates.add(cb.equal(studentDetailsRoot.get("studentID"), studentPrefRoot.get("studentID") ));


		if (searchRequest.getReadingLevel() != null) {
			filterPredicates.add(cb.equal(studentPrefRoot.get("readingLevel"), searchRequest.getReadingLevel()));
		}
		if (searchRequest.getMonthYear() != null) {
			filterPredicates.add(cb.equal(trendsMonthlyRoot.get("yearMonth"), searchRequest.getMonthYear()));
		}
		
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		
		// Build query
		TypedQuery<ChampStudentDTO> q = entityManager.createQuery(criteriaQuery);
		
		if(pageable.getPageSize() > 0) {
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


		System.out.println("Year month : "+searchRequest.getMonthYear());
		System.out.println("Reading level month : "+searchRequest.getReadingLevel());
		

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChampStudentDTO> criteriaQuery = cb.createQuery(ChampStudentDTO.class);

		Root<Champ> champRoot = criteriaQuery.from(Champ.class);
	
		criteriaQuery = criteriaQuery.select(cb.construct(ChampStudentDTO.class, 
				champRoot.get("studentPhoto"),
				champRoot.get("studentName"), 
				champRoot.get("surname"),
				champRoot.get("grade"),
				champRoot.get("schoolName"),
				//schoolRoot.get("cityId"),
				champRoot.get("cityName"),
				champRoot.get("monthlyScore")
				//studentDetailsRoot.get("studentID")
				));
		
		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

	
		if (searchRequest.getReadingLevel() != null) {
			filterPredicates.add(cb.equal(champRoot.get("readingLevel"), searchRequest.getReadingLevel()));
		}
		if (searchRequest.getMonthYear() != null) {
			filterPredicates.add(cb.equal(champRoot.get("trendyearMonth"), searchRequest.getMonthYear()));
		}
		
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		
		// Build query
		TypedQuery<ChampStudentDTO> q = entityManager.createQuery(criteriaQuery);
		
		if(pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
			
		}
		List<ChampStudentDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, champRoot);
		
		return new PageImpl<>(list, pageable, count);
	
	
	}
}
