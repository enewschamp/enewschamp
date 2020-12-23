package com.enewschamp.app.common.country.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.common.country.entity.Country;

public interface CountryRepositoryCustom {
	public Page<Country> findCountries(Pageable pageable);
}
