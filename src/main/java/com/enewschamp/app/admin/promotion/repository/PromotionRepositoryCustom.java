package com.enewschamp.app.admin.promotion.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.schoolchain.entity.SchoolChain;

public interface PromotionRepositoryCustom {
	public Page<Promotion> findPromotions(Pageable pageable);
}
