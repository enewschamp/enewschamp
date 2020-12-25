package com.enewschamp.subscription.pricing.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.admin.pricing.repository.IndividualPricingRepositoryCustom;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.pricing.entity.IndividualPricing;
import com.enewschamp.subscription.pricing.repository.IndividualPricingRepository;

@Service
public class IndividualPricingService {

	@Autowired
	private IndividualPricingRepository individualPricingRepository;
	
	@Autowired
	private IndividualPricingRepositoryCustom individualPricingRepositoryCustom;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	private ModelMapper modelMapperForPatch;

	public IndividualPricing create(IndividualPricing IndividualPricingEntity) {
		return individualPricingRepository.save(IndividualPricingEntity);
	}

	public IndividualPricing update(IndividualPricing IndividualPricingEntity) {
		Long IndividualPricingId = IndividualPricingEntity.getIndividualPricingId();
		IndividualPricing existingIndividualPricing = get(IndividualPricingId);
		modelMapper.map(IndividualPricingEntity, existingIndividualPricing);
		return individualPricingRepository.save(existingIndividualPricing);
	}

	public IndividualPricing patch(IndividualPricing IndividualPricing) {
		Long navId = IndividualPricing.getIndividualPricingId();
		IndividualPricing existingEntity = get(navId);
		modelMapperForPatch.map(IndividualPricing, existingEntity);
		return individualPricingRepository.save(existingEntity);
	}

	public void delete(Long IndividualPricingId) {
		individualPricingRepository.deleteById(IndividualPricingId);
	}

	public IndividualPricing get(Long IndividualPricingId) {
		Optional<IndividualPricing> existingEntity = individualPricingRepository.findById(IndividualPricingId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.SCHOOL_PRICING_NOT_FOUND);
		}
	}

	public IndividualPricing getPricingForIndividual(String editionId) {
		Date sysdate = new Date();
		LocalDate localSysDate = sysdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		List<IndividualPricing> individualPrice = individualPricingRepository.getPricingForIndividual(editionId,
				localSysDate);
		if (individualPrice != null && individualPrice.size() > 0) {
			return individualPrice.get(0);
		} else {
			throw new BusinessException(ErrorCodeConstants.INDIVIDUAL_PRICING_NOT_FOUND);
		}
	}
	

	public IndividualPricing read(IndividualPricing individualPricingEntity) {
		Long inPricingId = individualPricingEntity.getIndividualPricingId();
		IndividualPricing existingndividualPricing = get(inPricingId);
		existingndividualPricing.setRecordInUse(RecordInUseType.Y);
		return individualPricingRepository.save(existingndividualPricing);
	}

	public IndividualPricing close(IndividualPricing individualPricingEntity) {
		Long inPricingId = individualPricingEntity.getIndividualPricingId();
		IndividualPricing existingEntity = get(inPricingId);
		existingEntity.setRecordInUse(RecordInUseType.N);
		return individualPricingRepository.save(existingEntity);
	}

	public Page<IndividualPricing> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<IndividualPricing> genreList = individualPricingRepositoryCustom.findIndividualPricings(pageable);
		return genreList;
	}
}
