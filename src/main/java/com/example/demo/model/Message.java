package com.example.demo.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Message {
    private String topic;
    private String content;
    private LocalDateTime timestamp;
} 