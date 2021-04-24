package com.enewschamp.publication.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.app.admin.dashboard.handler.GenreView;
import com.enewschamp.publication.domain.common.GenreList;
import com.enewschamp.publication.domain.common.LOVProjection;
import com.enewschamp.publication.domain.entity.Genre;

@JaversSpringDataAuditable
interface GenreRepository extends JpaRepository<Genre, Long> {

	@Query(value = "select a.nameId as id, a.nameId as name from Genre a")
	public List<LOVProjection> getGenreLOV();

	@Query(value = "select a.genreId as genreId, a.nameId as name,a.imageName as image from Genre a")
	public List<GenreList> getGenreList();

	public List<GenreView> findAllProjectedBy();
}