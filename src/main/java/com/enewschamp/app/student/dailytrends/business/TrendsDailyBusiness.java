package com.enewschamp.app.student.dailytrends.business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.dailytrends.dto.TrendsDailyDTO;
import com.enewschamp.app.student.dailytrends.entity.TrendsDaily;
import com.enewschamp.app.student.dailytrends.service.TrendsDailyService;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class TrendsDailyBusiness {

	@Autowired
	TrendsDailyService trendsDailyService;
	
	@Autowired
	ModelMapper modelMapper;
	
	public TrendsDailyDTO saveDailyTrend(TrendsDailyDTO studentActivityDTO)
	{
		TrendsDaily  trendsDaily  = modelMapper.map(studentActivityDTO, TrendsDaily.class);
		// TO DO to be corrected
		
		trendsDaily.setArticleRead(0L);
		trendsDaily.setRecordInUse(RecordInUseType.Y);
		
		trendsDaily.setOperatorId("SYSTEM");
		
		trendsDaily = trendsDailyService.create(trendsDaily);
		TrendsDailyDTO studentActivityNew = modelMapper.map(trendsDaily, TrendsDailyDTO.class);

		return studentActivityNew;
	}
	
	public TrendsDailyDTO getDailyTrend(Long studentId, String editionId, LocalDate quizdate)
	{
		TrendsDaily  studentActivity=null;
		try {
			studentActivity = trendsDailyService.getDailyTrend(studentId,editionId,quizdate);
		}
		catch(Exception e)
		{
			return null;
		}
		if(studentActivity!=null) {
		TrendsDailyDTO studentActivityDTO = modelMapper.map(studentActivity, TrendsDailyDTO.class);
		
		return studentActivityDTO;
		}
		else return null;
	}
	
	public TrendsDailyDTO saveQuizData(Long studentId, String editionId, Long quizQAttempted, Long quizQCorrect)
	{
		Date startDate = new Date();
		LocalDate quizdate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		TrendsDailyDTO trendsDailyDTO = getDailyTrend(studentId, editionId, quizdate);
		if (trendsDailyDTO == null) {
			TrendsDailyDTO trendsDailyDTONew = new TrendsDailyDTO();
			trendsDailyDTONew.setStudentId(studentId);
			trendsDailyDTONew.setEditionId(editionId);
			trendsDailyDTONew.setQuizDate(quizdate);
			trendsDailyDTONew.setQuizQAttempted(quizQAttempted);
			trendsDailyDTONew.setQuizQCorrect(quizQCorrect);
			
			trendsDailyDTO = saveDailyTrend(trendsDailyDTONew);
		} else {
			Long quizQAttemptedTmp = (trendsDailyDTO.getQuizQAttempted()==null) ? 0 : trendsDailyDTO.getQuizQAttempted();
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;
			trendsDailyDTO.setQuizQAttempted(quizQAttemptedTmp);

			Long quizQCorrectTmp = (trendsDailyDTO.getQuizQCorrect()==null) ? 0 : trendsDailyDTO.getQuizQCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			trendsDailyDTO.setQuizQCorrect(quizQCorrectTmp);

			trendsDailyDTO = saveDailyTrend(trendsDailyDTO);

		}
		return trendsDailyDTO;
	}
}
