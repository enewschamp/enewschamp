package com.enewschamp.master.badge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.publication.domain.entity.Badge;

public interface BadgeRepositoryCustom {
	public Page<Badge> findBadges(Pageable pageable);
}
