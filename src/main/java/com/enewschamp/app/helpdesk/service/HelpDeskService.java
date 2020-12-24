package com.enewschamp.app.helpdesk.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.helpdesk.entity.HelpDesk;
import com.enewschamp.app.helpdesk.repository.HelpDeskRepository;
import com.enewschamp.app.helpdesk.repository.HelpDeskRepositoryCustom;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class HelpDeskService {

	@Autowired
	private HelpDeskRepository helpDeskRespository;
	
	@Autowired
	private HelpDeskRepositoryCustom helpDeskRespositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

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
			throw new BusinessException(ErrorCodeConstants.REQUESTID_NOT_FOUND);
		}
	}

	public HelpDesk read(HelpDesk helpDeskEntity) {
		Long helpDeskId = helpDeskEntity.getRequestId();
		HelpDesk existingHelpDesk = get(helpDeskId);
		existingHelpDesk.setRecordInUse(RecordInUseType.Y);
		return helpDeskRespository.save(existingHelpDesk);
	}

	public HelpDesk close(HelpDesk helpDeskEntity) {
		Long helpDeskId = helpDeskEntity.getRequestId();
		HelpDesk existingEntity = get(helpDeskId);
		existingEntity.setRecordInUse(RecordInUseType.N);
		return helpDeskRespository.save(existingEntity);
	}

	public Page<HelpDesk> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<HelpDesk> genreList = helpDeskRespositoryCustom.findHelpDesks(pageable);
		return genreList;
	}

}
