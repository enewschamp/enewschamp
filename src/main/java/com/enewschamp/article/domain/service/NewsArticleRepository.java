package com.enewschamp.article.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;

@JaversSpringDataAuditable
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long>{ 

	public List<NewsArticle> findByNewsArticleGroupId(long newsArticleGroupId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM NewsArticle a"
			+ " where a.newsArticleGroupId = :newsArticleGroupId")
	public void deleteByArticleGroupId(long newsArticleGroupId);
	
	@Query(value = "SELECT a.status FROM NewsArticle a"
			+ " where a.newsArticleId = :articleId")
	public ArticleStatusType getArticleStatusType(long articleId);
} 