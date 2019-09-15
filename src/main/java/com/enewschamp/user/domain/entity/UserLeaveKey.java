package com.enewschamp.user.domain.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.Id;

import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
@MappedSuperclass
public class UserLeaveKey implements Serializable {

	private static final long serialVersionUID = 2309003572872569643L;

	@Id
	@NotNull
	@Column(name = "UserId", length = ForeignKeyColumnLength.UserId)
	private String userId;

	@Id
	@NotNull
	@Column(name = "StartDate")
	private LocalDate startDate;	
	

}
