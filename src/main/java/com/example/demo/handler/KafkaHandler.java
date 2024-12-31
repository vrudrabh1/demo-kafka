package com.example.demo.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import com.example.demo.model.Message;
import com.example.demo.service.KafkaService;
import com.example.demo.validator.MessageValidator;
import com.example.demo.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaHandler {
    private final KafkaService kafkaService;
    private final MessageValidator messageValidator;

    public KafkaHandler(KafkaService kafkaService, MessageValidator messageValidator) {
        this.kafkaService = kafkaService;
        this.messageValidator = messageValidator;
    }

    public Mono<ServerResponse> publishMessage(ServerRequest request) {
        return request.bodyToMono(Message.class)
                .map(msg -> {
                    msg.setTimestamp(LocalDateTime.now());
                    return msg;
                })
                .flatMap(messageValidator::validate)
                .flatMap(msg -> kafkaService.sendMessage(msg.getTopic(), msg.getContent()))
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new HashMap<String, String>() {{
                            put("status", "Message sent successfully");
                            put("timestamp", LocalDateTime.now().toString());
                        }}))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> consumeMessages(ServerRequest request) {
        String topic = request.queryParam("topic")
                .orElseThrow(() -> new ValidationException("Topic is required"));

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(kafkaService.consumeMessages(topic), String.class)
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", error.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());

        HttpStatus status = error instanceof ValidationException ? 
            HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }
}