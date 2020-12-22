package com.enewschamp.app.student.monthlytrends.business;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyTotalDTO;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyTotal;
import com.enewschamp.app.student.monthlytrends.service.TrendsMonthlyTotalService;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class TrendsMonthlyTotalBusiness {

	@Autowired
	TrendsMonthlyTotalService trendsMonthlyService;

	@Autowired
	NewsArticleService newsArticleService;

	@Autowired
	ModelMapper modelMapper;

	public TrendsMonthlyTotalDTO saveMonthlyTrend(TrendsMonthlyTotalDTO trendsMonthlyTotalDTO) {
		TrendsMonthlyTotal trendsMonthlyTotal = modelMapper.map(trendsMonthlyTotalDTO, TrendsMonthlyTotal.class);

		// TO DO to be corrected
		trendsMonthlyTotal.setRecordInUse(RecordInUseType.Y);
		trendsMonthlyTotal.setOperatorId("SYSTEM");
		trendsMonthlyTotal = trendsMonthlyService.create(trendsMonthlyTotal);
		TrendsMonthlyTotalDTO trendsMonthlytotslDTOnew = modelMapper.map(trendsMonthlyTotal,
				TrendsMonthlyTotalDTO.class);

		return trendsMonthlytotslDTOnew;
	}

	public TrendsMonthlyTotalDTO getMonthlyTrend(Long studentId, String editionId, int readingLevel, String monthYear) {
		TrendsMonthlyTotal trendsMonthlyTotal = null;

		try {
			trendsMonthlyTotal = trendsMonthlyService.getMonthlyTrends(studentId, editionId, readingLevel, monthYear);
		} catch (Exception e) {
			return null;
		}
		if (trendsMonthlyTotal != null) {
			TrendsMonthlyTotalDTO trendsMonthlyByGenreDTO = modelMapper.map(trendsMonthlyTotal,
					TrendsMonthlyTotalDTO.class);
			return trendsMonthlyByGenreDTO;
		} else
			return null;
	}

	public TrendsMonthlyTotalDTO saveQuizData(Long newsArticleId, Long studentId, String editionId, int readingLevel,
			Long quizQAttempted, Long quizQCorrect) {
		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		LocalDate publicationDate = newsArticle.getPublicationDate();
		int year = publicationDate.getYear();
		int month = publicationDate.getMonthValue();
		String monthYear = year + "" + (month > 9 ? month : "0" + month);
		TrendsMonthlyTotalDTO trendsMonthlyTotalDTO = this.getMonthlyTrend(studentId, editionId, readingLevel,
				monthYear);
		if (trendsMonthlyTotalDTO == null) {
			TrendsMonthlyTotalDTO trendsMonthlyTotalDTONew = new TrendsMonthlyTotalDTO();
			trendsMonthlyTotalDTONew.setStudentId(studentId);
			trendsMonthlyTotalDTONew.setEditionId(editionId);
			trendsMonthlyTotalDTONew.setArticlesRead(Long.valueOf(1));
			trendsMonthlyTotalDTONew.setReadingLevel(readingLevel);
			trendsMonthlyTotalDTONew.setYearMonth(monthYear);
			trendsMonthlyTotalDTONew.setQuizAttempted(quizQAttempted);
			trendsMonthlyTotalDTONew.setQuizCorrect(quizQCorrect);
			trendsMonthlyTotalDTO = saveMonthlyTrend(trendsMonthlyTotalDTONew);
		} else {
			Long quizQAttemptedTmp = (trendsMonthlyTotalDTO.getQuizAttempted() == null) ? 0
					: trendsMonthlyTotalDTO.getQuizAttempted();
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;

			Long articlesReadTmp = (trendsMonthlyTotalDTO.getArticlesRead() == null) ? 0
					: trendsMonthlyTotalDTO.getArticlesRead();
			articlesReadTmp = articlesReadTmp + 1;
			trendsMonthlyTotalDTO.setQuizAttempted(quizQAttemptedTmp);
			trendsMonthlyTotalDTO.setArticlesRead(articlesReadTmp);
			Long quizQCorrectTmp = (trendsMonthlyTotalDTO.getQuizCorrect() == null) ? 0
					: trendsMonthlyTotalDTO.getQuizCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			trendsMonthlyTotalDTO.setQuizCorrect(quizQCorrectTmp);
			trendsMonthlyTotalDTO = saveMonthlyTrend(trendsMonthlyTotalDTO);

		}
		return trendsMonthlyTotalDTO;
	}
}
