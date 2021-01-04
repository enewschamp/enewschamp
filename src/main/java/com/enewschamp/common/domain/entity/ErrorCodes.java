package com.enewschamp.common.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ErrorCodes")
public class ErrorCodes extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "error_code_id_generator")
	@SequenceGenerator(name = "error_code_id_generator", sequenceName = "error_code_id_seq", allocationSize = 1)
	@Column(name = "errorCodeId", length = 3)
	private Long errorCodeId = 0L;

	@Column(name = "errorCategory", length = 20)
	private String errorCategory;

	@Column(name = "errorDescription", length = 100)
	private String errorDescription;

	@NotNull
	@Column(name = "errorCode", length = 50)
	private String errorCode;

	@NotNull
	@Column(name = "errorMessage", length = 200)
	private String errorMessage;

}
