package com.enewschamp.app.helpdesk.service;

import java.time.LocalDateTime;
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
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.helpdesk.entity.Helpdesk;
import com.enewschamp.app.helpdesk.repository.HelpdeskRepositoryCustom;
import com.enewschamp.app.helpdesk.repository.HelpdeskRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class HelpDeskService {

	@Autowired
	private HelpdeskRepository helpdeskRespository;

	@Autowired
	private HelpdeskRepositoryCustom helpDeskRespositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public Helpdesk create(Helpdesk helpDeskEntity) {
		Helpdesk helpDesk = null;
		try {
			helpDesk = helpdeskRespository.save(helpDeskEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return helpDesk;
	}

	public Helpdesk update(Helpdesk helpDeskEntity) {
		Long HelpDeskId = helpDeskEntity.getHelpdeskId();
		Helpdesk existingHelpDesk = get(HelpDeskId);
		if(existingHelpDesk.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		LocalDateTime createDateTime = existingHelpDesk.getCreateDateTime();
		modelMapper.map(helpDeskEntity, existingHelpDesk);
		existingHelpDesk.setCreateDateTime(createDateTime);
		return helpdeskRespository.save(existingHelpDesk);
	}

	public Helpdesk patch(Helpdesk helpdesk) {
		Long navId = helpdesk.getHelpdeskId();
		Helpdesk existingEntity = get(navId);
		modelMapperForPatch.map(helpdesk, existingEntity);
		return helpdeskRespository.save(existingEntity);
	}

	public void delete(Long HelpDeskId) {
		helpdeskRespository.deleteById(HelpDeskId);
	}

	public Helpdesk get(Long HelpDeskId) {
		Optional<Helpdesk> existingEntity = helpdeskRespository.findById(HelpDeskId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.REQUESTID_NOT_FOUND);
		}
	}

	public Helpdesk read(Helpdesk helpDesk) {
		Long helpDeskId = helpDesk.getHelpdeskId();
		Helpdesk helpDeskEntity = get(helpDeskId);
        return helpDeskEntity;
	}

	public Helpdesk close(Helpdesk helpDeskEntity) {
		Long helpDeskId = helpDeskEntity.getHelpdeskId();
		Helpdesk existingEntity = get(helpDeskId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return helpdeskRespository.save(existingEntity);
	}

	public Helpdesk reinstate(Helpdesk helpdeskEntity) {
		Long helpdeskId = helpdeskEntity.getHelpdeskId();
		Helpdesk existingHelpdesk = get(helpdeskId);
		if (existingHelpdesk.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingHelpdesk.setRecordInUse(RecordInUseType.Y);
		existingHelpdesk.setOperationDateTime(null);
		return helpdeskRespository.save(existingHelpdesk);
	}

	public Helpdesk getByStudentId(Long studentId) {
		List<Helpdesk> existingEntity = helpdeskRespository.findByStudentId(studentId);
		if (existingEntity != null && existingEntity.size() > 0) {
			return existingEntity.get(0);
		} else {
			return null;
		}
	}

	public Page<Helpdesk> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Helpdesk> helpDeskList = helpDeskRespositoryCustom.findHelpDesks(pageable, searchRequest);
		if(helpDeskList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return helpDeskList;
	}

}
