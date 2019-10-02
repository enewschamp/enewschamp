package com.enewschamp.app.student.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.student.login.entity.StudentLogin;

public interface StudentLoginRepository extends JpaRepository<StudentLogin, Long>{

	
	@Query("Select s from StudentLogin s where s.emailId= :emailId and s.deviceId= :deviceId")
	public Optional<StudentLogin> getLogin(@Param("emailId") String emailId, @Param("deviceId") String deviceId);
	
}
