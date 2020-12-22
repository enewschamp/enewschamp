package com.enewschamp.subscription.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentPaymentWork;

public interface StudentPaymentWorkRepository extends JpaRepository<StudentPaymentWork, Long> {

	@Query("Select s from StudentPaymentWork s where s.studentId = :studentId and s.editionId= :editionId")
	public Optional<StudentPaymentWork> getByStudentIdAndEdition(@Param("studentId") Long studentId,
			@Param("editionId") String editionId);


	@Query("Delete from StudentPaymentWork s where s.studentId = :studentId")
	public Optional<StudentPaymentWork> deleteByStudentId(@Param("studentId") Long studentId);

}
