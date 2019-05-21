package com.enewschamp.publication.domain.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.PublicationStatusType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="PublicationGroup")
public class PublicationGroup extends BaseEntity {
	
	private static final long serialVersionUID = 819426502462078317L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_grp_id_generator")
	@SequenceGenerator(name="publication_grp_id_generator", sequenceName = "pub_grp_id_seq", allocationSize=1)
	@Column(name = "PublicationGroupID", updatable = false, nullable = false)
	private Long publicationGroupId;
	
	@NotNull
	@Column(name = "EditionID", length=6)
	private String editionId;
	
	@NotNull
	@Column(name = "PublicationDate")
	private LocalDate publicationDate;
	
	@Column(name = "Status", length = 25)
	@Enumerated(EnumType.STRING)
	private PublicationStatusType status;
	
	
}
