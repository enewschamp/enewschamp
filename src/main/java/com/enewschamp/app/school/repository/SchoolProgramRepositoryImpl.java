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
				"Select new SchoolProgramLOV(s.schoolId as id,s.name as name,s.schoolProgramCode as schoolProgramCode,c.nameId as city,\"\n"
						+ "			+ \"c.description as cityName,st.nameId as state,st.description as stateName,ct.nameId as country,ct.description as countryName) \"\n"
						+ "			+ \"from School s,City c,State st,Country ct\"\n"
						+ "			+ \" where s.schoolProgram='Y' and s.recordInUse='Y' and s.cityId=c.nameId and s.stateId=st.nameId and s.countryId=ct.nameId");
		List<SchoolProgramLOV> list = query.getResultList();
		return list;
	}

}
