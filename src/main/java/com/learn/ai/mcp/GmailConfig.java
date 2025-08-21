package com.learn.ai.mcp;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
public class GmailConfig {

    private static final String APPLICATION_NAME = "MCP Gmail Tool";
    private static final java.io.File TOKENS_DIRECTORY = new java.io.File("tokens");

    private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_READONLY);
    private final CredentialsLoader credentialsLoader;

    public GmailConfig(CredentialsLoader credentialsLoader) {
        this.credentialsLoader = credentialsLoader;
    }

    private Credential authorize() throws IOException, GeneralSecurityException {

        GoogleClientSecrets clientSecrets =
                credentialsLoader.loadClientSecrets();

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(TOKENS_DIRECTORY))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Bean
    public Gmail gmailService() throws IOException, GeneralSecurityException {
        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                authorize())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
