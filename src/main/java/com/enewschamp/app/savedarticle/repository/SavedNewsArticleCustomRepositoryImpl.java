package com.enewschamp.app.savedarticle.repository;

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

import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.domain.service.RepositoryImpl;
import com.enewschamp.opinions.page.dto.OpinionsSearchRequest;

@Repository
public class SavedNewsArticleCustomRepositoryImpl extends RepositoryImpl implements SavedNewsArticleCustomRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<NewsArticleSummaryDTO> findArticles(SavedNewsArticleSearchRequest searchRequest, Pageable pageable) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<NewsArticleSummaryDTO> criteriaQuery = cb.createQuery(NewsArticleSummaryDTO.class);
		Root<NewsArticleGroup> articleGroupRoot = criteriaQuery.from(NewsArticleGroup.class);
		Root<NewsArticle> articleRoot = criteriaQuery.from(NewsArticle.class);
		Root<StudentActivity> student = criteriaQuery.from(StudentActivity.class);
		criteriaQuery = criteriaQuery.select(cb.construct(NewsArticleSummaryDTO.class, articleRoot.get("newsArticleId"),
				articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("publicationDate"),
				articleRoot.get("readingLevel"), articleGroupRoot.get("genreId"), articleGroupRoot.get("headline"),
				articleRoot.get("content"), articleRoot.get("authorWorked"), articleGroupRoot.get("credits"),
				(cb.selectCase()
						.when(cb.or(cb.equal(articleGroupRoot.get("noQuiz"), "Y"),
								cb.equal(articleGroupRoot.get("imageOnly"), "Y")), cb.literal("N"))
						.otherwise(cb.literal("Y"))),
				(cb.selectCase().when(cb.isNull(student.get("quizScore")), cb.literal("N")).otherwise(cb.literal("Y"))),
				student.<String>get("quizScore"), student.<String>get("saved"), student.<String>get("opinion"),
				student.<String>get("reaction"), articleRoot.get("likeHCount"), articleRoot.get("likeLCount"),
				articleRoot.get("likeOCount"), articleRoot.get("likeSCount"), articleRoot.get("likeWCount"),
				articleGroupRoot.get("cityId"), articleGroupRoot.get("imageName")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates
				.add(cb.equal(articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("newsArticleGroupId")));
		filterPredicates.add(cb.equal(student.get("newsArticleId"), articleRoot.get("newsArticleId")));
		filterPredicates.add(cb.equal(student.get("studentId"), searchRequest.getStudentId()));
		filterPredicates.add(cb.equal(student.get("saved"), "Y"));

		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publicationDate"), searchRequest.getPublicationDate()));
		}

		if (searchRequest.getPublicationDateFrom() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleRoot.get("publicationDate"),
					searchRequest.getPublicationDateFrom()));
		}
		if (searchRequest.getPublicationDateTo() != null) {
			filterPredicates.add(
					cb.lessThanOrEqualTo(articleRoot.get("publicationDate"), searchRequest.getPublicationDateTo()));
		}

		if (searchRequest.getGenre() != null && (!"".equalsIgnoreCase(searchRequest.getGenre()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("genreId"), searchRequest.getGenre()));
		}
		if (searchRequest.getEditionId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getHeadline() != null && (!"".equalsIgnoreCase(searchRequest.getHeadline()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("headline"), "%" + searchRequest.getHeadline() + "%"));
		}

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(articleRoot.get("publicationDate")), cb.asc(articleRoot.get("sequence")));

		// Build query
		TypedQuery<NewsArticleSummaryDTO> q = entityManager.createQuery(criteriaQuery);

		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<NewsArticleSummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, articleRoot);

		return new PageImpl<>(list, pageable, count);
	}

	@Override
	public Page<NewsArticleSummaryDTO> findArticlesWithOpinions(OpinionsSearchRequest searchRequest,
			Pageable pageable) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<NewsArticleSummaryDTO> criteriaQuery = cb.createQuery(NewsArticleSummaryDTO.class);
		Root<NewsArticleGroup> articleGroupRoot = criteriaQuery.from(NewsArticleGroup.class);
		Root<NewsArticle> articleRoot = criteriaQuery.from(NewsArticle.class);
		Root<StudentActivity> student = criteriaQuery.from(StudentActivity.class);
		criteriaQuery = criteriaQuery.select(cb.construct(NewsArticleSummaryDTO.class, articleRoot.get("newsArticleId"),
				articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("publicationDate"),
				articleRoot.get("readingLevel"), articleGroupRoot.get("genreId"), articleGroupRoot.get("headline"),
				articleRoot.get("content"), articleRoot.get("authorWorked"), articleGroupRoot.get("credits"),
				(cb.selectCase()
						.when(cb.or(cb.equal(articleGroupRoot.get("noQuiz"), "Y"),
								cb.equal(articleGroupRoot.get("imageOnly"), "Y")), cb.literal("N"))
						.otherwise(cb.literal("Y"))),
				(cb.selectCase().when(cb.isNull(student.get("quizScore")), cb.literal("N")).otherwise(cb.literal("Y"))),
				student.<String>get("quizScore"), student.<String>get("saved"), student.<String>get("opinion"),
				student.<String>get("reaction"), articleRoot.get("likeHCount"), articleRoot.get("likeLCount"),
				articleRoot.get("likeOCount"), articleRoot.get("likeSCount"), articleRoot.get("likeWCount"),
				articleGroupRoot.get("cityId"), articleGroupRoot.get("imageName")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates
				.add(cb.equal(articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("newsArticleGroupId")));
		filterPredicates.add(cb.equal(student.get("newsArticleId"), articleRoot.get("newsArticleId")));
		filterPredicates.add(cb.equal(student.get("studentId"), searchRequest.getStudentId()));
		filterPredicates.add(cb.isNotNull(student.get("opinion")));

		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publicationDate"), searchRequest.getPublicationDate()));
		}

		if (searchRequest.getPublicationDateFrom() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleRoot.get("publicationDate"),
					searchRequest.getPublicationDateFrom()));
		}
		if (searchRequest.getPublicationDateTo() != null) {
			filterPredicates.add(
					cb.lessThanOrEqualTo(articleRoot.get("publicationDate"), searchRequest.getPublicationDateTo()));
		}

		if (searchRequest.getGenre() != null && (!"".equalsIgnoreCase(searchRequest.getGenre()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("genreId"), searchRequest.getGenre()));
		}
		if (searchRequest.getEditionId() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getHeadline() != null && (!"".equalsIgnoreCase(searchRequest.getHeadline()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("headline"), "%" + searchRequest.getHeadline() + "%"));
		}

		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));
		criteriaQuery.orderBy(cb.desc(articleRoot.get("publicationDate")), cb.asc(articleRoot.get("sequence")));

		// Build query
		TypedQuery<NewsArticleSummaryDTO> q = entityManager.createQuery(criteriaQuery);
		if (pageable.getPageSize() > 0) {
			int pageNumber = pageable.getPageNumber();
			q.setFirstResult(pageNumber * pageable.getPageSize());
			q.setMaxResults(pageable.getPageSize());

		}
		List<NewsArticleSummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, articleRoot);
		return new PageImpl<>(list, pageable, count);
	}
}
