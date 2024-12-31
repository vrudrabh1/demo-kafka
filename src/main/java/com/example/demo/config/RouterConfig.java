package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;
import com.example.demo.handler.KafkaHandler;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class RouterConfig {
    
    @Bean
    public RouterFunction<ServerResponse> route(KafkaHandler kafkaHandler) {
        return RouterFunctions
                .route(POST("/api/kafka/publish"), kafkaHandler::publishMessage)
                .andRoute(GET("/api/kafka/consume"), kafkaHandler::consumeMessages);
    }
} 