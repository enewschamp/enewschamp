package com.enewschamp.subscription.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentPayment;

public interface StudentPaymentRepository extends JpaRepository<StudentPayment, Long> {
	@Query("Select s from StudentPayment s where s.studentId = :studentId and s.editionId= :editionId order by s.operationDateTime desc")
	public List<StudentPayment> getAllByStudentIdAndEdition(@Param("studentId") Long studentId,
			@Param("editionId") String editionId);

	@Query("Select s from StudentPayment s where s.orderId = :orderId")
	public Optional<StudentPayment> getByOrderId(@Param("orderId") String orderId);

}
