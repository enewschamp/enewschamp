package com.enewschamp.app.student.scores.service;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.scores.entity.ScoresDaily;
import com.enewschamp.app.student.scores.repository.ScoresDailyRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class ScoresDailyService {

	@Autowired
	ScoresDailyRepository scoresDailyRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public ScoresDaily create(ScoresDaily scoresDailyEntity) {
		ScoresDaily existing = getScoresDaily(scoresDailyEntity.getStudentId(), scoresDailyEntity.getEditionId(),
				scoresDailyEntity.getReadingLevel(), scoresDailyEntity.getPublicationDate());
		if (existing == null) {
			existing = scoresDailyRepository.save(scoresDailyEntity);
		} else {
			existing = update(scoresDailyEntity);
		}
		return existing;
	}

	public ScoresDaily update(ScoresDaily scoresDailyEntity) {
		Long scoresDailyId = scoresDailyEntity.getScoresDailyId();
		ScoresDaily existingScoresDaily = get(scoresDailyId);
		modelMapper.map(scoresDailyEntity, existingScoresDaily);
		return scoresDailyRepository.save(existingScoresDaily);
	}

	public ScoresDaily patch(ScoresDaily scoresDaily) {
		Long navId = scoresDaily.getScoresDailyId();
		ScoresDaily existingEntity = get(navId);
		modelMapperForPatch.map(scoresDaily, existingEntity);
		return scoresDailyRepository.save(existingEntity);
	}

	public void delete(Long ScoresDailyId) {
		scoresDailyRepository.deleteById(ScoresDailyId);
	}

	public ScoresDaily get(Long scoresDailyId) {
		Optional<ScoresDaily> existingEntity = scoresDailyRepository.findById(scoresDailyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCORES_DAILY_NOT_FOUND);
		}
	}

	public ScoresDaily getScoresDaily(Long studentId, String editionId, int readingLevel, LocalDate quizdate) {
		Optional<ScoresDaily> existingEntity = scoresDailyRepository.getScoresDaily(studentId, editionId, readingLevel,
				quizdate);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

}
