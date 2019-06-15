package com.enewschamp.subscription.pricing.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.subscription.pricing.entity.IndividualPricing;
import com.enewschamp.subscription.pricing.repository.IndividualPricingRepository;

@Service
public class IndividualPricingService {

	@Autowired
	IndividualPricingRepository individualPricingRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
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
			throw new BusinessException(ErrorCodes.SCHOOL_PRICING_NOT_FOUND);
		}
	}
	
	public IndividualPricing getPricingForIndividual(String editionId)
	{
		Date sysdate = new Date();
		LocalDate localSysDate = sysdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Optional<IndividualPricing> individualPrice = individualPricingRepository.getPricingForIndividual( editionId, localSysDate);
		if(individualPrice.isPresent())
		{
			return individualPrice.get();
		}
		else {
			throw new BusinessException(ErrorCodes.INDIVIDUAL_PRICING_NOT_FOUND);
		}
	}
}
