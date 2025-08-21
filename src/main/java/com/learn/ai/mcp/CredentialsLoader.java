package com.learn.ai.mcp;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.regions.Region;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;

@Component
public class CredentialsLoader {
    private static final String BUCKET_NAME = "secure-my-creds-2025";   // replace with your bucket
    private static final String OBJECT_KEY  = "credentials.json"; // path inside bucket

    private final S3Client s3Client = S3Client.builder().region(Region.US_EAST_1).build();

    public GoogleClientSecrets loadClientSecrets() throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(OBJECT_KEY)
                .build();

        try (InputStream s3is = s3Client.getObject(getObjectRequest);
             InputStreamReader reader = new InputStreamReader(s3is)) {
            return GoogleClientSecrets.load(
                    GsonFactory.getDefaultInstance(),
                    reader
            );
        }
    }
}
