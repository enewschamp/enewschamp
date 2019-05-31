package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PublicationArticleLinkage")
public class PublicationArticleLinkage extends BaseEntity {

	private static final long serialVersionUID = 6691771323336628236L;

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@NotNull
	@Column(name = "PublicationID")
	private long publicationId = 0L;
	
	@NotNull
	@Column(name = "NewsArticleID")
	private long newsArticleId = 0L;
	
	@NotNull
	@Column(name = "Sequence")
	private int sequence;

}
