package com.enewschamp.app.student.login.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.ErrorCodes;
import com.enewschamp.app.student.login.entity.StudentLogin;
import com.enewschamp.app.student.login.repository.StudentLoginRepository;
import com.enewschamp.domain.service.AbstractDomainService;
import com.enewschamp.problem.BusinessException;

@Service
public class StudentLoginService extends AbstractDomainService {

	@Autowired
	StudentLoginRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	@Qualifier("modelPatcher")
	ModelMapper modelMapperForPatch;
	
	public StudentLogin create(StudentLogin studentLogin) {
		
		return repository.save(studentLogin);
	}
	
	public StudentLogin update(StudentLogin studentLogin) {
		Long StudentLoginId = studentLogin.getStudentLoginId();
		StudentLogin existingStudentLogin = get(StudentLoginId);
		modelMapper.map(studentLogin, existingStudentLogin);
		return repository.save(existingStudentLogin);
	}
	
	public StudentLogin patch(StudentLogin studentLogin) {
		Long StudentLoginId = studentLogin.getStudentLoginId();
		StudentLogin existingEntity = get(StudentLoginId);
		modelMapperForPatch.map(studentLogin, existingEntity);
		return repository.save(existingEntity);
	}
	
	public void delete(Long StudentLoginId) {
		repository.deleteById(StudentLoginId);
	}
	
	public StudentLogin get(Long StudentLoginId) {
		Optional<StudentLogin> existingEntity = repository.findById(StudentLoginId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			throw new BusinessException(ErrorCodes.STUD_LOGIN_NOT_FOUND, String.valueOf(StudentLoginId));
		}
	}
	
	public StudentLogin getStudenLogin(final String emailId, final String deviceId) {
		Optional<StudentLogin> existingEntity = repository.getLogin(emailId, deviceId);
		if (existingEntity.isPresent()) {
			return existingEntity.get();
		} else {
			return null;
			//throw new BusinessException(ErrorCodes.STUD_LOGIN_NOT_FOUND, emailId);
		}
	}
	
}
