package com.enewschamp.app.fw.page.navigation.service;

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

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.page.navigator.rule.repository.PageNavigatorRulesRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.fw.page.navigation.dto.PageNavigatorRulesDTO;
import com.enewschamp.app.fw.page.navigation.entity.PageNavigatorRules;
import com.enewschamp.app.fw.page.navigation.repository.PageNavigatorRulesRepository;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.google.common.reflect.TypeToken;

@Service
public class PageNavigationRulesService {

	@Autowired
	PageNavigatorRulesRepository pageNavigatorRulesRepository;

	@Autowired
	PageNavigatorRulesRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public PageNavigatorRules create(PageNavigatorRules PageNavigatorRulesEntity) {
		PageNavigatorRules pageNavigatorEntity = null;
		try {
			pageNavigatorEntity = pageNavigatorRulesRepository.save(PageNavigatorRulesEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return pageNavigatorEntity;
	}

	public PageNavigatorRules update(PageNavigatorRules PageNavigatorRules) {
		Long ruleId = PageNavigatorRules.getRuleId();
		PageNavigatorRules existingPageNavigatorRules = get(ruleId);
		if (existingPageNavigatorRules.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(PageNavigatorRules, existingPageNavigatorRules);
		return pageNavigatorRulesRepository.save(existingPageNavigatorRules);
	}

	public PageNavigatorRules patch(PageNavigatorRules PageNavigatorRules) {
		Long ruleId = PageNavigatorRules.getRuleId();
		PageNavigatorRules existingEntity = get(ruleId);
		modelMapperForPatch.map(PageNavigatorRules, existingEntity);
		return pageNavigatorRulesRepository.save(existingEntity);
	}

	public void delete(Long PageNavigatorRulesId) {
		pageNavigatorRulesRepository.deleteById(PageNavigatorRulesId);
	}

	public PageNavigatorRules get(Long ruleId) {
		Optional<PageNavigatorRules> existingEntity = pageNavigatorRulesRepository.findById(ruleId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PAGE_NOT_FOUND);

		}
	}

	public List<PageNavigatorRulesDTO> getNavRuleList(Long ruleId) {
		List<PageNavigatorRules> existingEntity = pageNavigatorRulesRepository.getNavRules(ruleId);
		java.lang.reflect.Type listType = new TypeToken<List<PageNavigatorRulesDTO>>() {
		}.getType();
		List<PageNavigatorRulesDTO> pageNavList = modelMapper.map(existingEntity, listType);

		return pageNavList;
	}

	public String getAudit(Long ruleId) {
		PageNavigatorRules PageNavigatorRules = new PageNavigatorRules();
		PageNavigatorRules.setNavId(ruleId);
		return auditService.getEntityAudit(PageNavigatorRules);
	}

	public PageNavigatorRules read(PageNavigatorRules pageNavigator) {
		Long pageNavigatorId = pageNavigator.getRuleId();
		PageNavigatorRules existingPageNavigator = get(pageNavigatorId);
		return existingPageNavigator;
	}

	public PageNavigatorRules close(PageNavigatorRules pageNavigator) {
		Long pageNavigatorId = pageNavigator.getRuleId();
		PageNavigatorRules existingPageNavigator = get(pageNavigatorId);
		if (existingPageNavigator.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingPageNavigator.setRecordInUse(RecordInUseType.N);
		existingPageNavigator.setOperationDateTime(null);
		return pageNavigatorRulesRepository.save(existingPageNavigator);
	}

	public PageNavigatorRules reInstate(PageNavigatorRules pageNavigator) {
		Long pageNavigatorId = pageNavigator.getRuleId();
		PageNavigatorRules existingPageNavigator = get(pageNavigatorId);
		if (existingPageNavigator.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingPageNavigator.setRecordInUse(RecordInUseType.Y);
		existingPageNavigator.setOperationDateTime(null);
		return pageNavigatorRulesRepository.save(existingPageNavigator);
	}

	public Page<PageNavigatorRules> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<PageNavigatorRules> pageList = repositoryCustom.findAll(pageable, searchRequest);
		if (pageList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return pageList;
	}
}
