package com.enewschamp.subscription.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enewschamp.subscription.domin.entity.StudentControl;
import com.enewschamp.subscription.domin.entity.StudentControlWork;

public interface StudentControlWorkRepository extends JpaRepository<StudentControlWork, Long>{

	@Query("Select * from StudentControl_Work where eMail = :eMail")
	public StudentControlWork searchByEmail(@Param("eMail")String eMail);
}
