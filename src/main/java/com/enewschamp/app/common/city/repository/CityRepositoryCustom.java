package com.enewschamp.app.common.city.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.common.city.entity.City;

public interface CityRepositoryCustom {
	public Page<City> findCities(AdminSearchRequest searchRequest, Pageable pageable);
}
