package com.enewschamp.article.domain.service;

import java.util.List;

import javax.transaction.Transactional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.article.domain.entity.NewsArticleQuiz;

@JaversSpringDataAuditable
public interface NewsArticleQuizRepository extends JpaRepository<NewsArticleQuiz, Long>{ 

	public List<NewsArticleQuiz> findByNewsArticleId(long articleId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM NewsArticleQuiz a"
			+ " where a.newsArticleId = :articleId")
	public void deleteByArticleId(long articleId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM NewsArticleQuiz a"
			+ " where a.newsArticleQuizId = :articleQuizId")
	public void deleteById(long articleQuizId);
} 