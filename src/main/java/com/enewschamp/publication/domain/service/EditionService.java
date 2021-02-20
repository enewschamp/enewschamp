package com.enewschamp.publication.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.edition.repository.EditionRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Edition;

@Service
public class EditionService extends AbstractDomainService {

	@Autowired
	EditionRepository repository;

	@Autowired
	EditionRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Edition create(Edition editionEntity) {
		Edition edition = null;
		try {
			edition = repository.save(editionEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return edition;
	}

	public Edition update(Edition edition) {
		String editionId = edition.getEditionId();
		Edition existingEdition = get(editionId);
		if(existingEdition.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public Edition read(Edition edition) {
		String editionId = edition.getEditionId();
		Edition editionEntity = get(editionId);
		return editionEntity;
	}

	public Edition close(Edition editionEntity) {
		String editionId = editionEntity.getEditionId();
		Edition existingEntity = get(editionId);
		if(existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	
	public Edition reinstate(Edition editionEntity) {
		String editionId = editionEntity.getEditionId();
		Edition existingEdition = get(editionId);
		if(existingEdition.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingEdition.setRecordInUse(RecordInUseType.Y);
		existingEdition.setOperationDateTime(null);
		return repository.save(existingEdition);
	}
	
	public Page<Edition> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Edition> editionList = repositoryCustom.findAll(pageable, null);
		if(editionList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return editionList;
	}
}
