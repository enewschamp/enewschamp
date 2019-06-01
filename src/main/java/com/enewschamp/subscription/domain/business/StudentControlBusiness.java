package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.app.dto.StudentControlDTO;
import com.enewschamp.subscription.app.dto.StudentControlWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentControl;
import com.enewschamp.subscription.domain.entity.StudentControlWork;
import com.enewschamp.subscription.domain.service.StudentControlService;
import com.enewschamp.subscription.domain.service.StudentControlWorkService;

@Service
public class StudentControlBusiness {

	@Autowired
	StudentControlWorkService studentControlWorkService; 
	
	@Autowired
	StudentControlService studentControlService; 
	
	@Autowired
	ModelMapper modelMapper;
	public StudentControlWork saveAsWork(StudentControlWorkDTO studenControlWorkDto)
	{
		
		StudentControlWork studentControlWork =modelMapper.map(studenControlWorkDto,StudentControlWork.class);
		// to be changed
		studentControlWork.setOperatorId("APP");
		
		studentControlWork.setRecordInUse(RecordInUseType.Y);

		studentControlWork = studentControlWorkService.create(studentControlWork);
		return studentControlWork;
	}
	public StudentControl saveAsMaster(StudentControlDTO studenControlDto)
	{
		
		StudentControl studentControl =modelMapper.map(studenControlDto,StudentControl.class);
		// to be changed
		studentControl.setOperatorId("APP");
		
		studentControl.setRecordInUse(RecordInUseType.Y);

		studentControl = studentControlService.create(studentControl);
		return studentControl;
	}
	public void workToMaster(Long studentId)
	{
		StudentControlWork workEntity = studentControlWorkService.get(studentId);
		StudentControl masterEntity = modelMapper.map(workEntity,StudentControl.class);
		studentControlService.create(masterEntity);
	}
	
	public void deleteFromWork(StudentControlWorkDTO studenControlWorkDto)
	{
		studentControlWorkService.delete(studenControlWorkDto.getStudentID());
	}
	
	public boolean isStudentExist(Long studentId)
	{
		return studentControlService.studentExist(studentId);
	}
	
	public StudentControlDTO getStudentFromMaster(String emailId)
	{
		StudentControl studentEntity = 	studentControlService.getStudentByEmail(emailId);
		if(studentEntity!=null) {
		StudentControlDTO studentDto = modelMapper.map(studentEntity,StudentControlDTO.class);
		return studentDto;
		}
		else
			return null;
		
	}
	
	public StudentControlWorkDTO getStudentFromWork(String emailId)
	{
		StudentControlWork studentEntityWork = 	studentControlWorkService.getStudentByEmail(emailId);
		if(studentEntityWork!=null) {
		StudentControlWorkDTO studentDto = modelMapper.map(studentEntityWork,StudentControlWorkDTO.class);
		return studentDto;
		}
		else
			return null;
		
	}
	
	public Long getStudentId(String emailId)
	{
		Long studentId=0L;
		StudentControlDTO studentControlDTO = getStudentFromMaster(emailId);
		if (studentControlDTO != null) {
			studentId = studentControlDTO.getStudentID();
		} else {
			StudentControlWorkDTO studentControlWorkDTO = getStudentFromWork(emailId);
			if(studentControlWorkDTO!=null)
			studentId = studentControlWorkDTO.getStudentID();
		}
		return studentId;
	}
}
