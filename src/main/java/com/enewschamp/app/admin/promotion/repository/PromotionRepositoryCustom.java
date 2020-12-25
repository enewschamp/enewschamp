package com.enewschamp.app.admin.promotion.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PromotionRepositoryCustom {
	public Page<Promotion> findPromotions(Pageable pageable);
}
