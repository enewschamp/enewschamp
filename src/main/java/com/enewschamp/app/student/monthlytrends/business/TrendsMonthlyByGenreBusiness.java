package com.enewschamp.app.student.monthlytrends.business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
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

	public TrendsMonthlyByGenreDTO getMonthlyTrend(Long studentId, String editionId, String monthYear, String genreId) {
		TrendsMonthlyByGenre trendsMonthlyByGenre = null;
		try {
			trendsMonthlyByGenre = trendsMonthlyService.getDailyTrend(studentId, editionId, monthYear, genreId);
		} catch (Exception e) {
			return null;
		}
		if(trendsMonthlyByGenre!=null) {
		TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO = modelMapper.map(trendsMonthlyByGenre,
				TrendsMonthlyByGenreDTO.class);
		return trendsMonthlyByGenreDTO;
		}
		else return null;
	}

	public TrendsMonthlyByGenreDTO saveQuizData(Long newsArticleId, Long studentId, String editionId,
			Long quizQAttempted, Long quizQCorrect) {
		// get the genre Id..
		String genreId = getGenreId(newsArticleId);

		Date date = new Date();
		LocalDate currDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int year = currDate.getYear();
		int month = currDate.getMonthValue();
		String monthYear = year + "" + month;
		System.out.println("monthYear :"+monthYear);
		TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTO = getMonthlyTrend(studentId, editionId, monthYear, genreId);
		if (trendsMonthlyByGenreDTO == null) {
			TrendsMonthlyByGenreDTO trendsMonthlyByGenreDTONew = new TrendsMonthlyByGenreDTO();
			trendsMonthlyByGenreDTONew.setStudentId(studentId);
			trendsMonthlyByGenreDTONew.setEditionId(editionId);
			trendsMonthlyByGenreDTONew.setGenreId(genreId);
			trendsMonthlyByGenreDTONew.setQuizQAttempted(quizQAttempted);
			trendsMonthlyByGenreDTONew.setQuizQCorrect(quizQCorrect);
			trendsMonthlyByGenreDTONew.setYearMonth(Long.valueOf(monthYear));
			trendsMonthlyByGenreDTO = saveMonthlyTrend(trendsMonthlyByGenreDTONew);
			

		} else {
			Long quizQAttemptedTmp = (trendsMonthlyByGenreDTO.getQuizQAttempted() == null) ? 0
					: trendsMonthlyByGenreDTO.getQuizQAttempted();
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;
			trendsMonthlyByGenreDTO.setQuizQAttempted(quizQAttemptedTmp);

			Long quizQCorrectTmp = (trendsMonthlyByGenreDTO.getQuizQCorrect() == null) ? 0
					: trendsMonthlyByGenreDTO.getQuizQCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			trendsMonthlyByGenreDTO.setQuizQCorrect(quizQCorrectTmp);

			trendsMonthlyByGenreDTO = saveMonthlyTrend(trendsMonthlyByGenreDTO);
		}
		return trendsMonthlyByGenreDTO;
	}

	public String getGenreId(Long newsArticleId) {
		// TO DO Code to be written to fetch the genre ID..
		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		if (newsArticle == null) {
			throw new BusinessException(ErrorCodes.ARTICLE_NOT_FOUND);

		}
		Long groupId = newsArticle.getNewsArticleGroupId();

		NewsArticleGroup newsArticleGroup = newsArticleGroupService.get(groupId);
		if (newsArticleGroup == null) {
			throw new BusinessException(ErrorCodes.ARTICLE_GROUP_NOT_FOUND);

		}
		String genre = newsArticleGroup.getGenreId();

		return genre;
	}
}
