package com.enewschamp.app.trends.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
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
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
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

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Override
	public PageDTO handleAction(PageRequestDTO pageRequest) {
		return null;
	}

	@Override
	public PageDTO loadPage(PageNavigationContext pageNavigationContext) {
		String methodName = pageNavigationContext.getLoadMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[1];
			params[0] = PageNavigationContext.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	public PageDTO handleYearlyTrends(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		TrendsSearchData searchData = new TrendsSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					TrendsSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			LocalDate today = LocalDate.now();
			String year = "" + today.getYear();
			String month = (today.getMonthValue() > 9 ? "" + today.getMonthValue() : "0" + today.getMonthValue());
			searchData.setMonthYear(year + month);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<MonthlyArticleDTO> monthlyArticlesTrend = trendsService.findMonthlyNewsArticlesTrend(searchData);
		List<MonthlyQuizScoresDTO> monthlyQuizScoresTrend = trendsService.findMonthlyQuizScoreTrend(searchData);
		List<YearlyArticlesGenreDTO> yearlyArticlesGenre = trendsService.findYearlyArticlesByGenreTrend(searchData);
		List<YearlyQuizGenreDTO> yearlyQuizGenre = trendsService.findYearlyQuizScoreByGenre(searchData);
		TrendsYearlyPageData pageData = new TrendsYearlyPageData();
		pageData.setMonthlyNewsArticles(monthlyArticlesTrend);
		pageData.setMonthlyQuizScores(monthlyQuizScoresTrend);
		pageData.setYearlyArticlesByGenre(yearlyArticlesGenre);
		pageData.setYearlyQuizByGenre(yearlyQuizGenre);
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO handleMonthlyTrends(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		TrendsSearchData searchData = new TrendsSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					TrendsSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			String yearMonth = searchData.getMonthYear();
			if (yearMonth != null && !"".equals(yearMonth)) {
				if (yearMonth.length() < 6) {
					throw new BusinessException(ErrorCodeConstants.INVALID_YEARMONTH_FORMAT);
				}
			} else {
				LocalDate now = LocalDate.now();
				int month = now.getMonthValue();
				String monthStr = (month < 10) ? "0" + month : "" + month;
				String year = "" + now.getYear();
				yearMonth = year + monthStr;
				searchData.setMonthYear(yearMonth);
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<DailyArticleDTO> dailyArticlesTrend = trendsService.findDailyNewsArticlesTrend(searchData);
		List<DailyQuizScoresDTO> dailyQuizScoresTrend = trendsService.findDailyQuizScoreTrend(searchData);
		List<MonthlyArticlesGenreDTO> monthlyArticlesGenre = trendsService.findMonthlyArticlesByGenreTrend(searchData);
		List<MonthlyQuizGenreDTO> monthlyQuizGenre = trendsService.findMonthlyQuizScoreByGenre(searchData);
		TrendsPageData pageData = new TrendsPageData();
		pageData.setYearMonth("" + searchData.getMonthYear());
		pageData.setDailyNewsArticles(dailyArticlesTrend);
		pageData.setDailyQuizScores(dailyQuizScoresTrend);
		pageData.setMonthlyArticlesByGenre(monthlyArticlesGenre);
		pageData.setMonthlyQuizByGenre(monthlyQuizGenre);
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO handleNextMonthlyTrends(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		TrendsSearchData searchData = new TrendsSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					TrendsSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			String yearMonth = searchData.getMonthYear();
			if (yearMonth != null && !"".equals(yearMonth)) {
				if (yearMonth.length() < 6) {
					throw new BusinessException(ErrorCodeConstants.INVALID_YEARMONTH_FORMAT);
				}
			} else {
				LocalDate now = LocalDate.now();
				int month = now.getMonthValue();
				String monthStr = (month < 10) ? "0" + month : "" + month;
				String year = "" + now.getYear();
				yearMonth = year + monthStr;
				searchData.setMonthYear(yearMonth);
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String yearMonth = searchData.getMonthYear();
		String year = yearMonth.substring(0, 4);
		String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
		int month = Integer.parseInt(monthStr);
		if (month > 12 || month < 1) {
			throw new BusinessException(ErrorCodeConstants.INVALID_MONTH);
		}
		if (month < 12)
			month = month + 1;

		monthStr = (month >= 10) ? "" + month : "0" + month;
		yearMonth = year + monthStr;
		searchData.setMonthYear(yearMonth);

		List<DailyArticleDTO> dailyArticlesTrend = trendsService.findDailyNewsArticlesTrend(searchData);
		List<DailyQuizScoresDTO> dailyQuizScoresTrend = trendsService.findDailyQuizScoreTrend(searchData);
		List<MonthlyArticlesGenreDTO> monthlyArticlesGenre = trendsService.findMonthlyArticlesByGenreTrend(searchData);
		List<MonthlyQuizGenreDTO> monthlyQuizGenre = trendsService.findMonthlyQuizScoreByGenre(searchData);
		TrendsPageData pageData = new TrendsPageData();
		pageData.setYearMonth("" + searchData.getMonthYear());
		pageData.setDailyNewsArticles(dailyArticlesTrend);
		pageData.setDailyQuizScores(dailyQuizScoresTrend);
		pageData.setMonthlyArticlesByGenre(monthlyArticlesGenre);
		pageData.setMonthlyQuizByGenre(monthlyQuizGenre);
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO handlePreviousMonthlyTrends(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		TrendsSearchData searchData = new TrendsSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == null || studentId == 0L) {
			throw new BusinessException(ErrorCodeConstants.STUDENT_DTLS_NOT_FOUND);
		}
		int readingLevel = 0;
		StudentPreferencesDTO preferenceDto = null;
		if (studentId > 0) {
			preferenceDto = preferenceBusiness.getPreferenceFromMaster(studentId);
		}
		if (preferenceDto != null) {
			readingLevel = Integer.parseInt(preferenceDto.getReadingLevel());
		} else {
			readingLevel = 3;
		}
		try {
			searchData = objectMapper.readValue(pageNavigationContext.getPageRequest().getData().toString(),
					TrendsSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			String yearMonth = searchData.getMonthYear();
			if (yearMonth != null && !"".equals(yearMonth)) {
				if (yearMonth.length() < 6) {
					throw new BusinessException(ErrorCodeConstants.INVALID_YEARMONTH_FORMAT);
				}
			} else {
				LocalDate now = LocalDate.now();
				int month = now.getMonthValue();
				String monthStr = (month < 10) ? "0" + month : "" + month;
				String year = "" + now.getYear();
				yearMonth = year + monthStr;
				searchData.setMonthYear(yearMonth);
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String yearMonth = searchData.getMonthYear();
		String year = yearMonth.substring(0, 4);
		String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
		int month = Integer.parseInt(monthStr);
		if (month > 1) {
			month = month - 1;
		}
		monthStr = (month >= 10) ? "" + month : "0" + month;
		yearMonth = year + monthStr;
		searchData.setMonthYear(yearMonth);

		List<DailyArticleDTO> dailyArticlesTrend = trendsService.findDailyNewsArticlesTrend(searchData);
		List<DailyQuizScoresDTO> dailyQuizScoresTrend = trendsService.findDailyQuizScoreTrend(searchData);
		List<MonthlyArticlesGenreDTO> monthlyArticlesGenre = trendsService.findMonthlyArticlesByGenreTrend(searchData);
		List<MonthlyQuizGenreDTO> monthlyQuizGenre = trendsService.findMonthlyQuizScoreByGenre(searchData);
		TrendsPageData pageData = new TrendsPageData();
		pageData.setYearMonth("" + searchData.getMonthYear());
		pageData.setDailyNewsArticles(dailyArticlesTrend);
		pageData.setDailyQuizScores(dailyQuizScoresTrend);
		pageData.setMonthlyArticlesByGenre(monthlyArticlesGenre);
		pageData.setMonthlyQuizByGenre(monthlyQuizGenre);
		pageDto.setData(pageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDto = new PageDTO();

		return pageDto;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}

}
