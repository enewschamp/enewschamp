package com.enewschamp.app.article.page.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.enewschamp.app.article.page.dto.ArticlePageData;
import com.enewschamp.app.article.page.dto.NewsArticlePageData;
import com.enewschamp.app.article.page.dto.PublicationData;
import com.enewschamp.app.article.page.dto.PublicationPageData;
import com.enewschamp.app.common.CommonFilterData;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PageDTO;
import com.enewschamp.app.common.PageRequestDTO;
import com.enewschamp.app.common.PaginationData;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorDTO;
import com.enewschamp.app.savedarticle.dto.SavedArticleData;
import com.enewschamp.app.savedarticle.dto.SavedArticlePageData;
import com.enewschamp.app.savedarticle.dto.SavedNewsArticleSearchRequest;
import com.enewschamp.app.savedarticle.service.SavedNewsArticleService;
import com.enewschamp.app.school.repository.NewsArticleSummaryRepository;
import com.enewschamp.app.student.business.StudentActivityBusiness;
import com.enewschamp.app.student.dto.StudentActivityDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.IPageHandler;
import com.enewschamp.domain.common.PageNavigationContext;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.service.GenreService;
import com.enewschamp.subscription.app.dto.StudentPreferencesDTO;
import com.enewschamp.subscription.domain.business.PreferenceBusiness;
import com.enewschamp.subscription.domain.business.StudentControlBusiness;
import com.enewschamp.subscription.domain.business.SubscriptionBusiness;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "NewsArticlePageHandler")
public class NewsArticlePageHandler implements IPageHandler {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	NewsArticleSummaryRepository customImpl;

	@Autowired
	SubscriptionBusiness subscriptionBusiness;

	@Autowired
	StudentControlBusiness studentControlBusiness;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CommonService commonService;

	@Autowired
	private StudentActivityBusiness studentActivityBusiness;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	SavedNewsArticleService savedNewsArticleService;

	@Autowired
	PreferenceBusiness preferenceBusiness;

	@Autowired
	GenreService genreService;

	@Autowired
	private PropertiesService propertiesService;

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

	public PageDTO loadNewsArticlesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();
		PublicationPageData pageData = new PublicationPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setPublicationDateTo(publicationDate);
		searchRequestData.setArticleType(ArticleType.NEWSARTICLE);
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);

		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO loadNextNewsArticlesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		NewsArticlePageData pageData = new NewsArticlePageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int readingLevel = pageData.getReadingLevel();
		LocalDate publicationDate = pageData.getPublicationDate();
		Long nextNewsArticleId = newsArticleService.getNextNewsArticleAvailable(publicationDate, editionId,
				readingLevel, ArticleType.NEWSARTICLE, pageData.getNewsArticleId());
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setArticleId(nextNewsArticleId);
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, 1, 10,
				pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		if (!pageResult.getContent().isEmpty()) {
			NewsArticleSummaryDTO newsArticleSummary = pageResult.getContent().get(0);
			pageData = modelMapper.map(newsArticleSummary, NewsArticlePageData.class);
			// pageData.setQuizAvailable(newsArticleSummary.isNoQuiz() == true ? "N" : "Y");
			// if (!newsArticleSummary.isNoQuiz() && studentId > 0) {
			if (1 == 1) {
				StudentActivityDTO stdactivity = studentActivityBusiness.getActivity(studentId,
						newsArticleSummary.getNewsArticleId());
				if (stdactivity != null) {
					pageData.setOpinionText(stdactivity.getOpinion());
					pageData.setReaction(stdactivity.getLikeLevel());
					pageData.setSaved(stdactivity.getSaved() != null ? stdactivity.getSaved() : "N");
					pageData.setQuizScore(stdactivity.getQuizScore());
					pageData.setQuizCompleted(stdactivity.getQuizScore() != null ? "Y" : "N");
				}
			} else {
				pageData.setOpinionText("");
				pageData.setReaction("");
				pageData.setSaved("N");
				pageData.setQuizScore(0L);
				pageData.setQuizCompleted("N");
			}
		}
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO loadPreviousNewsArticlesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		NewsArticlePageData pageData = new NewsArticlePageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int readingLevel = pageData.getReadingLevel();
		LocalDate publicationDate = pageData.getPublicationDate();
		Long nextNewsArticleId = newsArticleService.getPreviousNewsArticleAvailable(publicationDate, editionId,
				readingLevel, ArticleType.NEWSARTICLE, pageData.getNewsArticleId());
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		searchRequestData.setArticleId(nextNewsArticleId);
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, 1, 10,
				pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		if (!pageResult.getContent().isEmpty()) {
			NewsArticleSummaryDTO newsArticleSummary = pageResult.getContent().get(0);
			pageData = modelMapper.map(newsArticleSummary, NewsArticlePageData.class);
			// pageData.setQuizAvailable(newsArticleSummary.isNoQuiz() == true ? "N" : "Y");
			// if (!newsArticleSummary.isNoQuiz() && studentId > 0) {
			if (1 == 1) {
				StudentActivityDTO stdactivity = studentActivityBusiness.getActivity(studentId,
						newsArticleSummary.getNewsArticleId());
				if (stdactivity != null) {
					pageData.setOpinionText(stdactivity.getOpinion());
					pageData.setReaction(stdactivity.getLikeLevel());
					pageData.setSaved(stdactivity.getSaved() != null ? stdactivity.getSaved() : "N");
					pageData.setQuizScore(stdactivity.getQuizScore());
					pageData.setQuizCompleted(stdactivity.getQuizScore() != null ? "Y" : "N");
				}
			} else {
				pageData.setOpinionText("");
				pageData.setReaction("");
				pageData.setSaved("N");
				pageData.setQuizScore(0L);
				pageData.setQuizCompleted("N");
			}
		}
		pageDto.setData(pageData);
		return pageDto;
	}

	public PageDTO viewArticlesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		NewsArticlePageData pageData = new NewsArticlePageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		List<NewsArticleSummaryDTO> pageResult = customImpl.getArticleDetails(pageData.getNewsArticleId(), studentId);
		if (pageResult != null && pageResult.size() > 0) {
			pageData = modelMapper.map(pageResult.get(0), NewsArticlePageData.class);
		}
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		pageDto.setData(pageData);
		return pageDto;
	}
	/*
	 * public PageDTO viewArticlesPage(PageNavigationContext pageNavigationContext)
	 * { PageDTO pageDto = new PageDTO();
	 * pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader()); String
	 * emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
	 * Long studentId = studentControlBusiness.getStudentId(emailId);
	 * NewsArticlePageData pageData = new NewsArticlePageData(); pageData =
	 * mapPageData(pageData, pageNavigationContext.getPageRequest());
	 * NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
	 * searchRequestData.setArticleId(pageData.getNewsArticleId());
	 * Page<NewsArticleSummaryDTO> pageResult =
	 * newsArticleService.findArticles(searchRequestData, studentId, 1, 10,
	 * pageNavigationContext.getPageRequest().getHeader()); HeaderDTO header =
	 * pageNavigationContext.getPageRequest().getHeader();
	 * pageDto.setHeader(header); if (!pageResult.getContent().isEmpty()) {
	 * NewsArticleSummaryDTO newsArticleSummary = pageResult.getContent().get(0);
	 * pageData = modelMapper.map(newsArticleSummary, NewsArticlePageData.class);
	 * pageData.setQuizAvailable(newsArticleSummary.isNoQuiz() == true ? "N" : "Y");
	 * if (!newsArticleSummary.isNoQuiz() && studentId > 0) { StudentActivityDTO
	 * stdactivity = studentActivityBusiness.getActivity(studentId,
	 * newsArticleSummary.getNewsArticleId()); if (stdactivity != null) {
	 * pageData.setOpinionText(stdactivity.getOpinion());
	 * pageData.setReaction(stdactivity.getLikeLevel());
	 * pageData.setSaved(stdactivity.getSaved() != null ? stdactivity.getSaved() :
	 * "N"); pageData.setQuizScore(stdactivity.getQuizScore());
	 * pageData.setQuizCompleted(stdactivity.getQuizScore() != null ? "Y" : "N"); }
	 * } else { pageData.setOpinionText(""); pageData.setReaction("");
	 * pageData.setSaved("N"); pageData.setQuizScore(0L);
	 * pageData.setQuizCompleted("N"); } } pageDto.setData(pageData); return
	 * pageDto; }
	 */

	public PageDTO loadSavedArticlesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();

		SavedArticlePageData pageData = new SavedArticlePageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		CommonFilterData filterData = new CommonFilterData();
		filterData = commonService.mapPageData(filterData, pageNavigationContext.getPageRequest());
		SavedNewsArticleSearchRequest searchRequestData = new SavedNewsArticleSearchRequest();
		searchRequestData.setEditionId(editionId);
		searchRequestData.setGenre(filterData.getGenre());
		searchRequestData.setHeadline(filterData.getHeadline());
		searchRequestData.setMonth(filterData.getMonth());
		searchRequestData.setStudentId(studentId);
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		Page<NewsArticleSummaryDTO> pageResult = savedNewsArticleService.findSavedArticles(searchRequestData, pageNo,
				pageSize);
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);

		List<NewsArticleSummaryDTO> savedArticleSummaryList = pageResult.getContent();
		SavedArticlePageData savedPageData = new SavedArticlePageData();
		List<SavedArticleData> savedArticleList = new ArrayList<SavedArticleData>();
		List<StudentActivityDTO> savedArticlesList = studentActivityBusiness.getSavedArticles(studentId);
		for (NewsArticleSummaryDTO dto : savedArticleSummaryList) {
			Long savedArticleId = 0L;
			if (savedArticlesList != null && !savedArticlesList.isEmpty()) {
				for (StudentActivityDTO studentSavedArticles : savedArticlesList) {
					savedArticleId = studentSavedArticles.getNewsArticleId();
				}
				if (dto.getNewsArticleId() == savedArticleId) {
					SavedArticleData savedData = new SavedArticleData();
					savedData.setGenre(dto.getGenre());
					savedData.setHeadline(dto.getHeadline());
					savedData.setPublicationDate(dto.getPublicationDate());
					savedData.setNewsArticleId(dto.getNewsArticleId());
					savedArticleList.add(savedData);
				}
			}

		}
		if (savedArticleList.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		savedPageData.setSavedNewsArticles(savedArticleList);
		savedPageData.setGenreLOV(genreService.getLOV());
		pageDto.setData(savedPageData);
		return pageDto;
	}

	public PageDTO loadNextPrevNewsArticlesPage(PageNavigationContext pageNavigationContext) {
		PageDTO pageDto = new PageDTO();
		pageDto.setHeader(pageNavigationContext.getPageRequest().getHeader());
		String emailId = pageNavigationContext.getPageRequest().getHeader().getEmailId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		String editionId = pageNavigationContext.getPageRequest().getHeader().getEditionId();
		LocalDate publicationDate = pageNavigationContext.getPageRequest().getHeader().getPublicationDate();
		NewsArticleSearchRequest searchRequestData = new NewsArticleSearchRequest();
		PublicationPageData pageData = new PublicationPageData();
		pageData = mapPageData(pageData, pageNavigationContext.getPageRequest());
		int pageNo = 1;
		int pageSize = Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		if (pageData.getPagination() != null) {
			pageNo = pageData.getPagination().getPageNumber() > 0 ? pageData.getPagination().getPageNumber() : 1;
			pageSize = pageData.getPagination().getPageSize() > 0 ? pageData.getPagination().getPageSize()
					: Integer.valueOf(propertiesService.getProperty(PropertyConstants.PAGE_SIZE));
		} else {
			PaginationData paginationData = new PaginationData();
			paginationData.setPageNumber(pageNo);
			paginationData.setPageSize(pageSize);
			pageData.setPagination(paginationData);
		}
		searchRequestData.setEditionId(editionId);
		searchRequestData.setPublicationDate(publicationDate);
		searchRequestData.setEditionId(editionId);
		searchRequestData.setArticleType(ArticleType.NEWSARTICLE);
		Page<NewsArticleSummaryDTO> pageResult = newsArticleService.findArticles(searchRequestData, studentId, pageNo,
				pageSize, pageNavigationContext.getPageRequest().getHeader());
		HeaderDTO header = pageNavigationContext.getPageRequest().getHeader();
		pageDto.setHeader(header);
		List<PublicationData> publicationData = commonService.mapStudentData(pageResult, studentId);
		if (publicationData.size() == 0) {
			pageData.getPagination().setPageNumber(-1);
		}
		pageData.setNewsArticles(publicationData);
		pageDto.setData(pageData);
		return pageDto;
	}

	@Override
	public PageDTO saveAsMaster(PageRequestDTO pageRequest) {
		PageDTO pageDTO = new PageDTO();
		return pageDTO;
	}

	@Override
	public PageDTO handleAppAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		String methodName = pageNavigatorDTO.getSubmissionMethod();
		if (methodName != null && !"".equals(methodName)) {
			Class[] params = new Class[2];
			params[0] = PageRequestDTO.class;
			params[1] = PageNavigatorDTO.class;
			Method m = null;
			try {
				m = this.getClass().getDeclaredMethod(methodName, params);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			try {
				return (PageDTO) m.invoke(this, pageRequest, pageNavigatorDTO);
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
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleLikeAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		ArticlePageData articlePageData = new ArticlePageData();
		articlePageData = mapPageData(articlePageData, pageRequest);
		Long newsArticleId = articlePageData.getNewsArticleId();
		StudentPreferencesDTO studPref = preferenceBusiness.getPreferenceFromMaster(studentId);
		int readingLevel = 3;
		if (studPref != null && studPref.getReadingLevel() != null) {
			readingLevel = Integer.parseInt(studPref.getReadingLevel());
		}
		NewsArticle article = newsArticleService.get(newsArticleId);
		if (article == null) {
			throw new BusinessException(ErrorCodeConstants.ARTICLE_NOT_FOUND, String.valueOf(newsArticleId));
		} else if (article.getReadingLevel() != readingLevel) {
			throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS, String.valueOf(newsArticleId));
		}
		try {
			String likeFlag = articlePageData.getLikeFlag();
			studentActivityBusiness.likeArticle(studentId, newsArticleId, likeFlag, readingLevel, editionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, e.getMessage());
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleSaveOpinionAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		ArticlePageData articlePageData = new ArticlePageData();
		articlePageData = mapPageData(articlePageData, pageRequest);
		Long newsArticleId = articlePageData.getNewsArticleId();
		StudentPreferencesDTO studPref = preferenceBusiness.getPreferenceFromMaster(studentId);
		int readingLevel = 3;
		if (studPref != null && studPref.getReadingLevel() != null) {
			readingLevel = Integer.parseInt(studPref.getReadingLevel());
		}
		NewsArticle article = newsArticleService.get(newsArticleId);
		if (article == null) {
			throw new BusinessException(ErrorCodeConstants.ARTICLE_NOT_FOUND, String.valueOf(newsArticleId));
		} else if (article.getReadingLevel() != readingLevel) {
			throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS, String.valueOf(newsArticleId));
		}
		try {
			String opinion = articlePageData.getOpinionText();
			studentActivityBusiness.saveOpinion(studentId, newsArticleId, opinion, readingLevel, editionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, e.getMessage());
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	public PageDTO handleSaveArticleAction(PageRequestDTO pageRequest, PageNavigatorDTO pageNavigatorDTO) {
		PageDTO pageDTO = new PageDTO();
		String emailId = pageRequest.getHeader().getEmailId();
		String editionId = pageRequest.getHeader().getEditionId();
		Long studentId = studentControlBusiness.getStudentId(emailId);
		ArticlePageData articlePageData = new ArticlePageData();
		articlePageData = mapPageData(articlePageData, pageRequest);
		Long newsArticleId = articlePageData.getNewsArticleId();
		StudentPreferencesDTO studPref = preferenceBusiness.getPreferenceFromMaster(studentId);
		int readingLevel = 3;
		if (studPref != null && studPref.getReadingLevel() != null) {
			readingLevel = Integer.parseInt(studPref.getReadingLevel());
		}
		NewsArticle article = newsArticleService.get(newsArticleId);
		if (article == null) {
			throw new BusinessException(ErrorCodeConstants.ARTICLE_NOT_FOUND, String.valueOf(newsArticleId));
		} else if (article.getReadingLevel() != readingLevel) {
			throw new BusinessException(ErrorCodeConstants.UNAUTH_ACCESS, String.valueOf(newsArticleId));
		}
		try {
			String saveFlag = articlePageData.getSaveFlag();
			studentActivityBusiness.saveArticle(studentId, newsArticleId, saveFlag, readingLevel, editionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ErrorCodeConstants.RUNTIME_EXCEPTION, e.getMessage());
		}
		pageDTO.setHeader(pageRequest.getHeader());
		return pageDTO;
	}

	private ArticlePageData mapPageData(ArticlePageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), ArticlePageData.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageData;
	}

	private NewsArticlePageData mapPageData(NewsArticlePageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), NewsArticlePageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}

	private SavedArticlePageData mapPageData(SavedArticlePageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), SavedArticlePageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}

	private PublicationPageData mapPageData(PublicationPageData pageData, PageRequestDTO pageRequest) {
		try {
			pageData = objectMapper.readValue(pageRequest.getData().toString(), PublicationPageData.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return pageData;
	}
}
