package com.enewschamp.publication.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.article.monthly.ArticlePublicationMonthlyGenre;
import com.enewschamp.app.admin.article.monthly.total.ArticlePublicationMonthlyTotal;
import com.enewschamp.app.admin.publication.monthly.repository.PublicationMonthySummaryRepositoryCustomImpl;
import com.enewschamp.app.admin.publication.monthly.total.repository.PublicationMonthyTotalRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.common.domain.service.PropertiesBackendService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationMonthlySummaryDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationGroup;
import com.enewschamp.publication.domain.entity.PublicationMonthlySummary;
import com.enewschamp.publication.page.data.PublicationSummaryRequest;
import com.enewschamp.publication.page.data.PublicationSummaryResponse;

@Service
public class PublicationMonthlySummaryService extends AbstractDomainService {

	@Autowired
	private PublicationMonthlySummaryRepository repository;

	@Autowired
	private NewsArticleService articleService;

	@Autowired
	private NewsArticleGroupService articleGroupService;

	@Autowired
	private PublicationSummaryRepositoryCustom customRepository;
	
	@Autowired
	private PublicationMonthyTotalRepositoryCustomImpl monthlyTotalRepository;

	@Autowired
	private PublicationMonthySummaryRepositoryCustomImpl repositoryCustom;
	
	@Autowired
	PropertiesBackendService propertiesService;

	public PublicationMonthlySummary get(String recordId) {
		Optional<PublicationMonthlySummary> existingEntity = repository.findById(recordId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public PublicationMonthlySummary load(String recordId) {
		PublicationMonthlySummary existingEntity = get(recordId);
		if (existingEntity != null) {
			return existingEntity;
		} else {
			throw new BusinessException(ErrorCodeConstants.PUBLICATION_MONTHLY_SUMMARY_NOT_FOUND, recordId);
		}
	}

	public PublicationMonthlySummary get(PublicationMonthlySummary publicationMonthlySummary) {
		return get(publicationMonthlySummary.calculateRecordId());
	}

	public void saveSummary(PublicationGroup publicationGroup, Publication publication) {
		Map<String, List<NewsArticle>> genreWiseArticlesMap = getGenreWiseArticles(publication);
		genreWiseArticlesMap.forEach((genreId, articleList) -> {
			PublicationMonthlySummary publicationMonthlySummary = new PublicationMonthlySummary();
			publicationMonthlySummary.setYear(publication.getPublicationDate().getYear());
			publicationMonthlySummary.setMonth(publication.getPublicationDate().getMonthValue());
			publicationMonthlySummary.setEditionId(publicationGroup.getEditionId());
			publicationMonthlySummary.setReadingLevel(publication.getReadingLevel());
			publicationMonthlySummary.setGenreId(genreId);

			PublicationMonthlySummary existingSummary = get(publicationMonthlySummary);
			if (existingSummary != null) {
				publicationMonthlySummary = existingSummary;
			}
			publicationMonthlySummary
					.setNewsArticleCount(publicationMonthlySummary.getNewsArticleCount() + articleList.size());
			publicationMonthlySummary
					.setQuizCount(publicationMonthlySummary.getQuizCount() + getQuizCount(articleList));
			publicationMonthlySummary.setOperatorId(publicationGroup.getOperatorId());
			repository.save(publicationMonthlySummary);
		});
	}

	private int getQuizCount(List<NewsArticle> articles) {
		int count = 0;
		for (NewsArticle article : articles) {
			if (article != null && article.getNewsArticleQuiz() != null) {
				count = count + article.getNewsArticleQuiz().size();
			}
		}
		return count;
	}

	private int getArticleLinkagesCount(List<NewsArticle> linkages) {
		int count = 0;
		if (linkages != null) {
			count = linkages.size();
		}
		return count;
	}

	private Map<String, List<NewsArticle>> getGenreWiseArticles(Publication publication) {
		Map<String, List<NewsArticle>> genreWiseArticlesMap = new HashMap<String, List<NewsArticle>>();
		if (publication.getNewsArticles() != null) {
			for (NewsArticle linkage : publication.getNewsArticles()) {
				if (linkage != null) {
					NewsArticle article = articleService.get(linkage.getNewsArticleId());
					if (article == null) {
						continue;
					}
					String genre = getGenre(article.getNewsArticleGroupId());
					List<NewsArticle> articleList = genreWiseArticlesMap.get(genre);
					if (articleList == null) {
						articleList = new ArrayList<NewsArticle>();
						articleList.add(article);
						genreWiseArticlesMap.put(genre, articleList);
					} else {
						articleList.add(article);
					}
				}
			}
		}
		return genreWiseArticlesMap;
	}

	private String getGenre(Long newsArticleGroupId) {
		return articleGroupService.load(newsArticleGroupId).getGenreId();
	}

	public PublicationSummaryResponse fetchSummary(PublicationSummaryRequest summaryRequest) {
		int pageNumber = summaryRequest.getPageNumber();
		pageNumber = pageNumber > 0 ? (pageNumber - 1) : 0;
		Pageable pageable = PageRequest.of(pageNumber, summaryRequest.getPageSize());

		Page<PublicationMonthlySummaryDTO> pageResult = customRepository.fetchMonthlySummary(summaryRequest, pageable);

		PublicationSummaryResponse response = new PublicationSummaryResponse();
		response.setIsLastPage(pageResult.isLast());
		response.setPageCount(pageResult.getTotalPages());
		response.setRecordCount(pageResult.getNumberOfElements());
		response.setPageNumber(pageResult.getNumber() + 1);
		response.setMonthlySummary(pageResult.getContent());

		return response;
	}
	
	public Page<ArticlePublicationMonthlyGenre> listPublicationMonthlySummary(AdminSearchRequest searchRequest, int pageNo,
			int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<ArticlePublicationMonthlyGenre> monthlySummaryList = repositoryCustom.findAll(pageable, searchRequest);
		if (monthlySummaryList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return monthlySummaryList;
	}
	

	public Page<ArticlePublicationMonthlyTotal> listPublicationMonthlyTotal(AdminSearchRequest searchRequest, int pageNo,
			int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<ArticlePublicationMonthlyTotal> monthlySummaryList = monthlyTotalRepository.findAll(pageable, searchRequest);
		if (monthlySummaryList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return monthlySummaryList;
	}
}