# Day 7 - Redis Caching with Spring Boot including Docker Setup defined in last section

## Objective

Learn Redis caching with Spring Boot and understand:

* Docker basics
* Redis basics
* Cache Hit / Cache Miss
* @Cacheable
* @CachePut
* @CacheEvict
* Serialization Issue
* TTL (Time To Live)

---

# Docker Basics

## What is Docker?

Docker is a platform used to run applications inside containers.

### Java Analogy

```text
Image     = Class
Container = Object
```

Example:

```text
Image     = redis
Container = redis-container
```

## Important Commands

### Check Docker Version

```bash
docker --version
```

### Show Running Containers

```bash
docker ps
```

### Show All Containers

```bash
docker ps -a
```

### Show Downloaded Images

```bash
docker images
```

### Run Redis

```bash
docker run -d --name redis -p 6379:6379 redis
```

---

# Redis Basics

Redis is an in-memory key-value datastore.

### Example

```text
Key   = name
Value = Akash
```

Redis Commands:

```redis
PING
SET name Akash
GET name
KEYS *
```

Example:

```redis
SET name Akash
GET name
```

Output:

```text
Akash
```

---

# Spring Boot Redis Configuration

application.yml

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

---

# Enable Caching

Main Application Class

```java
@SpringBootApplication
@EnableCaching
public class SpringBootHelloApplication {
}
```

Without @EnableCaching:

```text
@Cacheable ignored
```

With @EnableCaching:

```text
Caching enabled
```

---

# Cache Flow

## First Request

```http
GET /employees/1
```

Flow:

```text
Redis
 ↓
Not Found
 ↓
Database
 ↓
Store in Redis
 ↓
Response
```

This is called:

```text
Cache Miss
```

---

## Second Request

```http
GET /employees/1
```

Flow:

```text
Redis
 ↓
Found
 ↓
Response
```

Database is not called.

This is called:

```text
Cache Hit
```

---

# @Cacheable

Used for READ operations.

```java
@Cacheable(value = "employees", key = "#id")
public Employee getEmployeeById(Long id) {

    System.out.println("Fetching from DB");

    return employeeRepository.findById(id)
            .orElseThrow();
}
```

### Verification

First Call:

```text
Fetching from DB
```

Second Call:

```text
No log
```

Meaning:

```text
Response served from Redis
```

---

# Redis Key Structure

Annotation:

```java
@Cacheable(value = "employees", key = "#id")
```

Request:

```http
GET /employees/1
```

Redis Key:

```text
employees::1
```

Meaning:

```text
Key   = employees::1
Value = Employee Object
```

Check:

```redis
KEYS *
```

Output:

```text
employees::1
```

---

# Serialization Issue (Real Issue Faced)

Error:

```text
Cannot serialize
DefaultSerializer requires a Serializable payload
```

Reason:

```text
Employee object was not Serializable
```

Fix:

```java
import java.io.Serializable;

@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

}
```

### Why?

Redis stores bytes.

Spring converts object → byte array before storing.

Serializable allows Java objects to be converted into bytes.

---

# @CachePut

Used for UPDATE operations.

```java
@CachePut(value = "employees", key = "#id")
public Employee updateEmployee(Long id, Employee employee) {

    // update logic

    return updatedEmployee;
}
```

Flow:

```text
Update DB
 ↓
Update Redis
 ↓
Return Latest Data
```

Purpose:

```text
Avoid stale cache after update
```

---

# @CacheEvict

Used for DELETE operations.

```java
@CacheEvict(value = "employees", key = "#id")
public boolean deleteEmployee(Long id) {

    // delete logic
}
```

Flow:

```text
Delete DB Record
 ↓
Remove Cache Entry
```

Purpose:

```text
Avoid stale cache after delete
```

Without @CacheEvict:

```text
Deleted data may still be returned from Redis
```

---

# Cache Inconsistency

Scenario:

```text
DB Record Deleted
 ↓
Redis Entry Still Exists
 ↓
Old Data Returned
```

This is called:

```text
Stale Data
```

Solution:

```java
@CacheEvict
```

---

# Redis CLI Commands

Enter Redis CLI:

```bash
docker exec -it redis redis-cli
```

Show Keys:

```redis
KEYS *
```

Delete Single Key:

```redis
DEL employees::1
```

Delete All Keys:

```redis
FLUSHALL
```

---

# TTL (Time To Live)

TTL = Automatic Expiration Time

Example:

```text
employees::1
TTL = 10 Minutes
```

After 10 minutes:

```text
Key automatically removed
```

---

# Redis Configuration with TTL

Package:

```text
config
 └── RedisConfig.java
```

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration cacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }
}
```

---

# Verify TTL

Create cache:

```http
GET /employees/1
```

Check TTL:

```redis
TTL employees::1
```

Output:

```text
598
```

Meaning:

```text
598 seconds remaining
```

Special Values:

```text
-1  = No expiry
-2  = Key does not exist
```

---

# Production Best Practices

Use:

```java
@Cacheable
@CachePut
@CacheEvict
```

AND

```text
TTL
```

Reason:

```text
@CachePut  -> Update cache
@CacheEvict -> Remove cache
TTL        -> Safety net if stale cache exists
```

---

# Interview Questions

## What is Redis?

In-memory key-value datastore used for caching.

---

## What is Cache Hit?

Data found in Redis.

---

## What is Cache Miss?

Data not found in Redis, so DB is called.

---

## Why Serializable is required?

Redis stores bytes.

Java object must be converted into byte array before storing.

---

## What is TTL?

Time To Live.

Automatically removes cache entries after a configured duration.

---

## How to avoid stale cache?

1. @CachePut after updates
2. @CacheEvict after deletes
3. TTL configuration

---

# Final Revision

```text
Image     = Class
Container = Object

Redis = Key → Value

@Cacheable = Read
@CachePut  = Update
@CacheEvict = Delete

employees::1
Key   = employees::1
Value = Employee Object

Serializable
↓
Object can be stored in Redis

TTL
↓
Automatic Cache Expiry
```
# Docker Quick Notes

## Why Docker?

Used to run services without installing them directly on the machine.

Examples:

* Redis
* PostgreSQL
* Kafka
* MongoDB
* Ollama

## Java Analogy

```text
Image     = Class
Container = Object
```

## Commands

Check Docker Version

```bash
docker --version
```

Show Running Containers

```bash
docker ps
```

Show All Containers

```bash
docker ps -a
```

Show Downloaded Images

```bash
docker images
```

Run Redis

```bash
docker run -d --name redis -p 6379:6379 redis
```

Access Redis CLI

```bash
docker exec -it redis redis-cli
```

## Learning

Spring Boot Restart ≠ Redis Restart

Redis runs independently inside a Docker container.

Cache remains available even after Spring Boot application restart.
