package com.enewschamp.article.domain.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.article.domain.entity.NewsArticle;

public interface NewsArticleNoAuditRepository extends JpaRepository<NewsArticle, Long> {

}