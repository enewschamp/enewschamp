package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.subscription.domin.entity.StudentSubscriptionWork;

@JaversSpringDataAuditable
public interface StudentSubscriptionWorkRepository extends JpaRepository<StudentSubscriptionWork, Long>{

	@Query("Select * from StudentSubscrition_work where studentId= :studentId and EditionID = :edition")
	public Optional<StudentSubscriptionWork> getByStudentAndEdition(Long studentId, String edition);
	
	@Query("Select * from StudentSubscrition_work where emailid= :eMailId")
	public Optional<StudentSubscriptionWork> getStudentByEmail(String eMailId);
	
	
}
