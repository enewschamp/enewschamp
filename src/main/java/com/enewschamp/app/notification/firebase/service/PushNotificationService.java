package com.enewschamp.app.notification.firebase.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enewschamp.app.notification.firebase.FCMService;
import com.enewschamp.app.notification.firebase.model.PushNotificationRequest;

import lombok.extern.java.Log;

@Service
@Log
public class PushNotificationService {

	@Value("#{${app.notifications.defaults}}")
	private Map<String, String> defaults;

	private FCMService fcmService;

	public PushNotificationService(FCMService fcmService) {
		this.fcmService = fcmService;
	}

	// @Scheduled(initialDelay = 60000, fixedDelay = 60000)
	public void sendSamplePushNotification() {
		try {
			fcmService.sendMessageWithoutData(getSamplePushNotificationRequest());
		} catch (InterruptedException | ExecutionException e) {
			log.severe(e.getMessage());
		}
	}

	public void sendPushNotification(PushNotificationRequest request) {
		try {
			fcmService.sendMessage(getSamplePayloadData(), request);
		} catch (InterruptedException | ExecutionException e) {
			log.severe(e.getMessage());
		}
	}

	public void sendPushNotificationWithoutData(PushNotificationRequest request) {
		try {
			fcmService.sendMessageWithoutData(request);
		} catch (InterruptedException | ExecutionException e) {
			log.severe(e.getMessage());
		}
	}

	public void sendPushNotificationToToken(PushNotificationRequest request) {
		try {
			fcmService.sendMessageToToken(request);
		} catch (InterruptedException | ExecutionException e) {
			log.severe(e.getMessage());
		}
	}

	private Map<String, String> getSamplePayloadData() {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("messageId", defaults.get("payloadMessageId"));
		pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
		return pushData;
	}

	private PushNotificationRequest getSamplePushNotificationRequest() {
		PushNotificationRequest request = new PushNotificationRequest(defaults.get("title"), defaults.get("message"),
				defaults.get("topic"));
		return request;
	}

}
