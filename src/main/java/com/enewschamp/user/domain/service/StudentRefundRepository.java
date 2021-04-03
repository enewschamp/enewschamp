package com.enewschamp.user.domain.service;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.enewschamp.user.domain.entity.StudentRefund;

@JaversSpringDataAuditable
interface StudentRefundRepository extends JpaRepository<StudentRefund, Long> {

	@Query("Select s from StudentRefund s where s.paytmStatus ='PENDING' order by s.operationDateTime desc")
	public List<StudentRefund> getPendingRefundList();
}
