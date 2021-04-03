package com.enewschamp.app.student.scores.business;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.scores.dto.ScoresDailyDTO;
import com.enewschamp.app.student.scores.entity.ScoresDaily;
import com.enewschamp.app.student.scores.service.ScoresDailyService;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class ScoresDailyBusiness {

	@Autowired
	ScoresDailyService scoresDailyService;

	@Autowired
	NewsArticleService newsArticleService;

	@Autowired
	ModelMapper modelMapper;

	public ScoresDailyDTO saveScoresDaily(ScoresDailyDTO studentActivityDTO) {
		ScoresDaily scoresDaily = modelMapper.map(studentActivityDTO, ScoresDaily.class);
		scoresDaily = scoresDailyService.create(scoresDaily);
		ScoresDailyDTO studentActivityNew = modelMapper.map(scoresDaily, ScoresDailyDTO.class);
		return studentActivityNew;
	}

	public ScoresDailyDTO getScoresDaily(Long studentId, String editionId, int readingLevel, LocalDate quizdate) {
		ScoresDaily studentActivity = null;
		try {
			studentActivity = scoresDailyService.getScoresDaily(studentId, editionId, readingLevel, quizdate);
		} catch (Exception e) {
			return null;
		}
		if (studentActivity != null) {
			ScoresDailyDTO studentActivityDTO = modelMapper.map(studentActivity, ScoresDailyDTO.class);

			return studentActivityDTO;
		} else
			return null;
	}

	public ScoresDailyDTO saveQuizData(Long newsArticleId, Long studentId, String emailId, String editionId,
			int readingLevel, Long quizQAttempted, Long quizQCorrect) {
		NewsArticle newsArticle = newsArticleService.get(newsArticleId);
		LocalDate publicationDate = newsArticle.getPublicationDate();
		ScoresDailyDTO scoresDailyDTO = getScoresDaily(studentId, editionId, readingLevel, publicationDate);
		if (scoresDailyDTO == null) {
			ScoresDailyDTO scoresDailyDTONew = new ScoresDailyDTO();
			scoresDailyDTONew.setStudentId(studentId);
			scoresDailyDTONew.setEditionId(editionId);
			scoresDailyDTONew.setReadingLevel(readingLevel);
			scoresDailyDTONew.setPublicationDate(publicationDate);
			scoresDailyDTONew.setArticlesRead(Long.valueOf(1));
			scoresDailyDTONew.setQuizAttempted(quizQAttempted);
			scoresDailyDTONew.setQuizCorrect(quizQCorrect);
			scoresDailyDTONew.setOperatorId(""+studentId);
			scoresDailyDTONew.setRecordInUse(RecordInUseType.Y);
			scoresDailyDTO = saveScoresDaily(scoresDailyDTONew);
		} else {
			Long quizQAttemptedTmp = (scoresDailyDTO.getQuizAttempted() == null) ? 0
					: scoresDailyDTO.getQuizAttempted();
			Long articlesReadTmp = (scoresDailyDTO.getArticlesRead() == null) ? 0 : scoresDailyDTO.getArticlesRead();
			articlesReadTmp = articlesReadTmp + 1;
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;
			scoresDailyDTO.setQuizAttempted(quizQAttemptedTmp);
			scoresDailyDTO.setArticlesRead(articlesReadTmp);
			Long quizQCorrectTmp = (scoresDailyDTO.getQuizCorrect() == null) ? 0 : scoresDailyDTO.getQuizCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			scoresDailyDTO.setQuizCorrect(quizQCorrectTmp);
			scoresDailyDTO.setOperatorId(""+studentId);
			scoresDailyDTO.setRecordInUse(RecordInUseType.Y);
			scoresDailyDTO = saveScoresDaily(scoresDailyDTO);
		}
		return scoresDailyDTO;
	}
}