package com.enewschamp.app.student.scores.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.scores.entity.ScoresMonthlyGenre;
import com.enewschamp.app.student.scores.repository.ScoresMonthlyGenreRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class ScoresMonthlyGenreService {

	@Autowired
	ScoresMonthlyGenreRepository scoresMonthlyGenreRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public ScoresMonthlyGenre create(ScoresMonthlyGenre scoresMonthlyGenreEntity) {
		ScoresMonthlyGenre existing = getScoresMonthly(scoresMonthlyGenreEntity.getStudentId(),
				scoresMonthlyGenreEntity.getEditionId(), scoresMonthlyGenreEntity.getReadingLevel(),
				scoresMonthlyGenreEntity.getYearMonth().toString(), scoresMonthlyGenreEntity.getGenreId());
		if (existing == null) {
			existing = scoresMonthlyGenreRepository.save(scoresMonthlyGenreEntity);
		} else {
			existing = update(scoresMonthlyGenreEntity);
		}
		return existing;
	}

	public ScoresMonthlyGenre update(ScoresMonthlyGenre scoresMonthlyGenreEntity) {
		Long scoresMonthlyGenreId = scoresMonthlyGenreEntity.getScoresMonthlyGenreId();
		ScoresMonthlyGenre existingScoresMonthlyGenre = get(scoresMonthlyGenreId);
		modelMapper.map(scoresMonthlyGenreEntity, existingScoresMonthlyGenre);
		return scoresMonthlyGenreRepository.save(existingScoresMonthlyGenre);
	}

	public ScoresMonthlyGenre patch(ScoresMonthlyGenre scoresMonthlyGenreEntity) {
		Long scoresMonthlyGenreId = scoresMonthlyGenreEntity.getScoresMonthlyGenreId();
		ScoresMonthlyGenre existingEntity = get(scoresMonthlyGenreId);
		modelMapperForPatch.map(scoresMonthlyGenreEntity, existingEntity);
		return scoresMonthlyGenreRepository.save(existingEntity);
	}

	public void delete(Long scoresMonthlyGenreId) {
		scoresMonthlyGenreRepository.deleteById(scoresMonthlyGenreId);
	}

	public ScoresMonthlyGenre get(Long scoresMonthlyGenreId) {
		Optional<ScoresMonthlyGenre> existingEntity = scoresMonthlyGenreRepository.findById(scoresMonthlyGenreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCORES_MONTHLY_GENRE_NOT_FOUND);
		}
	}

	public ScoresMonthlyGenre getScoresMonthly(Long studentId, String editionId, int readingLevel, String yearMonth,
			String genreId) {
		Optional<ScoresMonthlyGenre> existingEntity = scoresMonthlyGenreRepository.getScoresMonthlyGenre(studentId,
				editionId, readingLevel, Long.valueOf(yearMonth), genreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

}
