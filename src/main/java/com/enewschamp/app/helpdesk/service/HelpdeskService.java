package com.enewschamp.app.helpdesk.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.helpdesk.entity.Helpdesk;
import com.enewschamp.app.helpdesk.repository.HelpdeskRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class HelpdeskService {

	@Autowired
	HelpdeskRepository helpdeskRespository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public Helpdesk create(Helpdesk helpDeskEntity) {
		return helpdeskRespository.save(helpDeskEntity);
	}

	public Helpdesk update(Helpdesk helpDeskEntity) {
		Long helpdeskId = helpDeskEntity.getHelpdeskId();
		Helpdesk existingHelpdesk = get(helpdeskId);
		modelMapper.map(helpDeskEntity, existingHelpdesk);
		return helpdeskRespository.save(existingHelpdesk);
	}

	public Helpdesk patch(Helpdesk Helpdesk) {
		Long helpdeskId = Helpdesk.getHelpdeskId();
		Helpdesk existingEntity = get(helpdeskId);
		modelMapperForPatch.map(Helpdesk, existingEntity);
		return helpdeskRespository.save(existingEntity);
	}

	public void delete(Long helpdeskId) {
		helpdeskRespository.deleteById(helpdeskId);
	}

	public Helpdesk get(Long helpdeskId) {
		Optional<Helpdesk> existingEntity = helpdeskRespository.findById(helpdeskId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.REQUESTID_NOT_FOUND);
		}
	}

	public Helpdesk getByStudentId(Long studentId) {
		List<Helpdesk> existingEntity = helpdeskRespository.findByStudentId(studentId);
		if (existingEntity != null && existingEntity.size() > 0) {
			return existingEntity.get(0);
		} else {
			return null;
		}
	}

}
