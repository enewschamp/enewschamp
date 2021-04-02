package com.enewschamp.subscription.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.app.common.LocalDateCryptoConverter;
import com.enewschamp.app.common.StringCryptoConverter;
import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentDetails")
public class StudentDetails extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId = 0L;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "Name", length = 50)
	private String name;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "Surname", length = 50)
	private String surname;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "OtherNames", length = 100)
	private String otherNames;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "gender", length = 50)
	private String gender;

	@Convert(converter = LocalDateCryptoConverter.class)
	@Column(name = "DoB")
	private LocalDate doB;

	@Convert(converter = StringCryptoConverter.class)
	@Column(name = "MobileNumber", length = 100)
	private String mobileNumber;

	@Column(name = "approvalRequired", length = 1)
	private String approvalRequired;

}
