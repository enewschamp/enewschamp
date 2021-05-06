package com.enewschamp.app.student.quiz.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.student.quiz.repository.QuizScoreRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.student.quiz.entity.QuizScore;
import com.enewschamp.app.student.quiz.repository.QuizScoreRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizScoreService {
    private final QuizScoreRepository quizScoreRepository;
    private final ModelMapper modelMapper;
    private final QuizScoreRepositoryCustomImpl quizScoreRepositoryCustom;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public QuizScore create(QuizScore quizScore) {
		QuizScore quizScoreEntity = null;
		try {
			quizScoreEntity = quizScoreRepository.save(quizScore);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return quizScoreEntity;
	}

	public QuizScore update(QuizScore QuizScore) {
		Long QuizScoreId = QuizScore.getQuizScoreId();
		QuizScore existingQuizScore = get(QuizScoreId);
		if (existingQuizScore.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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
	
	public QuizScore read(QuizScore quizScoreEntity) {
		Long quizScoreId = quizScoreEntity.getQuizScoreId();
		QuizScore quizScore = get(quizScoreId);
		return quizScore;

	}

	public QuizScore close(QuizScore quizScoreEntity) {
		Long quizScoreId = quizScoreEntity.getQuizScoreId();
		QuizScore existingQuizScore = get(quizScoreId);
		if (!(existingQuizScore.getRecordInUse().equals(RecordInUseType.Y))) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingQuizScore.setRecordInUse(RecordInUseType.N);
		existingQuizScore.setOperationDateTime(null);
		return quizScoreRepository.save(existingQuizScore);
	}

	public QuizScore reInstateQuizScore(QuizScore quizScoreEntity) {
		Long quizScoreId = quizScoreEntity.getQuizScoreId();
		QuizScore existingQuizScore = get(quizScoreId);
		if (existingQuizScore.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingQuizScore.setRecordInUse(RecordInUseType.Y);
		existingQuizScore.setOperationDateTime(null);
		return quizScoreRepository.save(existingQuizScore);
	}
	
	public Page<QuizScore> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<QuizScore> quizScoreList = quizScoreRepositoryCustom.findAll(pageable, searchRequest);
		return quizScoreList;
	}

}
