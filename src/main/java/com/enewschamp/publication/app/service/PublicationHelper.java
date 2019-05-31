package com.enewschamp.publication.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.publication.app.dto.PublicationArticleLinkageDTO;
import com.enewschamp.publication.app.dto.PublicationDTO;
import com.enewschamp.publication.domain.entity.Publication;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;
import com.enewschamp.publication.domain.service.PublicationArticleLinkageRepository;
import com.enewschamp.publication.domain.service.PublicationRepository;
import com.enewschamp.publication.domain.service.PublicationService;

@Component
public class PublicationHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private PublicationService publicationService;
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	PublicationArticleLinkageRepository publicationLinkageRepository;
	
	@Autowired
	private PublicationArticleLinkageHelper publicationLinkageHelper;
	
	@Transactional
	public PublicationDTO createPublication(PublicationDTO publicationDTO) {
		
		List<PublicationArticleLinkageDTO> articleLinkages = publicationDTO.getArticleLinkages();
		for (PublicationArticleLinkageDTO articleLinkageDTO : articleLinkages) {
			articleLinkageDTO.setRecordInUse(publicationDTO.getRecordInUse());
			articleLinkageDTO.setOperatorId(publicationDTO.getOperatorId());
		}
		
		Publication publication;
		
		// Remove articles which have been deleted on the UI
		removeDelinkedArticles(publicationDTO.getPublicationId(), articleLinkages);
		
		publication = modelMapper.map(publicationDTO, Publication.class);
		publication = publicationService.create(publication);
		publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		
		articleLinkages = publicationDTO.getArticleLinkages(); 
		for(PublicationArticleLinkageDTO articleLinkageDTO: articleLinkages) {
			articleLinkageDTO.setPublicationId(publicationDTO.getPublicationId());
		}
		
		return publicationDTO;
	}

	private void removeDelinkedArticles(Long publicationId,
										List<PublicationArticleLinkageDTO> articleLinkages) {
		if(publicationId == null || publicationId <= 0) {
			return;
		}
		Publication publication = publicationService.get(publicationId);
		if(publication == null) {
			return;
		}
		for (PublicationArticleLinkage existingArticleLinkage : publication.getArticleLinkages()) {
			if (!isExistingLinkageFound(existingArticleLinkage.getId(), articleLinkages)) {
				System.out.println("Delete linkage for id: " + existingArticleLinkage.getId());
				publicationLinkageRepository.deleteById(existingArticleLinkage.getId());
			}
		}
	}
	
	private boolean isExistingLinkageFound(long linkageId, List<PublicationArticleLinkageDTO> existingLinkages) {
		
		for(PublicationArticleLinkageDTO articleLinkage: existingLinkages) {
			if(articleLinkage.getId() == linkageId) {
				return true;
			}
		}
		
		return false;
	}

	public PublicationDTO getPublication(Long publicationId) {
		Publication publication = publicationService.get(publicationId);
		PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		
		List<PublicationArticleLinkageDTO> articles = publicationLinkageHelper.getByPublicationId(publicationId);
		publicationDTO.setArticleLinkages(articles);
		return publicationDTO;
	}
	
	public List<PublicationDTO> getByPublicationGroupId(long publicationGroupId) {
		List<Publication> publications = publicationRepository.findByPublicationGroupId(publicationGroupId);
		List<PublicationDTO> publicationDTOs = new ArrayList<PublicationDTO>();
		for(Publication publication: publications) {
			PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
			//publicationDTO = fillArticleLinkages(publicationDTO);
			publicationDTOs.add(publicationDTO);
		}
		return publicationDTOs;
	}
	
	private PublicationDTO fillArticleLinkages(PublicationDTO publicationDTO) {
		List<PublicationArticleLinkage> articleLinkageList = publicationLinkageRepository.findByPublicationId(publicationDTO.getPublicationId());
		List<PublicationArticleLinkageDTO> linkageDTOs = new ArrayList<PublicationArticleLinkageDTO>();
		for(PublicationArticleLinkage linkage: articleLinkageList) {
			PublicationArticleLinkageDTO linkageDTO = modelMapper.map(linkage, PublicationArticleLinkageDTO.class);
			linkageDTOs.add(linkageDTO);
		}
		publicationDTO.setArticleLinkages(linkageDTOs);
		return publicationDTO;
	}
	
	public void deleteByPublicationGroupId(long publicationGroupId) {
		publicationRepository.deleteByPublicationGroupId(publicationGroupId);
	}
	
}
