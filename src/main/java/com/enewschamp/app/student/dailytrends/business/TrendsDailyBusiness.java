package com.enewschamp.app.student.dailytrends.business;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.dailytrends.dto.TrendsDailyDTO;
import com.enewschamp.app.student.dailytrends.entity.TrendsDaily;
import com.enewschamp.app.student.dailytrends.service.TrendsDailyService;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class TrendsDailyBusiness {

	@Autowired
	TrendsDailyService trendsDailyService;

	@Autowired
	NewsArticleService newsArticleService;

	@Autowired
	ModelMapper modelMapper;

	public TrendsDailyDTO saveDailyTrend(TrendsDailyDTO studentActivityDTO) {
		TrendsDaily trendsDaily = modelMapper.map(studentActivityDTO, TrendsDaily.class);
		trendsDaily.setRecordInUse(RecordInUseType.Y);
		trendsDaily.setOperatorId("SYSTEM");
		trendsDaily = trendsDailyService.create(trendsDaily);
		TrendsDailyDTO studentActivityNew = modelMapper.map(trendsDaily, TrendsDailyDTO.class);
		return studentActivityNew;
	}

	public TrendsDailyDTO getDailyTrend(Long studentId, String editionId, int readingLevel, LocalDate quizdate) {
		TrendsDaily studentActivity = null;
		try {
			studentActivity = trendsDailyService.getDailyTrend(studentId, editionId, readingLevel, quizdate);
		} catch (Exception e) {
			return null;
		}
		if (studentActivity != null) {
			TrendsDailyDTO studentActivityDTO = modelMapper.map(studentActivity, TrendsDailyDTO.class);

			return studentActivityDTO;
		} else
			return null;
	}

	public TrendsDailyDTO saveQuizData(Long newsArticleId, Long studentId, String editionId, int readingLevel,
			Long quizQAttempted, Long quizQCorrect) {
		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		LocalDate publicationDate = newsArticle.getPublicationDate();
		TrendsDailyDTO trendsDailyDTO = getDailyTrend(studentId, editionId, readingLevel, publicationDate);
		if (trendsDailyDTO == null) {
			TrendsDailyDTO trendsDailyDTONew = new TrendsDailyDTO();
			trendsDailyDTONew.setStudentId(studentId);
			trendsDailyDTONew.setEditionId(editionId);
			trendsDailyDTONew.setReadingLevel(readingLevel);
			trendsDailyDTONew.setQuizPublicationDate(publicationDate);
			trendsDailyDTONew.setArticlesRead(Long.valueOf(1));
			trendsDailyDTONew.setQuizAttempted(quizQAttempted);
			trendsDailyDTONew.setQuizCorrect(quizQCorrect);
			trendsDailyDTO = saveDailyTrend(trendsDailyDTONew);
		} else {
			Long quizQAttemptedTmp = (trendsDailyDTO.getQuizAttempted() == null) ? 0
					: trendsDailyDTO.getQuizAttempted();
			Long articlesReadTmp = (trendsDailyDTO.getArticlesRead() == null) ? 0 : trendsDailyDTO.getArticlesRead();
			articlesReadTmp = articlesReadTmp + 1;
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;
			trendsDailyDTO.setQuizAttempted(quizQAttemptedTmp);
			trendsDailyDTO.setArticlesRead(articlesReadTmp);
			Long quizQCorrectTmp = (trendsDailyDTO.getQuizCorrect() == null) ? 0 : trendsDailyDTO.getQuizCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			trendsDailyDTO.setQuizCorrect(quizQCorrectTmp);
			trendsDailyDTO = saveDailyTrend(trendsDailyDTO);
		}
		return trendsDailyDTO;
	}
}
