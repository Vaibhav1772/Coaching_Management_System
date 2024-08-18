package com.example.myapplication.GenerativeAI;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class ChatHistory {
    @Getter
    private static final List<ChatMessage> chatMessages = new ArrayList<>();

    public static void addMessage(ChatMessage message) {
        chatMessages.add(message);
    }
}