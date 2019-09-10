package com.enewschamp.app.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.enewschamp.domain.common.BaseEntity;
import com.enewschamp.publication.domain.common.ForeignKeyColumnLength;
import com.enewschamp.subscription.common.SubscriptionType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="Notifications")
public class Notification extends BaseEntity {	
	
	private static final long serialVersionUID = -1133770182869410493L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_generator")
	@SequenceGenerator(name="notification_id_generator", sequenceName = "notification_id_seq", allocationSize=1)
	@Column(name = "NotificationId", updatable = false, nullable = false)
	private Long notificationId;
	
	@NotNull
	@Column(name = "LanguageId", length=ForeignKeyColumnLength.LanguageId)
	private String languageId;
	
	@Column(name = "SubscriptionType")
	@Enumerated(EnumType.STRING)
	private SubscriptionType subscriptionType;
	
	@Column(name = "CityId", length=ForeignKeyColumnLength.CityId)
	private String cityId;
	
	@Column(name = "SchoolId", length=ForeignKeyColumnLength.SchoolId)
	private String schoolId;
	
	@NotNull
	@Column(name = "NotificationText", length=200)
	private String notificationText;
	
	@Column(name = "NotificationEmailText")
	@Lob
	private String notificationEmailText;
	
	public String getKeyAsString() {
		return String.valueOf(this.notificationId);
	}
	
}
