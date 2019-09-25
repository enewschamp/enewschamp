package com.enewschamp.domain.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.entity.LOV;
import com.enewschamp.domain.repository.LOVRepository;
import com.enewschamp.page.dto.ListOfValuesItem;
import com.enewschamp.problem.BusinessException;

@Service
public class LOVService extends AbstractDomainService {

	@Autowired
	LOVRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public LOV create(LOV lov) {
		return repository.save(lov);
	}
	
	public LOV update(LOV lov) {
		Long lovId = lov.getLovId();
		LOV existingEntity = get(lovId);
		modelMapper.map(lov, existingEntity);
		return repository.save(existingEntity);
	}
	
	public LOV patch(LOV lov) {
		Long lovId = lov.getLovId();
		LOV existingEntity = get(lovId);
		modelMapperForPatch.map(lov, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long lovId) {
		repository.deleteById(lovId);
	}
	
	public LOV get(Long lovId) {
		Optional<LOV> existingEntity = repository.findById(lovId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.LOV_NOT_FOUND);
		}
	}
	
	public String getAudit(Long lovId) {
		LOV lov = new LOV();
		lov.setLovId(lovId);
		return auditService.getEntityAudit(lov);
	}
	
	public List<ListOfValuesItem> getLOV(String lovType) {
		return toListOfValuesItems(repository.getLOV(lovType));
	}

}
