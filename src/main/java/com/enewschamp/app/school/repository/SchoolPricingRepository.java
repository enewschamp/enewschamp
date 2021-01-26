package com.enewschamp.app.school.repository;

import java.time.LocalDate;
import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.school.entity.SchoolPricing;

@JaversSpringDataAuditable
public interface SchoolPricingRepository extends JpaRepository<SchoolPricing, Long> {

	@Query("Select q from SchoolPricing q where q.institutionId= :institutionId and q.institutionType=:institutionType and q.effectiveDate=(Select max(p.effectiveDate) from SchoolPricing p where p.recordInUse='Y' and p.institutionId= :institutionId and p.institutionType=:institutionType and p.editionId= :editionId and p.effectiveDate<= :effectiveDate)")
	public List<SchoolPricing> getPricingForInstitution(@Param(value = "institutionId") Long institutionId,
			@Param(value = "editionId") String editionId, @Param("institutionType") String institutionType,
			@Param("effectiveDate") LocalDate effectiveDate);
}
