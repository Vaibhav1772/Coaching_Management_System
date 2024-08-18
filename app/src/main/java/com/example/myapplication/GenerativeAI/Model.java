package com.example.myapplication.GenerativeAI;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.Getter;

@Getter
public class Model {

    private final GenerativeModelFutures model;
    private ChatFutures chat;
    private final List<ChatMessage> chatHistory;
    private final Executor executor;

    public Model(String apiKey) {
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", apiKey);
        this.model = GenerativeModelFutures.from(gm);
        this.chatHistory = new ArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void startChat() {
        List<Content> initialHistory = new ArrayList<>();
        for (ChatMessage message : chatHistory) {
            // Ensure you use the correct methods to build Content
            Content.Builder contentBuilder = new Content.Builder();
            contentBuilder.setRole(message.getRole()) ;
            contentBuilder.addText(message.getContent()) ;

            initialHistory.add(contentBuilder.build());
        }
        this.chat = model.startChat(initialHistory);
    }

    public void sendMessage(String messageText, FutureCallback<GenerateContentResponse> callback) {
        Content.Builder userMessageBuilder = new Content.Builder();
        userMessageBuilder.setRole("user");
        userMessageBuilder.addText(messageText);
        Content userMessage = userMessageBuilder.build();

        chatHistory.add(new ChatMessage("user", messageText, LocalTime.now()));
        ListenableFuture<GenerateContentResponse> response = chat.sendMessage(userMessage);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                Content.Builder modelMessageBuilder = new Content.Builder();
                modelMessageBuilder.setRole("model");
                modelMessageBuilder.addText(result.getText());
                Content modelMessage = modelMessageBuilder.build();
                chatHistory.add(new ChatMessage("model", result.getText(), LocalTime.now()));
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        }, executor);
    }

}
