# Bank Transaction Demo

A simple Spring Boot application for managing banking transactions.

## Features

- Create, read, update, and delete transactions
- In-memory storage with thread-safe implementation
- Caching for improved performance
- Comprehensive validation and error handling
- Pagination support for listing transactions
- RESTful API design
- Containerization with Docker

## Technologies Used

- Java 21
- Spring Boot 3.3.9
- Spring WebFlux for reactive programming
- Spring Validation for input validation
- Spring Boot JSON for JSON processing
- Spring Boot Cache and Caffeine Cache improve performance
- Apache Commons Lang3 for utility functions
- Project Lombok for reducing boilerplate code
- JUnit 5 for testing
- Reactor Test for testing reactive components
- Docker for containerization
- Maven for project management

## Caching Implementation

The application uses Spring Cache to improve performance by caching frequently accessed transaction data:

- **Cache Configuration**: Configured in `CacheConfig.java` using Caffeine as the cache provider
- **Cache Usage**:
  - Transaction retrieval operations are cached to reduce database/storage access
  - Cache is automatically invalidated when transactions are updated or deleted
  - Custom TTL (Time-To-Live) settings to ensure data freshness
- **Cache Annotations**:
  - `@Cacheable`: Applied to retrieval methods to store and fetch from cache
  - `@CacheEvict`: Used on update/delete operations to remove stale data
  - `@CachePut`: Used to update the cache when transaction data changes

This caching strategy significantly reduces response times for repeated transaction queries.



## API Endpoints

| Method | URL                      | Description                                |
|--------|---------------------------|--------------------------------------------|
| POST   | /api/transactions        | Create a new transaction                   |
| GET    | /api/transactions/{TransactionId}   | Get a transaction by TransactionId                    |
| PUT    | /api/transactions/{TransactionId}   | Update an existing transaction             |
| DELETE | /api/transactions/{TransactionId}   | Delete a transaction                       |
| GET    | /api/transactions        | Get all transactions with pagination       |

## Running the Application

### Using Maven

```bash
./mvnw spring-boot:run
```
After starting the application, you can opening your browser and navigating to:
`http://localhost:8080`

### Using Docker

Build the Docker image:

```bash
docker build -t bank-transaction-demo .
```

The application uses Eclipse Temurin JDK 21 for building and JRE 21 Alpine for runtime. For more information about these images, see: https://hub.docker.com/_/eclipse-temurin

You can modify the Dockerfile to use different Java images according to your requirements. For example, to use a different JDK version or distribution, simply update the base images in the Dockerfile

Run the Docker container:

```bash
docker run -p 8080:8080 bank-transaction-demo
```

## Testing the Application

Run the tests using Maven:

```bash
./mvnw test
```

## Sample Requests

### Create a Transaction

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "12345678",
    "opponentAccountId": "12345678_",
    "amount": 100.00,
    "transactionType": 1,
    "description": "Initial deposit"
  }'
```

### Page List Transactions

```bash
curl -X GET "http://localhost:8080/api/transactions?page=1&size=10"
```

## Project Structure

- `model`: Contains the domain model classes
- `dto`: Contains the Data Transfer Objects for requests and responses
- `controller`: Contains the REST controllers
- `service`: Contains the business logic
- `exception`: Contains custom exceptions and global exception handler
- `config`: Contains configuration classes

## Performance Considerations

- Uses ConcurrentHashMap for thread-safe in-memory storage
- Implements caching to improve read performance
- Pagination for efficient retrieval of large datasets
- Proper exception handling for robustness