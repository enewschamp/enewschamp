package com.enewschamp.publication.domain.service;

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

import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.publication.app.dto.PublicationDailySummaryDTO;
import com.enewschamp.publication.app.dto.PublicationMonthlySummaryDTO;
import com.enewschamp.publication.domain.entity.PublicationDailySummary;
import com.enewschamp.publication.domain.entity.PublicationMonthlySummary;
import com.enewschamp.publication.page.data.PublicationSummaryRequest;

public class PublicationSummaryRepositoryImpl extends RepositoryImpl implements PublicationSummaryRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<PublicationDailySummaryDTO> fetchDailySummary(PublicationSummaryRequest summaryRequest,
			Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PublicationDailySummaryDTO> criteriaQuery = cb.createQuery(PublicationDailySummaryDTO.class);
		Root<PublicationDailySummary> publicationDailySummaryRoot = criteriaQuery.from(PublicationDailySummary.class);

		criteriaQuery.select(cb.construct(PublicationDailySummaryDTO.class,
				publicationDailySummaryRoot.get("publicationDate"), publicationDailySummaryRoot.get("newsArticleCount"),
				publicationDailySummaryRoot.get("quizCount")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		if (summaryRequest.getMonth() != null) {
			filterPredicates.add(cb.equal(publicationDailySummaryRoot.get("month"), summaryRequest.getMonth()));
		}
		if (summaryRequest.getYear() != null) {
			filterPredicates.add(cb.equal(publicationDailySummaryRoot.get("year"), summaryRequest.getYear()));
		}
		if (summaryRequest.getDate() != null) {
			filterPredicates
					.add(cb.equal(publicationDailySummaryRoot.get("publicationDate"), summaryRequest.getDate()));
		}
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));

		// Build query
		TypedQuery<PublicationDailySummaryDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<PublicationDailySummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, publicationDailySummaryRoot);

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<PublicationMonthlySummaryDTO> fetchMonthlySummary(PublicationSummaryRequest summaryRequest,
			Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PublicationMonthlySummaryDTO> criteriaQuery = cb.createQuery(PublicationMonthlySummaryDTO.class);
		Root<PublicationMonthlySummary> publicationDailySummaryRoot = criteriaQuery
				.from(PublicationMonthlySummary.class);

		criteriaQuery.select(cb.construct(PublicationMonthlySummaryDTO.class,
				publicationDailySummaryRoot.get("recordId"), publicationDailySummaryRoot.get("year"),
				publicationDailySummaryRoot.get("month"), publicationDailySummaryRoot.get("editionId"),
				publicationDailySummaryRoot.get("genreId"), publicationDailySummaryRoot.get("readingLevel"),
				publicationDailySummaryRoot.get("newsArticleCount"), publicationDailySummaryRoot.get("quizCount"),
				publicationDailySummaryRoot.get("lastUpdatedDateTime")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		if (summaryRequest.getMonth() != null) {
			filterPredicates.add(cb.equal(publicationDailySummaryRoot.get("month"), summaryRequest.getMonth()));
		}
		if (summaryRequest.getYear() != null) {
			filterPredicates.add(cb.equal(publicationDailySummaryRoot.get("year"), summaryRequest.getYear()));
		}
		if (summaryRequest.getEditionId() != null) {
			filterPredicates.add(cb.equal(publicationDailySummaryRoot.get("editionId"), summaryRequest.getEditionId()));
		}
		if (summaryRequest.getGenreId() != null) {
			filterPredicates.add(cb.equal(publicationDailySummaryRoot.get("genreId"), summaryRequest.getGenreId()));
		}
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));

		// Build query
		TypedQuery<PublicationMonthlySummaryDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<PublicationMonthlySummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, publicationDailySummaryRoot);

		return new PageImpl<>(list, pageable, count);
	}

}
