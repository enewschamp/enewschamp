package com.enewschamp.app.scores.page.handler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.handler.NewsArticlePageHandler;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.scores.dto.DailyScoreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoreGenreDTO;
import com.enewschamp.app.scores.dto.MonthlyScoresDTO;
import com.enewschamp.app.scores.dto.ScorePageData;
import com.enewschamp.app.scores.dto.YearlyScorePageData;
import com.enewschamp.app.scores.dto.YearlyScoresGenreDTO;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.scores.service.ScoreTrendService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "ScoresPageHandler")
public class ScoresPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	NewsArticlePageHandler newsArticlePageHandler;
	
	@Autowired
	ScoreTrendService scoreTrendService;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String action = pageNavigationContext.getActionName();
		
		//String action = pageNavigationContext.getActionName();
		//if (PageAction.home.toString().equalsIgnoreCase(action)) {
//			pageDto = newsArticlePageHandler.loadPage(pageNavigationContext);

//		}
		String operation = pageNavigationContext.getPageRequest().getHeader().getOperation();
		if ("ListMonthlyScores".equals(operation) || PageAction.Scores.toString().equalsIgnoreCase(action)) {
			pageDto = handleScoreTrends(pageNavigationContext);
		}
		if ("ListYearlyScores".equals(operation)) {
			pageDto = handleYearlyScoreTrends(pageNavigationContext);
		}
		return pageDto;
	}
	
	public PageDTO handleYearlyScoreTrends(PageNavigationContext pageNavigationContext) {
		


		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		String action = pageNavigationContext.getActionName();
		ScoresSearchData searchData = new ScoresSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();

		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					ScoresSearchData.class);
			searchData.setStudentId(studentId);

			String yearMonth = searchData.getMonthYear();
			if (yearMonth != null && !"".equals(yearMonth)) {
				if (yearMonth.length() < 6) {
					throw new BusinessException(ErrorCodes.INVALID_YEARMONTH_FORMAT, "Invalid Year Month Format");
				}
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (PageAction.Year.toString().equalsIgnoreCase(action) || PageAction.Month.toString().equalsIgnoreCase(action) ) {

			List<YearlyScoresGenreDTO> yearlyScoresByGenre = scoreTrendService.findYearlyScoresByGenre(searchData);
			List<MonthlyScoresDTO> monthlyScores = scoreTrendService.findYMonthlyScores(searchData);
			
			YearlyScorePageData pageData = new YearlyScorePageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setYearlyScoresByGenre(yearlyScoresByGenre);
			pageData.setMonthlyScores(monthlyScores);

			pageDto.setData(pageData);
		}
		if (PageAction.next.toString().equalsIgnoreCase(action)
				|| PageAction.RightSwipe.toString().equalsIgnoreCase(action)) {

			String yearMonth = searchData.getMonthYear();
			String yearStr = yearMonth.substring(0, 4);
			String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
			int year = Integer.parseInt(yearStr);
			year = year + 1;

			yearMonth = year + monthStr;
			System.out.println("yearMonth :" + yearMonth);

			searchData.setMonthYear(yearMonth);

			List<YearlyScoresGenreDTO> yearlyScoresByGenre = scoreTrendService.findYearlyScoresByGenre(searchData);
			List<MonthlyScoresDTO> monthlyScores = scoreTrendService.findYMonthlyScores(searchData);
			
			YearlyScorePageData pageData = new YearlyScorePageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setYearlyScoresByGenre(yearlyScoresByGenre);
			pageData.setMonthlyScores(monthlyScores);
			
			pageDto.setData(pageData);
		}
		if (PageAction.previous.toString().equalsIgnoreCase(action) || PageAction.RightSwipe.toString().equalsIgnoreCase(action)
				) {

			String yearMonth = searchData.getMonthYear();
			System.out.println("yearMonth :" + yearMonth);
			String yearStr = yearMonth.substring(0, 4);
			String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
			int year = Integer.parseInt(yearStr);
			System.out.println("***year  " + year);

			year = year - 1;

			yearMonth = year + monthStr;
			System.out.println("yearMonth :" + yearMonth);

			searchData.setMonthYear(yearMonth);

			List<YearlyScoresGenreDTO> yearlyScoresByGenre = scoreTrendService.findYearlyScoresByGenre(searchData);
			List<MonthlyScoresDTO> monthlyScores = scoreTrendService.findYMonthlyScores(searchData);
			
			YearlyScorePageData pageData = new YearlyScorePageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setYearlyScoresByGenre(yearlyScoresByGenre);
			pageData.setMonthlyScores(monthlyScores);

			pageDto.setData(pageData);
		}
		return pageDto;

	
	}
	public PageDTO handleScoreTrends(PageNavigationContext pageNavigationContext) {

		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		String action = pageNavigationContext.getActionName();
		ScoresSearchData searchData = new ScoresSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();

		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					ScoresSearchData.class);
			searchData.setStudentId(studentId);

			String yearMonth = searchData.getMonthYear();
			if (yearMonth != null && !"".equals(yearMonth)) {
				if (yearMonth.length() < 6) {
					throw new BusinessException(ErrorCodes.INVALID_YEARMONTH_FORMAT, "Invalid Year Month Format");
				}
			}else
			{
				
				// if the year month is null, default with current year month... 
				LocalDate now = LocalDate.now();
				int month = now.getMonthValue();
				String monthStr = (month<10) ? "0"+month : ""+month; 
				String year = ""+now.getYear();
				yearMonth = year+monthStr;
				System.out.println("year month "+yearMonth);
				searchData.setMonthYear(yearMonth);
				action="month";
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (PageAction.Month.toString().equalsIgnoreCase(action) || PageAction.Year.toString().equalsIgnoreCase(action)) {

			List<DailyScoreDTO> dailyScores = scoreTrendService.findDailyScores(searchData);
			List<MonthlyScoreGenreDTO> monthlyScoresByGenre = scoreTrendService.findMonthlyScoresByGenre(searchData);
			
			ScorePageData pageData = new ScorePageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setDailyScores(dailyScores);
			pageData.setMonthlyScoresByGenre(monthlyScoresByGenre);
			pageDto.setData(pageData);
		}
		if (PageAction.next.toString().equalsIgnoreCase(action)
				|| PageAction.LeftSwipe.toString().equalsIgnoreCase(action)) {

			String yearMonth = searchData.getMonthYear();
			String year = yearMonth.substring(0, 4);
			String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
			int month = Integer.parseInt(monthStr);
			if(month>12 || month <1)
			{
				throw new BusinessException(ErrorCodes.INVALID_MONTH, "Invalid Month (Month should be between 1 - 12)");

			}
			if(month<12)
			month = month + 1;

			monthStr = (month>=10) ? ""+month : "0"+monthStr;
//			
//			if (month >= 10) {
//				monthStr = "" + month;
//			} else {
//				monthStr = "0" + month;
//
//			}
			yearMonth = year + monthStr;
			System.out.println("yearMonth :" + yearMonth);

			searchData.setMonthYear(yearMonth);

			List<DailyScoreDTO> dailyScores = scoreTrendService.findDailyScores(searchData);
			List<MonthlyScoreGenreDTO> monthlyScoresByGenre = scoreTrendService.findMonthlyScoresByGenre(searchData);
			
			ScorePageData pageData = new ScorePageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setDailyScores(dailyScores);
			pageData.setMonthlyScoresByGenre(monthlyScoresByGenre);
			pageDto.setData(pageData);

		}
		if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.RightSwipe.toString().equalsIgnoreCase(action)) {

			String yearMonth = searchData.getMonthYear();
			System.out.println("yearMonth :" + yearMonth);
			String year = yearMonth.substring(0, 4);
			String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
			int month = Integer.parseInt(monthStr);
			System.out.println("***month  " + month);
			if (month > 1) {
				month = month - 1;
			}

			monthStr = (month >=10) ? ""+month : "0"+month;
			
//			if (month >= 10) {
//				monthStr = "" + month;
//			} else {
//				monthStr = "0" + month;
//
//			}
			yearMonth = year + monthStr;
			System.out.println("yearMonth :" + yearMonth);

			searchData.setMonthYear(yearMonth);

			List<DailyScoreDTO> dailyScores = scoreTrendService.findDailyScores(searchData);
			List<MonthlyScoreGenreDTO> monthlyScoresByGenre = scoreTrendService.findMonthlyScoresByGenre(searchData);
			
			ScorePageData pageData = new ScorePageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setDailyScores(dailyScores);
			pageData.setMonthlyScoresByGenre(monthlyScoresByGenre);
			pageDto.setData(pageData);
		}
		return pageDto;

	}
	@Override
	public PageDTO saveAsMaster(String actionName, PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(String actionName, PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

}
