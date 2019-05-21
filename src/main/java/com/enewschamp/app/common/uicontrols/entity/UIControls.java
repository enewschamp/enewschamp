package com.enewschamp.app.common.uicontrols.entity;

import java.util.Date;

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
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="UIControls")
public class UIControls extends BaseEntity{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uicontrols_id_generator")
		@SequenceGenerator(name="uicontrols_id_generator", sequenceName = "uicontrols_seq", allocationSize=1)
		@Column(name = "UIControlID", updatable = false, nullable = false)
		private Long uiControlId;
		
		@NotNull
		@Column(name = "ScreenName", length=50)
		private String screenName;
		
		@NotNull
		@Column(name = "ControlName",length=50)
		private String controlName;
		
		@NotNull
		@Column(name = "contrrolType",length=50)
		private String contrrolType;
		
		@NotNull
		@Column(name = "DataSize", length=4)
		private String dataSize;
		
		@NotNull
		@Column(name = "DataType", length=3)
		private String dataType;
		
		@NotNull
		@Column(name="creationDateTime")
		private Date creationTime;

		
}
