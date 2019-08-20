package com.enewschamp.app.student.monthlytrends.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyByGenre;
import com.enewschamp.app.student.monthlytrends.repository.TrendsMonthlyByGenreRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

@Service
public class TrendsMonthlyByGenreService {

	@Autowired
	TrendsMonthlyByGenreRepository trendsMonthlyRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public TrendsMonthlyByGenre create(TrendsMonthlyByGenre trendsMonthlyEntity) {
		TrendsMonthlyByGenre existing = getDailyTrend(trendsMonthlyEntity.getStudentId(),trendsMonthlyEntity.getEditionId(),trendsMonthlyEntity.getYearMonth().toString(),trendsMonthlyEntity.getGenreId());
		if(existing==null)
		{
			existing = trendsMonthlyRepository.save(trendsMonthlyEntity);
		}
		else
		{
			existing = update(trendsMonthlyEntity);
		}
		return existing;
	}

	public TrendsMonthlyByGenre update(TrendsMonthlyByGenre trendsMonthlyEntity) {
		Long trendsDailyId = trendsMonthlyEntity.getTrendsMonthlyGenreId();
		TrendsMonthlyByGenre existingTrendsMonthlyByGenre = get(trendsDailyId);
		modelMapper.map(trendsMonthlyEntity, existingTrendsMonthlyByGenre);
		return trendsMonthlyRepository.save(existingTrendsMonthlyByGenre);
	}

	public TrendsMonthlyByGenre patch(TrendsMonthlyByGenre trendsDaily) {
		Long navId = trendsDaily.getTrendsMonthlyGenreId();
		TrendsMonthlyByGenre existingEntity = get(navId);
		modelMapperForPatch.map(trendsDaily, existingEntity);
		return trendsMonthlyRepository.save(existingEntity);
	}

	public void delete(Long TrendsMonthlyByGenreId) {
		trendsMonthlyRepository.deleteById(TrendsMonthlyByGenreId);
	}

	public TrendsMonthlyByGenre get(Long trendsDailyId) {
		Optional<TrendsMonthlyByGenre> existingEntity = trendsMonthlyRepository.findById(trendsDailyId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException( ErrorCodes.TREND_DAILY_NOT_FOUND);
		}
	}
	public TrendsMonthlyByGenre getDailyTrend(Long studentId, String editionId, String monthYear, String genreId) {
		Optional<TrendsMonthlyByGenre> existingEntity = trendsMonthlyRepository.getMonghlyTrendsByGenre(studentId, editionId, Long.valueOf(monthYear),genreId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
			//throw new BusinessException( ErrorCodes.TREND_DAILY_NOT_FOUND);

		}
	}
	
	
}
