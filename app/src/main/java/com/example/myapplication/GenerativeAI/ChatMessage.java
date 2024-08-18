package com.example.myapplication.GenerativeAI;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage{
    private String role;
    private String content;
    private LocalTime createdAt;
}
