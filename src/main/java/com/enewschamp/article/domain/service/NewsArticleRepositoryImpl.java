package com.enewschamp.article.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.student.entity.StudentActivity;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.app.dto.PublisherNewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.domain.common.AppConstants;
import com.enewschamp.domain.repository.RepositoryImpl;

public class NewsArticleRepositoryImpl extends RepositoryImpl implements NewsArticleRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<NewsArticleSummaryDTO> findArticles(NewsArticleSearchRequest searchRequest, Long studentId,
			Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<NewsArticleSummaryDTO> criteriaQuery = cb.createQuery(NewsArticleSummaryDTO.class);
		Root<NewsArticleGroup> articleGroupRoot = criteriaQuery.from(NewsArticleGroup.class);
		Root<NewsArticle> articleRoot = criteriaQuery.from(NewsArticle.class);
		Join<NewsArticle, StudentActivity> student = articleRoot.join("studentActivities", JoinType.LEFT); // left outer
		student.on(cb.equal(student.get("studentId"), studentId));
		criteriaQuery = criteriaQuery.select(cb.construct(NewsArticleSummaryDTO.class, articleRoot.get("newsArticleId"),
				articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("publicationDate"),
				articleRoot.get("readingLevel"), articleGroupRoot.get("genreId"), articleGroupRoot.get("headline"),
				articleRoot.get("content"), articleRoot.get("authorWorked"), articleGroupRoot.get("credits"),
				student.<String>get("quizAvailable"), student.<String>get("quizCompleted"),
				student.<String>get("quizScore"), student.<String>get("saved"), student.<String>get("opinion"),
				student.<String>get("likeLevel"), articleRoot.get("likeHCount"), articleRoot.get("likeLCount"),
				articleRoot.get("likeOCount"), articleRoot.get("likeSCount"), articleRoot.get("likeWCount"),
				articleGroupRoot.get("cityId"), articleGroupRoot.get("imageName")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();
		filterPredicates
				.add(cb.equal(articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("newsArticleGroupId")));
		filterPredicates.add(cb.equal(articleRoot.get("status"), ArticleStatusType.Published));
		filterPredicates.add(cb.lessThanOrEqualTo(articleRoot.get("publicationDate"), LocalDate.now()));

		if (searchRequest.getIsLinked() != null && ("Y".equalsIgnoreCase(searchRequest.getIsLinked().toString()))) {
			filterPredicates.add(cb.isNotNull(articleRoot.get("publicationId")));
		} else if (searchRequest.getIsLinked() != null
				&& ("N".equalsIgnoreCase(searchRequest.getIsLinked().toString()))) {
			filterPredicates.add(cb.isNull(articleRoot.get("publicationId")));
		}
		if (searchRequest.getArticleId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("newsArticleId"), searchRequest.getArticleId()));
		}
		if (searchRequest.getArticleGroupId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("newsArticleGroupId"), searchRequest.getArticleGroupId()));
		}
		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publicationDate"), searchRequest.getPublicationDate()));
		}
		if (searchRequest.getPublicationDateLimit() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleRoot.get("publicationDate"),
					searchRequest.getPublicationDateLimit()));
		}
		if (searchRequest.getPublicationDateFrom() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleRoot.get("publicationDate"),
					searchRequest.getPublicationDateFrom()));
		}
		if (searchRequest.getPublicationDateTo() != null) {
			filterPredicates.add(
					cb.lessThanOrEqualTo(articleRoot.get("publicationDate"), searchRequest.getPublicationDateTo()));
		}
		List<Predicate> readingLevelPredicates = buildReadingLevelPredicate(searchRequest, cb, articleRoot);
		if (readingLevelPredicates != null && !readingLevelPredicates.isEmpty()) {
			readingLevelPredicates.forEach(predicate -> {
				filterPredicates.add(predicate);
			});
		}
		if (searchRequest.getAuthorId() != null && (!"".equalsIgnoreCase(searchRequest.getAuthorId()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("authorId"), searchRequest.getAuthorId()));
		}
		if (searchRequest.getStatus() != null && searchRequest.getStatus().size() > 0) {
			filterPredicates.add(articleRoot.get("status").in(searchRequest.getStatus()));
		}
		if (searchRequest.getArticleType() != null
				&& (!"".equalsIgnoreCase(searchRequest.getArticleType().toString()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("articleType"), searchRequest.getArticleType()));
		}
		if (searchRequest.getGenreId() != null && (!"".equalsIgnoreCase(searchRequest.getGenreId()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("genreId"), searchRequest.getGenreId()));
		}
		if (searchRequest.getEditionId() != null && (!"".equalsIgnoreCase(searchRequest.getEditionId()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getHeadline() != null && (!"".equalsIgnoreCase(searchRequest.getHeadline()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("headline"), "%" + searchRequest.getHeadline() + "%"));
		}
		if (searchRequest.getCredits() != null && (!"".equalsIgnoreCase(searchRequest.getCredits()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("credits"), "%" + searchRequest.getCredits() + "%"));
		}
		if (searchRequest.getCityId() != null && (!"".equalsIgnoreCase(searchRequest.getCityId()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("cityId"), searchRequest.getCityId()));
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

	/*
	 * public List<NewsArticlePageData> getArticleDetaiils(Long newsArticleId, Long
	 * studentId) { Query query = entityManager.createNativeQuery(
	 * "select s.saved as saved,s.opinion as opinion,n.news_article_id as news_article_id,n.news_article_group_id as news_article_group_id "
	 * +
	 * "from news_article n left join news_article_group g on g.news_article_group_id=n.news_article_group_id "
	 * +
	 * "left join student_activity s on (s.student_id=?1 and s.news_article_id=n.news_article_id) "
	 * +
	 * "left join user u on u.user_id=n.author_worked where n.news_article_id=?2");
	 * query.setParameter(1, studentId); query.setParameter(2, newsArticleId);
	 * System.out.println(">>>>>>>query.getResultList()>>>>>>>>>>>>>>" +
	 * query.getResultList().size()); List<NewsArticlePageData> list =
	 * query.getResultList(); return list; }
	 */

	@Override
	public Page<PublisherNewsArticleSummaryDTO> findPublisherArticles(NewsArticleSearchRequest searchRequest,
			Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PublisherNewsArticleSummaryDTO> criteriaQuery = cb
				.createQuery(PublisherNewsArticleSummaryDTO.class);
		Root<NewsArticleGroup> articleGroupRoot = criteriaQuery.from(NewsArticleGroup.class);
		Root<NewsArticle> articleRoot = criteriaQuery.from(NewsArticle.class);
		criteriaQuery = criteriaQuery.select(cb.construct(PublisherNewsArticleSummaryDTO.class,
				articleRoot.get("newsArticleId"), articleRoot.get("newsArticleGroupId"),
				articleRoot.get("publicationDate"), articleRoot.get("readingLevel"), articleGroupRoot.get("authorId"),
				articleGroupRoot.get("editorId"), articleRoot.get("publisherWorked"), articleRoot.get("status"),
				articleGroupRoot.get("articleType"), articleRoot.get("content"), articleRoot.get("rating"),
				articleRoot.get("publicationId"), articleRoot.get("likeLCount"), articleRoot.get("likeHCount"),
				articleRoot.get("likeOCount"), articleRoot.get("likeWCount"), articleRoot.get("likeSCount"),
				articleRoot.get("authorWorked"), articleRoot.get("editorWorked"), articleRoot.get("publisherWorked"),
				articleGroupRoot.get("genreId"), articleGroupRoot.get("headline"), articleGroupRoot.get("cityId"),
				articleGroupRoot.get("credits"), articleGroupRoot.get("intendedPubDate"),
				articleGroupRoot.get("intendedPubMonth"), articleGroupRoot.get("intendedPubDay"),
				articleGroupRoot.get("targetCompletionDate"), articleGroupRoot.get("url")));

		// Build filter conditions
		List<Predicate> filterPredicates = new ArrayList<>();

		filterPredicates
				.add(cb.equal(articleGroupRoot.get("newsArticleGroupId"), articleRoot.get("newsArticleGroupId")));

		if (searchRequest.getIsLinked() != null && ("Y".equalsIgnoreCase(searchRequest.getIsLinked().toString()))) {
			filterPredicates.add(cb.isNotNull(articleRoot.get("publicationId")));
		} else if (searchRequest.getIsLinked() != null
				&& ("N".equalsIgnoreCase(searchRequest.getIsLinked().toString()))) {
			filterPredicates.add(cb.isNull(articleRoot.get("publicationId")));
		}
		if (searchRequest.getArticleId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("newsArticleId"), searchRequest.getArticleId()));
		}
		if (searchRequest.getArticleGroupId() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("newsArticleGroupId"), searchRequest.getArticleGroupId()));
		}
		if (searchRequest.getPublicationDate() != null) {
			filterPredicates.add(cb.equal(articleRoot.get("publicationDate"), searchRequest.getPublicationDate()));
		}
		if (searchRequest.getIntendedPubMonth() != null) {
			filterPredicates
					.add(cb.equal(articleGroupRoot.get("intendedPubMonth"), searchRequest.getIntendedPubMonth()));
		}
		if (searchRequest.getIntendedPubDay() != null) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("intendedPubDay"), searchRequest.getIntendedPubDay()));
		}
		if (searchRequest.getPublicationDateLimit() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleRoot.get("publicationDate"),
					searchRequest.getPublicationDateLimit()));
		}
		if (searchRequest.getPublicationDateFrom() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleRoot.get("publicationDate"),
					searchRequest.getPublicationDateFrom()));
		}
		if (searchRequest.getPublicationDateTo() != null) {
			filterPredicates.add(
					cb.lessThanOrEqualTo(articleRoot.get("publicationDate"), searchRequest.getPublicationDateTo()));
		}
		if (searchRequest.getIntendedPubDateFrom() != null) {
			filterPredicates.add(cb.greaterThanOrEqualTo(articleGroupRoot.get("intendedPubDate"),
					searchRequest.getIntendedPubDateFrom()));
		}
		if (searchRequest.getIntendedPubDateTo() != null) {
			filterPredicates.add(cb.lessThanOrEqualTo(articleGroupRoot.get("intendedPubDate"),
					searchRequest.getIntendedPubDateTo()));
		}
		List<Predicate> readingLevelPredicates = buildReadingLevelPredicate(searchRequest, cb, articleRoot);
		if (readingLevelPredicates != null && !readingLevelPredicates.isEmpty()) {
			readingLevelPredicates.forEach(predicate -> {
				filterPredicates.add(predicate);
			});
		}
		if (searchRequest.getAuthorId() != null && (!"".equalsIgnoreCase(searchRequest.getAuthorId()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("authorId"), searchRequest.getAuthorId()));
		}
		if (searchRequest.getEditorId() != null && (!"".equalsIgnoreCase(searchRequest.getEditorId()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editorId"), searchRequest.getEditorId()));
		}
		if (searchRequest.getPublisherId() != null && (!"".equalsIgnoreCase(searchRequest.getPublisherId()))) {
			filterPredicates.add(cb.equal(articleRoot.get("publisherWorked"), searchRequest.getPublisherId()));
		}
		if (searchRequest.getStatus() != null && searchRequest.getStatus().size() > 0) {
			filterPredicates.add(articleRoot.get("status").in(searchRequest.getStatus()));
		}
		if (searchRequest.getArticleType() != null
				&& (!"".equalsIgnoreCase(searchRequest.getArticleType().toString()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("articleType"), searchRequest.getArticleType()));
		}
		if (searchRequest.getGenreId() != null && (!"".equalsIgnoreCase(searchRequest.getGenreId()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("genreId"), searchRequest.getGenreId()));
		}
		if (searchRequest.getEditionId() != null && (!"".equalsIgnoreCase(searchRequest.getEditionId()))) {
			filterPredicates.add(cb.equal(articleGroupRoot.get("editionId"), searchRequest.getEditionId()));
		}
		if (searchRequest.getHeadline() != null && (!"".equalsIgnoreCase(searchRequest.getHeadline()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("headline"), "%" + searchRequest.getHeadline() + "%"));
		}
		if (searchRequest.getCredits() != null && (!"".equalsIgnoreCase(searchRequest.getCredits()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("credits"), "%" + searchRequest.getCredits() + "%"));
		}
		if (searchRequest.getCityId() != null && (!"".equalsIgnoreCase(searchRequest.getCityId()))) {
			filterPredicates.add(cb.like(articleGroupRoot.get("cityId"), searchRequest.getCityId()));
		}
		criteriaQuery.where(cb.and((Predicate[]) filterPredicates.toArray(new Predicate[0])));

		// Build query
		TypedQuery<PublisherNewsArticleSummaryDTO> q = entityManager.createQuery(criteriaQuery);
		List<PublisherNewsArticleSummaryDTO> list = q.getResultList();
		long count = getRecordCount(criteriaQuery, filterPredicates, articleRoot);
		return new PageImpl<>(list, pageable, count);
	}

	private List<Predicate> buildReadingLevelPredicate(NewsArticleSearchRequest searchRequest, CriteriaBuilder cb,
			Root<NewsArticle> articleRoot) {
		List<Predicate> readingLevelOrPredicates = new ArrayList<>();
		List<Predicate> readingLevelAndPredicates = new ArrayList<>();

		if (searchRequest.getReadingLevel1() != null) {
			if (searchRequest.getReadingLevel1().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(articleRoot.get("readingLevel"), 1));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 1));
			}
		}
		if (searchRequest.getReadingLevel2() != null) {
			if (searchRequest.getReadingLevel2().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(articleRoot.get("readingLevel"), 2));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 2));
			}
		}
		if (searchRequest.getReadingLevel3() != null) {
			if (searchRequest.getReadingLevel3().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(articleRoot.get("readingLevel"), 3));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 3));
			}
		}
		if (searchRequest.getReadingLevel4() != null) {
			if (searchRequest.getReadingLevel4().equals(AppConstants.YES)) {
				readingLevelOrPredicates.add(cb.equal(articleRoot.get("readingLevel"), 4));
			} else {
				readingLevelAndPredicates.add(cb.notEqual(articleRoot.get("readingLevel"), 4));
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
