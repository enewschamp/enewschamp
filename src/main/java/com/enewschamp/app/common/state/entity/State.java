package com.enewschamp.app.common.state.entity;

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
@Table(name = "State",uniqueConstraints = {
		@UniqueConstraint(columnNames = { "countryId", "nameId" }) })
@EqualsAndHashCode(callSuper = false)
public class State extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_id_generator")
	@SequenceGenerator(name = "state_id_generator", sequenceName = "state_id_seq", allocationSize = 1)
	@Column(name = "stateId", updatable = false, nullable = false)
	private Long stateId;

	@NotNull
	@Column(name = "countryId", length = 2)
	private String countryId;

	@NotNull
	@Column(name = "nameId", length = 50)
	private String nameId;

	@NotNull
	@Column(name = "description", length = 50)
	private String description;

}
