package com.enewschamp.app.admin.promotion.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.promotion.repository.Promotion;
import com.enewschamp.app.admin.promotion.repository.PromotionRepository;
import com.enewschamp.app.admin.promotion.repository.PromotionRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class PromotionService {

	@Autowired
	private PromotionRepository repository;

	@Autowired
	private PromotionRepositoryCustomImpl repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public Promotion create(Promotion promotionEntity) {
		Promotion promotion = null;
		try {
			promotion = repository.save(promotionEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return promotion;
	}

	public Promotion update(Promotion promotionEntity) {
		Long promotionId = promotionEntity.getPromotionId();
		Promotion existingPromotion = get(promotionId);
		if (existingPromotion.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		modelMapper.map(promotionEntity, existingPromotion);
		return repository.save(existingPromotion);
	}

	public Promotion get(Long promotionId) {
		Optional<Promotion> existingEntity = repository.findById(promotionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.PROMOTION_NOT_FOUND);
		}
	}

	public Promotion read(Promotion promotion) {
		Long promotionId = promotion.getPromotionId();
		Promotion promotionEntity = get(promotionId);
		return promotionEntity;

	}

	public Promotion close(Promotion promotionEntity) {
		Long promotionId = promotionEntity.getPromotionId();
		Promotion existingEntity = get(promotionId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return repository.save(existingEntity);
	}

	public Promotion reinstate(Promotion promtion) {
		Long promotionId = promtion.getPromotionId();
		Promotion existingPromotion = get(promotionId);
		if (existingPromotion.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingPromotion.setRecordInUse(RecordInUseType.Y);
		existingPromotion.setOperationDateTime(null);
		return repository.save(existingPromotion);
	}

	public Page<Promotion> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Promotion> promotionList = repositoryCustom.findAll(pageable, null);
		return promotionList;
	}
}
