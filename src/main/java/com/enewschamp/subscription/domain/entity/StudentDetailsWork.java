package com.enewschamp.subscription.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentDetailsWork")
public class StudentDetailsWork extends BaseEntity {

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

	@Column(name = "MobileNumer", length = 15)
	private String mobileNumber;

	@Column(name = "AvatarId", length = 10)
	private Long avatarId;

	@Transient
	private String photoBase64;
}
