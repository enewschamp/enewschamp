package com.enewschamp.app.workinghours.service;


import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.workinghours.entity.WorkingHours;
import com.enewschamp.app.workinghours.repository.WorkingHourRepository;
import com.enewschamp.problem.BusinessException;

@Service
public class WorkingHoursService {

	@Autowired
	WorkingHourRepository WorkingHoursRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public WorkingHours create(WorkingHours WorkingHoursEntiry) {
		return WorkingHoursRepository.save(WorkingHoursEntiry);
	}

	public WorkingHours update(WorkingHours WorkingHoursEntiry) {
		Long WorkingHoursId = WorkingHoursEntiry.getWorkingHoursId();
		WorkingHours existingWorkingHours = get(WorkingHoursId);
		modelMapper.map(WorkingHoursEntiry, existingWorkingHours);
		return WorkingHoursRepository.save(existingWorkingHours);
	}

	public WorkingHours patch(WorkingHours WorkingHours) {
		Long navId = WorkingHours.getWorkingHoursId();
		WorkingHours existingEntity = get(navId);
		modelMapperForPatch.map(WorkingHours, existingEntity);
		return WorkingHoursRepository.save(existingEntity);
	}

	public void delete(Long WorkingHoursId) {
		WorkingHoursRepository.deleteById(WorkingHoursId);
	}

	public WorkingHours get(Long WorkingHoursId) {
		Optional<WorkingHours> existingEntity = WorkingHoursRepository.findById(WorkingHoursId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.WORKING_HOUR_NOT_FOUND);
		}
	}
	
	public WorkingHours getWorkingHours(String editionId)
	{
		Optional<WorkingHours> existingEntity = WorkingHoursRepository.getWorkingHours(editionId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.WORKING_HOUR_NOT_FOUND);
		}
	}
}
