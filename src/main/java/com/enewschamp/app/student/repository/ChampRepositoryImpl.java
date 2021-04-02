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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.enewschamp.app.champs.entity.Champ;
import com.enewschamp.app.champs.page.data.ChampsSearchData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.student.dto.ChampStudentDTO;
import com.enewschamp.app.student.dto.ChampStudentMYDTO;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.service.RepositoryImpl;

@Repository
public class ChampRepositoryImpl extends RepositoryImpl implements ChampStudentRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PropertiesBackendService propertiesService;

	@Override
	public Page<ChampStudentDTO> findChampions(ChampsSearchData searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ChampStudentDTO> criteriaQuery = cb.createQuery(ChampStudentDTO.class);
		Root<Champ> champRoot = criteriaQuery.from(Champ.class);
		criteriaQuery = criteriaQuery.select(cb.construct(ChampStudentDTO.class, champRoot.get("studentId"),
				champRoot.get("name"), champRoot.get("surname"), champRoot.get("grade"), champRoot.get("school"),
				champRoot.get("city"), champRoot.get("state"), champRoot.get("country"), champRoot.get("score"),
				champRoot.get("rank"), champRoot.get("avatarName"), champRoot.get("photoName"),
				champRoot.get("featureProfileInChamps"), champRoot.get("champCity"), champRoot.get("champProfilePic"),
				champRoot.get("champSchool"), champRoot.get("imageApprovalRequired"),
				champRoot.get("studentDetailsApprovalRequired"), champRoot.get("schoolDetailsApprovalRequired")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();
		if (searchRequest.getReadingLevel() != null) {
			filterPredicates.add(cb.equal(champRoot.get("readingLevel"), searchRequest.getReadingLevel()));
		}
		if (searchRequest.getYearMonth() != null) {
			filterPredicates.add(cb.equal(champRoot.get("yearMonth"), searchRequest.getYearMonth()));
		}
		filterPredicates.add(cb.notEqual(champRoot.get("studentDetailsApprovalRequired"), "Y"));
		filterPredicates.add(cb.equal(champRoot.get("featureProfileInChamps"), "Y"));
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
				"SELECT student_id,reading_level,feature_profile_in_champs,champ_city,champ_school,champ_profile_pic,image_approval_required,student_details_approval_required,school_details_approval_required,avatar_name,photo_name,name,surname,grade,school,city,state,country,SUM(score) AS score,DENSE_RANK() OVER (PARTITION BY reading_level ORDER BY score DESC) AS `rank` FROM champs_vw WHERE feature_profile_in_champs='Y' AND student_details_approval_required<>'Y' AND `year_month` IN (?,?,?) AND reading_level=? GROUP BY student_id,reading_level",
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
				"SELECT student_id,reading_level,feature_profile_in_champs,champ_city,champ_school,champ_profile_pic,image_approval_required,student_details_approval_required,school_details_approval_required,avatar_name,photo_name,name,surname,grade,school,city,state,country,SUM(score) AS score,DENSE_RANK() OVER (PARTITION BY reading_level ORDER BY score DESC) AS `rank` FROM champs_vw WHERE feature_profile_in_champs='Y' AND student_details_approval_required<>'Y' AND SUBSTRING(`year_month`,1,4)=? AND reading_level=? GROUP BY student_id,reading_level",
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
