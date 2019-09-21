package com.enewschamp.publication.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.javers.core.metamodel.annotation.DiffIgnore;

import com.enewschamp.domain.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="PublicationArticles",uniqueConstraints={@UniqueConstraint(columnNames = {"publicationId", "newsArticleId"})})
public class PublicationArticleLinkage extends BaseEntity {

	private static final long serialVersionUID = 6691771323336628236L;

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long linkageId;

	@NotNull
	@Column(name = "PublicationId")
	@DiffIgnore
	private long publicationId = 0L;
	
	@NotNull
	@Column(name = "NewsArticleId")
	private long newsArticleId = 0L;
	
	@NotNull
	@Column(name = "Sequence")
	private int sequence;
	
	public String getKeyAsString() {
		return String.valueOf(this.linkageId);
	}

}