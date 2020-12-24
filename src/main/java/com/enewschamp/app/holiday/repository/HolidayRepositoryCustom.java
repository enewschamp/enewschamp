package com.enewschamp.app.holiday.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.holiday.entity.Holiday;

public interface HolidayRepositoryCustom {
	public Page<Holiday> findHolidays(Pageable pageable);
}
