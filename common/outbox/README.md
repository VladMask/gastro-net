### Summary
A lightweight Spring Boot starter implementing the Transactional Outbox pattern with automatic event publishing to Kafka.

### Components
The starter auto-registers:
* **OutboxEventProducer** — publishes events from the outbox table to Kafka.
* **OutboxEventService** — persists events into the outbox table within the same database transaction.
* **OutboxEventScheduler** — periodically reads unsent events and triggers the producer.
* **ObjectMapper** — used internally to serialize event payloads.

And provides **OutboxEvent** JPA entity

### Setup
Add the dependency to your project:
```xml
<dependency>
    <groupId>grsu.by</groupId>
    <artifactId>common-outbox-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
Because the starter provides a JPA entity (**OutboxEvent**) and relies on **Spring Data JPA**, **ShedLock** and **Kafka**,
you must include these dependencies in your application as well:
```xml
<!-- Required for OutboxEvent JPA entity -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Required for distributed scheduling (used by OutboxEventScheduler) -->
<dependency>
    <groupId>net.javacrumbs.shedlock</groupId>
    <artifactId>shedlock-spring</artifactId>
</dependency>
<dependency>
    <groupId>net.javacrumbs.shedlock</groupId>
    <artifactId>shedlock-provider-jdbc-template</artifactId>
</dependency>

<!-- Kafka integration -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```
### Database Migration
The starter expects a database table corresponding to the OutboxEvent entity.
You should create a migration (for example using **Flyway** or **Liquibase**) that defines this table:
```sql
create table outbox_events (
    id bigserial primary key,
    header varchar not null,
    payload varchar not null,
    status varchar not null,
    retry smallint not null default 0,
    created_at timestamp
);
```
This table will store pending and processed outbox events that are later published to Kafka.
### Configuration

Add basic configuration in your ```application.yml``` or ```application.properties```:
```yaml
common:
  outbox:
    producer:
      kafka-topic: orders-outbox
    scheduler:
      enabled: true
      fixed-delay: 5000
      initial-delay: 2000
      lock-at-most-for: 2m

spring:
  kafka:
    bootstrap-servers: localhost:9092
```
```properties
#Kafka
common.outbox.producer.kafka-topic=users
spring.kafka.bootstrap-servers=localhost:9092
#Scheduler
common.outbox.scheduler.lock-at-most-for=2m
common.outbox.scheduler.initial-delay=10000
common.outbox.scheduler.fixed-delay=2000
common.outbox.scheduler.enabled=true
```
