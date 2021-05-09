package com.enewschamp.user.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentPayment;
import com.enewschamp.user.domain.entity.StudentRefund;

@JaversSpringDataAuditable
public interface StudentRefundRepository extends JpaRepository<StudentRefund, Long> {

	@Query("Select s from StudentRefund s where s.paytmStatus ='PENDING' order by s.operationDateTime desc")
	public List<StudentRefund> getPendingRefundList();

	@Query("Select s from StudentRefund s where s.studentId = :studentId and s.editionId= :editionId order by s.operationDateTime desc")
	public List<StudentRefund> getAllByStudentIdAndEdition(@Param("studentId") Long studentId,
			@Param("editionId") String editionId);
}
