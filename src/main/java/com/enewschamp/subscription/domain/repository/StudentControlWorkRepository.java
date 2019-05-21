package com.enewschamp.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domain.entity.StudentControlWork;

public interface StudentControlWorkRepository extends JpaRepository<StudentControlWork, Long>{

	@Query("Select c from StudentControlWork c where c.eMail = :eMail")
	public StudentControlWork searchByEmail(@Param("eMail")String eMail);
}
