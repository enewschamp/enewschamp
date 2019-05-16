package com.enewschamp.publication.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.publication.domain.common.LOVProjection;
import com.enewschamp.publication.domain.entity.Genre;

@JaversSpringDataAuditable
interface GenreRepository extends JpaRepository<Genre, String>{ 
	 
	@Query(value="select a.genreId as id, b.text as name "
			+ "from Genre a, MultiLanguageText b "
			+ "where a.nameId=b.multiLanguageTextId")
    public List<LOVProjection> getGenreLOV();
	
	
} 