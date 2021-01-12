package com.enewschamp.app.admin.schoolsubscription.service;

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
import com.enewschamp.app.admin.schoolsubscription.entity.SchoolSubscriptionGrade;
import com.enewschamp.app.admin.schoolsubscription.repository.SchoolSubscriptionGradeRepository;
import com.enewschamp.app.admin.schoolsubscription.repository.SchoolSubscriptionGradeRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class SchoolSubscriptionGradeService {

	@Autowired
	private SchoolSubscriptionGradeRepository repository;

	@Autowired
	private SchoolSubscriptionGradeRepositoryCustom repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public SchoolSubscriptionGrade create(SchoolSubscriptionGrade schoolSubscriptionGrade) {
		SchoolSubscriptionGrade schoolSubscriptionGradeEntity = null;
		try {
			schoolSubscriptionGradeEntity = repository.save(schoolSubscriptionGrade);
		}
		catch(DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return schoolSubscriptionGradeEntity;
	}

	public SchoolSubscriptionGrade update(SchoolSubscriptionGrade instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getSchoolSubscriptionId();
		SchoolSubscriptionGrade existingSchoolSubscriptionGrade = get(stakeHolderId);
		if(existingSchoolSubscriptionGrade.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(instStakeHolderEntity, existingSchoolSubscriptionGrade);
		return repository.save(existingSchoolSubscriptionGrade);
	}

	public SchoolSubscriptionGrade get(Long SchoolSubscriptionGradeId) {
		Optional<SchoolSubscriptionGrade> existingEntity = repository.findById(SchoolSubscriptionGradeId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCHOOL_SUBS_GRADE_NOT_FOUND);
		}
	}

	public SchoolSubscriptionGrade read(SchoolSubscriptionGrade instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getSchoolSubscriptionId();
		SchoolSubscriptionGrade stakeHolder = get(stakeHolderId);
		return stakeHolder;
	}

	public SchoolSubscriptionGrade close(SchoolSubscriptionGrade instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getSchoolSubscriptionId();
		SchoolSubscriptionGrade existingSchoolSubscriptionGrade = get(stakeHolderId);
		if (existingSchoolSubscriptionGrade.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingSchoolSubscriptionGrade.setRecordInUse(RecordInUseType.N);
		existingSchoolSubscriptionGrade.setOperationDateTime(null);
		return repository.save(existingSchoolSubscriptionGrade);
	}

	public SchoolSubscriptionGrade reInstate(SchoolSubscriptionGrade instStakeHolderEntity) {
		Long stakeHolderId = instStakeHolderEntity.getSchoolSubscriptionId();
		SchoolSubscriptionGrade existingSchoolSubscriptionGrade = get(stakeHolderId);
		if (existingSchoolSubscriptionGrade.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingSchoolSubscriptionGrade.setRecordInUse(RecordInUseType.Y);
		existingSchoolSubscriptionGrade.setOperationDateTime(null);
		return repository.save(existingSchoolSubscriptionGrade);
	}

	public Page<SchoolSubscriptionGrade> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<SchoolSubscriptionGrade> stakeHolderList = repositoryCustom.findSchoolSubscriptionGrades(pageable,
				searchRequest);
		if(stakeHolderList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return stakeHolderList;
	}

}