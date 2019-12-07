package com.enewschamp.article.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.article.domain.entity.NewsArticleQuiz;

@JaversSpringDataAuditable
public interface NewsArticleQuizRepository extends JpaRepository<NewsArticleQuiz, Long> {

	public List<NewsArticleQuiz> findByNewsArticleId(long articleId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM NewsArticleQuiz a" + " where a.newsArticleId = :articleId")
	public void deleteByArticleId(@Param("articleId") long articleId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM NewsArticleQuiz a" + " where a.newsArticleQuizId = :articleQuizId")
	public void deleteById(@Param("articleQuizId") long articleQuizId);

	@Query("Select n from NewsArticleQuiz n where n.newsArticleId= :newsArticleId and n.recordInUse='Y'")
	public List<NewsArticleQuiz> findByNewsArticle(@Param("newsArticleId") long articleId);

}