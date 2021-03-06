package com.enewschamp.app.helpdesk.entity;

import java.time.LocalDateTime;

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
@Entity
@Table(name = "Helpdesk")
@EqualsAndHashCode(callSuper = false)
public class Helpdesk extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "helpdesk_id_generator")
	@SequenceGenerator(name = "helpdesk_id_generator", sequenceName = "helpdesk_id_seq", allocationSize = 1)
	@Column(name = "helpdeskId", updatable = false, nullable = false)
	private Long helpdeskId;

	@NotNull
	@Column(name = "studentId", length = 100)
	private Long studentId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;

	@NotNull
	@Column(name = "categoryId", length = 4)
	private String categoryId;

	@NotNull
	@Column(name = "details", length = 399)
	private String details;

	@Column(name = "phoneNumber", length = 15)
	private String phoneNumber;

	@Column(name = "supportUserId", length = 20)
	private String supportUserId;

	@Column(name = "supportComments", length = 399)
	private String supportComments;

	@Column(name = "closeFlag", length = 1)
	private String closeFlag;

	@Column(name = "callbackDateTime")
	private LocalDateTime callbackDateTime;

	@Column(name = "createDateTime")
	private LocalDateTime createDateTime;

}