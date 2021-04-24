package com.enewschamp.app.admin.celebration.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Celebrations",uniqueConstraints = {
		@UniqueConstraint(columnNames = { "editionId", "date", "readingLevel", "gender", "nameId" }) })
public class Celebration extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "celebrations_id_generator")
	@SequenceGenerator(name = "celebrations_id_generator", sequenceName = "celebrations_id_seq", allocationSize = 1)
	@Column(name = "celebrationId", length = 3)
	private Long celebrationId = 0L;

	@NotNull
	@Column(name = "nameId", length = 10)
	private String nameId;

	@NotNull
	@Column(name = "editionId", length = 6)
	private String editionId;
	
	@Column(name = "date")
	private LocalDate date;
	
	@Column(name = "occasion")
	private String occasion;

	@NotNull
	@Column(name = "readingLevel", length = 1)
	private int readingLevel;

	@Column(name = "imageName", length = 100)
	private String imageName;

	@Column(name = "audioFileName", length = 100)
	private String audioFileName;

	@Transient
	private String imageBase64;
	@Transient
	private String audioFileBase64;
	@Transient
    private String audioFileTypeExt;
	@Transient
	private String imageTypeExt;
	
	@Column(name = "gender", length = 1)
	private String gender;
	

}
