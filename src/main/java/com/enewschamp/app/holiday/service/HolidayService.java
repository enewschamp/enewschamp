package com.enewschamp.app.holiday.service;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.app.holiday.repository.HolidayRepository;
import com.enewschamp.problem.BusinessException;

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
			throw new BusinessException(ErrorCodes.HOLIDAY_NOT_FOUND);
		}
	}
	
	public boolean isHoliday(LocalDate date, String editionId)
	{
		System.out.println("date in isHoliday is "+date);

		Optional<Holiday> holiday = holidayRepository.getHoliday(date, editionId);
		System.out.println("Holiday is "+holiday);
		
		if(holiday.isPresent())
			return true;
		else 
			return false;
	}
	
}