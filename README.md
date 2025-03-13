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
- Apache Commons Lang3 for utility functions
- Project Lombok for reducing boilerplate code
- JUnit 5 for testing
- Reactor Test for testing reactive components
- Docker for containerization
- Maven for project management



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

### Using Docker

Build the Docker image:

```bash
docker build -t bank-transaction-demo .
```

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