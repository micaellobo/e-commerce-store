# e-commerce-store

Spring microservice-based application to manage orders, products, inventory and product reviews.

# Microservices Overview

- **Eureka Service:** Service discovery and registration.
- **API Gateway:** Routes requests to appropriate microservices.
- **User Service:** Handles user management.
- **Auth Service:** Provides authentication.
- **Inventory Service:** Manages product inventory and availability.
- **Reviews Service:** Gather and display product reviews and ratings from users.
- **Reviews Service:** Manages product reviews and ratings.
- **Order Service:** Manage and process customer orders.

# How to run

### Local Development

#### Prerequisites

- Postgres
- Java 17
- Maven

Follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project's root directory.
3. Configure your local Postgres database.
4. Build and run each microservice using Maven.

### Local Development with Docker Compose

This option builds Docker images from your current local codebase:

#### Prerequisites

- Docker
- Docker Compose

1. Clone the repository to your local machine.
2. Run the following command:

        docker-compose -f docker-compose-dev.yaml up -d

### Docker Compose (Docker Hub Registry)

This option uses pre-built Docker images from a Docker Hub registry. Simpler and faster way to run the application.

#### Prerequisites

- Docker
- Docker Compose

Clone the repository or just download the
[docker-compose.yaml](https://github.com/micaellobo/e-commerce-store/raw/master/docker-compose.yaml) file and run the
following command:

        docker-compose up -d

# Next Steps and Improvements

As the project is actively being developed, there are several possibilities for future enhancements and progress. Here
are a few ideas:

- **API Documentation:** API documentation using `Swagger`.
- **Security:** Transitioning from `JWT` to `OAuth` 2.0 with `Keycloak` for more robust authentication and authorization
  mechanisms.
- **Testing:** Implement integration tests using `testContainers` to closely resemble the production environment.
- **CI/CD:** Set up pipelines to automate testing and deployment processes with `Jenkins`.
- **Container Orchestration:** Explore container orchestration platforms like `Kubernetes`.
