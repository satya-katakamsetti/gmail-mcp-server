package com.learn.ai.mcp;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class CredentialsLoader {
    private static final String BUCKET_NAME = "secure-my-creds-2025";   // replace with your bucket
    private static final String OBJECT_KEY  = "credentials.json"; // path inside bucket

    private final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("us-east-1").build();

    public GoogleClientSecrets loadClientSecrets() throws IOException {
        // S3 automatically handles decryption if object was uploaded with KMS key
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, OBJECT_KEY);

        try (S3ObjectInputStream s3is = s3Object.getObjectContent();
             InputStreamReader reader = new InputStreamReader(s3is)) {
            return GoogleClientSecrets.load(
                    GsonFactory.getDefaultInstance(),
                    reader
            );
        }
    }
}
