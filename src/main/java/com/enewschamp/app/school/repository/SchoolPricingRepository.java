package com.enewschamp.app.school.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.school.entity.SchoolPricing;

public interface SchoolPricingRepository extends JpaRepository<SchoolPricing, Long>{

	@Query("Select p from SchoolPricing p where p.institutionId= :institutionId and p.editionId= :editionId and p.recordInUse='Y'")
	public Optional<SchoolPricing> getPricingForInstitution(@Param(value="institutionId")Long institutionId, @Param(value="editionId") String editionId);
}
