package com.enewschamp.app.common.uicontrols.service;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.uicontrols.rule.repository.UIControlsRulesRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.common.uicontrols.dto.UIControlsRulesDTO;
import com.enewschamp.app.common.uicontrols.entity.UIControlsRules;
import com.enewschamp.app.common.uicontrols.repository.UIControlsRulesRepository;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.google.common.reflect.TypeToken;

@Service
public class UIControlsRulesService {

	@Autowired
	UIControlsRulesRepository UIControlsRulesRepository;
	
	@Autowired
	UIControlsRulesRepositoryCustomImpl uiControlsRulesRepositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public UIControlsRules create(UIControlsRules entity) {
		UIControlsRules uiControlsRules = null;
		try {
			uiControlsRules = UIControlsRulesRepository.save(entity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return uiControlsRules;
	}
	
	public UIControlsRules updateOne(UIControlsRules UIControlsRules) {
		Long uiControlId = UIControlsRules.getRuleId();
		UIControlsRules existingUIControlsRules = get(uiControlId);
		if (existingUIControlsRules.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(UIControlsRules, existingUIControlsRules);
		return UIControlsRulesRepository.save(existingUIControlsRules);
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
			throw new BusinessException(ErrorCodeConstants.UICONTROLS_NOT_FOUND);

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
	
	public UIControlsRules read(UIControlsRules uiControlEntity) {
		Long ruleId = uiControlEntity.getRuleId();
		UIControlsRules existingEntity = get(ruleId);
		return existingEntity;

	}

	public UIControlsRules close(UIControlsRules uiControlEntity) {
		Long roleId = uiControlEntity.getRuleId();
		UIControlsRules existingUIControls = getOne(roleId);
		if (existingUIControls.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingUIControls.setRecordInUse(RecordInUseType.N);
		existingUIControls.setOperationDateTime(null);
		return UIControlsRulesRepository.save(existingUIControls);
	}

	public UIControlsRules reinstate(UIControlsRules uiControlEntity) {
		Long ruleId = uiControlEntity.getRuleId();
		UIControlsRules existingUIControls = getOne(ruleId);
		if (existingUIControls.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingUIControls.setRecordInUse(RecordInUseType.Y);
		existingUIControls.setOperationDateTime(null);
		return UIControlsRulesRepository.save(existingUIControls);
	}

	public Page<UIControlsRules> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<UIControlsRules> uiControlRuleList = uiControlsRulesRepositoryCustom.findAll(pageable, searchRequest);
		if (uiControlRuleList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return uiControlRuleList;
	}

	public UIControlsRules getOne(Long uiControlsRulesId) {
		Optional<UIControlsRules> existingEntity = UIControlsRulesRepository.findById(uiControlsRulesId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.UICONTROLS_NOT_FOUND);
		}
	}

	public int createAll(List<UIControlsRules> uiControlsRules) {
		int noOfRecords = 0;
		try {
			noOfRecords = UIControlsRulesRepository.saveAll(uiControlsRules).size();
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return noOfRecords;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void clean() {
		UIControlsRulesRepository.truncate();
		UIControlsRulesRepository.deleteSequences();
		UIControlsRulesRepository.initializeSequence();
	}
}