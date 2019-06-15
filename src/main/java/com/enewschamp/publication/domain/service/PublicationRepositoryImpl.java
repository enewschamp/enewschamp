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

import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.page.data.PublicationSearchRequest;

public class PublicationRepositoryImpl implements PublicationRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<PublicationDTO> findAllPage(PublicationSearchRequest searchRequest, Pageable pageable) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PublicationDTO> query = cb.createQuery(PublicationDTO.class);
		Root<Publication> publicationRoot = query.from(Publication.class);
		//Root<Author> authorRoot = query.from(Author.class);

		List<Predicate> filterPredicates = new ArrayList<>();
		query.select(cb.construct(PublicationDTO.class, 
								  publicationRoot.get("publicationId"), 
								  publicationRoot.get("publicationGroupId"), 
								  publicationRoot.get("editionId"),
								  publicationRoot.get("readingLevel"),
								  publicationRoot.get("publishDate"),
								  publicationRoot.get("status"),
								  publicationRoot.get("editorId"),
								  publicationRoot.get("publisherId")));
		
				//.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		
		TypedQuery<PublicationDTO> q = entityManager.createQuery(query);
		
		if(pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			pageNumber = pageNumber > 0 ? (pageNumber - 1) : 0;
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());
		}
		return new PageImpl<>(q.getResultList(), pageable, getAllCount(searchRequest));
	}

	private Long getAllCount(PublicationSearchRequest searchRequest) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<Publication> publicationRoot = query.from(Publication.class);

		List<Predicate> filterPredicates = new ArrayList<>();
		query.select(cb.count(publicationRoot));
		//.where(cb.and((Predicate[])filterPredicates.toArray(new Predicate[0])));
		return (Long) entityManager.createQuery(query).getSingleResult();
	}

}
