package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Edition;

@Service
public class EditionService extends AbstractDomainService {

	@Autowired
	EditionRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Edition create(Edition edition) {
		return repository.save(edition);
	}

	public Edition update(Edition edition) {
		String editionId = edition.getEditionId();
		Edition existingEdition = get(editionId);
		modelMapper.map(edition, existingEdition);
		return repository.save(existingEdition);
	}

	public Edition patch(Edition edition) {
		String editionId = edition.getEditionId();
		Edition existingEntity = get(editionId);
		modelMapperForPatch.map(edition, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(String editionId) {
		repository.deleteById(editionId);
	}

	public Edition get(String editionId) {
		Optional<Edition> existingEntity = repository.findById(editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.EDITION_NOT_FOUND, editionId);
		}
	}

	public String getAudit(String editionId) {
		Edition edition = new Edition();
		edition.setEditionId(editionId);
		return auditService.getEntityAudit(edition);
	}

	public Edition getEdition(String editionId) {
		Optional<Edition> existingEntity = repository.findById(editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.EDITION_NOT_FOUND, editionId);
		}
	}

	public List<ListOfValuesItem> getLOV() {
		return toListOfValuesItems(repository.getEditionLOV());
	}
}
