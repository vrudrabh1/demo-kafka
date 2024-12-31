package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.service.KafkaService;

@RestController
@RequestMapping("/api/kafka")
@Slf4j
public class KafkaController {
    private final KafkaService kafkaService;

    public KafkaController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping("/publish")
    public Mono<ResponseEntity<String>> publishMessage(
            @RequestParam String topic,
            @RequestBody String message) {
        return kafkaService.sendMessage(topic, message)
                .map(result -> ResponseEntity.ok("Message sent successfully"))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send message"));
    }

    @GetMapping("/consume")
    public Flux<String> consumeMessages(@RequestParam String topic) {
        return kafkaService.consumeMessages(topic);
    }
} 