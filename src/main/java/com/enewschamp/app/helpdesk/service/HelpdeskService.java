package com.enewschamp.app.helpdesk.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.helpdesk.repository.HelpdeskRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.helpdesk.entity.Helpdesk;
import com.enewschamp.app.helpdesk.repository.HelpdeskRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class HelpdeskService {

	@Autowired
	HelpdeskRepository helpdeskRespository;
	
	@Autowired
	private HelpdeskRepositoryCustomImpl helpDeskRespositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public Helpdesk create(Helpdesk helpDeskEntity) {
		Helpdesk existingHelpdesk = getByStudentId(helpDeskEntity.getStudentId());
        if(existingHelpdesk !=null) {
        	throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
        }
		return helpdeskRespository.save(helpDeskEntity);
	}

	public Helpdesk update(Helpdesk helpDeskEntity) {
		Long helpdeskId = helpDeskEntity.getHelpdeskId();
		Helpdesk existingHelpdesk = get(helpdeskId);
		if (existingHelpdesk.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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
	
	public Page<Helpdesk> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Helpdesk> helpDeskList = helpDeskRespositoryCustom.findAll(pageable, searchRequest);
		if(helpDeskList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return helpDeskList;
	}

}