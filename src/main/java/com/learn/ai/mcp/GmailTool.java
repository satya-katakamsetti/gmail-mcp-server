package com.learn.ai.mcp;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GmailTool {

    @Autowired
    private Gmail gmailService;

    @Tool(name = "get-emails", description = "Get last 10 emails for a given Gmail account")
    public List<Map<String, Object>> getEmails(@ToolParam String email) throws IOException {
        ListMessagesResponse response = gmailService.users().messages()
                .list(email.equals("") ? "me" : email)
                .setMaxResults(10L)
                .execute();

        List<Map<String, Object>> results = new ArrayList<>();

        if (response.getMessages() != null) {
            for (Message msg : response.getMessages()) {
                Message fullMsg = gmailService.users()
                        .messages()
                        .get("me", msg.getId()).execute();

                results.add(Map.of(
                        "id", msg.getId(),
                        "snippet", fullMsg.getSnippet())
                );
            }
        }

        return results;
    }
}