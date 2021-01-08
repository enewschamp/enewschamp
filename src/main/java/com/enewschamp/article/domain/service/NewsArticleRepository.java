package com.enewschamp.article.domain.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.entity.NewsArticle;

@JaversSpringDataAuditable
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {

	@Query(value = "SELECT a FROM NewsArticle a" + " where a.newsArticleGroupId = :newsArticleGroupId")
	public List<NewsArticle> findByNewsArticleGroupId(@Param("newsArticleGroupId") long newsArticleGroupId);

	@Query(value = "SELECT a FROM NewsArticle a" + " where a.publicationId = :publicationId")
	public List<NewsArticle> findByPublicationId(@Param("publicationId") long publicationId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM NewsArticle a" + " where a.newsArticleGroupId = :newsArticleGroupId")
	public void deleteByArticleGroupId(@Param("newsArticleGroupId") long newsArticleGroupId);

	@Query(value = "SELECT a.status FROM NewsArticle a" + " where a.newsArticleId = :articleId")
	public ArticleStatusType getCurrentStatus(@Param("articleId") long articleId);

	@Query(value = "SELECT a.previousStatus FROM NewsArticle a" + " where a.newsArticleId = :articleId")
	public ArticleStatusType getPreviousStatus(@Param("articleId") long articleId);

	@Query(value = "SELECT min(a.publicationDate) FROM NewsArticle a,NewsArticleGroup b"
			+ " where a.newsArticleGroupId=b.newsArticleGroupId and a.publicationDate > :givenDate"
			+ " and b.editionId = :editionId and b.articleType= :articleType"
			+ " and a.readingLevel = :readingLevel and a.status='Published' and a.publicationDate<=current_date")
	public LocalDate getNextAvailablePublicationDate(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType);

	@Query(value = "SELECT min(a.publicationDate) FROM NewsArticle a,NewsArticleGroup b"
			+ " where a.newsArticleGroupId=b.newsArticleGroupId and a.publicationDate > :givenDate"
			+ " and b.editionId = :editionId and b.articleType= :articleType"
			+ " and a.readingLevel = :readingLevel and ((a.status='Published' and a.publicationDate<=current_date) or (a.readyForTest='Y'))")
	public LocalDate getNextAvailablePublicationDateTestUser(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType);

	@Query(value = "SELECT max(a.publicationDate) FROM NewsArticle a,NewsArticleGroup b"
			+ " where a.newsArticleGroupId=b.newsArticleGroupId and b.editionId = :editionId and b.articleType= :articleType"
			+ " and a.readingLevel = :readingLevel and a.status='Published' and a.publicationDate<=current_date")
	public LocalDate getLatestPublicationDate(@Param("editionId") String editionId,
			@Param("readingLevel") int readingLevel, @Param("articleType") ArticleType articleType);

	@Query(value = "SELECT max(a.publicationDate) FROM NewsArticle a,NewsArticleGroup b"
			+ " where a.newsArticleGroupId=b.newsArticleGroupId and b.editionId = :editionId and b.articleType= :articleType"
			+ " and a.readingLevel = :readingLevel and ((a.status='Published' and a.publicationDate<=current_date) or (a.readyForTest='Y'))")
	public LocalDate getLatestPublicationDateTestUser(@Param("editionId") String editionId,
			@Param("readingLevel") int readingLevel, @Param("articleType") ArticleType articleType);

	@Query(value = "SELECT max(a.publicationDate) FROM NewsArticle a,NewsArticleGroup b"
			+ " where a.newsArticleGroupId=b.newsArticleGroupId and a.publicationDate < :givenDate"
			+ " and b.editionId = :editionId and b.articleType= :articleType"
			+ " and a.readingLevel = :readingLevel and a.status='Published' and a.publicationDate<=current_date")
	public LocalDate getPreviousAvailablePublicationDate(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType);

	@Query(value = "SELECT max(a.publicationDate) FROM NewsArticle a,NewsArticleGroup b"
			+ " where a.newsArticleGroupId=b.newsArticleGroupId and a.publicationDate < :givenDate"
			+ " and b.editionId = :editionId and b.articleType= :articleType"
			+ " and a.readingLevel = :readingLevel and ((a.status='Published' and a.publicationDate<=current_date) or (a.readyForTest='Y'))")
	public LocalDate getPreviousAvailablePublicationDateTestUser(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType);

	@Query(value = "SELECT a.newsArticleId FROM NewsArticle a where a.publicationDate=:givenDate"
			+ " and a.readingLevel = :readingLevel and a.status='Published' and a.sequence=(select min(d.sequence) from NewsArticle d,NewsArticleGroup b where d.newsArticleGroupId=b.newsArticleGroupId and d.publicationDate=:givenDate  and b.editionId = :editionId and b.articleType= :articleType and d.readingLevel = :readingLevel and d.status='Published' and d.sequence>(select c.sequence from NewsArticle c where c.newsArticleId=:newsArticleId))")
	public Long getNextNewsArticleAvailable(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType, @Param("newsArticleId") Long newsArticleId);

	@Query(value = "SELECT a.newsArticleId FROM NewsArticle a where a.publicationDate=:givenDate"
			+ " and a.readingLevel = :readingLevel and (a.status='Published' or a.readyForTest='Y') and a.sequence=(select min(d.sequence) from NewsArticle d,NewsArticleGroup b where d.newsArticleGroupId=b.newsArticleGroupId and d.publicationDate=:givenDate  and b.editionId = :editionId and b.articleType= :articleType and d.readingLevel = :readingLevel and (d.status='Published' or d.readyForTest='Y')and d.sequence>(select c.sequence from NewsArticle c where c.newsArticleId=:newsArticleId))")
	public Long getNextNewsArticleAvailableTestUser(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType, @Param("newsArticleId") Long newsArticleId);

	@Query(value = "SELECT a.newsArticleId FROM NewsArticle a where a.publicationDate=:givenDate"
			+ " and a.readingLevel = :readingLevel and a.status='Published' and a.sequence=(select max(d.sequence) from NewsArticle d,NewsArticleGroup b where d.newsArticleGroupId=b.newsArticleGroupId and d.publicationDate=:givenDate  and b.editionId = :editionId and b.articleType= :articleType and d.readingLevel = :readingLevel and d.status='Published' and d.sequence<(select c.sequence from NewsArticle c where c.newsArticleId=:newsArticleId))")
	public Long getPreviousNewsArticleAvailable(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType, @Param("newsArticleId") Long newsArticleId);

	@Query(value = "SELECT a.newsArticleId FROM NewsArticle a where a.publicationDate=:givenDate"
			+ " and a.readingLevel = :readingLevel and (a.status='Published' or a.readyForTest='Y') and a.sequence=(select max(d.sequence) from NewsArticle d,NewsArticleGroup b where d.newsArticleGroupId=b.newsArticleGroupId and d.publicationDate=:givenDate  and b.editionId = :editionId and b.articleType= :articleType and d.readingLevel = :readingLevel and (d.status='Published' or d.readyForTest='Y') and d.sequence<(select c.sequence from NewsArticle c where c.newsArticleId=:newsArticleId))")
	public Long getPreviousNewsArticleAvailableTestUser(@Param("givenDate") LocalDate givenDate,
			@Param("editionId") String editionId, @Param("readingLevel") int readingLevel,
			@Param("articleType") ArticleType articleType, @Param("newsArticleId") Long newsArticleId);

	@Query(value = "SELECT (CASE WHEN (b.noQuiz='Y' OR b.imageOnly='Y') THEN 'N' ELSE 'Y' END) AS quizAvailable from NewsArticle d,NewsArticleGroup b where d.newsArticleId=:newsArticleId and d.newsArticleGroupId=b.newsArticleGroupId")
	public String isQuizAvailable(@Param("newsArticleId") Long newsArticleId);
}