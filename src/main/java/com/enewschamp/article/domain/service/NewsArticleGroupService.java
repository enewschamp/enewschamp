package com.enewschamp.article.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.CommonService;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.domain.common.ArticleGroupStatusType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.page.data.PropertyAuditData;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditQueryCriteria;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.user.domain.service.UserRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NewsArticleGroupService {

	@Autowired
	NewsArticleGroupRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CommonService commonService;

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
	private NewsArticleRepository newsArticleRepository;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	UserRoleService userRoleService;

	public NewsArticleGroup create(NewsArticleGroup articleGroup) {
		deriveStatus(articleGroup);
		String imageType = "jpg";
		if (articleGroup.getImageTypeExt() != null && !"".equalsIgnoreCase(articleGroup.getImageTypeExt())) {
			imageType = articleGroup.getImageTypeExt();
		}
		String base64Image = articleGroup.getBase64Image();
		articleGroup = repository.save(articleGroup);
		boolean updateFlag = false;
		String newImageName = articleGroup.getNewsArticleGroupId() + "_" + System.currentTimeMillis();
		String currentImageName = articleGroup.getImageName();
		boolean saveFlag = commonService.saveImages("Publisher", "article", imageType, base64Image, newImageName);
		if (saveFlag) {
			articleGroup.setImageName(newImageName + "." + imageType);
			updateFlag = true;
		}
		if (currentImageName != null && !"".equals(currentImageName)) {
			commonService.deleteImages("Publisher", "article", currentImageName);
			updateFlag = true;
		}
		if (updateFlag) {
			articleGroup = repository.save(articleGroup);
		}
		return articleGroup;
	}

	public NewsArticleGroup update(NewsArticleGroup articleGroup) {
		deriveStatus(articleGroup);
		String imageType = "jpg";
		if (articleGroup.getImageTypeExt() != null && !"".equalsIgnoreCase(articleGroup.getImageTypeExt())) {
			imageType = articleGroup.getImageTypeExt();
		}
		String base64Image = articleGroup.getBase64Image();
		Long articleGroupId = articleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = load(articleGroupId);
		String currentImageName = "";
		if (existingEntity.getImageName() != null) {
			articleGroup.setImageName(existingEntity.getImageName());
			currentImageName = articleGroup.getImageName();
		}
		modelMapper.map(articleGroup, existingEntity);
		articleGroup = repository.save(existingEntity);
		boolean updateFlag = false;
		String newImageName = articleGroup.getNewsArticleGroupId() + "_" + System.currentTimeMillis();
		boolean saveFlag = commonService.saveImages("Publisher", "article", imageType, base64Image, newImageName);
		if (saveFlag) {
			articleGroup.setImageName(newImageName + "." + imageType);
			updateFlag = true;
		}
		if (currentImageName != null && !"".equals(currentImageName)
				&& (saveFlag || "Y".equals(articleGroup.getDeleteImage()))) {
			commonService.deleteImages("Publisher", "article", currentImageName);
			updateFlag = true;
			if ("Y".equals(articleGroup.getDeleteImage())) {
				articleGroup.setImageName(null);
			}
		}
		if (updateFlag) {
			articleGroup = repository.save(articleGroup);
		}
		return articleGroup;
	}

	public NewsArticleGroup patch(NewsArticleGroup articleGroup) {
		deriveStatus(articleGroup);
		String imageType = "jpg";
		if (articleGroup.getImageTypeExt() != null && !"".equalsIgnoreCase(articleGroup.getImageTypeExt())) {
			imageType = articleGroup.getImageTypeExt();
		}
		String base64Image = articleGroup.getBase64Image();
		Long articleGroupId = articleGroup.getNewsArticleGroupId();
		NewsArticleGroup existingEntity = load(articleGroupId);
		modelMapperForPatch.map(articleGroup, existingEntity);
		articleGroup = repository.save(existingEntity);
		boolean updateFlag = false;
		String newImageName = articleGroup.getNewsArticleGroupId() + "_" + System.currentTimeMillis();
		String currentImageName = articleGroup.getImageName();
		boolean saveFlag = commonService.saveImages("Publisher", "article", imageType, base64Image, newImageName);
		if (saveFlag) {
			articleGroup.setImageName(newImageName + "." + imageType);
			updateFlag = true;
		}
		if (currentImageName != null && !"".equals(currentImageName)) {
			commonService.deleteImages("Publisher", "article", currentImageName);
			updateFlag = true;
		}
		if (updateFlag) {
			articleGroup = repository.save(articleGroup);
		}
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
			throw new BusinessException(ErrorCodeConstants.ARTICLE_GROUP_NOT_FOUND, String.valueOf(articleGroupId));
		}
	}

	public NewsArticleGroup get(Long articleGroupId) {
		if (articleGroupId == null) {
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
		AuditBuilder auditBuilder = AuditBuilder
				.getInstance(auditService, this, newsArticleService, objectMapper, appConfig)
				.forParentObject(articleGroup);
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
		ArticleGroupStatusType newStatus = deriveStatus(articleGroup, articleGroup.getNewsArticles());
		articleGroup.setStatus(newStatus);
		return newStatus;
	}

	private ArticleGroupStatusType deriveStatus(NewsArticleGroup articleGroup, List<NewsArticle> articles) {
		ArticleGroupStatusType newStatus = null;
		if (articles != null) {
			for (NewsArticle article : articles) {
				ArticleStatusType articleStatus = newsArticleService.deriveArticleStatus(articleGroup, article, false);
				if (articleStatus != null) {
					ArticleGroupStatusType status = ArticleGroupStatusType.fromArticleStatus(articleStatus);
					if (status.equals(ArticleGroupStatusType.WIP)) {
						newStatus = status;
						break;
					}
					if (newStatus == null) {
						newStatus = status;
						continue;
					}
					if (status.getOrder() < newStatus.getOrder()) {
						newStatus = status;
					}
				}
			}
		}
		return newStatus;
	}

	public ArticleGroupStatusType deriveNewStatus(List<NewsArticle> articles) {
		ArticleGroupStatusType newStatus = null;
		if (isAllClosed(articles)) {
			newStatus = ArticleGroupStatusType.Closed;
		} else if (isAllPublished(articles)) {
			newStatus = ArticleGroupStatusType.Published;
		} else if (isAllReadyToPublish(articles)) {
			newStatus = ArticleGroupStatusType.ReadyToPublish;
		} else if (isAllAssigned(articles)) {
			newStatus = ArticleGroupStatusType.Assigned;
		} else if (isAllUnAssigned(articles)) {
			newStatus = ArticleGroupStatusType.Unassigned;
		} else {
			newStatus = ArticleGroupStatusType.WIP;
		}

		return newStatus;
	}

	private boolean isAllClosed(List<NewsArticle> articles) {
		boolean flag = true;
		for (NewsArticle article : articles) {
			if (!ArticleStatusType.Closed.equals(article.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private boolean isAllReadyToPublish(List<NewsArticle> articles) {
		boolean flag = true;
		for (NewsArticle article : articles) {
			if (!ArticleStatusType.ReadyToPublish.equals(article.getStatus())
					&& !ArticleStatusType.Published.equals(article.getStatus())
					&& !ArticleStatusType.Closed.equals(article.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private boolean isAllPublished(List<NewsArticle> articles) {
		boolean flag = true;
		for (NewsArticle article : articles) {
			if (!ArticleStatusType.Published.equals(article.getStatus())
					&& !ArticleStatusType.Closed.equals(article.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private boolean isAllAssigned(List<NewsArticle> articles) {
		boolean flag = true;
		for (NewsArticle article : articles) {
			if (!ArticleStatusType.Assigned.equals(article.getStatus())
					&& !ArticleStatusType.Closed.equals(article.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private boolean isAllUnAssigned(List<NewsArticle> articles) {
		boolean flag = true;
		for (NewsArticle article : articles) {
			if (!ArticleStatusType.Unassigned.equals(article.getStatus())
					&& !ArticleStatusType.Closed.equals(article.getStatus())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	public NewsArticleGroupDTO assignAuthor(Long articleGroupId, String authorId, String operatorId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		String currentAuthorId = articleGroup.getAuthorId();
		if (ArticleGroupStatusType.Published.equals((articleGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		} else if (authorId.equalsIgnoreCase(currentAuthorId)) {
			throw new BusinessException(ErrorCodeConstants.NOT_CHANGES_FOUND);
		}
		articleGroup.setOperatorId(operatorId);
		newsArticleService.assignAuthor(articleGroupId, operatorId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		ArticleGroupStatusType newStatus = deriveNewStatus(
				newsArticleRepository.findByNewsArticleGroupId(articleGroup.getNewsArticleGroupId()));
		articleGroup.setAuthorId(authorId);
		articleGroup.setStatus(newStatus);
		articleGroup = repository.save(articleGroup);
		articleGroupDTO.setStatus(articleGroup.getStatus());
		return articleGroupDTO;
	}

	public NewsArticleGroupDTO assignEditor(Long articleGroupId, String editorId, String operatorId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		String currentEditorId = articleGroup.getEditorId();
		if (ArticleGroupStatusType.Published.equals((articleGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		} else if (editorId.equalsIgnoreCase(currentEditorId)) {
			throw new BusinessException(ErrorCodeConstants.NOT_CHANGES_FOUND);
		}
		articleGroup.setOperatorId(operatorId);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		ArticleGroupStatusType newStatus = deriveNewStatus(
				newsArticleRepository.findByNewsArticleGroupId(articleGroup.getNewsArticleGroupId()));
		articleGroup.setEditorId(editorId);
		articleGroup.setStatus(newStatus);
		repository.save(articleGroup);
		return articleGroupDTO;
	}

	public NewsArticleGroupDTO closeNewsArticleGroup(Long articleGroupId, String userId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		if (ArticleGroupStatusType.Published.equals((articleGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		}
		articleGroup.setOperatorId(userId);
		List<NewsArticleDTO> articleList = newsArticleService.closeArticles(articleGroupId, userId);
		ArticleGroupStatusType newStatus = deriveNewStatus(
				newsArticleRepository.findByNewsArticleGroupId(articleGroup.getNewsArticleGroupId()));
		articleGroup.setStatus(newStatus);
		articleGroup = repository.save(articleGroup);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);

		articleGroupDTO.setNewsArticles(articleList);
		return articleGroupDTO;
	}

	public NewsArticleGroupDTO reinstateNewsArticleGroup(Long articleGroupId, String userId) {
		NewsArticleGroup articleGroup = load(articleGroupId);
		if (!ArticleGroupStatusType.Closed.equals((articleGroup.getStatus()))) {
			throw new BusinessException(ErrorCodeConstants.NO_CHANGES_ALLOWED);
		}
		articleGroup.setOperatorId(userId);
		List<NewsArticleDTO> articleList = newsArticleService.reinstateArticles(articleGroup, articleGroupId, userId);
		ArticleGroupStatusType newStatus = deriveNewStatus(
				newsArticleRepository.findByNewsArticleGroupId(articleGroup.getNewsArticleGroupId()));
		articleGroup.setStatus(newStatus);
		articleGroup = repository.save(articleGroup);
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(articleGroup, NewsArticleGroupDTO.class);
		articleGroupDTO.setNewsArticles(articleList);
		return articleGroupDTO;
	}

	public List<PropertyAuditData> getPreviousComments(Long articleGroupId) {
		NewsArticleGroup articleGroup = new NewsArticleGroup();
		articleGroup.setNewsArticleGroupId(articleGroupId);
		AuditBuilder auditBuilder = AuditBuilder
				.getInstance(auditService, this, newsArticleService, objectMapper, appConfig)
				.forParentObject(articleGroup);
		auditBuilder.forProperty("comments");
		return auditBuilder.buildPropertyAudit();
	}

}