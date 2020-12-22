package com.enewschamp.publication.domain.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationDailySummaryDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationDailySummary;
import com.enewschamp.publication.domain.entity.PublicationGroup;
import com.enewschamp.publication.page.data.PublicationSummaryRequest;
import com.enewschamp.publication.page.data.PublicationSummaryResponse;

@Service
public class PublicationDailySummaryService extends AbstractDomainService {

	@Autowired
	private PublicationDailySummaryRepository repository;

	@Autowired
	private NewsArticleService articleService;

	@Autowired
	private PublicationMonthlySummaryService monthlySummaryService;

	@Autowired
	private PublicationSummaryRepositoryCustom customRepository;

	public PublicationDailySummary load(LocalDate publicationDate) {
		PublicationDailySummary existingEntity = get(publicationDate);
		if (existingEntity != null) {
			return existingEntity;
		} else {
			throw new BusinessException(ErrorCodeConstants.PUBLICATION_DAILY_SUMMARY_NOT_FOUND,
					publicationDate.toString());
		}
	}

	public PublicationDailySummary get(LocalDate publicationDate) {
		Optional<PublicationDailySummary> existingEntity = repository.findById(publicationDate);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}

	public PublicationDailySummary saveSummary(PublicationGroup publicationGroup, Publication publication) {
		PublicationDailySummary publicationDailySummary = get(publication.getPublicationDate());
		if (publicationDailySummary != null) {
			publicationDailySummary.setNewsArticleCount(publicationDailySummary.getNewsArticleCount()
					+ getArticleLinkagesCount(publication.getNewsArticles()));
			publicationDailySummary
					.setQuizCount(publicationDailySummary.getQuizCount() + getQuizCount(publication.getNewsArticles()));
		} else {
			publicationDailySummary = new PublicationDailySummary();
			publicationDailySummary.setPublicationDate(publication.getPublicationDate());
			if (publication.getNewsArticles() == null) {
				publicationDailySummary.setNewsArticleCount(0);
			} else {
				publicationDailySummary.setNewsArticleCount(getArticleLinkagesCount(publication.getNewsArticles()));
			}
			publicationDailySummary.setQuizCount(getQuizCount(publication.getNewsArticles()));
			publicationDailySummary.setOperatorId("SYSTEM");
		}
		repository.save(publicationDailySummary);
		monthlySummaryService.saveSummary(publicationGroup, publication);
		return publicationDailySummary;
	}

	private int getQuizCount(List<NewsArticle> linkages) {
		int count = 0;
		if (linkages != null) {
			for (NewsArticle linkage : linkages) {
				if (linkage != null) {
					NewsArticle article = articleService.get(linkage.getNewsArticleId());
					if (article != null && article.getNewsArticleQuiz() != null) {
						count = count + article.getNewsArticleQuiz().size();
					}
				}
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

	public PublicationSummaryResponse fetchSummary(PublicationSummaryRequest summaryRequest) {
		int pageNumber = summaryRequest.getPageNumber();
		pageNumber = pageNumber > 0 ? (pageNumber - 1) : 0;
		Pageable pageable = PageRequest.of(pageNumber, summaryRequest.getPageSize());

		Page<PublicationDailySummaryDTO> pageResult = customRepository.fetchDailySummary(summaryRequest, pageable);

		PublicationSummaryResponse response = new PublicationSummaryResponse();
		response.setIsLastPage(pageResult.isLast());
		response.setPageCount(pageResult.getTotalPages());
		response.setRecordCount(pageResult.getNumberOfElements());
		response.setPageNumber(pageResult.getNumber() + 1);
		response.setDailySummary(pageResult.getContent());

		return response;
	}

}
