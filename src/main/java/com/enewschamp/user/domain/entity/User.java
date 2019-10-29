package com.enewschamp.user.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.domain.common.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="User")
public class User extends BaseEntity {

	private static final long serialVersionUID = -7423524921019152899L;

	@Id
	@NotNull
	@Column(name = "UserId", length = 8)
	private String userId;
	
	@NotNull
	@Column(name = "Title")
	private Long title;
	
	@NotNull
	@Column(name = "Name", length = 50)
	private String name;
	
	@NotNull
	@Column(name = "Surname", length = 50)
	private String surname;
	
	@Column(name = "OtherNames", length = 100)
	private String otherNames;
	
	@NotNull
	@Column(name = "Gender", length=1)
	private Gender gender;
	
	@NotNull
	@Column(name = "ContractStartDate")
	private LocalDate contractStartDate;
	
	@NotNull
	@Column(name = "ContractEndDate")
	private LocalDate contractEndDate;
	
	@NotNull
	@Column(name = "Mobile1", length=15)
	private long mobile1 = 0;
	
	@Column(name = "Mobile2", length=15)
	private long mobile2 = 0;
	
	@Column(name = "LandLine1", length=12)
	private long landLine1 = 0;
	
	@Column(name = "LandLine2", length=12)
	private long landLine2 = 0;
	
	@NotNull
	@Column(name = "Email1", length = 99)
	private String email1;
	
	@Column(name = "Email2", length = 99)
	private String email2;
	
	@Column(name = "Comments", length = 999)
	private String comments;

	@Column(name = "Photo")
	@Lob
	private String photo;
	
	@Column(name = "Password", length = 80)
	private String password;
}
