package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;

@JaversSpringDataAuditable
public interface StudentSubscriptionWorkRepository extends JpaRepository<StudentSubscriptionWork, Long>{

	@Query("Select s from StudentSubscriptionWork s where s.studentID= :studentId and s.editionID = :edition")
	public Optional<StudentSubscriptionWork> getByStudentAndEdition(Long studentId, String edition);
	
	/*@Query("Select ss from StudentSubscriptionWork ss where ss.emailid= :eMailId")
	public Optional<StudentSubscriptionWork> getStudentByEmail(String eMailId);
	*/
	
}
