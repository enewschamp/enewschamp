package com.enewschamp.app.user.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long>{

	
	@Query("Select s from UserLogin s where s.userId= :userId and s.deviceId= :deviceId and s.userType= :userType")
	public Optional<UserLogin> getLogin(@Param("userId") String emailId, @Param("deviceId") String deviceId, @Param("userType") UserType userType);
	
}
