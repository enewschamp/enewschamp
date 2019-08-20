package com.enewschamp.app.student.monthlytrends.business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.student.monthlytrends.dto.TrendsMonthlyTotalDTO;
import com.enewschamp.app.student.monthlytrends.entity.TrendsMonthlyTotal;
import com.enewschamp.app.student.monthlytrends.service.TrendsMonthlyTotalService;
import com.enewschamp.domain.common.RecordInUseType;

@Service
public class TrendsMonthlyTotalBusiness {

	@Autowired
	TrendsMonthlyTotalService trendsMonthlyService;

	@Autowired
	ModelMapper modelMapper;

	public TrendsMonthlyTotalDTO saveMonthlyTrend(TrendsMonthlyTotalDTO trendsMonthlyTotalDTO) {
		TrendsMonthlyTotal trendsMonthlyTotal = modelMapper.map(trendsMonthlyTotalDTO, TrendsMonthlyTotal.class);

		// TO DO to be corrected
		trendsMonthlyTotal.setRecordInUse(RecordInUseType.Y);
		trendsMonthlyTotal.setOperatorId("SYSTEM");
		trendsMonthlyTotal = trendsMonthlyService.create(trendsMonthlyTotal);
		TrendsMonthlyTotalDTO trendsMonthlytotslDTOnew = modelMapper.map(trendsMonthlyTotal,
				TrendsMonthlyTotalDTO.class);

		return trendsMonthlytotslDTOnew;
	}

	public TrendsMonthlyTotalDTO getMonthlyTrend(Long studentId, String editionId, String monthYear) {
		TrendsMonthlyTotal trendsMonthlyTotal = null;

		try {
			trendsMonthlyTotal = trendsMonthlyService.getMonthlyTrends(studentId, editionId, monthYear);
		} catch (Exception e) {
			return null;
		}
		if (trendsMonthlyTotal != null) {
			TrendsMonthlyTotalDTO trendsMonthlyByGenreDTO = modelMapper.map(trendsMonthlyTotal,
					TrendsMonthlyTotalDTO.class);
			return trendsMonthlyByGenreDTO;
		} else
			return null;
	}

	public TrendsMonthlyTotalDTO saveQuizData(Long studentId, String editionId, Long quizQAttempted,
			Long quizQCorrect) {
		Date date = new Date();
		LocalDate currDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		int year = currDate.getYear();
		int month = currDate.getMonthValue();
		String monthYear = year + "" + month;
		TrendsMonthlyTotalDTO trendsMonthlyTotalDTO = this.getMonthlyTrend(studentId, editionId, monthYear);
		if (trendsMonthlyTotalDTO == null) {
			TrendsMonthlyTotalDTO trendsMonthlyTotalDTONew = new TrendsMonthlyTotalDTO();
			trendsMonthlyTotalDTONew.setStudentId(studentId);
			trendsMonthlyTotalDTONew.setEditionId(editionId);
			trendsMonthlyTotalDTONew.setYearMonth(monthYear);
			trendsMonthlyTotalDTONew.setQuizQAttempted(quizQAttempted);
			trendsMonthlyTotalDTONew.setQuizQCorrect(quizQCorrect);
			trendsMonthlyTotalDTO = saveMonthlyTrend(trendsMonthlyTotalDTONew);
		} else {
			Long quizQAttemptedTmp = (trendsMonthlyTotalDTO.getQuizQAttempted() == null) ? 0
					: trendsMonthlyTotalDTO.getQuizQAttempted();
			quizQAttemptedTmp = quizQAttemptedTmp + quizQAttempted;
			trendsMonthlyTotalDTO.setQuizQAttempted(quizQAttemptedTmp);

			Long quizQCorrectTmp = (trendsMonthlyTotalDTO.getQuizQCorrect() == null) ? 0
					: trendsMonthlyTotalDTO.getQuizQCorrect();
			quizQCorrectTmp = quizQCorrectTmp + quizQCorrect;
			trendsMonthlyTotalDTO.setQuizQCorrect(quizQCorrectTmp);
			trendsMonthlyTotalDTO = saveMonthlyTrend(trendsMonthlyTotalDTO);

		}
		return trendsMonthlyTotalDTO;
	}
}
