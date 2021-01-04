package com.enewschamp.app.school.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.app.dto.NewsArticleViewDTO;

@Repository
public class NewsArticleSummaryRepositoryImpl implements NewsArticleSummaryRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<NewsArticleSummaryDTO> getArticleDetails(Long newsArticleId, Long studentId) {
		Query query = entityManager.createNativeQuery(
				"SELECT n.news_article_id,n.news_article_group_id,n.publication_date,n.reading_level,g.genre_id AS genre,\n"
						+ "g.headline,n.content,u.name||' '||u.surname AS author,g.credits,(CASE WHEN (g.no_quiz=1 OR g.image_only=1) THEN 'N' ELSE 'Y' END) AS quiz_available,(CASE WHEN s.quiz_score is null THEN 'N' ELSE 'Y' END) AS quiz_completed,\n"
						+ "s.quiz_score AS quiz_score,s.saved,s.opinion AS opinion_text,s.reaction AS reaction,n.likehcount AS reactionHCount,\n"
						+ "n.likelcount AS reactionLCount,n.likeocount AS reactionOCount,n.likescount AS reactionSCount,\n"
						+ "n.likewcount AS reactionWCount,g.city_id AS city,g.image_name FROM news_article n \n"
						+ "LEFT JOIN news_article_group g ON g.news_article_group_id=n.news_article_group_id \n"
						+ "LEFT JOIN student_activity s ON (s.student_id=?1 AND s.news_article_id=n.news_article_id) \n"
						+ "LEFT JOIN USER u ON u.user_id=n.author_worked WHERE n.news_article_id=?2",
				NewsArticleViewDTO.class);
		query.setParameter(1, studentId);
		query.setParameter(2, newsArticleId);
		List<NewsArticleSummaryDTO> list = query.getResultList();
		return list;
	}

}
