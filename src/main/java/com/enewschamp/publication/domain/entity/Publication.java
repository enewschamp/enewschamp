package com.enewschamp.publication.domain.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OrderBy;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.javers.spring.annotation.JaversSpringDataAuditable;

import com.enewschamp.article.domain.entity.NewsArticle;
import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.enewschamp.publication.domain.common.PublicationActionType;
import com.enewschamp.publication.domain.common.PublicationStatusType;
import com.enewschamp.publication.domain.service.PublicationBusinessPolicy;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Publication", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "publicationId", "readingLevel", "publicationDate" }) })
@JaversSpringDataAuditable
public class Publication extends BaseEntity {

	private static final long serialVersionUID = -6656836773546374871L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_id_generator")
	@SequenceGenerator(name = "publication_id_generator", sequenceName = "publication_id_seq", allocationSize = 1)
	@Column(name = "PublicationId", updatable = false, nullable = false)
	private Long publicationId;

	@NotNull
	@Column(name = "PublicationGroupId")
	private Long publicationGroupId = 0L;

	@NotNull
	@Column(name = "ReadingLevel")
	private int readingLevel = 0;

	@NotNull
	@Column(name = "PublicationDate")
	private LocalDate publicationDate;

	@NotNull
	@Column(name = "Status")
	@Enumerated(EnumType.STRING)
	private PublicationStatusType status;

	@Column(name = "PreviousStatus")
	@Enumerated(EnumType.STRING)
	@DiffIgnore
	private PublicationStatusType previousStatus;

	@Column(name = "Rating")
	private String rating;

	@Column(name = "EditorWorked", length = ForeignKeyColumnLength.UserId)
	private String editorWorked;

	@Column(name = "PublisherWorked", length = ForeignKeyColumnLength.UserId)
	private String publisherWorked;

	@Column(name = "ReadyForTest", length = 1)
	private String readyForTest;

	@Column(name = "Comments")
	@Lob
	private String comments;

	@Enumerated(EnumType.STRING)
	@DiffIgnore
	private PublicationActionType currentAction;

	@OneToMany(cascade = CascadeType.DETACH, mappedBy = "publicationId", targetEntity = NewsArticle.class)
	@OrderBy(clause = "sequence asc")
	private List<NewsArticle> newsArticles;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		super.prePersist();
		new PublicationBusinessPolicy(this).validateAndThrow();
	}

	public String getKeyAsString() {
		return String.valueOf(this.publicationId);
	}

	public void setStatus(PublicationStatusType status, PublicationStatusType previousStatus) {
		this.previousStatus = (previousStatus == null ? status : previousStatus);
		this.status = status;
	}
}