package com.enewschamp.user.domin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	@Column(name = "UserID", length = 8)
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
	@Column(name = "Gender")
	private Gender gender;
	
	@NotNull
	@Column(name = "ContractStartDate")
	private Date contractStartDate;
	
	@NotNull
	@Column(name = "ContractEndDate")
	private Date contractEndDate;
	
	@NotNull
	@Column(name = "Mobile1", length=12)
	private long mobile1 = 0;
	
	@Column(name = "Mobile2", length=12)
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

}
