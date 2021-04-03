package com.enewschamp.app.helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.app.helpdesk.entity.Helpdesk;

public interface HelpdeskRepository extends JpaRepository<Helpdesk, Long> {

	@Query(value = "SELECT a FROM Helpdesk a where a.studentId = :studentId and IFNULL(a.closeFlag,'N')<>'Y' and a.recordInUse='Y' order by a.operationDateTime desc")
	public List<Helpdesk> findByStudentId(@Param("studentId") Long studentId);

}