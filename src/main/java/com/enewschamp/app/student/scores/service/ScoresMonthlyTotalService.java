package com.enewschamp.app.student.scores.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.scores.entity.ScoresMonthlyTotal;
import com.enewschamp.app.student.scores.repository.ScoresMonthlyTotalRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class ScoresMonthlyTotalService {

	@Autowired
	ScoresMonthlyTotalRepository scoresMonthlyTotalRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public ScoresMonthlyTotal create(ScoresMonthlyTotal scoresMonthlyEntity) {
		ScoresMonthlyTotal existing = getScoresMonthly(scoresMonthlyEntity.getStudentId(),
				scoresMonthlyEntity.getEditionId(), scoresMonthlyEntity.getReadingLevel(),
				scoresMonthlyEntity.getYearMonth().toString());
		if (existing == null) {
			existing = scoresMonthlyTotalRepository.save(scoresMonthlyEntity);
		} else {
			existing = update(scoresMonthlyEntity);
		}
		return existing;
	}

	public ScoresMonthlyTotal update(ScoresMonthlyTotal scoresMonthlyEntity) {
		Long scoresMonthlyTotalId = scoresMonthlyEntity.getScoresMonthlyTotalId();
		ScoresMonthlyTotal scoresMonthlyTotal = get(scoresMonthlyTotalId);
		modelMapper.map(scoresMonthlyEntity, scoresMonthlyTotal);
		return scoresMonthlyTotalRepository.save(scoresMonthlyTotal);
	}

	public ScoresMonthlyTotal patch(ScoresMonthlyTotal scoresMonthly) {
		Long navId = scoresMonthly.getScoresMonthlyTotalId();
		ScoresMonthlyTotal existingEntity = get(navId);
		modelMapperForPatch.map(scoresMonthly, existingEntity);
		return scoresMonthlyTotalRepository.save(existingEntity);
	}

	public void delete(Long scoresMonthlyGenreId) {
		scoresMonthlyTotalRepository.deleteById(scoresMonthlyGenreId);
	}

	public ScoresMonthlyTotal get(Long scoresMonthlyTotalId) {
		Optional<ScoresMonthlyTotal> existingEntity = scoresMonthlyTotalRepository.findById(scoresMonthlyTotalId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCORES_MONTHLY_TOTAL_NOT_FOUND);
		}
	}

	public ScoresMonthlyTotal getScoresMonthly(Long studentId, String editionId, int readingLevel, String yearMonth) {
		Optional<ScoresMonthlyTotal> existingEntity = scoresMonthlyTotalRepository.getScoresMonthly(studentId,
				editionId, readingLevel, Long.valueOf(yearMonth));
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

}
