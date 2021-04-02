package com.enewschamp.app.student.scores.business;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.scores.dto.ScoresMonthlyTotalDTO;
import com.enewschamp.app.student.scores.entity.ScoresMonthlyTotal;
import com.enewschamp.app.student.scores.service.ScoresMonthlyTotalService;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class ScoresMonthlyTotalBusiness {

	@Autowired
	ScoresMonthlyTotalService scoresMonthlyTotalService;

	@Autowired
	NewsArticleService newsArticleService;

	@Autowired
	ModelMapper modelMapper;

	public ScoresMonthlyTotalDTO saveScoresMonthly(ScoresMonthlyTotalDTO scoresMonthlyTotalDTO) {
		ScoresMonthlyTotal scoresMonthlyTotal = modelMapper.map(scoresMonthlyTotalDTO, ScoresMonthlyTotal.class);
		scoresMonthlyTotal = scoresMonthlyTotalService.create(scoresMonthlyTotal);
		ScoresMonthlyTotalDTO scoresMonthlyTotalDTONew = modelMapper.map(scoresMonthlyTotal,
				ScoresMonthlyTotalDTO.class);
		return scoresMonthlyTotalDTONew;
	}

	public ScoresMonthlyTotalDTO getScoresMonthly(Long studentId, String editionId, int readingLevel,
			String yearMonth) {
		ScoresMonthlyTotal scoresMonthlyTotal = null;
		try {
			scoresMonthlyTotal = scoresMonthlyTotalService.getScoresMonthly(studentId, editionId, readingLevel,
					yearMonth);
		} catch (Exception e) {
			return null;
		}
		if (scoresMonthlyTotal != null) {
			ScoresMonthlyTotalDTO scoresMonthlyTotalDTO = modelMapper.map(scoresMonthlyTotal,
					ScoresMonthlyTotalDTO.class);
			return scoresMonthlyTotalDTO;
		} else
			return null;
	}

	public ScoresMonthlyTotalDTO saveQuizData(Long newsArticleId, Long studentId, String editionId, int readingLevel,
			Long quizQAttempted, Long quizQCorrect) {
		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		LocalDate publicationDate = newsArticle.getPublicationDate();
		int year = publicationDate.getYear();
		int month = publicationDate.getMonthValue();
		String yearMonth = year + "" + (month > 9 ? month : "0" + month);
		ScoresMonthlyTotalDTO scoresMonthlyTotalDTO = this.getScoresMonthly(studentId, editionId, readingLevel,
				yearMonth);
		if (scoresMonthlyTotalDTO == null) {
			ScoresMonthlyTotalDTO scoresMonthlyTotalDTONew = new ScoresMonthlyTotalDTO();
			scoresMonthlyTotalDTONew.setStudentId(studentId);
			scoresMonthlyTotalDTONew.setEditionId(editionId);
			scoresMonthlyTotalDTONew.setArticlesRead(Long.valueOf(1));
			scoresMonthlyTotalDTONew.setReadingLevel(readingLevel);
			scoresMonthlyTotalDTONew.setYearMonth(yearMonth);
			scoresMonthlyTotalDTONew.setQuizAttempted(quizQAttempted);
			scoresMonthlyTotalDTONew.setQuizCorrect(quizQCorrect);
			scoresMonthlyTotalDTONew.setRecordInUse(RecordInUseType.Y);
			scoresMonthlyTotalDTONew.setOperatorId("" + studentId);
			scoresMonthlyTotalDTO = saveScoresMonthly(scoresMonthlyTotalDTONew);
		} else {
			Long quizQAttemptedTmp = (scoresMonthlyTotalDTO.getQuizAttempted() == null) ? 0
					: scoresMonthlyTotalDTO.getQuizAttempted();
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;

			Long articlesReadTmp = (scoresMonthlyTotalDTO.getArticlesRead() == null) ? 0
					: scoresMonthlyTotalDTO.getArticlesRead();
			articlesReadTmp = articlesReadTmp + 1;
			scoresMonthlyTotalDTO.setQuizAttempted(quizQAttemptedTmp);
			scoresMonthlyTotalDTO.setArticlesRead(articlesReadTmp);
			Long quizQCorrectTmp = (scoresMonthlyTotalDTO.getQuizCorrect() == null) ? 0
					: scoresMonthlyTotalDTO.getQuizCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			scoresMonthlyTotalDTO.setRecordInUse(RecordInUseType.Y);
			scoresMonthlyTotalDTO.setOperatorId("" + studentId);
			scoresMonthlyTotalDTO.setQuizCorrect(quizQCorrectTmp);
			scoresMonthlyTotalDTO = saveScoresMonthly(scoresMonthlyTotalDTO);
		}
		return scoresMonthlyTotalDTO;
	}
}
