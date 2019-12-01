package com.enewschamp.subscription.domain.business;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.domain.common.RecordInUseType;
import com.enewschamp.subscription.app.dto.StudentDetailsDTO;
import com.enewschamp.subscription.app.dto.StudentDetailsWorkDTO;
import com.enewschamp.subscription.domain.entity.StudentDetails;
import com.enewschamp.subscription.domain.entity.StudentDetailsWork;
import com.enewschamp.subscription.domain.service.StudentDetailsService;
import com.enewschamp.subscription.domain.service.StudentDetailsWorkService;

@Service
public class StudentDetailsBusiness {

	@Autowired
	StudentDetailsWorkService studentDetailsWorkService; 

	
	@Autowired
	StudentDetailsService studentDetailsService; 
	
	@Autowired
	ModelMapper modelMapper;
	public StudentDetailsWork saveAsWork(StudentDetailsWorkDTO studentDetailsWorkDTO)
	{
		
		StudentDetailsWork studentDetailsWork =modelMapper.map(studentDetailsWorkDTO,StudentDetailsWork.class);
		// to be changed
		studentDetailsWork.setOperatorId("APP");
		
		studentDetailsWork.setRecordInUse(RecordInUseType.Y);

		studentDetailsWork = studentDetailsWorkService.create(studentDetailsWork);
		return studentDetailsWork;
	}
	
	public void workToMaster(Long studentId)
	{
		StudentDetailsWork workEntity = studentDetailsWorkService.get(studentId);
		StudentDetails masterEntity = modelMapper.map(workEntity,StudentDetails.class);
		studentDetailsService.create(masterEntity);
	}
	
	public StudentDetails saveAsMaster(StudentDetailsDTO studentDetailsDTO)
	{
		
		StudentDetails studentEntity = modelMapper.map(studentDetailsDTO, StudentDetails.class);
		studentEntity.setOperatorId("APP");
		studentEntity.setRecordInUse(RecordInUseType.Y);
		studentDetailsService.create(studentEntity);
		return studentEntity;
	}

	public StudentDetailsWorkDTO getStudentDetailsFromWork(Long studentId) {
		StudentDetailsWorkDTO studentDetailsWorkDTO = null;
		StudentDetailsWork studentDetailsWork = studentDetailsWorkService.get(studentId);
		if (studentDetailsWork != null) {
			studentDetailsWorkDTO = modelMapper.map(studentDetailsWork, StudentDetailsWorkDTO.class);
		}
		return studentDetailsWorkDTO;
	}

	public StudentDetailsDTO getStudentDetailsFromMaster(Long studentId) {
		StudentDetailsDTO studentDetailsDTO = null;
		StudentDetails studentDetails = studentDetailsService.get(studentId);
		if (studentDetails != null) {
			studentDetailsDTO = modelMapper.map(studentDetails, StudentDetailsDTO.class);
		}
		return studentDetailsDTO;
	}
	
	/*public void deleteFromWork(StudentDetailsWorkDTO studentDetailsWorkDTO)
	{
		studentDetailsWorkService.delete(studentDetailsWorkDTO.getStudentID());
	}*/
	
	/*public boolean isStudentExist(Long studentId)
	{
		return studentDetailsService.studentExist(studentId);
	}*/
	
	/*public StudentControlDTO getStudentFromMaster(String emailId)
	{
		StudentControl studentEntity = 	studentDetailsService.getStudentByEmail(emailId);
		if(studentEntity!=null) {
		StudentControlDTO studentDto = modelMapper.map(studentEntity,StudentControlDTO.class);
		return studentDto;
		}
		else
			return null;
		
	}*/
	
	/*public StudentControlWorkDTO getStudentFromWork(String emailId)
	{
		StudentControlWork studentEntityWork = 	studentControlWorkService.getStudentByEmail(emailId);
		StudentControlWorkDTO studentDto = modelMapper.map(studentEntityWork,StudentControlWorkDTO.class);
		return studentDto;
		
	}*/
}
