package com.enewschamp.app.fw.page.navigation.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorRulesDTO;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;
import com.enewschamp.app.fw.page.navigation.repository.PageNavigatorRulesRepository;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;
import com.google.common.reflect.TypeToken;

@Service
public class PageNavigationRulesService {

	@Autowired
	PageNavigatorRulesRepository PageNavigatorRulesRepository;

	

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public PageNavigatorRules create(PageNavigatorRules PageNavigatorRulesEntity) {
		return PageNavigatorRulesRepository.save(PageNavigatorRulesEntity);
	}

	public PageNavigatorRules update(PageNavigatorRules PageNavigatorRules) {
		Long navId = PageNavigatorRules.getNavId();
		PageNavigatorRules existingPageNavigatorRules = get(navId);
		modelMapper.map(PageNavigatorRules, existingPageNavigatorRules);
		return PageNavigatorRulesRepository.save(existingPageNavigatorRules);
	}

	public PageNavigatorRules patch(PageNavigatorRules PageNavigatorRules) {
		Long navId = PageNavigatorRules.getNavId();
		PageNavigatorRules existingEntity = get(navId);
		modelMapperForPatch.map(PageNavigatorRules, existingEntity);
		return PageNavigatorRulesRepository.save(existingEntity);
	}

	public void delete(Long PageNavigatorRulesId) {
		PageNavigatorRulesRepository.deleteById(PageNavigatorRulesId);
	}

	public PageNavigatorRules get(Long navId) {
		Optional<PageNavigatorRules> existingEntity = PageNavigatorRulesRepository.findById(navId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw  new BusinessException(ErrorCodes.PAGE_NOT_FOUND);

		}
	}

	public List<PageNavigatorRulesDTO> getNavRuleList(Long navId) {
		List<PageNavigatorRules> existingEntity = PageNavigatorRulesRepository.getNavRules(navId);
		java.lang.reflect.Type listType = new TypeToken<List<PageNavigatorRulesDTO>>() {
		}.getType();
		List<PageNavigatorRulesDTO> pageNavList = modelMapper.map(existingEntity, listType);
		
		return pageNavList;
	}

	

	public String getAudit(Long navId) {
		PageNavigatorRules PageNavigatorRules = new PageNavigatorRules();
		PageNavigatorRules.setNavId(navId);
		return auditService.getEntityAudit(PageNavigatorRules);
	}
}
