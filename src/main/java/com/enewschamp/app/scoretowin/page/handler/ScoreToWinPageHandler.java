package com.enewschamp.app.scoretowin.page.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.recognition.page.data.RecognitionData;
import com.enewschamp.app.scores.dto.StudentScoresMonthlyDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.scores.service.ScoreService;
import com.enewschamp.app.scoretowin.page.data.ScoreToWinPageData;
import com.enewschamp.app.student.badges.business.StudentBadgesBusiness;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;

@Component(value = "ScoreToWinPageHandler")
public class ScoreToWinPageHandler implements IPageHandler {

	@Autowired
	ScoreService scoreService;

	@Autowired
	StudentBadgesBusiness studentBadgesBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		StudentPreferencesDTO studentPreferencesDTO = preferenceBusiness.getPreferenceFromMaster(studentId);
		int readingLevel = 3;
		if (studentPreferencesDTO != null) {
			readingLevel = Integer.valueOf(studentPreferencesDTO.getReadingLevel());
		}
		ScoreToWinPageData scoreToWinPageData = new ScoreToWinPageData();
		LocalDate fromDate = LocalDate.now().minusMonths(1);
		int year = fromDate.getYear();
		int month = fromDate.getMonthValue();
		Long yearMonth = Long.valueOf(year + "" + (month > 9 ? month : "0" + month));
		List<RecognitionData> studentbadges = studentBadgesBusiness.getBadgeDetails(studentId, editionId, readingLevel,
				yearMonth);
		List<StudentScoresMonthlyDTO> scoresMonthly = new ArrayList<StudentScoresMonthlyDTO>();
		ScoresSearchData searchData = new ScoresSearchData();
		searchData.setStudentId(studentId);
		searchData.setReadingLevel(readingLevel);
		searchData.setYearMonth("" + yearMonth);
		List<StudentScoresMonthlyTotalDTO> monthlyTotalScoresDTO = scoreService.findMonthWiseTotalScores(searchData);
		List<StudentScoresMonthlyGenreDTO> monthlyGenreScoresDTO = scoreService.findMonthWiseGenreScores(searchData);
		StudentScoresMonthlyDTO prevMonth = new StudentScoresMonthlyDTO();
		prevMonth.setMonth("" + yearMonth);
		prevMonth.setTotal(monthlyTotalScoresDTO);
		prevMonth.setByGenre(monthlyGenreScoresDTO);
		scoresMonthly.add(prevMonth);
		LocalDate todaysDate = LocalDate.now();
		year = todaysDate.getYear();
		month = todaysDate.getMonthValue();
		yearMonth = Long.valueOf(year + "" + (month > 9 ? month : "0" + month));
		searchData.setYearMonth("" + yearMonth);
		monthlyTotalScoresDTO = scoreService.findMonthWiseTotalScores(searchData);
		monthlyGenreScoresDTO = scoreService.findMonthWiseGenreScores(searchData);
		StudentScoresMonthlyDTO currMonth = new StudentScoresMonthlyDTO();
		currMonth.setMonth("" + yearMonth);
		currMonth.setTotal(monthlyTotalScoresDTO);
		currMonth.setByGenre(monthlyGenreScoresDTO);
		scoresMonthly.add(currMonth);
		scoreToWinPageData.setScoresMonthly(scoresMonthly);
		scoreToWinPageData.setBadgeDetails(studentbadges);
		pageDto.setData(scoreToWinPageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageRequest.getHeader());
		return pageDto;
	}

}
