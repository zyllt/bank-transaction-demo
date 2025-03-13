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
- Spring WebFlux
- Spring Validation
- JUnit 5 for testing
- Docker for containerization
- Maven for project management

## API Endpoints

| Method | URL                      | Description                                |
|--------|---------------------------|--------------------------------------------|
| POST   | /api/transactions        | Create a new transaction                   |
| GET    | /api/transactions/{id}   | Get a transaction by ID                    |
| PUT    | /api/transactions/{id}   | Update an existing transaction             |
| DELETE | /api/transactions/{id}   | Delete a transaction                       |
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
    "accountNumber": "12345678",
    "amount": 100.00,
    "type": "DEPOSIT",
    "description": "Initial deposit"
  }'
```

### Get All Transactions

```bash
curl -X GET http://localhost:8080/api/transactions?page=0&size=10
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

## Future Improvements

- Add authentication and authorization
- Implement persistent storage with a database
- Add more comprehensive logging
- Implement rate limiting
- Add API documentation with Swagger/OpenAPI
