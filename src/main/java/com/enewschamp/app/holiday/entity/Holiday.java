package com.enewschamp.app.holiday.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "Holidays", uniqueConstraints = { @UniqueConstraint(columnNames = { "editionId", "holidayDate" }) })
@EqualsAndHashCode(callSuper = false)
public class Holiday extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holidays_id_generator")
	@SequenceGenerator(name = "holidays_id_generator", sequenceName = "holidays_id_seq", allocationSize = 1)
	@Column(name = "holidayId", updatable = false, nullable = false)
	private Long holidayId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "holidayDate")
	private LocalDate holidayDate;

	@NotNull
	@Column(name = "holiday", length = 30)
	private String holiday;

	@NotNull
	@Column(name = "publication", length = 1)
	private String publication;

	@NotNull
	@Column(name = "helpdesk", length = 1)
	private String helpdesk;

}
