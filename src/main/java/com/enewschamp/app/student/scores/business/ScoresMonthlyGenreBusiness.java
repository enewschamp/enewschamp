package com.enewschamp.app.student.scores.business;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.scores.dto.ScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.entity.ScoresMonthlyGenre;
import com.enewschamp.app.student.scores.service.ScoresMonthlyGenreService;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.problem.BusinessException;

@Service
public class ScoresMonthlyGenreBusiness {

	@Autowired
	ScoresMonthlyGenreService scoresMonthlyGenreService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	NewsArticleService newsArticleService;

	@Autowired
	NewsArticleGroupService newsArticleGroupService;

	public ScoresMonthlyGenreDTO saveScoresMonthly(ScoresMonthlyGenreDTO scoresMonthlyGenreDTO) {
		ScoresMonthlyGenre scoresMonthlyGenre = modelMapper.map(scoresMonthlyGenreDTO, ScoresMonthlyGenre.class);
		scoresMonthlyGenre = scoresMonthlyGenreService.create(scoresMonthlyGenre);
		ScoresMonthlyGenreDTO scoresMonthlyGenreDTOnew = modelMapper.map(scoresMonthlyGenre,
				ScoresMonthlyGenreDTO.class);
		return scoresMonthlyGenreDTOnew;
	}

	public ScoresMonthlyGenreDTO getScoresMonthly(Long studentId, String editionId, int readingLevel, String yearMonth,
			String genreId) {
		ScoresMonthlyGenre scoresMonthlyGenre = null;
		try {
			scoresMonthlyGenre = scoresMonthlyGenreService.getScoresMonthly(studentId, editionId, readingLevel,
					yearMonth, genreId);
		} catch (Exception e) {
			return null;
		}
		if (scoresMonthlyGenre != null) {
			ScoresMonthlyGenreDTO scoresMonthlyGenreDTO = modelMapper.map(scoresMonthlyGenre,
					ScoresMonthlyGenreDTO.class);
			return scoresMonthlyGenreDTO;
		} else
			return null;
	}

	public ScoresMonthlyGenreDTO saveQuizData(Long newsArticleId, Long studentId, String editionId, int readingLevel,
			Long quizQAttempted, Long quizQCorrect) {
		// get the genre Id..
		String genreId = getGenreId(newsArticleId);

		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		LocalDate publicationDate = newsArticle.getPublicationDate();
		int year = publicationDate.getYear();
		int month = publicationDate.getMonthValue();
		String yearMonth = year + "" + (month > 9 ? month : "0" + month);

		ScoresMonthlyGenreDTO scoresMonthlyGenreDTO = getScoresMonthly(studentId, editionId, readingLevel, yearMonth,
				genreId);
		if (scoresMonthlyGenreDTO == null) {
			ScoresMonthlyGenreDTO scoresMonthlyGenreDTONew = new ScoresMonthlyGenreDTO();
			scoresMonthlyGenreDTONew.setStudentId(studentId);
			scoresMonthlyGenreDTONew.setEditionId(editionId);
			scoresMonthlyGenreDTONew.setGenreId(genreId);
			scoresMonthlyGenreDTONew.setArticlesRead(Long.valueOf(1));
			scoresMonthlyGenreDTONew.setReadingLevel(readingLevel);
			scoresMonthlyGenreDTONew.setQuizAttempted(quizQAttempted);
			scoresMonthlyGenreDTONew.setQuizCorrect(quizQCorrect);
			scoresMonthlyGenreDTONew.setYearMonth(Long.valueOf(yearMonth));
			scoresMonthlyGenreDTO = saveScoresMonthly(scoresMonthlyGenreDTONew);

		} else {
			Long quizQAttemptedTmp = (scoresMonthlyGenreDTO.getQuizAttempted() == null) ? 0
					: scoresMonthlyGenreDTO.getQuizAttempted();
			Long articlesReadTmp = (scoresMonthlyGenreDTO.getArticlesRead() == null) ? 0
					: scoresMonthlyGenreDTO.getArticlesRead();
			articlesReadTmp = articlesReadTmp + 1;
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;
			scoresMonthlyGenreDTO.setArticlesRead(articlesReadTmp);
			scoresMonthlyGenreDTO.setQuizAttempted(quizQAttemptedTmp);

			Long quizQCorrectTmp = (scoresMonthlyGenreDTO.getQuizCorrect() == null) ? 0
					: scoresMonthlyGenreDTO.getQuizCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			scoresMonthlyGenreDTO.setQuizCorrect(quizQCorrectTmp);

			scoresMonthlyGenreDTO = saveScoresMonthly(scoresMonthlyGenreDTO);
		}
		return scoresMonthlyGenreDTO;
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
