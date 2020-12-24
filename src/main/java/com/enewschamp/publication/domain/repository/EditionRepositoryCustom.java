package com.enewschamp.publication.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.publication.domain.entity.Edition;

public interface EditionRepositoryCustom {
	public Page<Edition> findEditions(Pageable pageable);
}
