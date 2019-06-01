package com.enewschamp.subscription.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name="StudentDetails")
public class StudentDetails extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "StudentID", length=10)
	private Long studentID= 0L;
	
	@NotNull
	@Column(name = "Name", length=50)
	private String name;
	@NotNull
	@Column(name = "Surname", length=50)
	private String surname;
	
	@Column(name = "OtherName", length=100)
	private String otherName;
	@NotNull
	@Column(name = "gender", length=1)
	private String gender;
	@NotNull
	@Column(name = "DoB")
	private LocalDate dob;
	
	@NotNull
	@Column(name = "MobileNumer", length=15)
	private String mobileNumber;
	
	@Lob
	@Column(name = "Photo")
	private String photo;
	
	@Column(name = "AvtarID", length=10)
	private Long avtarID;
	
}
