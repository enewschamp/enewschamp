package com.enewschamp.publication.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.publication.domain.entity.Genre;

public interface GenreRepositoryCustom {
	public Page<Genre> findGenres(Pageable pageable);
}
