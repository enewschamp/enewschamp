package com.enewschamp.subscription.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentDetails", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "name", "surname", "dob" }) })
public class StudentDetails extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "StudentId", length = 10)
	private Long studentId = 0L;

	@Column(name = "Name", length = 50)
	private String name;

	@Column(name = "Surname", length = 50)
	private String surname;

	@Column(name = "OtherNames", length = 100)
	private String otherNames;

	@Column(name = "gender", length = 1)
	private String gender;

	@Column(name = "DoB")
	private LocalDate doB;

	@Column(name = "MobileNumber", length = 15)
	private String mobileNumber;

}
