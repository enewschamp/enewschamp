package com.enewschamp.app.helpdesk.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.helpdesk.entity.HelpDesk;
import com.enewschamp.app.helpdesk.repository.HelpDeskRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class HelpDeskService {

	@Autowired
	HelpDeskRepository helpDeskRespository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public HelpDesk create(HelpDesk helpDeskEntity) {
		return helpDeskRespository.save(helpDeskEntity);
	}

	public HelpDesk update(HelpDesk helpDeskEntity) {
		Long HelpDeskId = helpDeskEntity.getRequestId();
		HelpDesk existingHelpDesk = get(HelpDeskId);
		modelMapper.map(helpDeskEntity, existingHelpDesk);
		return helpDeskRespository.save(existingHelpDesk);
	}

	public HelpDesk patch(HelpDesk HelpDesk) {
		Long navId = HelpDesk.getRequestId();
		HelpDesk existingEntity = get(navId);
		modelMapperForPatch.map(HelpDesk, existingEntity);
		return helpDeskRespository.save(existingEntity);
	}

	public void delete(Long HelpDeskId) {
		helpDeskRespository.deleteById(HelpDeskId);
	}

	public HelpDesk get(Long HelpDeskId) {
		Optional<HelpDesk> existingEntity = helpDeskRespository.findById(HelpDeskId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.REQUESTID_NOT_FOUND);
		}
	}
	
	
}
