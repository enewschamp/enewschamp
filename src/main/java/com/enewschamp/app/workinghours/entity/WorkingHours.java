package com.enewschamp.app.workinghours.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="WorkingHours")
public class WorkingHours extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workingHours_id_generator")
	@SequenceGenerator(name="workingHours_id_generator", sequenceName = "workingHours_id_seq", allocationSize=1)
	@Column(name = "workingHoursId", updatable = false, nullable = false)
	private Long workingHoursId;
	
	@NotNull
	@Column(name = "editionId", length=6)
	private String editionId;

	@NotNull
	@Column(name = "timeZone", length=10)
	private Long timeZone;
	
	@NotNull
	@Column(name = "startTime", length=4)
	private Long startTime;
	
	@NotNull
	@Column(name = "endTime", length=4)
	private Long endTime;
	
}
