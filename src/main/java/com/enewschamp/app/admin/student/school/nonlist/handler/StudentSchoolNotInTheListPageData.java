package com.enewschamp.app.admin.student.school.nonlist.handler;

import java.time.LocalDateTime;

import com.enewschamp.app.common.PageData;
import com.enewschamp.domain.common.RecordInUseType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentSchoolNotInTheListPageData extends PageData {
	private static final long serialVersionUID = 1L;
	private StateNilDTO state;
	private CityNilDTO city;
	private SchoolNilDTO school;
	private StudentSchoolNilDTO studentSchool;
	
	@JsonProperty
	public void setOperatorId(String operatorId) {
		 this.operatorId = operatorId;
	}
	@JsonIgnore
	public String getOperatorId() {
		return this.operatorId;
	}
	
	@JsonIgnore
	public LocalDateTime getLastUpdate() {
		return this.lastUpdate;
	}
	
	@JsonIgnore
	public RecordInUseType getRecordInUse() {
		return this.recordInUse;
	}
}