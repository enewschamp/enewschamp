package com.enewschamp.app.notification.firebase.model;

import java.net.URI;

import lombok.Data;

@Data
public class PushNotificationRequest {

	private String title;
	private String message;
	private String topic;
	private String deviceToken;
	private String name;
	private URI imageUrl;

	public PushNotificationRequest() {
	}

	public PushNotificationRequest(String title, String messageBody, String topicName) {
		this.title = title;
		this.message = messageBody;
		this.topic = topicName;
	}

}
