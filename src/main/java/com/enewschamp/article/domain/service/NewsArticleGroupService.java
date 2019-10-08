package com.enewschamp.article.domain.service;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.page.data.PropertyAuditData;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditQueryCriteria;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.utils.ImageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Service
public class NewsArticleGroupService {

	@Autowired
	NewsArticleGroupRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	@Autowired 
	private NewsArticleService newsArticleService;
	
	@Autowired
	private EnewschampApplicationProperties appConfig;
	
	
	public NewsArticleGroup create(NewsArticleGroup articleGroup) {
		deriveStatus(articleGroup);
		articleGroup = repository.save(articleGroup);
		saveImages(articleGroup.getBase64Image(), articleGroup.getNewsArticleGroupId());
		return articleGroup;
	}
	
	public NewsArticleGroup update(NewsArticleGroup articleGroup) {
		Long articleGroupId = articleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = load(articleGroupId);
		modelMapper.map(articleGroup, existingEntity);
		articleGroup = repository.save(existingEntity);
		saveImages(articleGroup.getBase64Image(), articleGroup.getNewsArticleGroupId());
		return repository.save(existingEntity);
	}
	
	public NewsArticleGroup patch(NewsArticleGroup articleGroup) {
		Long articleGroupId = articleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = load(articleGroupId);
		modelMapperForPatch.map(articleGroup, existingEntity);
		articleGroup = repository.save(existingEntity);
		saveImages(articleGroup.getBase64Image(), articleGroup.getNewsArticleGroupId());
		return articleGroup;
	}
	
	public void delete(Long articleId) {
		repository.deleteById(articleId);
	}
	
	public NewsArticleGroup load(Long articleGroupId) {
		Optional<NewsArticleGroup> existingEntity = repository.findById(articleGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else { 
			throw new BusinessException(ErrorCodes.ARTICLE_GROUP_NOT_FOUND, String.valueOf(articleGroupId));
		}
	}
	
	public NewsArticleGroup get(Long articleGroupId) {
		if(articleGroupId == null) {
			return null;
		}
		Optional<NewsArticleGroup> existingEntity = repository.findById(articleGroupId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
		}
	}
	
	public String getAudit(Long articleGroupId) {
		NewsArticleGroup articleGroup = new NewsArticleGroup();
		articleGroup.setNewsArticleGroupId(articleGroupId);
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig).forParentObject(articleGroup);
		return auditBuilder.build();
	}
	
	public String getCommentsAudit(Long articleGroupId) {
		NewsArticleGroup articleGroup = new NewsArticleGroup();
		articleGroup.setNewsArticleGroupId(articleGroupId);
		AuditQueryCriteria queryCriteria = new AuditQueryCriteria();
		queryCriteria.setPropertyName("comments");
		queryCriteria.setWithNewObjectChanges(true);
		return auditService.getEntityAuditByCriteria(articleGroup, queryCriteria);
	}
	
	public ArticleGroupStatusType deriveStatus(NewsArticleGroup articleGroup) {
		ArticleGroupStatusType newStatus = deriveStatus(articleGroup.getNewsArticles());
		articleGroup.setStatus(newStatus);
		return newStatus;
	}
	
	private ArticleGroupStatusType deriveStatus(List<NewsArticle> articles) {
		ArticleGroupStatusType newStatus = null;
		if(articles != null) {
			for(NewsArticle article: articles) {
				ArticleStatusType articleStatus = newsArticleService.deriveArticleStatus(article);
				if(articleStatus != null) {
					ArticleGroupStatusType status = ArticleGroupStatusType.fromArticleStatus(articleStatus);
					if(status.equals(ArticleGroupStatusType.WIP)) {
						newStatus = status;
						break;
					}
					if(newStatus == null) {
						newStatus = status;
						continue;
					}
					if(status.getOrder() < newStatus.getOrder()) {
						newStatus = status;
					}
				}
			}
		}
		return newStatus;
	}
	
	public NewsArticleGroup assignAuthor(Long articleGroupId, String authorId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		articleGroup.setAuthorId(authorId);
		List<NewsArticle> articles = newsArticleService.assignAuthor(articleGroupId, authorId);
		articleGroup.setStatus(deriveStatus(articles));
		repository.save(articleGroup);
		return articleGroup;
	}
	
	public NewsArticleGroup assignEditor(Long articleGroupId, String editorId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		articleGroup.setEditorId(editorId);
		newsArticleService.assignEditor(articleGroupId, editorId);
		repository.save(articleGroup);
		return articleGroup;
	}
	
	public List<PropertyAuditData> getPreviousComments(Long articleGroupId) {
		NewsArticleGroup articleGroup = new NewsArticleGroup();
		articleGroup.setNewsArticleGroupId(articleGroupId);
		
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig).forParentObject(articleGroup);
		auditBuilder.forProperty("comments");
		
		return auditBuilder.buildPropertyAudit();
	}
	
	private void saveImages(String base64Image, Long articleGroupId) {
		if (base64Image == null || articleGroupId == null) {
			return;
		}
		String size1FileNameWithoutExtension = appConfig.getArticleImageConfig().getSize1FolderPath() + articleGroupId;
		String size2FileNameWithoutExtension = appConfig.getArticleImageConfig().getSize2FolderPath() + articleGroupId;
		String size3FileNameWithoutExtension = appConfig.getArticleImageConfig().getSize3FolderPath() + articleGroupId;
		String size4FileNameWithoutExtension = appConfig.getArticleImageConfig().getSize4FolderPath() + articleGroupId;
		String outputFileName = appConfig.getArticleImageConfig().getSize1FolderPath() + articleGroupId;

		File imageFile = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
			imageFile = new File(outputFileName);

			FileUtils.writeByteArrayToFile(imageFile, decodedBytes);

			// Size 1
			List<Integer> size1Dimension = appConfig.getArticleImageConfig().getSize1Dimention();
			ImageUtils.resizeImage(imageFile, new Dimension(size1Dimension.get(0), size1Dimension.get(1)),
					ImageUtils.JPG_IMAGE_TYPE, size1FileNameWithoutExtension);

			// Size 2
			List<Integer> size2Dimension = appConfig.getArticleImageConfig().getSize2Dimention();
			ImageUtils.resizeImage(imageFile, new Dimension(size2Dimension.get(0), size2Dimension.get(1)),
					ImageUtils.JPG_IMAGE_TYPE, size2FileNameWithoutExtension);

			// Size 3
			List<Integer> size3Dimension = appConfig.getArticleImageConfig().getSize3Dimention();
			ImageUtils.resizeImage(imageFile, new Dimension(size3Dimension.get(0), size3Dimension.get(1)),
					ImageUtils.JPG_IMAGE_TYPE, size3FileNameWithoutExtension);

			// Size 4
			List<Integer> size4Dimension = appConfig.getArticleImageConfig().getSize4Dimention();
			ImageUtils.resizeImage(imageFile, new Dimension(size4Dimension.get(0), size4Dimension.get(1)),
					ImageUtils.JPG_IMAGE_TYPE, size4FileNameWithoutExtension);

			FileUtils.forceDelete(imageFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			base64Image = null;
			imageFile = null;
		}

	}
}
