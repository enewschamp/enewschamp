package com.enewschamp.app.admin.institution.entity;

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
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "InstitutionAddress")
public class InstitutionAddress extends BaseEntity {

	private static final long serialVersionUID = -6268188630471167106L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "institution_id_generator")
	@SequenceGenerator(name = "institution_id_generator", sequenceName = "institution_id_seq", allocationSize = 1)
	@Column(name = "institutionId", updatable = false, nullable = false)
	private Long institutionId;

	@NotNull
	@Column(name = "institutionType", length = 1)
	private String institutionType;

	@NotNull
	@Column(name = "addressType", length = 3)
	private String addressType;

	@NotNull
	@Column(name = "stateId", length = 3)
	private String stateId;

	@NotNull
	@Column(name = "countryId", length = 2)
	private String countryId;

	@NotNull
	@Column(name = "cityId", length = 50)
	private String cityId;

	@Column(name = "address", length = 399)
	private String address;

	@Column(name = "pinCode", length = 10)
	private String pinCode;
	
	@Column(name = "landLine1", length = 20)
	private String landLine1;
	
	@Column(name = "landLine2", length = 20)
	private String landLine2;
	
	@Column(name = "landLine3", length = 20)
	private String landLine3;
	
	@Column(name = "mobile1", length = 20)
	private String mobile1;
	
	@Column(name = "mobile2", length = 20)
	private String mobile2;
	
	@Column(name = "mobile3", length = 20)
	private String mobile3;
	
	@Column(name = "email1", length = 99)
	private String email1;
	
	@Column(name = "email2", length = 99)
	private String email2;
	
	@Column(name = "email3", length = 99)
	private String email3;
	
	@Column(name = "comments", length = 999)
	private String comments;
}
