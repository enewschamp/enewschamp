package com.enewschamp.app.common.uicontrols.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.uicontrols.dto.UIControlsRulesDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;
import com.enewschamp.app.common.uicontrols.repository.UIControlsRulesRepository;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.problem.BusinessException;
import com.google.common.reflect.TypeToken;

@Service
public class UIControlsRulesService {

	@Autowired
	UIControlsRulesRepository UIControlsRulesRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public UIControlsRules create(UIControlsRules UIControlsRulesEntity) {
		return UIControlsRulesRepository.save(UIControlsRulesEntity);
	}

	public UIControlsRules update(UIControlsRules UIControlsRules) {
		Long uiControlId = UIControlsRules.getUiControlId();
		UIControlsRules existingUIControlsRules = get(uiControlId);
		modelMapper.map(UIControlsRules, existingUIControlsRules);
		return UIControlsRulesRepository.save(existingUIControlsRules);
	}

	public UIControlsRules patch(UIControlsRules UIControlsRules) {
		Long uiControlId = UIControlsRules.getUiControlId();
		UIControlsRules existingEntity = get(uiControlId);
		modelMapperForPatch.map(UIControlsRules, existingEntity);
		return UIControlsRulesRepository.save(existingEntity);
	}

	public void delete(Long UIControlsRulesId) {
		UIControlsRulesRepository.deleteById(UIControlsRulesId);
	}

	public UIControlsRules get(Long uiControlId) {
		Optional<UIControlsRules> existingEntity = UIControlsRulesRepository.findById(uiControlId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PAGE_NOT_FOUND);

		}
	}

	public List<UIControlsRulesDTO> getUIControlsRuleList(Long uiControlId) {
		List<UIControlsRules> existingEntity = UIControlsRulesRepository.getControlRules(uiControlId);
		java.lang.reflect.Type listType = new TypeToken<List<UIControlsRulesDTO>>() {
		}.getType();
		List<UIControlsRulesDTO> uiControlsRuleList = modelMapper.map(existingEntity, listType);
		return uiControlsRuleList;
	}

	public String getAudit(Long uiControlId) {
		UIControlsRules UIControlsRules = new UIControlsRules();
		UIControlsRules.setUiControlId(uiControlId);
		return auditService.getEntityAudit(UIControlsRules);
	}
}
