package com.enewschamp.app.admin.schoolsubscription.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enewschamp.app.admin.AdminSearchRequest;
import com.enewschamp.app.admin.schoolsubscription.entity.SchoolSubscriptionGrade;

public interface SchoolSubscriptionGradeRepositoryCustom {
	public Page<SchoolSubscriptionGrade> findSchoolSubscriptionGrades(Pageable pageable,
			AdminSearchRequest searchRequest);
}
