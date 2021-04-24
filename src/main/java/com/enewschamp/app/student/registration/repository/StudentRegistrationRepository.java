package com.enewschamp.app.student.registration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.registration.entity.StudentRegistration;

public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Long> {

	@Query("Select s from  StudentRegistration s where s.emailId= :emailId")
	public Optional<StudentRegistration> getStudent(@Param("emailId") String emailId);

	@Query("Select s.emailId from  StudentRegistration s where s.studentKey= :studentKey")
	public String getStudentEmailByKey(@Param("studentKey") String studentKey);

}