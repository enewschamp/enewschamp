package com.enewschamp.app.holiday.service;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.app.holiday.repository.HolidayRepository;
import com.enewschamp.app.holiday.repository.HolidayRepositoryCustom;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.entity.Genre;

@Service
public class HolidayService {

	@Autowired
	HolidayRepository holidayRepository;
	
	@Autowired
	HolidayRepositoryCustom holidayRepositoryCustom;

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
		System.out.println("date in isHoliday is " + date);

		Optional<Holiday> holiday = holidayRepository.getHoliday(date, editionId);
		System.out.println("Holiday is " + holiday);

		if (holiday.isPresent())
			return true;
		else
			return false;
	}
	
	

	public Holiday read(Holiday holidayEntity) {
		Long holidayId = holidayEntity.getHolidayId();
		Holiday existingHoliday = get(holidayId);
		existingHoliday.setRecordInUse(RecordInUseType.Y);
		return holidayRepository.save(existingHoliday);
	}

	public Holiday close(Holiday holidayEntity) {
		Long holidayId = holidayEntity.getHolidayId();
		Holiday existingEntity = get(holidayId);
		existingEntity.setRecordInUse(RecordInUseType.N);
		return holidayRepository.save(existingEntity);
	}

	public Page<Holiday> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Holiday> genreList = holidayRepositoryCustom.findHolidays(pageable);
		return genreList;
	}

}
