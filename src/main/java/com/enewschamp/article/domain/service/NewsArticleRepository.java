package com.enewschamp.article.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;

@JaversSpringDataAuditable
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long>{ 

	public List<NewsArticle> findByNewsArticleGroupId(long newsArticleGroupId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM NewsArticle a"
			+ " where a.newsArticleGroupId = :newsArticleGroupId")
	public void deleteByArticleGroupId(@Param("newsArticleGroupId") long newsArticleGroupId);
	
	@Query(value = "SELECT a.status FROM NewsArticle a"
			+ " where a.newsArticleId = :articleId")
	public ArticleStatusType getCurrentStatus(@Param("articleId") long articleId);
	
	@Query(value = "SELECT a.previousStatus FROM NewsArticle a"
			+ " where a.newsArticleId = :articleId")
	public ArticleStatusType getPreviousStatus(@Param("articleId") long articleId);
} 