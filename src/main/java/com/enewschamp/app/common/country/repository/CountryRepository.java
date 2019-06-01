package com.enewschamp.app.common.country.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.app.common.country.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long>{
	
	@Query("Select c from Country c")
	public List<Country> getAll();
}
