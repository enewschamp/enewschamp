package com.enewschamp.app.school.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import com.enewschamp.app.admin.schoolpricing.repository.SchoolPricingRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.school.entity.School;
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.app.school.repository.SchoolPricingRepository;
import com.enewschamp.app.school.repository.SchoolRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class SchoolPricingService {

	@Autowired
	private SchoolPricingRepository schoolPricingRepository;
	
	@Autowired
	private SchoolPricingRepositoryCustom schoolPricingRepositoryCustom;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public SchoolPricing create(SchoolPricing schoolPricingEntity) {
		SchoolPricing schoolPricing = null;
		try {
			schoolPricing = schoolPricingRepository.save(schoolPricingEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return schoolPricing;
	}

	public SchoolPricing update(SchoolPricing SchoolPricingEntity) {
		Long SchoolPricingId = SchoolPricingEntity.getSchoolPricingId();
		SchoolPricing existingSchoolPricing = get(SchoolPricingId);
		modelMapper.map(SchoolPricingEntity, existingSchoolPricing);
		return schoolPricingRepository.save(existingSchoolPricing);
	}

	public SchoolPricing patch(SchoolPricing SchoolPricing) {
		Long navId = SchoolPricing.getSchoolPricingId();
		SchoolPricing existingEntity = get(navId);
		modelMapperForPatch.map(SchoolPricing, existingEntity);
		return schoolPricingRepository.save(existingEntity);
	}

	public void delete(Long SchoolPricingId) {
		schoolPricingRepository.deleteById(SchoolPricingId);
	}

	public SchoolPricing get(Long SchoolPricingId) {
		Optional<SchoolPricing> existingEntity = schoolPricingRepository.findById(SchoolPricingId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCHOOL_PRICING_NOT_FOUND);
		}
	}

	public SchoolPricing getPricingForInstitution(Long institutionId, String editionId) {
		Date sysdate = new Date();
		LocalDate localSysDate = sysdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		List<SchoolPricing> schoolPrice = schoolPricingRepository.getPricingForInstitution(institutionId, editionId,
				"S", localSysDate);
		if (schoolPrice != null && schoolPrice.size() > 0) {
			return schoolPrice.get(0);
		} else {
			// get school pricing for school chain id
			Optional<School> existingEntity = schoolRepository.findById(institutionId);
			if (existingEntity.isPresent()) {
				School school = existingEntity.get();
				schoolPrice = schoolPricingRepository.getPricingForInstitution(school.getSchoolChainId(), editionId,
						"C", localSysDate);
				if (schoolPrice != null && schoolPrice.size() > 0) {
					return schoolPrice.get(0);
				} else {
					throw new BusinessException(ErrorCodeConstants.SCHOOL_PRICING_NOT_FOUND);
				}
			} else {
				throw new BusinessException(ErrorCodeConstants.SCHOOL_PRICING_NOT_FOUND);
			}
		}
	}
	
	public SchoolPricing read(SchoolPricing schoolPricingEntity) {
		Long stakeHolderId = schoolPricingEntity.getSchoolPricingId();
		SchoolPricing stakeHolder = get(stakeHolderId);
		return stakeHolder;
	}

	public SchoolPricing close(SchoolPricing schoolPricingEntity) {
		Long stakeHolderId = schoolPricingEntity.getSchoolPricingId();
		SchoolPricing existingSchoolPricing = get(stakeHolderId);
		if (existingSchoolPricing.getRecordInUse().equals(RecordInUseType.N)) {
			return existingSchoolPricing;
		}
		existingSchoolPricing.setRecordInUse(RecordInUseType.N);
		existingSchoolPricing.setOperationDateTime(null);
		return schoolPricingRepository.save(existingSchoolPricing);
	}

	public SchoolPricing reInstate(SchoolPricing schoolPricingEntity) {
		Long stakeHolderId = schoolPricingEntity.getSchoolPricingId();
		SchoolPricing existingSchoolPricing = get(stakeHolderId);
		if (existingSchoolPricing.getRecordInUse().equals(RecordInUseType.Y)) {
			return existingSchoolPricing;
		}
		existingSchoolPricing.setRecordInUse(RecordInUseType.Y);
		existingSchoolPricing.setOperationDateTime(null);
		return schoolPricingRepository.save(existingSchoolPricing);
	}

	public Page<SchoolPricing> list(AdminSearchRequest searchRequest, int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<SchoolPricing> stakeHolderList = schoolPricingRepositoryCustom.findSchoolPricings(pageable,
				searchRequest);
		return stakeHolderList;
	}
}
