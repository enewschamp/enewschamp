package com.enewschamp.app.user.login.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.user.login.entity.UserLogin;
import com.enewschamp.app.user.login.entity.UserType;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

	@Query("Select s from UserLogin s where s.userId= :userId and s.deviceId= :deviceId and s.tokenId=:tokenId and s.userType= :userType and s.loginFlag='Y'")
	public Optional<UserLogin> getLogin(@Param("userId") String emailId, @Param("deviceId") String deviceId,
			@Param("tokenId") String tokenId, @Param("userType") UserType userType);

	@Query("Select s from UserLogin s where s.userId= :userId and s.deviceId= :deviceId and s.tokenId=:tokenId and s.userType= :userType and s.loginFlag='N'")
	public Optional<UserLogin> getNonLogin(@Param("userId") String emailId, @Param("deviceId") String deviceId,
			@Param("tokenId") String tokenId, @Param("userType") UserType userType);

	@Query("Select s from UserLogin s where s.userId= :userId and s.deviceId= :deviceId and s.tokenId=:tokenId and s.userType= :userType")
	public Optional<UserLogin> getDeviceLogin(@Param("userId") String emailId, @Param("deviceId") String deviceId,
			@Param("tokenId") String tokenId, @Param("userType") UserType userType);

	@Query("Select max(s.lastLoginTime) from UserLogin s where s.userId= :userId and s.deviceId= :deviceId and s.userType= :userType")
	public LocalDateTime getLastLoginDate(@Param("userId") String emailId, @Param("deviceId") String deviceId,
			@Param("userType") UserType userType);

	@Query("Select s from UserLogin s where s.userId= :userId and s.deviceId= :deviceId and s.tokenId= :tokenId and s.userType= 'P'")
	public Optional<UserLogin> getOperatorLogin(@Param("userId") String userId, @Param("deviceId") String deviceId,
			@Param("tokenId") String tokenId);
	
	@Query("Select s from UserLogin s where s.userId= :userId and s.deviceId= :deviceId and s.tokenId= :tokenId and s.userType= :userType")
	public Optional<UserLogin> getOperatorLogin(@Param("userId") String userId, @Param("deviceId") String deviceId,
			@Param("tokenId") String tokenId, @Param("userType") UserType userType);


	@Query("Select s from UserLogin s where s.userId= :userId and s.userType= 'P'")
	public Optional<UserLogin> getOperatorLogin(@Param("userId") String userId);

	@Query("Select s from UserLogin s where s.deviceId= :deviceId and s.userType= 'S'")
	public Optional<UserLogin> getStudentLoginByDeviceId(@Param("deviceId") String deviceId);

	@Query("Select s from UserLogin s where s.deviceId= :deviceId and s.tokenId=:tokenId and s.userType= 'S' and s.operationDateTime=(Select max(p.operationDateTime) from UserLogin p where p.deviceId= :deviceId and p.tokenId=:tokenId and p.userType= 'S')")
	public Optional<UserLogin> getDeviceLogin(@Param("deviceId") String deviceId, @Param("tokenId") String tokenId);
	
	@Query("Select s from UserLogin s where s.deviceId= :deviceId and s.tokenId=:tokenId and s.userType= 'P' and s.operationDateTime=(Select max(p.operationDateTime) from UserLogin p where p.deviceId= :deviceId and p.tokenId=:tokenId and p.userType= 'P')")
	public Optional<UserLogin> getBODeviceLogin(@Param("deviceId") String deviceId, @Param("tokenId") String tokenId);

	@Query("Select s from UserLogin s where s.userId= :userId and s.userType= :userType")
	public Optional<UserLogin> getOperatorLogin(@Param("userId") String userId, @Param("userType") UserType userType);
}