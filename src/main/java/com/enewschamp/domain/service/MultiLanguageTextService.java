package com.enewschamp.domain.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.entity.MultiLanguageText;
import com.enewschamp.domain.repository.MultiLanguageTextRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class MultiLanguageTextService extends AbstractDomainService {

	@Autowired
	MultiLanguageTextRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	@Autowired
	AuditService auditService;
	
	public MultiLanguageText create(MultiLanguageText multiLanguageText) {
		return repository.save(multiLanguageText);
	}
	
	public MultiLanguageText update(MultiLanguageText multiLanguageText) {
		Long multiLanguageTextId = multiLanguageText.getMultiLanguageTextId();
		MultiLanguageText existingEntity = get(multiLanguageTextId);
		modelMapper.map(multiLanguageText, existingEntity);
		return repository.save(existingEntity);
	}
	
	public MultiLanguageText patch(MultiLanguageText multiLanguageText) {
		Long multiLanguageTextId = multiLanguageText.getMultiLanguageTextId();
		MultiLanguageText existingEntity = get(multiLanguageTextId);
		modelMapperForPatch.map(multiLanguageText, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long multiLanguageTextId) {
		repository.deleteById(multiLanguageTextId);
	}
	
	public MultiLanguageText get(Long multiLanguageTextId) {
		Optional<MultiLanguageText> existingEntity = repository.findById(multiLanguageTextId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.MULTI_LANG_TEXT_NOT_FOUND);
		}
	}
	
	public String getAudit(Long multiLanguageTextId) {
		MultiLanguageText multiLanguageText = new MultiLanguageText();
		multiLanguageText.setMultiLanguageTextId(multiLanguageTextId);
		return auditService.getEntityAudit(multiLanguageText);
	}
	
}
