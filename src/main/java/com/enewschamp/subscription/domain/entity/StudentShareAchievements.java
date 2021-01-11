package com.enewschamp.subscription.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "StudentShareAchievements")
public class StudentShareAchievements extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "studentId", length = 10)
	private Long studentId;

	@NotNull
	@Column(name = "personalisedMessage", length = 200)
	private String personalisedMessage;

	@Column(name = "recipientName1", length = 99)
	private String recipientName1;

	@Column(name = "recipientName2", length = 99)
	private String recipientName2;

	@Column(name = "recipientName3", length = 99)
	private String recipientName3;

	@Column(name = "recipientName4", length = 99)
	private String recipientName4;

	@Column(name = "recipientName5", length = 99)
	private String recipientName5;

	@Column(name = "recipientName6", length = 99)
	private String recipientName6;

	@Column(name = "recipientName7", length = 99)
	private String recipientName7;

	@Column(name = "recipientName8", length = 99)
	private String recipientName8;

	@Column(name = "recipientName9", length = 99)
	private String recipientName9;

	@Column(name = "recipientName10", length = 99)
	private String recipientName10;

	@Column(name = "recipientGreeting1", length = 99)
	private String recipientGreeting1;

	@Column(name = "recipientGreeting2", length = 99)
	private String recipientGreeting2;

	@Column(name = "recipientGreeting3", length = 99)
	private String recipientGreeting3;

	@Column(name = "recipientGreeting4", length = 99)
	private String recipientGreeting4;

	@Column(name = "recipientGreeting5", length = 99)
	private String recipientGreeting5;

	@Column(name = "recipientGreeting6", length = 99)
	private String recipientGreeting6;

	@Column(name = "recipientGreeting7", length = 99)
	private String recipientGreeting7;

	@Column(name = "recipientGreeting8", length = 99)
	private String recipientGreeting8;

	@Column(name = "recipientGreeting9", length = 99)
	private String recipientGreeting9;

	@Column(name = "recipientGreeting10", length = 99)
	private String recipientGreeting10;

	@Column(name = "recipientContact1", length = 99)
	private String recipientContact1;

	@Column(name = "recipientContact2", length = 99)
	private String recipientContact2;

	@Column(name = "recipientContact3", length = 99)
	private String recipientContact3;

	@Column(name = "recipientContact4", length = 99)
	private String recipientContact4;

	@Column(name = "recipientContact5", length = 99)
	private String recipientContact5;

	@Column(name = "recipientContact6", length = 99)
	private String recipientContact6;

	@Column(name = "recipientContact7", length = 99)
	private String recipientContact7;

	@Column(name = "recipientContact8", length = 99)
	private String recipientContact8;

	@Column(name = "recipientContact9", length = 99)
	private String recipientContact9;

	@Column(name = "recipientContact10", length = 99)
	private String recipientContact10;

}
