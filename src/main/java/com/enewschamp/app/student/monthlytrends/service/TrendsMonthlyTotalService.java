package com.enewschamp.app.student.monthlytrends.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyTotal;
import com.enewschamp.app.student.monthlytrends.repository.TrendsMonthlyTotalRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

@Service
public class TrendsMonthlyTotalService {

	@Autowired
	TrendsMonthlyTotalRepository trendsMonthlyTotalRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public TrendsMonthlyTotal create(TrendsMonthlyTotal trendsMonthlyEntity) {
		TrendsMonthlyTotal existing = getMonthlyTrends(trendsMonthlyEntity.getStudentId(),trendsMonthlyEntity.getEditionId(),trendsMonthlyEntity.getYearMonth().toString());
		if(existing==null)
		{
			existing = trendsMonthlyTotalRepository.save(trendsMonthlyEntity);
		}
		else
		{
			existing = update(trendsMonthlyEntity);
		}
		return existing;
	}

	public TrendsMonthlyTotal update(TrendsMonthlyTotal trendsMonthlyEntity) {
		Long trendsDailyId = trendsMonthlyEntity.getTrendsMonthlyTotalId();
		TrendsMonthlyTotal trendsMonthlyTotal = get(trendsDailyId);
		modelMapper.map(trendsMonthlyEntity, trendsMonthlyTotal);
		return trendsMonthlyTotalRepository.save(trendsMonthlyTotal);
	}

	public TrendsMonthlyTotal patch(TrendsMonthlyTotal trendsMonthly) {
		Long navId = trendsMonthly.getTrendsMonthlyTotalId();
		TrendsMonthlyTotal existingEntity = get(navId);
		modelMapperForPatch.map(trendsMonthly, existingEntity);
		return trendsMonthlyTotalRepository.save(existingEntity);
	}

	public void delete(Long TrendsMonthlyByGenreId) {
		trendsMonthlyTotalRepository.deleteById(TrendsMonthlyByGenreId);
	}

	public TrendsMonthlyTotal get(Long trendsDailyId) {
		Optional<TrendsMonthlyTotal> existingEntity = trendsMonthlyTotalRepository.findById(trendsDailyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.TREND_MONTHLY_TOTAL_NOT_FOUND);
		}
	}
	public TrendsMonthlyTotal getMonthlyTrends(Long studentId, String editionId, String monthYear) {
		Optional<TrendsMonthlyTotal> existingEntity = trendsMonthlyTotalRepository.getMonthlyTrends(studentId, editionId, Long.valueOf(monthYear));
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
			//throw new BusinessException(ErrorCodes.TREND_MONTHLY_TOTAL_NOT_FOUND);

		}
	}
	
	
}
