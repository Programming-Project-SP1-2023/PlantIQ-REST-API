package com.plantiq.plantiqserver.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.plantiq.plantiqserver.PlantIqServerApplication;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class EmailService {
	private static final String APPLICATION_NAME = "Plantiq Server";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String USER = "me";
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	private static final String FROM = "iqplant0@gmail.com";

	static Credential getCredentials(final HttpTransport httpTransport) throws IOException, GeneralSecurityException {
		InputStream in = PlantIqServerApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets, GmailScopes.all())
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
				.setAccessType("offline")
				.setApprovalPrompt("force")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		credential.setExpirationTimeMilliseconds(System.currentTimeMillis() + 31536000000L);
		return credential;
	}

	public static void sendRecoveryEmail(String to, String token, String username) {
		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			Credential credential = getCredentials(httpTransport);

			Gmail service = new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME)
					.build();

			MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
			message.setFrom(new InternetAddress(FROM));
			message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Reset Password");
			message.setText("Hello " + username + "\n\nDid you forget your password? You can reset it at the following link:\nhttps://plantiq.azurewebsites.net/reset/" + token + "\n\nRegards,\nPlantIQ.");

			// Convert MimeMessage to Gmail message
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			message.writeTo(buffer);
			byte[] bytes = buffer.toByteArray();
			String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
			Message gmailMessage = new Message();
			gmailMessage.setRaw(encodedEmail);

			// Send message
			service.users().messages().send(USER, gmailMessage).execute();
			System.out.println("Sent message successfully....");
		} catch (IOException | MessagingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	public static void sendAlert(String to, String username, String field, String value, String plant) {
		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			Credential credential = getCredentials(httpTransport);

			Gmail service = new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME)
					.build();

			MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
			message.setFrom(new InternetAddress(FROM));
			message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
			String[] unit = {"%, °C"};
			int i;
			if (field == "temperature")
				i = 1;
			else
				i = 0;
			message.setSubject("Plant Alert");
			message.setText("Hello " + username + ",\n\nwe are reaching to you because the plant '" + plant + "' might need some attention. The current value " + field + " is " + value + unit[i] + ", which is out of range.\n\nRegards,\nPlantIQ.");

			// Convert MimeMessage to Gmail message
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			message.writeTo(buffer);
			byte[] bytes = buffer.toByteArray();
			String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
			Message gmailMessage = new Message();
			gmailMessage.setRaw(encodedEmail);

			// Send message
			service.users().messages().send(USER, gmailMessage).execute();
			System.out.println("Sent message successfully....");
		} catch (IOException | MessagingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
}