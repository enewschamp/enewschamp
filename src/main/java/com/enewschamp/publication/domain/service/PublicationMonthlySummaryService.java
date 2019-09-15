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

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationMonthlySummaryDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;
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
			throw new BusinessException(ErrorCodes.PUBLICATION_MONTHLY_SUMMARY_NOT_FOUND, recordId);
		}
	}
	
	public PublicationMonthlySummary get(PublicationMonthlySummary publicationMonthlySummary) {
		return get(publicationMonthlySummary.calculateRecordId());
	}
	
	public void saveSummary(Publication publication) {
		Map<String, List<NewsArticle>> genreWiseArticlesMap = getGenreWiseArticles(publication);
		genreWiseArticlesMap.forEach((genreId, articleList) -> {
			PublicationMonthlySummary publicationMonthlySummary = new PublicationMonthlySummary();
			publicationMonthlySummary.setYear(publication.getPublishDate().getYear());
			publicationMonthlySummary.setMonth(publication.getPublishDate().getMonthValue());
			publicationMonthlySummary.setEditionId(publication.getEditionId());
			publicationMonthlySummary.setReadingLevel(publication.getReadingLevel());
			publicationMonthlySummary.setGenreId(genreId);
			
			PublicationMonthlySummary existingSummary = get(publicationMonthlySummary);
			if (existingSummary != null) {
				publicationMonthlySummary = existingSummary;
			}
			publicationMonthlySummary.setNewsArticleCount(publicationMonthlySummary.getNewsArticleCount() + articleList.size());
			publicationMonthlySummary.setQuizCount(publicationMonthlySummary.getQuizCount() + getQuizCount(articleList));
			publicationMonthlySummary.setOperatorId("SYSTEM");
			repository.save(publicationMonthlySummary);
		});
	}
	
	private int getQuizCount(List<NewsArticle> articles) {
		int count = 0;
		for(NewsArticle article: articles) {
			if(article != null && article.getNewsArticleQuiz() != null) {
				count = count + article.getNewsArticleQuiz().size();
			}
		}
		return count;
	}
	
	private Map<String, List<NewsArticle>> getGenreWiseArticles(Publication publication) {
		Map<String, List<NewsArticle>> genreWiseArticlesMap = new HashMap<String, List<NewsArticle>>();
		for(PublicationArticleLinkage linkage: publication.getArticleLinkages()) {
			if(linkage != null) {
				NewsArticle article = articleService.get(linkage.getNewsArticleId());
				if(article == null) {
					continue;
				}
				String genre = getGenre(article.getNewsArticleGroupId());
				List<NewsArticle> articleList = genreWiseArticlesMap.get(genre);
				if(articleList == null) {
					articleList = new ArrayList<NewsArticle>();
					articleList.add(article);
					genreWiseArticlesMap.put(genre, articleList);
				} else {
					articleList.add(article);
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

		return response;
	}


}
