package com.reactspringboot.libraryapp.controller;

import com.reactspringboot.libraryapp.entity.Message;
import com.reactspringboot.libraryapp.requestmodels.AdminQuestionRequest;
import com.reactspringboot.libraryapp.service.MessagesService;
import com.reactspringboot.libraryapp.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessagesService messagesService;

    @Autowired
    public MessageController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token,
                            @RequestBody Message messageRequest) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messagesService.postMessage(messageRequest, userEmail);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization") String token,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        boolean isAdmin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"").equalsIgnoreCase("admin");
        if(!isAdmin) {
            throw new Exception("Administration access only.");
        }
        messagesService.putMessage(adminQuestionRequest, userEmail);
    }
}
