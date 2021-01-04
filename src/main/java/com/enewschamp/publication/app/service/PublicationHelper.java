package com.enewschamp.publication.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.service.NewsArticleHelper;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.service.NewsArticleGroupService;
import com.enewschamp.article.domain.service.NewsArticleRepository;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.service.PublicationRepository;
import com.enewschamp.publication.domain.service.PublicationService;
import com.enewschamp.user.domain.service.UserRoleService;

@Component
public class PublicationHelper {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PublicationService publicationService;

	@Autowired
	private PublicationRepository publicationRepository;

	@Autowired
	private NewsArticleHelper newsArticleHelper;

	@Autowired
	private NewsArticleRepository newsArticleRepository;

	@Autowired
	private NewsArticleService newsArticleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private NewsArticleGroupService newsArticleGroupService;

	@Transactional
	public PublicationDTO createPublication(PublicationDTO publicationDTO) {
		List<NewsArticleDTO> articleLinked = publicationDTO.getNewsArticles();
		if (articleLinked != null) {
			for (NewsArticleDTO newsArticleDTO : articleLinked) {
				newsArticleDTO.setRecordInUse(publicationDTO.getRecordInUse());
				newsArticleDTO.setOperatorId(publicationDTO.getOperatorId());
			}
		}
		// Remove articles which have been deleted on the UI
		removeDelinkedArticles(publicationDTO, articleLinked);
		publicationDTO.setNewsArticlesLinked(articleLinked);
		Publication publication = modelMapper.map(publicationDTO, Publication.class);
		publication = publicationService.create(publication);
		publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		List<NewsArticleDTO> newsArticleDTO = new ArrayList<NewsArticleDTO>();
		if (articleLinked != null) {
			for (NewsArticleDTO articleLinkageDTO : articleLinked) {
				articleLinkageDTO.setPublicationId(publicationDTO.getPublicationId());
				articleLinkageDTO.setPublicationDate(publicationDTO.getPublicationDate());
				if (userRoleService.getByUserIdAndRole(publicationDTO.getOperatorId(),
						CommonConstants.PUBLISHER_ROLE) != null) {
					articleLinkageDTO.setPublisherWorked(publicationDTO.getOperatorId());
				} else if (userRoleService.getByUserIdAndRole(publicationDTO.getOperatorId(),
						CommonConstants.EDITOR_ROLE) != null) {
					articleLinkageDTO.setEditorWorked(publicationDTO.getOperatorId());
				}
				NewsArticle newsArticle = newsArticleHelper.updatePublicationId(articleLinkageDTO,
						publication.getStatus(), publication.getPublicationDate());
				NewsArticleDTO articleDTO = modelMapper.map(newsArticle, NewsArticleDTO.class);
				newsArticleDTO.add(articleDTO);
			}
		}
		publicationDTO.setNewsArticles(newsArticleDTO);
		return publicationDTO;
	}

	private void removeDelinkedArticles(PublicationDTO publicationDTO, List<NewsArticleDTO> articleLinkages) {
		Long publicationId = publicationDTO.getPublicationId();
		if (publicationId == null || publicationId <= 0) {
			return;
		}
		Publication publication = publicationService.get(publicationId);
		if (publication == null) {
			return;
		}
		for (NewsArticle existingArticleLinkage : publication.getNewsArticles()) {
			if (!isExistingLinkageFound(existingArticleLinkage.getPublicationId(), articleLinkages)) {
				existingArticleLinkage = newsArticleService.get(existingArticleLinkage.getNewsArticleId());
				existingArticleLinkage.setPublicationDate(null);
				existingArticleLinkage.setPublicationId(null);
				existingArticleLinkage.setSequence(0);
				if (userRoleService.getByUserIdAndRole(publicationDTO.getOperatorId(),
						CommonConstants.PUBLISHER_ROLE) != null) {
					existingArticleLinkage.setPublisherWorked(publicationDTO.getOperatorId());
				} else if (userRoleService.getByUserIdAndRole(publicationDTO.getOperatorId(),
						CommonConstants.EDITOR_ROLE) != null) {
					existingArticleLinkage.setEditorWorked(publicationDTO.getOperatorId());
				}
				existingArticleLinkage.setOperatorId(publication.getOperatorId());
				newsArticleRepository.save(existingArticleLinkage);
			}
		}
	}

	private boolean isExistingLinkageFound(long publicationId, List<NewsArticleDTO> existingLinkages) {
		for (NewsArticleDTO articleLinkage : existingLinkages) {
			if (articleLinkage.getPublicationId() != null && articleLinkage.getPublicationId() == publicationId) {
				return true;
			}
		}
		return false;
	}

	public PublicationDTO getPublication(Long publicationId) {
		Publication publication = publicationService.get(publicationId);
		PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		List<NewsArticleDTO> articles = newsArticleHelper.getByPublicationId(publicationId);
		publicationDTO.setNewsArticles(articles);
		return publicationDTO;
	}

	public List<PublicationDTO> getByPublicationGroupId(long publicationGroupId) {
		List<Publication> publications = publicationRepository.findByPublicationGroupId(publicationGroupId);
		List<PublicationDTO> publicationDTOs = new ArrayList<PublicationDTO>();
		for (Publication publication : publications) {
			PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
			for (NewsArticleDTO newsArticleDTO : publicationDTO.getNewsArticles()) {
				NewsArticleGroup newsArticleGroup = newsArticleGroupService.get(newsArticleDTO.getNewsArticleGroupId());
				newsArticleDTO.setEditorId(newsArticleGroup.getEditorId());
				newsArticleDTO.setAuthorId(newsArticleGroup.getAuthorId());
				newsArticleDTO.setArticleType(newsArticleGroup.getArticleType());
				newsArticleDTO.setCityId(newsArticleGroup.getCityId());
				newsArticleDTO.setCredits(newsArticleGroup.getCredits());
				newsArticleDTO.setGenreId(newsArticleGroup.getGenreId());
				newsArticleDTO.setHashTags(newsArticleGroup.getHashTags());
				newsArticleDTO.setNoQuiz(newsArticleGroup.getNoQuiz());
				newsArticleDTO.setImageOnly(newsArticleGroup.getImageOnly());
				newsArticleDTO.setHeadline(newsArticleGroup.getHeadline());
				newsArticleDTO.setUrl(newsArticleGroup.getUrl());
				newsArticleDTO.setSourceText(newsArticleGroup.getSourceText());
				newsArticleDTO.setTargetCompletionDate(newsArticleGroup.getTargetCompletionDate());
				newsArticleDTO.setIntendedPubDate(newsArticleGroup.getIntendedPubDate());
				newsArticleDTO.setIntendedPubMonth(newsArticleGroup.getIntendedPubMonth());
				newsArticleDTO.setIntendedPubDay(newsArticleGroup.getIntendedPubDay());
			}
			publicationDTOs.add(publicationDTO);
		}
		return publicationDTOs;
	}

	private PublicationDTO fillArticleLinkages(PublicationDTO publicationDTO) {
		List<NewsArticle> articleLinkageList = newsArticleRepository
				.findByPublicationId(publicationDTO.getPublicationId());
		List<NewsArticleDTO> linkageDTOs = new ArrayList<NewsArticleDTO>();
		for (NewsArticle linkage : articleLinkageList) {
			NewsArticleDTO linkageDTO = modelMapper.map(linkage, NewsArticleDTO.class);
			linkageDTOs.add(linkageDTO);
		}
		publicationDTO.setNewsArticles(linkageDTOs);
		return publicationDTO;
	}

	public PublicationDTO get(Long publicationId) {
		Publication publication = publicationService.get(publicationId);
		PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		publicationDTO = fillArticleLinkages(publicationDTO);
		return publicationDTO;
	}

	public void deleteByPublicationGroupId(long publicationGroupId) {
		publicationRepository.deleteByPublicationGroupId(publicationGroupId);
	}
}