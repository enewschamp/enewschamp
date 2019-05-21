package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;
import com.enewschamp.subscription.domin.entity.StudentControl;
import com.enewschamp.subscription.domin.entity.StudentControlWork;

@Service
public class StudentControlBusiness {

	@Autowired
	StudentControlWorkService studentControlWorkService; 
	
	@Autowired
	StudentControlService studentControlService; 
	
	@Autowired
	ModelMapper mapper;
	public void saveAsWork(StudentControlWorkDTO studenControlWorkDto)
	{
		StudentControlWork studentControlWork =mapper.map(studenControlWorkDto,StudentControlWork.class);
		studentControlWorkService.create(studentControlWork);
	}
	
	public void workToMaster(Long studentId)
	{
		StudentControlWork workEntity = studentControlWorkService.get(studentId);
		StudentControl masterEntity = mapper.map(workEntity,StudentControl.class);
		studentControlService.create(masterEntity);
	}
	
	public void deleteFromWork(StudentControlWorkDTO studenControlWorkDto)
	{
		studentControlWorkService.delete(studenControlWorkDto.getStudentID());
	}
}
