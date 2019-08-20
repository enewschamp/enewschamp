package com.enewschamp.app.student.dailytrends.service;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.dailytrends.entity.TrendsDaily;
import com.enewschamp.app.student.dailytrends.repository.TrendsDailyRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

@Service
public class TrendsDailyService {

	@Autowired
	TrendsDailyRepository trendsDailyRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public TrendsDaily create(TrendsDaily trendsDailyEntity) {
		TrendsDaily existing = getDailyTrend(trendsDailyEntity.getStudentId(),trendsDailyEntity.getEditionId(),trendsDailyEntity.getQuizDate());
		if(existing==null)
		{
			existing = trendsDailyRepository.save(trendsDailyEntity);
		}
		else
		{
			existing = update(trendsDailyEntity);
		}
		return existing;
	}

	public TrendsDaily update(TrendsDaily trendsDailyEntity) {
		Long trendsDailyId = trendsDailyEntity.getTrendsDailyId();
		System.out.println("trendsDailyId in update(): "+trendsDailyId);
		TrendsDaily existingTrendsDaily = get(trendsDailyId);
		
		modelMapper.map(trendsDailyEntity, existingTrendsDaily);
		return trendsDailyRepository.save(existingTrendsDaily);
	}

	public TrendsDaily patch(TrendsDaily trendsDaily) {
		Long navId = trendsDaily.getTrendsDailyId();
		TrendsDaily existingEntity = get(navId);
		modelMapperForPatch.map(trendsDaily, existingEntity);
		
		return trendsDailyRepository.save(existingEntity);
	}

	public void delete(Long TrendsDailyId) {
		trendsDailyRepository.deleteById(TrendsDailyId);
	}

	public TrendsDaily get(Long trendsDailyId) {
		Optional<TrendsDaily> existingEntity = trendsDailyRepository.findById(trendsDailyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.TREND_DAILY_NOT_FOUND);
		}
	}
	public TrendsDaily getDailyTrend(Long studentId, String editionId, LocalDate quizdate) {
		Optional<TrendsDaily> existingEntity = trendsDailyRepository.getDailyTrend(studentId, editionId, quizdate);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
			//throw new BusinessException(ErrorCodes.TREND_DAILY_NOT_FOUND);

		}
	}
	
	
}
