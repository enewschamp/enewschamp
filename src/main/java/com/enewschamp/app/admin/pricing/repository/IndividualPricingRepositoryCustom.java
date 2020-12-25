package com.enewschamp.app.admin.pricing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.subscription.pricing.entity.IndividualPricing;

public interface IndividualPricingRepositoryCustom {
	public Page<IndividualPricing> findIndividualPricings(Pageable pageable);
}
