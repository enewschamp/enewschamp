package com.enewschamp.app.student.monthlytrends.business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyByGenreDTO;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyByGenre;
import com.enewschamp.app.student.monthlytrends.service.TrendsMonthlyByGenreService;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class TrendsMonthlyByGenreBusiness {

	@Autowired
	TrendsMonthlyByGenreService trendsMonthlyService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	NewsArticleService newsArticleService;

	@Autowired
	NewsArticleGroupService newsArticleGroupService;

	public TrendsMonthlyByGenreDTO saveMonthlyTrend(TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO) {
		TrendsMonthlyByGenre trendsMonthlyByGenre = modelMapper.map(trendsMonthlyByGenreDTO,
				TrendsMonthlyByGenre.class);

		// TO DO to be corrected
		trendsMonthlyByGenre.setRecordInUse(RecordInUseType.Y);
		trendsMonthlyByGenre.setOperatorId("SYSTEM");

		trendsMonthlyByGenre = trendsMonthlyService.create(trendsMonthlyByGenre);
		TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTOnew = modelMapper.map(trendsMonthlyByGenre,
				TrendsMonthlyByGenreDTO.class);

		return trendsMonthlyByGenreDTOnew;
	}

	public TrendsMonthlyByGenreDTO getMonthlyTrend(Long studentId, String editionId, int readingLevel, String monthYear,
			String genreId) {
		TrendsMonthlyByGenre trendsMonthlyByGenre = null;
		try {
			trendsMonthlyByGenre = trendsMonthlyService.getMonthlyTrend(studentId, editionId, readingLevel, monthYear,
					genreId);
		} catch (Exception e) {
			return null;
		}
		if (trendsMonthlyByGenre != null) {
			TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO = modelMapper.map(trendsMonthlyByGenre,
					TrendsMonthlyByGenreDTO.class);
			return trendsMonthlyByGenreDTO;
		} else
			return null;
	}

	public TrendsMonthlyByGenreDTO saveQuizData(Long newsArticleId, Long studentId, String editionId, int readingLevel,
			Long quizQAttempted, Long quizQCorrect) {
		// get the genre Id..
		String genreId = getGenreId(newsArticleId);

		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		LocalDate publicationDate = newsArticle.getPublicationDate();
		int year = publicationDate.getYear();
		int month = publicationDate.getMonthValue();
		String monthYear = year + "" + (month > 9 ? month : "0" + month);

		TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO = getMonthlyTrend(studentId, editionId, readingLevel, monthYear,
				genreId);
		if (trendsMonthlyByGenreDTO == null) {
			TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTONew = new TrendsMonthlyByGenreDTO();
			trendsMonthlyByGenreDTONew.setStudentId(studentId);
			trendsMonthlyByGenreDTONew.setEditionId(editionId);
			trendsMonthlyByGenreDTONew.setGenreId(genreId);
			trendsMonthlyByGenreDTONew.setArticlesRead(Long.valueOf(1));
			trendsMonthlyByGenreDTONew.setReadingLevel(readingLevel);
			trendsMonthlyByGenreDTONew.setQuizAttempted(quizQAttempted);
			trendsMonthlyByGenreDTONew.setQuizCorrect(quizQCorrect);
			trendsMonthlyByGenreDTONew.setYearMonth(Long.valueOf(monthYear));
			trendsMonthlyByGenreDTO = saveMonthlyTrend(trendsMonthlyByGenreDTONew);

		} else {
			Long quizQAttemptedTmp = (trendsMonthlyByGenreDTO.getQuizAttempted() == null) ? 0
					: trendsMonthlyByGenreDTO.getQuizAttempted();
			Long articlesReadTmp = (trendsMonthlyByGenreDTO.getArticlesRead() == null) ? 0
					: trendsMonthlyByGenreDTO.getArticlesRead();
			articlesReadTmp = articlesReadTmp + 1;
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;
			trendsMonthlyByGenreDTO.setArticlesRead(articlesReadTmp);
			trendsMonthlyByGenreDTO.setQuizAttempted(quizQAttemptedTmp);

			Long quizQCorrectTmp = (trendsMonthlyByGenreDTO.getQuizCorrect() == null) ? 0
					: trendsMonthlyByGenreDTO.getQuizCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			trendsMonthlyByGenreDTO.setQuizCorrect(quizQCorrectTmp);

			trendsMonthlyByGenreDTO = saveMonthlyTrend(trendsMonthlyByGenreDTO);
		}
		return trendsMonthlyByGenreDTO;
	}

	public String getGenreId(Long newsArticleId) {
		// TO DO Code to be written to fetch the genre Id..
		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		if (newsArticle == null) {
			throw new BusinessException(ErrorCodeConstants.ARTICLE_NOT_FOUND);

		}
		Long groupId = newsArticle.getNewsArticleGroupId();

		NewsArticleGroup newsArticleGroup = newsArticleGroupService.get(groupId);
		if (newsArticleGroup == null) {
			throw new BusinessException(ErrorCodeConstants.ARTICLE_GROUP_NOT_FOUND);

		}
		String genre = newsArticleGroup.getGenreId();

		return genre;
	}
}
