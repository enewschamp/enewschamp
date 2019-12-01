package com.enewschamp.subscription.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentPaymentWork;

public interface StudentPaymentWorkRepository extends JpaRepository<StudentPaymentWork, Long> {

	@Query("Select s from StudentPaymentWork s where s.studentID = :studentID and s.editionID= :editionID")
	public Optional<StudentPaymentWork> getByStudentIdAndEdition(@Param("studentID") Long studentId,
			@Param("editionID") String editionID);

}
