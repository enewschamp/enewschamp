package com.enewschamp.subscription.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentPaymentWork;

public interface StudentPaymentWorkRepository extends JpaRepository<StudentPaymentWork, Long> {

	@Query("Select s from StudentPaymentWork s where s.studentId = :studentId and s.editionId= :editionId order by s.operationDateTime desc")
	public List<StudentPaymentWork> getByStudentIdAndEdition(@Param("studentId") Long studentId,
			@Param("editionId") String editionId);

	@Query("Select s from StudentPaymentWork s where s.studentId = :studentId and s.editionId= :editionId and s.paytmStatus='TXN_SUCCESS' order by s.operationDateTime desc")
	public List<StudentPaymentWork> getSuccessTransactionByStudentIdAndEdition(@Param("studentId") Long studentId,
			@Param("editionId") String editionId);

	@Query("Select s from StudentPaymentWork s where s.orderId = :orderId")
	public Optional<StudentPaymentWork> getByOrderId(@Param("orderId") String orderId);

	@Query("Delete from StudentPaymentWork s where s.studentId = :studentId")
	public Optional<StudentPaymentWork> deleteByStudentId(@Param("studentId") Long studentId);

}
