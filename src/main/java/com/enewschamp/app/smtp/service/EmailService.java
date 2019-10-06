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

import com.enewschamp.EnewschampApplicationProperties;

@Service
public class EmailService {

	@Autowired
	EnewschampApplicationProperties appConfig;
	
	public boolean sendOTP(final String otp, final String toEmailId)
	{
		String otpBody = appConfig.getOtpEmailBodyText();
		otpBody = otpBody.replace("{OTP}", otp);
		String subject  = appConfig.getOtpEmailSubject();
				
		return this.sendMail(subject, otpBody, toEmailId);
	}
	public boolean sendMail(final String subject, final String body, final String toEmail)
	{
		boolean sendSuccess=true;
        final String username = appConfig.getFromEmailId();
        final String password = appConfig.getEmailPwd();

        Properties prop = new Properties();
		prop.put("mail.smtp.host", appConfig.getEmailHost());
        prop.put("mail.smtp.port", appConfig.getEmailPwd());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(appConfig.getFromEmailId()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
        	sendSuccess=false;
            e.printStackTrace();
        }
        
return sendSuccess;
	}
}
