package com.enewschamp.publication.app.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enewschamp.publication.app.dto.PublicationArticleLinkageDTO;
import com.enewschamp.publication.domain.entity.PublicationArticleLinkage;
import com.enewschamp.publication.domain.service.PublicationArticleLinkageRepository;
import com.enewschamp.publication.domain.service.PublicationArticleLinkageService;

import lombok.extern.java.Log;

@Log
@Component
public class PublicationArticleLinkageHelper {

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private PublicationArticleLinkageService publicationArticleLinkageService;
	
	@Autowired
	private PublicationArticleLinkageRepository publicationArticleLinkageRepository;
	
	public PublicationArticleLinkageDTO create(PublicationArticleLinkageDTO articleLinkageDTO) {
		
		PublicationArticleLinkage article = modelMapper.map(articleLinkageDTO, PublicationArticleLinkage.class);
		article = publicationArticleLinkageService.create(article);
		articleLinkageDTO = modelMapper.map(article, PublicationArticleLinkageDTO.class);
		
		return articleLinkageDTO;
	}

	public PublicationArticleLinkageDTO get(long articleLinkageId) {
		PublicationArticleLinkage articleLinkage = publicationArticleLinkageService.get(articleLinkageId);
		PublicationArticleLinkageDTO articleDTO = modelMapper.map(articleLinkage, PublicationArticleLinkageDTO.class);
		return articleDTO;
	}
	
	public List<PublicationArticleLinkageDTO> getByPublicationId(long publicationId) {
		List<PublicationArticleLinkage> articles = publicationArticleLinkageRepository.findByPublicationId(publicationId);
		List<PublicationArticleLinkageDTO> articleDTOs = new ArrayList<PublicationArticleLinkageDTO>();
		for(PublicationArticleLinkage article: articles) {
			PublicationArticleLinkageDTO articleDTO = modelMapper.map(article, PublicationArticleLinkageDTO.class);
			articleDTOs.add(articleDTO);
		}
		return articleDTOs;
	}
	
	public void deleteByPublicationId(long publicationId) {
		publicationArticleLinkageRepository.deleteByPublicationId(publicationId);
	}
	
}
