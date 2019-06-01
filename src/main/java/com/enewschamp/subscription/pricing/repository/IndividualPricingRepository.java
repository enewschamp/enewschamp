package com.enewschamp.subscription.pricing.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.pricing.entity.IndividualPricing;

public interface IndividualPricingRepository extends JpaRepository<IndividualPricing, Long>{

	@Query("Select p from IndividualPricing p where p.editionId= :editionId and p.effectiveDate>= :effectiveDate and p.recordInUse ='Y'")
	public Optional<IndividualPricing> getPricingForIndividual(@Param("editionId") String editionId, @Param("effectiveDate") LocalDate effectiveDate);
}
