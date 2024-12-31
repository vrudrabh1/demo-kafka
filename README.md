# Spring Boot Reactive Kafka Demo

A reactive Spring Boot application demonstrating Kafka message publishing and consuming using WebFlux.

## Features

- Reactive Kafka Producer and Consumer
- WebFlux endpoints
- Message validation
- Error handling
- Server-Sent Events (SSE) for real-time message consumption

## Prerequisites

- Java 17+
- Docker
- Maven

## Setup

1. Start Kafka using Docker:

/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

brew install --cask docker

# Either click the Docker Desktop icon in Applications
open /Applications/Docker.app

docker --version
docker ps

User the cocker-compose.yml file to start Kafka and Zookeeper
# Start Kafka and Zookeeper using docker-compose
docker-compose up -d

# Check running containers
docker ps

# Check Kafka logs
docker logs <kafka-container-id>


debugging kafka issues
docker exec -it <kafka-container-id> /bin/bash
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic demo-topic --from-beginning


Testing the application

mvn spring-boot:run

# Check running containers
docker ps

# Check Kafka logs
docker logs <kafka-container-id>

Publishing a message to the Kafka topic

curl -X POST http://localhost:8080/api/kafka/publish -H "Content-Type: application/json" -d '{"message": "Hello, Kafka!"}'

Consuming messages from the Kafka topic

curl http://localhost:8080/api/kafka/consume







