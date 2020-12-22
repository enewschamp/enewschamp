package com.enewschamp.app.school.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;

public interface NewsArticleSummaryRepository extends JpaRepository<NewsArticleSummaryDTO, Long> {

	@Query(value = "select new NewsArticleSummaryDTO(n.newsArticleId,n.newsArticleGroupId,n.publicationDate,n.readingLevel,g.genreId as genre,g.headline,n.content,u.name||' '||u.surname as author,g.credits,'Y' as quizAvailable,'Y' as quizCompleted,s.quizScore as quizScore,s.saved,s.opinion as opinionText,s.likeLevel as reaction,n.likeHCount as reactionHCount,n.likeLCount as reactionLCount,n.likeOCount as reactionOCount,n.likeSCount as reactionSCount,n.likeWCount as reactionWCount,g.cityId as city,g.imageName) from NewsArticle n left join NewsArticleGroup g on g.newsArticleGroupId=n.newsArticleGroupId left join StudentActivity s on (s.studentId=:studentId and s.newsArticleId=n.newsArticleId) left join User u on u.userId=n.authorWorked where n.newsArticleId=:newsArticleId")
	public List<NewsArticleSummaryDTO> getArticleDetails(@Param("newsArticleId") Long newsArticleId,
			@Param("studentId") Long studentId);

}
