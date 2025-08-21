package com.learn.ai.mcp;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class GmailController {
    @Autowired
    private GmailTool gmailTool;

    @Operation(summary = "Get last 10 emails for a given Gmail account")
    @GetMapping("/gmail/emails")
    public List<Map<String, Object>> getEmailsBySender(
            @RequestParam String senderEmail) throws IOException {
        return gmailTool.getEmails(senderEmail);
    }
}

