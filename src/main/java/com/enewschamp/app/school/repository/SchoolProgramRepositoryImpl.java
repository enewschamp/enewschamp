package com.enewschamp.app.school.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.enewschamp.subscription.app.dto.SchoolProgramLOV;

@Repository
public class SchoolProgramRepositoryImpl implements SchoolProgramRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<SchoolProgramLOV> getSchoolsForProgramCode() {
		Query query = entityManager.createNativeQuery(
				"select s.school_id as id,s.name as name,s.school_program_code as school_program_code,c.name_id as city,c.description as city_name,st.name_id as state,st.description as state_name,ct.name_id as country,ct.description as country_name from school s,city c,State st,country ct where s.school_program='Y' and s.record_in_use='Y' and s.city_id=c.name_id and s.state_id=st.name_id and s.country_id=ct.name_id",
				SchoolProgramLOV.class);
		List<SchoolProgramLOV> list = query.getResultList();
		return list;
	}

}
