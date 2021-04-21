package com.enewschamp.app.holiday.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.holiday.repository.HolidayRepositoryCustomImpl;
import com.enewschamp.app.common.ErrorCodeConstants;
import com.enewschamp.app.holiday.entity.Holiday;
import com.enewschamp.app.holiday.repository.HolidayRepository;
import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.problem.BusinessException;
import com.enewschamp.publication.domain.common.HolidayList;

@Service
public class HolidayService {

	@Autowired
	HolidayRepository holidayRepository;

	@Autowired
	HolidayRepositoryCustomImpl holidayRepositoryCustom;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;

	public Holiday create(Holiday holidayEntity) {
		Holiday holiday = null;
		try {
			holiday = holidayRepository.save(holidayEntity);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_EXIST);
		}
		return holiday;
	}

	public Holiday update(Holiday holidayEntity) {
		Long holidayId = holidayEntity.getHolidayId();
		Holiday existingHoliday = get(holidayId);
		if (existingHoliday.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
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

	public Holiday read(Holiday holiday) {
		Long holidayId = holiday.getHolidayId();
		Holiday holidayEntity = get(holidayId);
		return holidayEntity;
	}

	public Holiday close(Holiday holidayEntity) {
		Long holidayId = holidayEntity.getHolidayId();
		Holiday existingEntity = get(holidayId);
		if (existingEntity.getRecordInUse().equals(RecordInUseType.N)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_CLOSED);
		}
		existingEntity.setRecordInUse(RecordInUseType.N);
		existingEntity.setOperationDateTime(null);
		return holidayRepository.save(existingEntity);
	}

	public Holiday reinstate(Holiday holdayEntity) {
		Long holidayId = holdayEntity.getHolidayId();
		Holiday existingHoliday = get(holidayId);
		if (existingHoliday.getRecordInUse().equals(RecordInUseType.Y)) {
			throw new BusinessException(ErrorCodeConstants.RECORD_ALREADY_OPENED);
		}
		existingHoliday.setRecordInUse(RecordInUseType.Y);
		existingHoliday.setOperationDateTime(null);
		return holidayRepository.save(existingHoliday);
	}

	public Page<Holiday> list(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of((pageNo - 1), pageSize);
		Page<Holiday> holidayList = holidayRepositoryCustom.findAll(pageable, null);
		if (holidayList.getContent().isEmpty()) {
			throw new BusinessException(ErrorCodeConstants.NO_RECORD_FOUND);
		}
		return holidayList;
	}
}