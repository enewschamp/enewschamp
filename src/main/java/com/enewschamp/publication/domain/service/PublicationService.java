package com.enewschamp.publication.domain.service;

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

import com.enewschamp.common.domain.service.PropertiesService;
import com.enewschamp.EnewschampApplicationProperties;
import com.enewschamp.app.common.CommonConstants;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.HeaderDTO;
import com.enewschamp.article.domain.common.ArticleStatusType;
import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.article.domain.service.NewsArticleService;
import com.enewschamp.article.page.data.PropertyAuditData;
import com.enewschamp.audit.domain.AuditBuilder;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.StatusTransitionDTO;
import com.enewschamp.domain.common.StatusTransitionHandler;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.app.dto.PublicationSummaryDTO;
import com.enewschamp.publication.domain.common.PublicationActionType;
import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationGroup;
import com.enewschamp.publication.page.data.PublicationSearchRequest;
import com.enewschamp.user.domain.service.UserRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PublicationService {

	@Autowired
	PublicationRepository repository;

	@Autowired
	PublicationGroupRepository publicationGroupRepository;

	@Autowired
	private PublicationRepositoryCustom customRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private EnewschampApplicationProperties appConfig;

	@Autowired
	private StatusTransitionHandler statusTransitionHandler;

	@Autowired
	private NewsArticleService articleService;

	@Autowired
	private PublicationDailySummaryService dailySummaryService;

	@Autowired
	UserRoleService userRoleService;

	public Publication create(Publication publication) {
		PublicationGroup publicationGroup = publicationGroupRepository.getOne(publication.getPublicationGroupId());
		publication.setStatus(derivePublicationStatus(publicationGroup, publication, true), publication.getStatus());
		publication = repository.save(publication);
		checkAndPerformPublishActions(publicationGroup, publication);
		return publication;
	}

	public Publication update(Publication publication) {
		PublicationGroup publicationGroup = publicationGroupRepository.getOne(publication.getPublicationGroupId());
		derivePublicationStatus(publicationGroup, publication, true);
		Long publicationId = publication.getPublicationId();
		Publication existingEntity = load(publicationId);
		modelMapper.map(publication, existingEntity);
		checkAndPerformPublishActions(publicationGroup, publication);
		return repository.save(existingEntity);
	}

	public Publication patch(Publication publication) {
		PublicationGroup publicationGroup = publicationGroupRepository.getOne(publication.getPublicationGroupId());
		derivePublicationStatus(publicationGroup, publication, true);
		Long publicationId = publication.getPublicationId();
		Publication existingEntity = load(publicationId);
		modelMapperForPatch.map(publication, existingEntity);
		checkAndPerformPublishActions(publicationGroup, publication);
		return repository.save(existingEntity);
	}

	private void checkAndPerformPublishActions(PublicationGroup publicationGroup, Publication publication) {
		if (PublicationStatusType.Published.equals(publication.getStatus())) {
			articleService.markArticlesAsPublished(publication);
		}
		dailySummaryService.saveSummary(publicationGroup, publication);
	}

	public void delete(Long publicationId) {
		repository.deleteById(publicationId);
	}

	public Publication load(Long publicationId) {
		Optional<Publication> existingEntity = repository.findById(publicationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PUBLICATION_NOT_FOUND, String.valueOf(publicationId));
		}
	}

	public Publication get(Long publicationId) {
		if (publicationId == null) {
			return null;
		}
		Optional<Publication> existingEntity = repository.findById(publicationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		}
		return null;
	}

	public String getAudit(Long publicationId) {
		Publication publication = new Publication();
		publication.setPublicationId(publicationId);
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig)
				.forParentObject(publication);
		// Fetch article linkage changes
		// publication = load(publicationId);
		// for (NewsArticle articleLinkage : publication.getNewsArticles()) {
		// auditBuilder.forChildObject(articleLinkage);
		// AuditBuilder auditBuilder1 = AuditBuilder.getInstance(auditService,
		// objectMapper, propertyService).forParentObject(articleLinkage);
		// }
		return auditBuilder.build();
	}

	public PublicationStatusType derivePublicationStatus(PublicationGroup publicationGroup, Publication publication,
			boolean validateAccess) {
		PublicationStatusType status = PublicationStatusType.Assigned;
		PublicationStatusType existingStatus = null;
		PublicationActionType currentAction = publication.getCurrentAction();
		Publication existingPublication = get(publication.getPublicationId());
		PublicationGroup existingPublicationGroup = publicationGroup;
		if (publication.getPublicationGroupId() != null && publication.getPublicationGroupId() > 0) {
			existingPublicationGroup = publicationGroupRepository.getOne(publication.getPublicationGroupId());
		}
		if (existingPublication == null) {
			existingStatus = PublicationStatusType.Assigned;
			currentAction = PublicationActionType.SavePublicationGrp;
		} else {
			existingStatus = existingPublication.getStatus();
		}
		StatusTransitionDTO transition = null;
		if (currentAction != null) {
			transition = new StatusTransitionDTO(Publication.class.getSimpleName(),
					String.valueOf(publication.getPublicationId()), existingStatus.toString(), currentAction.toString(),
					null);
			String nextStatus = statusTransitionHandler.findNextStatus(transition);
			if (nextStatus.equals(StatusTransitionDTO.REVERSE_STATE)) {
				PublicationStatusType previousStatus = repository.getPreviousStatus(publication.getPublicationId());
				nextStatus = previousStatus.toString();
			}
			System.out.println(">>>>..nextStatus......>>>>>" + nextStatus);
			status = PublicationStatusType.fromValue(nextStatus);
		}
		System.out.println(">>>>..publication......>>>>>" + publication);
		System.out.println(">>>>..transition......>>>>>" + transition);
		System.out.println(">>>>..status......>>>>>" + status);
		if (transition != null && validateAccess) {
			statusTransitionHandler.validateStateTransitionAccess(transition, null,
					existingPublicationGroup.getEditorId(), publication.getOperatorId());
			validateStateTransition(publication, transition, status);
		}
		return status;
	}

	private void validateStateTransition(Publication publication, StatusTransitionDTO transition,
			PublicationStatusType newStatus) {
		if (newStatus == null) {
			return;
		}
		switch (newStatus) {
		case Published:
			if (publication.getRating() == null) {
				new BusinessException(ErrorCodeConstants.RATING_REQD_FOR_PUBLISH);
			}
			break;
		case Rework:
			if (publication.getComments() == null || publication.getComments().isEmpty()) {
				new BusinessException(ErrorCodeConstants.REWORK_COMMENTS_REQUIRED);
			}
			break;
		}
	}

	public List<PublicationDTO> closePublications(Long publicationGroupId, String userId) {
		List<PublicationDTO> publicationList = new ArrayList<PublicationDTO>();
		if (userRoleService.getByUserIdAndRole(userId, CommonConstants.EDITOR_ROLE) == null
				&& userRoleService.getByUserIdAndRole(userId, CommonConstants.PUBLISHER_ROLE) == null) {
			throw new BusinessException(ErrorCodeConstants.ROLE_NOT_ASSIGNED_TO_USER,
					CommonConstants.EDITOR_ROLE + " OR " + CommonConstants.PUBLISHER_ROLE, userId);
		}
		List<Publication> existingPublications = repository.findByPublicationGroupId(publicationGroupId);
		for (Publication publication : existingPublications) {
			if ((!PublicationStatusType.Published.equals((publication.getStatus())))
					&& (!PublicationStatusType.Closed.equals((publication.getStatus())))) {
				publication.setCurrentAction(PublicationActionType.Close);
				PublicationGroup publicationGroup = publicationGroupRepository
						.getOne(publication.getPublicationGroupId());
				PublicationStatusType status = derivePublicationStatus(publicationGroup, publication, true);
				publication.setStatus(status, publication.getStatus());
				publication = repository.save(publication);
			}
			PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
			publicationList.add(publicationDTO);
		}
		return publicationList;
	}

	public List<PublicationDTO> reinstatePublications(Long publicationGroupId, String userId) {
		List<PublicationDTO> publicationList = new ArrayList<PublicationDTO>();
		if (userRoleService.getByUserIdAndRole(userId, CommonConstants.EDITOR_ROLE) == null
				&& userRoleService.getByUserIdAndRole(userId, CommonConstants.PUBLISHER_ROLE) == null) {
			throw new BusinessException(ErrorCodeConstants.ROLE_NOT_ASSIGNED_TO_USER,
					CommonConstants.EDITOR_ROLE + " OR " + CommonConstants.PUBLISHER_ROLE, userId);
		}
		List<Publication> existingPublications = repository.findByPublicationGroupId(publicationGroupId);
		for (Publication publication : existingPublications) {
			publication.setCurrentAction(PublicationActionType.Reinstate);
			PublicationGroup publicationGroup = publicationGroupRepository.getOne(publication.getPublicationGroupId());
			PublicationStatusType status = derivePublicationStatus(publicationGroup, publication, true);
			publication.setStatus(status, publication.getStatus());
			publication = repository.save(publication);
			PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
			publicationList.add(publicationDTO);
		}
		return publicationList;
	}

	public List<PropertyAuditData> getPreviousComments(Long publicationId) {
		Publication publication = new Publication();
		publication.setPublicationId(publicationId);
		AuditBuilder auditBuilder = AuditBuilder.getInstance(auditService, objectMapper, appConfig)
				.forParentObject(publication);
		auditBuilder.forProperty("comments");
		return auditBuilder.buildPropertyAudit();
	}

	public Page<PublicationSummaryDTO> findPublications(PublicationSearchRequest searchRequest, HeaderDTO header) {
		Pageable pageable = null; // PageRequest.of(1, 10);
		return customRepository.findPublications(searchRequest, pageable);
	}
}
