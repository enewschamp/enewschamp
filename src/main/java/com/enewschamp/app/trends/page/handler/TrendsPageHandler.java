package com.enewschamp.app.trends.page.handler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.common.PageAction;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.trends.dto.DailyArticleDTO;
import com.enewschamp.app.trends.dto.DailyQuizScoresDTO;
import com.enewschamp.app.trends.dto.MonthlyArticleDTO;
import com.enewschamp.app.trends.dto.MonthlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizGenreDTO;
import com.enewschamp.app.trends.dto.MonthlyQuizScoresDTO;
import com.enewschamp.app.trends.dto.TrendsPageData;
import com.enewschamp.app.trends.dto.TrendsSearchData;
import com.enewschamp.app.trends.dto.TrendsYearlyPageData;
import com.enewschamp.app.trends.dto.YearlyArticlesGenreDTO;
import com.enewschamp.app.trends.dto.YearlyQuizGenreDTO;
import com.enewschamp.app.trends.service.TrendsService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "TrendsPageHandler")
public class TrendsPageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TrendsService trendsService;

	@Override
	public PageDTO handleAction(String actionName, PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = null;
		// pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		String operation = pageNavigationContext.getPageRequest().getHeader().getOperation();
		String action = pageNavigationContext.getPageRequest().getHeader().getAction();
		if ("ListMonthlyTrends".equals(operation) || PageAction.Trends.toString().equalsIgnoreCase(action)) {
			pageDto = handleMonthlyTrends(pageNavigationContext);
		}
		if ("ListYearlyTrends".equals(operation)) {
			pageDto = handleYearlyTrends(pageNavigationContext);

		}
		return pageDto;
	}

	public PageDTO handleYearlyTrends(PageNavigationContext pageNavigationContext) {

		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		String action = pageNavigationContext.getActionName();
		TrendsSearchData searchData = new TrendsSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();

		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					TrendsSearchData.class);
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
		if (PageAction.Year.toString().equalsIgnoreCase(action) || PageAction.Month.toString().equalsIgnoreCase(action)) {

			List<MonthlyArticleDTO> monthlyArticlesTrend = trendsService.findMonthlyNewsArticlesTrend(searchData);
			List<MonthlyQuizScoresDTO> monthlyQuizScoresTrend = trendsService.findMonthlyQuizScoreTrend(searchData);
			List<YearlyArticlesGenreDTO> yearlyArticlesGenre = trendsService.findYearlyArticlesByGenreTrend(searchData);
			List<YearlyQuizGenreDTO> yearlyQuizGenre = trendsService.findYearlyQuizScoreByGenre(searchData);
			TrendsYearlyPageData pageData = new TrendsYearlyPageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setMonthlyNewsArticles(monthlyArticlesTrend);
			pageData.setMonthlyQuizScores(monthlyQuizScoresTrend);
			pageData.setYearlyArticlesByGenre(yearlyArticlesGenre);
			pageData.setYearlyQuizByGenre(yearlyQuizGenre);

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

			List<MonthlyArticleDTO> monthlyArticlesTrend = trendsService.findMonthlyNewsArticlesTrend(searchData);
			List<MonthlyQuizScoresDTO> monthlyQuizScoresTrend = trendsService.findMonthlyQuizScoreTrend(searchData);
			List<YearlyArticlesGenreDTO> yearlyArticlesGenre = trendsService.findYearlyArticlesByGenreTrend(searchData);
			List<YearlyQuizGenreDTO> yearlyQuizGenre = trendsService.findYearlyQuizScoreByGenre(searchData);
			TrendsYearlyPageData pageData = new TrendsYearlyPageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setMonthlyNewsArticles(monthlyArticlesTrend);
			pageData.setMonthlyQuizScores(monthlyQuizScoresTrend);
			pageData.setYearlyArticlesByGenre(yearlyArticlesGenre);
			pageData.setYearlyQuizByGenre(yearlyQuizGenre);

			pageDto.setData(pageData);

			pageDto.setData(pageData);
		}
		if (PageAction.previous.toString().equalsIgnoreCase(action)
				|| PageAction.LeftSwipe.toString().equalsIgnoreCase(action)) {

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

			List<MonthlyArticleDTO> monthlyArticlesTrend = trendsService.findMonthlyNewsArticlesTrend(searchData);
			List<MonthlyQuizScoresDTO> monthlyQuizScoresTrend = trendsService.findMonthlyQuizScoreTrend(searchData);
			List<YearlyArticlesGenreDTO> yearlyArticlesGenre = trendsService.findYearlyArticlesByGenreTrend(searchData);
			List<YearlyQuizGenreDTO> yearlyQuizGenre = trendsService.findYearlyQuizScoreByGenre(searchData);
			TrendsYearlyPageData pageData = new TrendsYearlyPageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setMonthlyNewsArticles(monthlyArticlesTrend);
			pageData.setMonthlyQuizScores(monthlyQuizScoresTrend);
			pageData.setYearlyArticlesByGenre(yearlyArticlesGenre);
			pageData.setYearlyQuizByGenre(yearlyQuizGenre);

			pageDto.setData(pageData);

			pageDto.setData(pageData);
		}
		return pageDto;

	}

	public PageDTO handleMonthlyTrends(PageNavigationContext pageNavigationContext) {

		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());

		String action = pageNavigationContext.getActionName();
		TrendsSearchData searchData = new TrendsSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailID();

		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodes.STUDENT_DTLS_NOT_FOUND);
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					TrendsSearchData.class);
			searchData.setStudentId(studentId);

			String yearMonth = searchData.getMonthYear();
			if (yearMonth != null && !"".equals(yearMonth)) {
				if (yearMonth.length() < 6) {
					throw new BusinessException(ErrorCodes.INVALID_YEARMONTH_FORMAT, "Invalid Year Month Format");
				}
			}
			else
			{
				// Default load monthly data..
				
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

			List<DailyArticleDTO> dailyArticlesTrend = trendsService.findDailyNewsArticlesTrend(searchData);
			List<DailyQuizScoresDTO> dailyQuizScoresTrend = trendsService.findDailyQuizScoreTrend(searchData);
			List<MonthlyArticlesGenreDTO> monthlyArticlesGenre = trendsService
					.findMonthlyArticlesByGenreTrend(searchData);
			List<MonthlyQuizGenreDTO> monthlyQuizGenre = trendsService.findMonthlyQuizScoreByGenre(searchData);
			TrendsPageData pageData = new TrendsPageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setDailyNewsArticles(dailyArticlesTrend);
			pageData.setDailyQuizScores(dailyQuizScoresTrend);
			pageData.setMonthlyArticlesByGenre(monthlyArticlesGenre);
			pageData.setMonthlyQuizByGenre(monthlyQuizGenre);

			pageDto.setData(pageData);
		}
		if (PageAction.next.toString().equalsIgnoreCase(action) || PageAction.LeftSwipe.toString().equalsIgnoreCase(action)
				) {

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
			
			monthStr = (month>=10) ? ""+month :"0"+month;

			yearMonth = year + monthStr;
			System.out.println("yearMonth :" + yearMonth);

			searchData.setMonthYear(yearMonth);

			List<DailyArticleDTO> dailyArticlesTrend = trendsService.findDailyNewsArticlesTrend(searchData);
			List<DailyQuizScoresDTO> dailyQuizScoresTrend = trendsService.findDailyQuizScoreTrend(searchData);
			List<MonthlyArticlesGenreDTO> monthlyArticlesGenre = trendsService
					.findMonthlyArticlesByGenreTrend(searchData);
			List<MonthlyQuizGenreDTO> monthlyQuizGenre = trendsService.findMonthlyQuizScoreByGenre(searchData);
			TrendsPageData pageData = new TrendsPageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setDailyNewsArticles(dailyArticlesTrend);
			pageData.setDailyQuizScores(dailyQuizScoresTrend);
			pageData.setMonthlyArticlesByGenre(monthlyArticlesGenre);
			pageData.setMonthlyQuizByGenre(monthlyQuizGenre);

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
			monthStr = (month >= 10) ? ""+month : "0"+month;

			yearMonth = year + monthStr;
			System.out.println("yearMonth :" + yearMonth);

			searchData.setMonthYear(yearMonth);

			List<DailyArticleDTO> dailyArticlesTrend = trendsService.findDailyNewsArticlesTrend(searchData);
			List<DailyQuizScoresDTO> dailyQuizScoresTrend = trendsService.findDailyQuizScoreTrend(searchData);
			List<MonthlyArticlesGenreDTO> monthlyArticlesGenre = trendsService
					.findMonthlyArticlesByGenreTrend(searchData);
			List<MonthlyQuizGenreDTO> monthlyQuizGenre = trendsService.findMonthlyQuizScoreByGenre(searchData);
			TrendsPageData pageData = new TrendsPageData();
			pageData.setYearMonth("" + searchData.getMonthYear());
			pageData.setDailyNewsArticles(dailyArticlesTrend);
			pageData.setDailyQuizScores(dailyQuizScoresTrend);
			pageData.setMonthlyArticlesByGenre(monthlyArticlesGenre);
			pageData.setMonthlyQuizByGenre(monthlyQuizGenre);

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
