package com.enewschamp.subscription.domain.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentSubscriptionHistory;

public interface StudentSubscriptionHistoryRepository extends JpaRepository<StudentSubscriptionHistory, Long> {

	@Query("Select s from StudentSubscriptionHistory s where s.studentId = :studentId and s.editionId= :editionId order by s.operationDateTime desc")
	public List<StudentSubscriptionHistory> getAllByStudentIdAndEdition(@Param("studentId") Long studentId,
			@Param("editionId") String editionId);
}
