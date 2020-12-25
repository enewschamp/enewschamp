package com.enewschamp.app.admin.promotion.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.promotion.repository.Promotion;
import com.enewschamp.app.admin.promotion.repository.PromotionRepository;
import com.enewschamp.app.admin.promotion.repository.PromotionRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;

@Service
public class PromotionService {

	@Autowired
	private PromotionRepository repository;

	@Autowired
	private PromotionRepositoryCustom repositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public Promotion create(Promotion promotionEntity) {
		return repository.save(promotionEntity);
	}

	public Promotion update(Promotion promotionEntity) {
		Long promotionId = promotionEntity.getPromotionId();
		Promotion existingHoliday = get(promotionId);
		modelMapper.map(promotionEntity, existingHoliday);
		return repository.save(existingHoliday);
	}

	public Promotion get(Long promotionId) {
		Optional<Promotion> existingEntity = repository.findById(promotionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.HOLIDAY_NOT_FOUND);
		}
	}

	public Promotion read(Promotion promotionEntity) {
		Long promotionId = promotionEntity.getPromotionId();
		Promotion existingPromotion = get(promotionId);
		existingPromotion.setRecordInUse(RecordInUseType.Y);
		return repository.save(existingPromotion);
	}

	public Promotion close(Promotion promotionEntity) {
		Long promotionId = promotionEntity.getPromotionId();
		Promotion existingEntity = get(promotionId);
		existingEntity.setRecordInUse(RecordInUseType.N);
		return repository.save(existingEntity);
	}

	public Page<Promotion> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Promotion> genreList = repositoryCustom.findPromotions(pageable);
		return genreList;
	}
}
