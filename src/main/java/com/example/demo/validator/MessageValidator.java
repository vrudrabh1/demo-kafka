package com.example.demo.validator;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import com.example.demo.model.Message;
import com.example.demo.exception.ValidationException;

@Component
public class MessageValidator {
    
    public Mono<Message> validate(Message message) {
        if (message.getTopic() == null || message.getTopic().trim().isEmpty()) {
            return Mono.error(new ValidationException("Topic cannot be empty"));
        }
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            return Mono.error(new ValidationException("Message content cannot be empty"));
        }
        return Mono.just(message);
    }
} 