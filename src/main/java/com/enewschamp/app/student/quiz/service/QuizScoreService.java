package com.enewschamp.app.student.quiz.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.quiz.entity.QuizScore;
import com.enewschamp.app.student.quiz.repository.QuizScoreRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class QuizScoreService {

	@Autowired
	QuizScoreRepository quizScoreRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public QuizScore create(QuizScore quizScore) {
		return quizScoreRepository.save(quizScore);
	}

	public QuizScore update(QuizScore QuizScore) {
		Long QuizScoreId = QuizScore.getQuizScoreId();
		QuizScore existingQuizScore = get(QuizScoreId);
		modelMapper.map(QuizScore, existingQuizScore);
		return quizScoreRepository.save(existingQuizScore);
	}

	public QuizScore patch(QuizScore QuizScore) {
		Long navId = QuizScore.getQuizScoreId();
		QuizScore existingEntity = get(navId);
		modelMapperForPatch.map(QuizScore, existingEntity);
		return quizScoreRepository.save(existingEntity);
	}

	public void delete(Long QuizScoreId) {
		quizScoreRepository.deleteById(QuizScoreId);
	}

	public QuizScore get(Long QuizScoreId) {
		Optional<QuizScore> existingEntity = quizScoreRepository.findById(QuizScoreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.QUIZ_SCORE_NOT_FOUND);
		}
	}

	public QuizScore getQuizScore(Long studentId, Long articleId) {
		Optional<QuizScore> existingEntity = quizScoreRepository.getQuizScore(studentId, articleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.QUIZ_SCORE_NOT_FOUND);
		}
	}

}
