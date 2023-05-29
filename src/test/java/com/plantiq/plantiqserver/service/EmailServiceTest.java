package com.plantiq.plantiqserver.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

//org.mockito.exceptions.base.MockitoException:
//		Cannot mock/spy class com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
//		Mockito cannot mock/spy because :
//		- final class

class EmailServiceTest {
//	@Mock
//	private GoogleClientSecrets mockedClientSecrets;
//	@Mock
//	private GoogleAuthorizationCodeFlow mockedFlow;
//	@Mock
//	private Credential mockedCredential;

	@Test
	public void testGetCredentials() throws IOException, GeneralSecurityException {
		// Mock the necessary dependencies
//		InputStream mockedInputStream = mock(InputStream.class);
//		InputStreamReader mockedInputStreamReader = mock(InputStreamReader.class);
//		HttpTransport mockedHttpTransport = mock(HttpTransport.class);
//		GoogleClientSecrets mockedGoogleClientSecrets = mock(GoogleClientSecrets.class);
//		GoogleAuthorizationCodeFlow.Builder mockedFlowBuilder = mock(GoogleAuthorizationCodeFlow.Builder.class);
//		GoogleAuthorizationCodeFlow mockedFlow = mock(GoogleAuthorizationCodeFlow.class);
//		Credential mockedCredential = mock(Credential.class);
//
//		when(mockedInputStream.read()).thenReturn(-1); // End of stream
//		when(mockedInputStreamReader.read()).thenReturn(-1); // End of stream
//		when(mockedHttpTransport.createRequestFactory()).thenReturn(null);
//		when(mockedGoogleClientSecrets.getInstalled()).thenReturn(null);
//		when(mockedFlowBuilder.setDataStoreFactory(new MemoryDataStoreFactory())).thenReturn(mockedFlowBuilder);
//		when(mockedFlowBuilder.setAccessType("offline")).thenReturn(mockedFlowBuilder);
//		when(mockedFlowBuilder.setApprovalPrompt("force")).thenReturn(mockedFlowBuilder);
//		when(mockedFlowBuilder.build()).thenReturn(mockedFlow);
//		when(mockedFlow.loadCredential("user")).thenReturn(mockedCredential);
//		when(mockedCredential.getExpirationTimeMilliseconds()).thenReturn(System.currentTimeMillis() + 31536000000L);
//
//		// Create an instance of the EmailService class with mocked dependencies
//		EmailService emailService = new EmailService(mockedClientSecrets, mockedFlowBuilder);
//
//		// Call the method under test
//		Credential result = emailService.getCredentials(mockedHttpTransport);
//
//		// Verify the expected interactions and assertions
//		assertEquals(mockedCredential, result);
	}
}