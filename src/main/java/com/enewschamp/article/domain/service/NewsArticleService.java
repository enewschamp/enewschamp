package com.enewschamp.article.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.article.app.dto.NewsArticleDTO;
import com.enewschamp.article.app.dto.NewsArticleGroupDTO;
import com.enewschamp.article.app.dto.NewsArticleSummaryDTO;
import com.enewschamp.article.app.dto.PublisherNewsArticleSummaryDTO;
import com.enewschamp.article.domain.common.ArticleActionType;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.common.ArticleType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.entity.NewsArticleGroup;
import com.enewschamp.article.domain.entity.NewsArticleQuiz;
import com.enewschamp.article.page.data.NewsArticleSearchRequest;
import com.enewschamp.article.page.data.PropertyAuditData;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.domain.common.StatusTransitionDTO;
import com.enewschamp.domain.common.StatusTransitionHandler;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.user.domain.service.UserRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NewsArticleService {

	@Autowired
	private NewsArticleRepository repository;

	@Autowired
	private NewsArticleGroupRepository repositoryGroup;

	@Autowired
	private NewsArticleRepositoryCustom customRepository;

	@Autowired
	private NewsArticleNoAuditRepository repositoryNoAudit;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private PropertiesService propertiesService;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	private StatusTransitionHandler statusTransitionHandler;

	@Autowired
	UserRoleService userRoleService;

	public NewsArticle create(NewsArticle article) {
		new ArticleBusinessPolicy(article).validateAndThrow();
		NewsArticleGroup articleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
		article.setStatus(deriveArticleStatus(articleGroup, article, true), article.getStatus());
		if (ArticleActionType.SaveAsDraft.equals(article.getCurrentAction())
				&& Boolean.valueOf(propertiesService.getProperty(PropertyConstants.SAVE_AS_DRAFT_AUDIT_DISABLE))) {
			return repositoryNoAudit.save(article);
		} else {
			return repository.save(article);

		}
	}

	public NewsArticle update(NewsArticle article) {
		NewsArticleGroup articleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
		deriveArticleStatus(articleGroup, article, true);
		Long articleId = article.getNewsArticleId();
		NewsArticle existingEntity = load(articleId);
		modelMapper.map(article, existingEntity);
		return repository.save(existingEntity);
	}

	public NewsArticle updateLike(NewsArticle article) {
		return repositoryNoAudit.save(article);
	}

	public NewsArticle patch(NewsArticle article) {
		NewsArticleGroup articleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
		deriveArticleStatus(articleGroup, article, true);
		Long articleId = article.getNewsArticleId();
		NewsArticle existingEntity = load(articleId);
		modelMapperForPatch.map(article, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long articleId) {
		repository.deleteById(articleId);
	}

	public List<PropertyAuditData> getPreviousComments(Long articleId) {
		NewsArticle newsArticle = new NewsArticle();
		newsArticle.setNewsArticleId(articleId);
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig)
				.forParentObject(newsArticle);
		auditBuilder.forProperty("comments");
		return auditBuilder.buildPropertyAudit();
	}

	public NewsArticle load(Long articleId) {
		Optional<NewsArticle> existingEntity = repository.findById(articleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.ARTICLE_NOT_FOUND, String.valueOf(articleId));
		}
	}

	public NewsArticle get(Long articleId) {
		if (articleId == null) {
			return null;
		}
		Optional<NewsArticle> existingEntity = repository.findById(articleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		}
		return null;
	}

	public String getAudit(Long articleId) {
		NewsArticle article = new NewsArticle();
		article.setNewsArticleId(articleId);

		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig)
				.forParentObject(article);

		// Fetch article quiz changes
		article = load(articleId);
		for (NewsArticleQuiz quiz : article.getNewsArticleQuiz()) {
			auditBuilder.forChildObject(quiz);
		}
		return auditBuilder.build();
	}

	public ArticleStatusType deriveArticleStatus(NewsArticleGroup articleGroup, NewsArticle article,
			boolean validateAccess) {
		ArticleStatusType status = ArticleStatusType.Unassigned;
		StatusTransitionDTO transition = null;
		ArticleActionType currentAction = article.getCurrentAction();
		ArticleStatusType existingStatus = null;

		NewsArticle existingArticle = get(article.getNewsArticleId());
		NewsArticleGroup existingArticleGroup = articleGroup;
		if (article.getNewsArticleGroupId() != null && article.getNewsArticleGroupId() > 0) {
			existingArticleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
		}
		if (existingArticle == null) { // Check for first time creation
			currentAction = ArticleActionType.SaveNewsArticleGroup;
			if (existingArticleGroup.getAuthorId() == null || "".equals(existingArticleGroup.getAuthorId())) {
				existingStatus = ArticleStatusType.Unassigned;
			} else {
				existingStatus = ArticleStatusType.Assigned;
			}
		} else {
			existingStatus = existingArticle.getStatus();
			if (currentAction == null) {
				currentAction = ArticleActionType.SaveNewsArticleGroup;
			}
		}
		if (currentAction != null) {
			transition = new StatusTransitionDTO(NewsArticle.class.getSimpleName(),
					String.valueOf(article.getNewsArticleId()), existingStatus.toString(), currentAction.toString(),
					null);
			String nextStatus = statusTransitionHandler.findNextStatus(transition);
			if (nextStatus.equals(StatusTransitionDTO.REVERSE_STATE)) {
				ArticleStatusType previousStatus = repository.getPreviousStatus(article.getNewsArticleId());
				nextStatus = previousStatus.toString();
			}
			System.out.println(">>>>..nextStatus......>>>>>" + nextStatus);
			status = ArticleStatusType.fromValue(nextStatus);
		}
		System.out.println(">>>>..article......>>>>>" + article);
		System.out.println(">>>>..transition......>>>>>" + transition);
		System.out.println(">>>>..status......>>>>>" + status);

		if (transition != null && validateAccess) {
			statusTransitionHandler.validateStateTransitionAccess(transition, existingArticleGroup.getAuthorId(),
					existingArticleGroup.getEditorId(), article.getOperatorId());
			validateStateTransition(article, transition, status);
		}
		return status;
	}

	private void validateStateTransition(NewsArticle article, StatusTransitionDTO transition,
			ArticleStatusType newStatus) {
		if (newStatus == null) {
			return;
		}
		switch (newStatus) {
		case ReadyToPublish:
			if (article.getRating() == null) {
				new BusinessException(ErrorCodeConstants.RATING_REQD_FOR_PUBLISH);
			}
			break;
		case ReworkForAuthor:
		case ReworkForEditor:
			if (article.getComments() == null || article.getComments().isEmpty()) {
				new BusinessException(ErrorCodeConstants.REWORK_COMMENTS_REQUIRED);
			}
			break;
		}
	}

	public List<NewsArticleDTO> closeArticles(Long articleGroupId, String userId) {
		List<NewsArticleDTO> articleList = new ArrayList<NewsArticleDTO>();
		if (userRoleService.getByUserIdAndRole(userId, CommonConstants.EDITOR_ROLE) == null
				&& userRoleService.getByUserIdAndRole(userId, CommonConstants.PUBLISHER_ROLE) == null) {
			throw new BusinessException(ErrorCodeConstants.ROLE_NOT_ASSIGNED_TO_USER,
					CommonConstants.EDITOR_ROLE + " OR " + CommonConstants.PUBLISHER_ROLE, userId);
		}
		List<NewsArticle> existingArticles = repository.findByNewsArticleGroupId(articleGroupId);
		for (NewsArticle article : existingArticles) {
			if ((!ArticleStatusType.Published.equals((article.getStatus())))
					&& (!ArticleStatusType.Closed.equals((article.getStatus())))) {
				article.setCurrentAction(ArticleActionType.Close);
				article.setOperatorId(userId);
				NewsArticleGroup articleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
				ArticleStatusType status = deriveArticleStatus(articleGroup, article, true);
				article.setStatus(status, article.getStatus());
				article = repository.save(article);
			}
			NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
			articleList.add(articleDTO);
		}
		return articleList;
	}

	public List<NewsArticleDTO> assignAuthor(Long articleGroupId, String userId) {
		List<NewsArticleDTO> articleList = new ArrayList<NewsArticleDTO>();
		if (userRoleService.getByUserIdAndRole(userId, CommonConstants.EDITOR_ROLE) == null
				&& userRoleService.getByUserIdAndRole(userId, CommonConstants.PUBLISHER_ROLE) == null) {
			throw new BusinessException(ErrorCodeConstants.ROLE_NOT_ASSIGNED_TO_USER,
					CommonConstants.EDITOR_ROLE + " OR " + CommonConstants.PUBLISHER_ROLE, userId);
		}
		List<NewsArticle> existingArticles = repository.findByNewsArticleGroupId(articleGroupId);
		for (NewsArticle article : existingArticles) {
			if (ArticleStatusType.Unassigned.equals((article.getStatus()))) {
				article.setCurrentAction(ArticleActionType.AssignAuthor);
				article.setOperatorId(userId);
				NewsArticleGroup articleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
				ArticleStatusType status = deriveArticleStatus(articleGroup, article, true);
				article.setStatus(status, article.getStatus());
				article = repository.save(article);
			}
			NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
			articleList.add(articleDTO);
		}
		return articleList;
	}

	public List<NewsArticleDTO> reinstateArticles(NewsArticleGroup newsArticleGroup, Long articleGroupId,
			String userId) {
		List<NewsArticleDTO> articleList = new ArrayList<NewsArticleDTO>();
		NewsArticleGroupDTO articleGroupDTO = modelMapper.map(newsArticleGroup, NewsArticleGroupDTO.class);
		if (userRoleService.getByUserIdAndRole(userId, CommonConstants.EDITOR_ROLE) == null
				&& userRoleService.getByUserIdAndRole(userId, CommonConstants.PUBLISHER_ROLE) == null) {
			throw new BusinessException(ErrorCodeConstants.ROLE_NOT_ASSIGNED_TO_USER,
					CommonConstants.EDITOR_ROLE + " OR " + CommonConstants.PUBLISHER_ROLE, userId);
		}
		List<NewsArticle> existingArticles = repository.findByNewsArticleGroupId(articleGroupId);
		for (NewsArticle article : existingArticles) {
			if (!ArticleStatusType.Published.equals((article.getStatus()))
					&& (articleGroupDTO.isReadingLevelPresent(article.getReadingLevel()))) {
				article.setCurrentAction(ArticleActionType.Reinstate);
				article.setOperatorId(userId);
				NewsArticleGroup articleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
				ArticleStatusType status = deriveArticleStatus(articleGroup, article, true);
				article.setStatus(status, article.getStatus());
				article = repository.save(article);
			}
			NewsArticleDTO articleDTO = modelMapper.map(article, NewsArticleDTO.class);
			articleList.add(articleDTO);
		}
		return articleList;
	}

	public Page<NewsArticleSummaryDTO> findArticles(NewsArticleSearchRequest searchRequest, Long studentId, int pageNo,
			int pageSize, HeaderDTO header) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<NewsArticleSummaryDTO> list = customRepository.findArticles(searchRequest, studentId, pageable);
		return list;
	}

	public Page<PublisherNewsArticleSummaryDTO> findPublisherArticles(NewsArticleSearchRequest searchRequest,
			int pageNo, int pageSize, HeaderDTO header) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<PublisherNewsArticleSummaryDTO> list = customRepository.findPublisherArticles(searchRequest, pageable);
		return list;
	}

	public void markArticlesAsPublished(Publication publication) {
		if (publication == null || publication.getNewsArticles() == null) {
			return;
		}
		List<NewsArticle> articleLinkages = publication.getNewsArticles();
		articleLinkages.forEach(articleLinkage -> {
			NewsArticle article = load(articleLinkage.getNewsArticleId());
			article.setCurrentAction(ArticleActionType.Publish);
			article.setPublicationDate(publication.getPublicationDate());
			article.setPublisherWorked(publication.getPublisherWorked());
			article.setOperatorId(publication.getOperatorId());
			NewsArticleGroup articleGroup = repositoryGroup.getOne(article.getNewsArticleGroupId());
			deriveArticleStatus(articleGroup, article, true);
			repository.save(article);
		});
	}

	public LocalDate getLatestPublication(String editionId, int readingLevel, ArticleType articleType) {
		return repository.getLatestPublicationDate(editionId, readingLevel, articleType);
	}

	public LocalDate getNextAvailablePublicationDate(LocalDate givenDate, String editionId, int readingLevel,
			ArticleType articleType) {
		return repository.getNextAvailablePublicationDate(givenDate, editionId, readingLevel, articleType);
	}

	public LocalDate getPreviousAvailablePublicationDate(LocalDate givenDate, String editionId, int readingLevel,
			ArticleType articleType) {
		return repository.getPreviousAvailablePublicationDate(givenDate, editionId, readingLevel, articleType);
	}

	public Long getNextNewsArticleAvailable(LocalDate givenDate, String editionId, int readingLevel,
			ArticleType articleType, Long newsArticleId) {
		return repository.getNextNewsArticleAvailable(givenDate, editionId, readingLevel, articleType, newsArticleId);
	}

	public Long getPreviousNewsArticleAvailable(LocalDate givenDate, String editionId, int readingLevel,
			ArticleType articleType, Long newsArticleId) {
		return repository.getPreviousNewsArticleAvailable(givenDate, editionId, readingLevel, articleType,
				newsArticleId);
	}

}
