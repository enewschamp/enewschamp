package com.enewschamp.app.helpdesk.service;

import java.time.LocalDateTime;
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
		HelpDesk helpDesk = null;
		try {
			helpDesk = helpDeskRespository.save(helpDeskEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return helpDesk;
	}

	public HelpDesk update(HelpDesk helpDeskEntity) {
		Long HelpDeskId = helpDeskEntity.getRequestId();
		HelpDesk existingHelpDesk = get(HelpDeskId);
		LocalDateTime createDateTime = existingHelpDesk.getCreateDateTime();
		modelMapper.map(helpDeskEntity, existingHelpDesk);
		existingHelpDesk.setCreateDateTime(createDateTime);;
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
		if (existingHelpDesk.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingHelpDesk;
		}
		existingHelpDesk.setRecordInUse(RecordInUseType.Y);
		existingHelpDesk.setOperationDateTime(null);
		return helpDeskRespository.save(existingHelpDesk);
	}

	public HelpDesk close(HelpDesk helpDeskEntity) {
		Long helpDeskId = helpDeskEntity.getRequestId();
		HelpDesk existingEntity = get(helpDeskId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			return existingEntity;
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return helpDeskRespository.save(existingEntity);
	}
	
	public HelpDesk reinstate(HelpDesk helpdeskEntity) {
		Long helpdeskId = helpdeskEntity.getRequestId();
		HelpDesk existingHelpdesk = get(helpdeskId);
		if (existingHelpdesk.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingHelpdesk;
		}
		existingHelpdesk.setRecordInUse(RecordInUseType.Y);
		existingHelpdesk.setOperationDateTime(null);
		return helpDeskRespository.save(existingHelpdesk);
	}


	public Page<HelpDesk> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<HelpDesk> genreList = helpDeskRespositoryCustom.findHelpDesks(pageable, searchRequest);
		return genreList;
	}

}
