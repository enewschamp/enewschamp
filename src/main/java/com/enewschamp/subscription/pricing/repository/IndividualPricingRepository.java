package com.enewschamp.subscription.pricing.repository;

import java.time.LocalDate;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.pricing.entity.IndividualPricing;

@JaversSpringDataAuditable
public interface IndividualPricingRepository extends JpaRepository<IndividualPricing, Long> {

	@Query("Select p from IndividualPricing p where p.effectiveDate=(select max(q.effectiveDate) from IndividualPricing q where q.recordInUse ='Y' and q.editionId= :editionId and q.effectiveDate<= :effectiveDate)")
	public List<IndividualPricing> getPricingForIndividual(@Param("editionId") String editionId,
			@Param("effectiveDate") LocalDate effectiveDate);
}
