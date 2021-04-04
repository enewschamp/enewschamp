package com.enewschamp.app.scores.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.handler.NewsArticlePageHandler;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.scores.dto.StudentScoresYearlyGenreDTO;
import com.enewschamp.app.scores.page.data.ScorePageData;
import com.enewschamp.app.scores.page.data.ScoresSearchData;
import com.enewschamp.app.scores.page.data.YearlyScorePageData;
import com.enewschamp.app.scores.service.ScoreService;
import com.enewschamp.app.student.scores.dto.StudentScoresDailyDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyGenreDTO;
import com.enewschamp.app.student.scores.dto.StudentScoresMonthlyTotalDTO;
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
	ScoreService scoreService;

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
				return (PageDTO) m.invoke(this, pageNavigationContext);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if (e.getCause() instanceof BusinessException) {
					throw ((BusinessException) e.getCause());
				} else {
					throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, ExceptionUtils.getStackTrace(e));
				}
			} catch (NoSuchMethodException nsmEx) {
				nsmEx.printStackTrace();
			} catch (SecurityException seEx) {
				seEx.printStackTrace();
			}
		}
		PageDTO pageDTO = new PageDTO();
		pageDTO.setHeader(pageNavigationContext.getPageRequest().getHeader());
		return pageDTO;
	}

	public PageDTO loadScoresYearly(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		ScoresSearchData searchData = new ScoresSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == 0L) {
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
					ScoresSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			LocalDate today = LocalDate.now();
			String year = "" + today.getYear();
			String month = (today.getMonthValue() > 9 ? "" + today.getMonthValue() : "0" + today.getMonthValue());
			searchData.setYearMonth(year + month);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<StudentScoresYearlyGenreDTO> scoresYearlyGenre = scoreService.findScoresYearlyGenre(searchData);
		List<StudentScoresMonthlyTotalDTO> scoresMonthly = scoreService.findYScoresMonthly(searchData);
		YearlyScorePageData pageData = new YearlyScorePageData();
		pageData.setScoresYearlyGenre(scoresYearlyGenre);
		pageData.setScoresMonthly(scoresMonthly);
		pageDto.setData(pageData);
		return pageDto;

	}

	public PageDTO loadScoresMonthly(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		ScoresSearchData searchData = new ScoresSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == 0L) {
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
					ScoresSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			String yearMonth = searchData.getYearMonth();
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
				searchData.setYearMonth(yearMonth);
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<StudentScoresDailyDTO> scoresDaily = scoreService.findScoresDaily(searchData);
		List<StudentScoresMonthlyGenreDTO> scoresMonthlyGenre = scoreService.findScoresMonthlyGenre(searchData);
		ScorePageData pageData = new ScorePageData();
		pageData.setYearMonth("" + searchData.getYearMonth());
		pageData.setScoresDaily(scoresDaily);
		pageData.setScoresMonthlyGenre(scoresMonthlyGenre);
		pageDto.setData(pageData);
		return pageDto;

	}

	public PageDTO loadNextScoresMonthly(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		ScoresSearchData searchData = new ScoresSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == 0L) {
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
					ScoresSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			String yearMonth = searchData.getYearMonth();
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
				searchData.setYearMonth(yearMonth);
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String yearMonth = searchData.getYearMonth();
		String year = yearMonth.substring(0, 4);
		String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
		int month = Integer.parseInt(monthStr);
		if (month > 12 || month < 1) {
			throw new BusinessException(ErrorCodeConstants.INVALID_MONTH);
		}
		if (month < 12) {
			month = month + 1;
		}
		monthStr = (month >= 10) ? "" + month : "0" + month;
		yearMonth = year + monthStr;
		searchData.setYearMonth(yearMonth);
		List<StudentScoresDailyDTO> scoresDaily = scoreService.findScoresDaily(searchData);
		List<StudentScoresMonthlyGenreDTO> scoresMonthlyGenre = scoreService.findScoresMonthlyGenre(searchData);
		ScorePageData pageData = new ScorePageData();
		pageData.setYearMonth("" + searchData.getYearMonth());
		pageData.setScoresDaily(scoresDaily);
		pageData.setScoresMonthlyGenre(scoresMonthlyGenre);
		pageDto.setData(pageData);
		return pageDto;

	}

	public PageDTO loadPreviousScoresMonthly(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		ScoresSearchData searchData = new ScoresSearchData();
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		if (studentId == 0L) {
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
					ScoresSearchData.class);
			searchData.setStudentId(studentId);
			searchData.setReadingLevel(readingLevel);
			String yearMonth = searchData.getYearMonth();
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
				searchData.setYearMonth(yearMonth);
			}
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String yearMonth = searchData.getYearMonth();
		String year = yearMonth.substring(0, 4);
		String monthStr = yearMonth.substring(yearMonth.length() - 2, yearMonth.length());
		int month = Integer.parseInt(monthStr);
		if (month > 1) {
			month = month - 1;
		}
		monthStr = (month >= 10) ? "" + month : "0" + month;
		yearMonth = year + monthStr;
		searchData.setYearMonth(yearMonth);
		List<StudentScoresDailyDTO> scoresDaily = scoreService.findScoresDaily(searchData);
		List<StudentScoresMonthlyGenreDTO> scoresMonthlyGenre = scoreService.findScoresMonthlyGenre(searchData);
		ScorePageData pageData = new ScorePageData();
		pageData.setYearMonth("" + searchData.getYearMonth());
		pageData.setScoresDaily(scoresDaily);
		pageData.setScoresMonthlyGenre(scoresMonthlyGenre);
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
