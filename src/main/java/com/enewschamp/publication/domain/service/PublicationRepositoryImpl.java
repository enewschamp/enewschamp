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
import com.enewschamp.domain.service.RepositoryImpl;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.page.data.PublicationSearchRequest;

public class PublicationRepositoryImpl extends RepositoryImpl implements PublicationRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public Page<PublicationDTO> findPublications(PublicationSearchRequest searchRequest, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PublicationDTO> criteriaQuery = cb.createQuery(PublicationDTO.class);
		Root<Publication> publicationRoot = criteriaQuery.from(Publication.class);

		criteriaQuery.select(cb.construct(PublicationDTO.class, publicationRoot.get("publicationId"),
				publicationRoot.get("publicationGroupId"), publicationRoot.get("editionId"),
				publicationRoot.get("readingLevel"), publicationRoot.get("publishDate"), publicationRoot.get("status"),
				publicationRoot.get("editorId"), publicationRoot.get("publisherId")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		Predicate readingLevelPredicate = buildReadingLevelPredicate(searchRequest, cb, publicationRoot);
		if(readingLevelPredicate != null) {
			filterPredicates.add(readingLevelPredicate);
		}
		if (searchRequest.getEditorId() != null) {
			filterPredicates.add(cb.equal(publicationRoot.get("editorId"), searchRequest.getEditorId()));
		}
		if (searchRequest.getPublisherId() != null) {
			filterPredicates.add(cb.equal(publicationRoot.get("publisherId"), searchRequest.getPublisherId()));
		}
		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(publicationRoot.get("publishDate"), searchRequest.getPublicationDate()));
		}
		if (searchRequest.getPublicationStatusList() != null && searchRequest.getPublicationStatusList().size() > 0) {
			filterPredicates.add(publicationRoot.get("status").in(searchRequest.getPublicationStatusList()));
		}
		
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		
		// Build query
		TypedQuery<PublicationDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<PublicationDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, publicationRoot);

		return new PageImpl<>(list, pageable, count);
	}

	private Predicate buildReadingLevelPredicate(PublicationSearchRequest searchRequest, CriteriaBuilder cb,
												 Root<Publication> publicationRoot) {
		List<Predicate> readingLevelPredicates = new ArrayList<>();
		if (searchRequest.getReadingLevel1() != null) {
			if (searchRequest.getReadingLevel1().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 1));
			} else {
				readingLevelPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 1));
			}
		}
		if (searchRequest.getReadingLevel2() != null) {
			if (searchRequest.getReadingLevel2().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 2));
			} else {
				readingLevelPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 2));
			}
		}
		if (searchRequest.getReadingLevel3() != null) {
			if (searchRequest.getReadingLevel3().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 3));
			} else {
				readingLevelPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 3));
			}
		}
		if (searchRequest.getReadingLevel4() != null) {
			if (searchRequest.getReadingLevel4().equals(AppConstants.YES)) {
				readingLevelPredicates.add(cb.equal(publicationRoot.get("readingLevel"), 4));
			} else {
				readingLevelPredicates.add(cb.notEqual(publicationRoot.get("readingLevel"), 4));
			}
		}
		Predicate predicate = null;
		if (readingLevelPredicates.size() > 0) {
			predicate = cb.or(readingLevelPredicates.toArray(new Predicate[0]));
		}
		return predicate;
	}
}
