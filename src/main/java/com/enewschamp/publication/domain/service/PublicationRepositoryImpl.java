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

import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.repository.RepositoryImpl;
import com.enewschamp.publication.app.dto.PublicationSummaryDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationGroup;
import com.enewschamp.publication.page.data.PublicationSearchRequest;

public class PublicationRepositoryImpl extends RepositoryImpl implements PublicationRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public Page<PublicationSummaryDTO> findPublications(PublicationSearchRequest searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PublicationSummaryDTO> criteriaQuery = cb.createQuery(PublicationSummaryDTO.class);
		Root<PublicationGroup> publicationGroupRoot = criteriaQuery.from(PublicationGroup.class);
		Root<Publication> publicationRoot = criteriaQuery.from(Publication.class);

		criteriaQuery.select(cb.construct(PublicationSummaryDTO.class, publicationRoot.get("publicationId"),
				publicationRoot.get("readingLevel"), publicationRoot.get("status"),
				publicationRoot.get("publicationDate"), publicationGroupRoot.get("publicationGroupId"),
				publicationGroupRoot.get("editionId"), publicationGroupRoot.get("editorId"),
				publicationGroupRoot.get("publisherId")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates.add(
				cb.equal(publicationGroupRoot.get("publicationGroupId"), publicationRoot.get("publicationGroupId")));

		if (searchRequest.getEditionId() != null && (!"".equalsIgnoreCase(searchRequest.getEditionId()))) {
			filterPredicates.add(cb.equal(publicationGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getEditorId() != null && (!"".equalsIgnoreCase(searchRequest.getEditorId()))) {
			filterPredicates.add(cb.equal(publicationGroupRoot.get("editorId"), searchRequest.getEditorId()));
		}
		if (searchRequest.getPublisherId() != null && (!"".equalsIgnoreCase(searchRequest.getPublisherId()))) {
			filterPredicates.add(cb.equal(publicationGroupRoot.get("publisherId"), searchRequest.getPublisherId()));
		}
		if (searchRequest.getPublicationDateFrom() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(publicationRoot.get("publicationDate"),
					searchRequest.getPublicationDateFrom()));
		}
		if (searchRequest.getPublicationDateTo() != null) {
			filterPredicates.add(
					cb.lessThanOrEqualTo(publicationRoot.get("publicationDate"), searchRequest.getPublicationDateTo()));
		}
		if (searchRequest.getStatus() != null && searchRequest.getStatus().size() > 0) {
			filterPredicates.add(publicationRoot.get("status").in(searchRequest.getStatus()));
		}
		List<Predicate> readingLevelPredicates = buildReadingLevelPredicate(searchRequest, cb, publicationRoot);
		if (readingLevelPredicates != null && !readingLevelPredicates.isEmpty()) {
			readingLevelPredicates.forEach(predicate -> {
				filterPredicates.add(predicate);
			});

		}
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));

		// Build query
		TypedQuery<PublicationSummaryDTO> q = entityManager.createQuery(criteriaQuery);
		List<PublicationSummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, publicationRoot);
		return new PageImpl<>(list, pageable, count);
	}

	private List<Predicate> buildReadingLevelPredicate(PublicationSearchRequest searchRequest, CriteriaBuilder cb,
			Root<Publication> publicationRoot) {
		List<Predicate> readingLevelOrPredicates = new ArrayList<>();
		List<Predicate> readingLevelAndPredicates = new ArrayList<>();

		if (searchRequest.getReadingLevel1() != null) {
			if (searchRequest.getReadingLevel1().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 1));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 1));
			}
		}
		if (searchRequest.getReadingLevel2() != null) {
			if (searchRequest.getReadingLevel2().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 2));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 2));
			}
		}
		if (searchRequest.getReadingLevel3() != null) {
			if (searchRequest.getReadingLevel3().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 3));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 3));
			}
		}
		if (searchRequest.getReadingLevel4() != null) {
			if (searchRequest.getReadingLevel4().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 4));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 4));
			}
		}
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (readingLevelOrPredicates.size() > 0) {
			predicates.add(cb.or(readingLevelOrPredicates.toArray(new Predicate[0])));
		}
		if (readingLevelAndPredicates.size() > 0) {
			predicates.add(cb.and(readingLevelAndPredicates.toArray(new Predicate[0])));
		}
		return predicates;
	}

}
