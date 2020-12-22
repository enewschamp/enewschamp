package com.enewschamp.app.smtp.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enewschamp.app.common.PropertyConstants;
import com.enewschamp.common.domain.service.PropertiesService;

@Service
public class EmailService {

	@Autowired
	PropertiesService propertiesService;

	public boolean sendOTP(final String otp, final String toEmailId) {
		String otpBody = propertiesService.getProperty(PropertyConstants.OTP_EMAIL_BODY_TEXT);
		otpBody = otpBody.replace("{OTP}", otp);
		String subject = propertiesService.getProperty(PropertyConstants.OTP_EMAIL_SUBJECT);
		return this.sendMail(subject, otpBody, toEmailId);
	}

	public boolean sendMail(final String subject, final String body, final String toEmail) {
		boolean sendSuccess = true;
		final String username = propertiesService.getProperty(PropertyConstants.FROM_MAIL_ID);
		final String password = propertiesService.getProperty(PropertyConstants.EMAIL_PWD);
		Properties prop = new Properties();
		prop.put("mail.smtp.host", propertiesService.getProperty(PropertyConstants.EMAIL_HOST));
		prop.put("mail.smtp.port", propertiesService.getProperty(PropertyConstants.EMAIL_PORT));
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(propertiesService.getProperty(PropertyConstants.FROM_MAIL_ID)));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (MessagingException e) {
			sendSuccess = false;
			e.printStackTrace();
		}
		return sendSuccess;
	}
}
