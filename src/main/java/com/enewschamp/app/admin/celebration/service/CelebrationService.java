package com.enewschamp.app.admin.celebration.service;

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
import com.enewschamp.app.admin.celebration.entity.Celebration;
import com.enewschamp.app.admin.celebration.repository.CelebrationRepository;
import com.enewschamp.app.admin.celebration.repository.CelebrationRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.audit.domain.AuditService;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class CelebrationService {

	@Autowired
	private CelebrationRepository repository;

	@Autowired
	private CelebrationRepositoryCustomImpl repositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	@Autowired
	AuditService auditService;

	public Celebration create(Celebration celebrationEntity) {
		Celebration celebration = null;
		try {
			celebration = repository.save(celebrationEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return celebration;
	}

	public Celebration update(Celebration celebration) {
		Long celebrationId = celebration.getCelebrationId();
		Celebration existingCelebration = get(celebrationId);
		if (existingCelebration.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(celebration, existingCelebration);
		return repository.save(existingCelebration);
	}

	public Celebration patch(Celebration celebration) {
		Long celebrationId = celebration.getCelebrationId();
		Celebration existingEntity = get(celebrationId);
		modelMapperForPatch.map(celebration, existingEntity);
		return repository.save(existingEntity);
	}

	public void delete(Long celebrationId) {
		repository.deleteById(celebrationId);
	}

	public Celebration get(Long celebrationId) {
		Optional<Celebration> existingEntity = repository.findById(celebrationId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.GENRE_NOT_FOUND, String.valueOf(celebrationId));
		}
	}

	public String getAudit(Long celebrationId) {
		Celebration celebration = new Celebration();
		celebration.setCelebrationId(celebrationId);
		return auditService.getEntityAudit(celebration);
	}

	public Celebration read(Celebration celebration) {
		Long celebrationId = celebration.getCelebrationId();
		Celebration celebrationEntity = get(celebrationId);
		return celebrationEntity;
	}

	public Celebration close(Celebration celebrationEntity) {
		Long celebrationId = celebrationEntity.getCelebrationId();
		Celebration existingEntity = get(celebrationId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public Celebration reinstate(Celebration celebrationEntity) {
		Long celebrationId = celebrationEntity.getCelebrationId();
		Celebration existingCelebration = get(celebrationId);
		if (existingCelebration.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingCelebration.setRecordInUse(RecordInUseType.Y);
		existingCelebration.setOperationDateTime(null);
		return repository.save(existingCelebration);
	}

//	public List<CelebrationList> getCelebrationList() {
//		return repository.getCelebrationList();
//	}

	public Page<Celebration> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Celebration> celebrationList = repositoryCustom.findAll(pageable, null);
		return celebrationList;
	}
}
