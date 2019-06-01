package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;

@JaversSpringDataAuditable
public interface StudentSubscriptionWorkRepository extends JpaRepository<StudentSubscriptionWork, Long>{

	@Query("Select s from StudentSubscriptionWork s where s.studentID = :studentID and s.editionID= :editionID")
	public Optional<StudentSubscriptionWork> getStudentByIdAndEdition(@Param("studentID") Long studentId, @Param("editionID") String editionID);
}
