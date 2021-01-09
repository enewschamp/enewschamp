package com.enewschamp.app.admin.schoolpricing.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enewschamp.app.school.entity.SchoolPricing;

@JaversSpringDataAuditable
public interface SchoolPricingRepository extends JpaRepository<SchoolPricing, Long> {
}
