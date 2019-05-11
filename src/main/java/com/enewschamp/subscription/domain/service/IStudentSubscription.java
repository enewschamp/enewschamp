package com.enewschamp.subscription.domain.service;

import java.io.Serializable;

import com.enewschamp.subscription.domin.entity.StudentSubscription;

public interface IStudentSubscription extends Serializable {

	public StudentSubscription create(StudentSubscription ss);
	public StudentSubscription update(StudentSubscription ss);
	public void delete(Long studenId, Long editionId);
	public StudentSubscription get(Long studenId, Long editionId);
	public String getAudit(Long studenId, Long editionId) ;

}
