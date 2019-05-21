package com.enewschamp.publication.app.service;

import java.util.ArrayList;
import java.util.List;

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
	PublicationArticleLinkageRepository publicationPublicationLinkageRepository;
	
	@Autowired
	private PublicationArticleLinkageHelper publicationPublicationLinkageHelper;
	
	public PublicationDTO createPublication(PublicationDTO publicationDTO) {
		
		List<PublicationArticleLinkageDTO> articleLinkages = publicationDTO.getArticleLinkages();
		
		Publication publication = modelMapper.map(publicationDTO, Publication.class);
		publication = publicationService.create(publication);
		publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		
		List<PublicationArticleLinkageDTO> articleLinkageList = new ArrayList<PublicationArticleLinkageDTO>(); 
		for(PublicationArticleLinkageDTO articleLinkageDTO: articleLinkages) {
			articleLinkageDTO.getPublicationArticleLinkageKey().setPublicationId(publication.getPublicationId());
			articleLinkageDTO.setRecordInUse(publication.getRecordInUse());
			articleLinkageDTO.setOperatorId(publication.getOperatorId());
			articleLinkageDTO = publicationPublicationLinkageHelper.create(articleLinkageDTO);
			articleLinkageList.add(articleLinkageDTO);
		}
		publicationDTO.setArticleLinkages(articleLinkageList);
		
		return publicationDTO;
	}

	public PublicationDTO getPublication(Long publicationId) {
		Publication publication = publicationService.get(publicationId);
		PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
		
		List<PublicationArticleLinkageDTO> articles = publicationPublicationLinkageHelper.getByPublicationId(publicationId);
		publicationDTO.setArticleLinkages(articles);
		return publicationDTO;
	}
	
	public List<PublicationDTO> getByPublicationGroupId(long publicationGroupId) {
		List<Publication> publications = publicationRepository.findByPublicationGroupId(publicationGroupId);
		List<PublicationDTO> publicationDTOs = new ArrayList<PublicationDTO>();
		for(Publication publication: publications) {
			PublicationDTO publicationDTO = modelMapper.map(publication, PublicationDTO.class);
			publicationDTO = fillArticleLinkages(publicationDTO);
			publicationDTOs.add(publicationDTO);
		}
		return publicationDTOs;
	}
	
	private PublicationDTO fillArticleLinkages(PublicationDTO publicationDTO) {
		List<PublicationArticleLinkage> articleLinkageList = publicationPublicationLinkageRepository.findByPublicationId(publicationDTO.getPublicationId());
		List<PublicationArticleLinkageDTO> linkageDTOs = new ArrayList<PublicationArticleLinkageDTO>();
		for(PublicationArticleLinkage linkage: articleLinkageList) {
			PublicationArticleLinkageDTO linkageDTO = modelMapper.map(linkage, PublicationArticleLinkageDTO.class);
			linkageDTOs.add(linkageDTO);
		}
		publicationDTO.setArticleLinkages(linkageDTOs);
		return publicationDTO;
	}
	
}
