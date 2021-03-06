package com.enewschamp.app.notification.firebase;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.java.Log;

//@Service
@Log
public class FCMInitializer {

	@Value("${app.firebase-configuration-file}")
	private String firebaseConfigPath;

	@PostConstruct
	public void initialize() {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(
							GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				log.info("Firebase application has been initialized");
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
	}

}
