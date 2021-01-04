package com.enewschamp.app.holiday.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.app.holiday.repository.HolidayRepository;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.common.HolidayList;

@Service
public class HolidayService {

	@Autowired
	HolidayRepository holidayRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public Holiday create(Holiday holidayEntity) {
		return holidayRepository.save(holidayEntity);
	}

	public Holiday update(Holiday holidayEntity) {
		Long holidayId = holidayEntity.getHolidayId();
		Holiday existingHoliday = get(holidayId);
		modelMapper.map(holidayEntity, existingHoliday);
		return holidayRepository.save(existingHoliday);
	}

	public Holiday patch(Holiday Holiday) {
		Long navId = Holiday.getHolidayId();
		Holiday existingEntity = get(navId);
		modelMapperForPatch.map(Holiday, existingEntity);
		return holidayRepository.save(existingEntity);
	}

	public void delete(Long holidayId) {
		holidayRepository.deleteById(holidayId);
	}

	public Holiday get(Long holidayId) {
		Optional<Holiday> existingEntity = holidayRepository.findById(holidayId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodeConstants.HOLIDAY_NOT_FOUND);
		}
	}

	public boolean isHoliday(LocalDate date, String editionId) {
		Optional<Holiday> holiday = holidayRepository.getHoliday(date, editionId);
		if (holiday.isPresent())
			return true;
		else
			return false;
	}

	public List<HolidayList> getHolidayList() {
		return holidayRepository.getHolidayList();
	}

}
