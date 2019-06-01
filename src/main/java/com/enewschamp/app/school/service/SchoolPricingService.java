package com.enewschamp.app.school.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.school.entity.SchoolPricing;
import com.enewschamp.app.school.repository.SchoolPricingRepository;
import com.enewschamp.problem.Fault;
import com.enewschamp.problem.HttpStatusAdapter;

@Service
public class SchoolPricingService {

	@Autowired
	SchoolPricingRepository schoolPricingRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public SchoolPricing create(SchoolPricing SchoolPricingEntity) {
		return schoolPricingRepository.save(SchoolPricingEntity);
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
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.SCHOOL_PRICING_NOT_FOUND,
					"School Pricing not found!");
		}
	}
	
	public SchoolPricing getPricingForInstitution(Long institutionId, String editionId)
	{
		Optional<SchoolPricing> schoolPrice = schoolPricingRepository.getPricingForInstitution(institutionId, editionId);
		if(schoolPrice.isPresent())
		{
			return schoolPrice.get();
		}
		else {
			throw new Fault(new HttpStatusAdapter(HttpStatus.NOT_FOUND), ErrorCodes.SCHOOL_PRICING_NOT_FOUND,
					"School Pricing not found!");
		}
	}
}
