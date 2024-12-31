package com.example.demo.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Collections;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.sender.SenderResult;

@Service
@Slf4j
public class KafkaService {
    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate;
    private final ReceiverOptions<String, String> receiverOptions;

    public KafkaService(ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate,
                       ReceiverOptions<String, String> receiverOptions) {
        this.kafkaProducerTemplate = kafkaProducerTemplate;
        this.receiverOptions = receiverOptions;
    }

    public Mono<SenderResult<Void>> sendMessage(String topic, String message) {
        return kafkaProducerTemplate.send(topic, message)
                .doOnSuccess(result -> log.info("Message sent successfully to topic {}: {}", 
                    topic, message))
                .doOnError(error -> log.error("Failed to send message to topic {}: {}", 
                    topic, error.getMessage()));
    }

    public Flux<String> consumeMessages(String topic) {
        log.info("Starting to consume messages from topic: {}", topic);
        return KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(topic)))
                .receive()
                .doOnSubscribe(sub -> log.info("Subscribed to topic: {}", topic))
                .doOnNext(record -> log.info("Received record: {}", record))
                .map(record -> {
                    log.info("Processing message from topic {}: {}", topic, record.value());
                    return record.value();
                })
                .doOnError(error -> log.error("Error consuming messages: {}", error.getMessage()))
                .doOnComplete(() -> log.info("Completed consuming messages"));
    }
} 