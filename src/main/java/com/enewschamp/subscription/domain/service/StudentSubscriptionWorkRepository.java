package com.enewschamp.subscription.domain.service;

import java.util.Optional;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentSubscriptionWork;

@JaversSpringDataAuditable
public interface StudentSubscriptionWorkRepository extends JpaRepository<StudentSubscriptionWork, Long> {

	@Query("Select s from StudentSubscriptionWork s where s.studentId = :studentId and s.editionId= :editionId")
	public Optional<StudentSubscriptionWork> getStudentByIdAndEdition(@Param("studentId") Long studentId,
			@Param("editionId") String editionId);
}
