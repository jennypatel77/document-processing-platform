# Document Processing Platform

A Spring Boot backend project to learn software engineering and system design concepts including queues, caching, logging, Docker, load balancing, and memory troubleshooting.

## Phase 1 Features
- Create job
- Get job by id
- List jobs
- PostgreSQL persistence
- Validation and exception handling

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL

## Run Locally
1. Create PostgreSQL database `doc_processor`
2. Update datasource config in `application.properties`
3. Run the Spring Boot app
4. Test endpoints in Postman

## Current Architecture

```mermaid
flowchart TD
    A[Client / Postman] --> B[Spring Boot API]
    B --> C[Controller Layer]
    C --> D[Service Layer]
    D --> E[Repository Layer]
    E --> F[(PostgreSQL)]
```

## Target Architecture

```mermaid
flowchart TD
    A[Client] --> B[Nginx Load Balancer]

    B --> C[API Service - Instance 1]
    B --> D[API Service - Instance 2]

    C --> E[(PostgreSQL)]
    D --> E

    C --> F[(Redis Cache)]
    D --> F

    C --> G[RabbitMQ]
    D --> G

    G --> H[Worker Service]

    H --> E

    C --> I[Logstash]
    D --> I
    H --> I
```


## Job Processing Flow

```mermaid
flowchart TD
    A[Client sends POST /jobs] --> B[API validates request]
    B --> C[Job saved with status PENDING]
    C --> D[Job published to RabbitMQ]
    D --> E[Worker consumes message]
    E --> F[Job status updated to PROCESSING]
    F --> G[Worker processes file]
    G --> H[Save result to database]
    H --> I[Job status updated to COMPLETED]

    G --> J[If processing fails]
    J --> K[Job status updated to FAILED]
```


## Job Status Lifecycle

```mermaid
stateDiagram-v2
    [*] --> PENDING
    PENDING --> PROCESSING
    PROCESSING --> COMPLETED
    PROCESSING --> FAILED
```