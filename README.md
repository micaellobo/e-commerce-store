# e-commerce-store

Spring microservice-based application to manage orders, products, inventory and product reviews.

# Microservices Overview

- **Eureka Service:** Service discovery and registration.
- **API Gateway:** Routes requests to appropriate microservices.
- **User Service:** Handles user management.
- **Auth Service:** Provides authentication.
- **Inventory Service:** Manages product inventory and availability.
- **Reviews Service:** Gather and display product reviews and ratings from users.
- **Order Service:** Manage and process customer orders.

# How to run

### Docker Compose (Docker Hub Registry)

This option uses pre-built Docker images from a Docker Hub registry. Simpler and faster way to run the application.

#### Prerequisites

- Docker
- Docker Compose

Clone the repository or just download the
[docker-compose.yaml](https://github.com/micaellobo/e-commerce-store/raw/master/docker-compose.yaml) file and run the
following command:

        docker-compose up -d

### Local Development with Docker Compose

This option builds each time new Docker images from your current local codebase, can be a bit slower. \
For local development is preferable to have installed `Java 17`, `Maven` and run the necessary databases in separated containers.

#### Prerequisites

- Docker
- Docker Compose

1. Clone the repository to your local machine.
2. Run the following command:

       docker-compose -f docker-compose-dev.yaml up -d

# Next Steps and Improvements

As the project is actively being developed, there are several possibilities for future enhancements and progress. Here
are a few ideas:

- **API Documentation:** API documentation using `Swagger`.
- **Security:** Transitioning from `JWT` to `OAuth` 2.0 with `Keycloak` for more robust authentication and authorization
  mechanisms.
- **Testing:** Implement integration tests using `testContainers` to closely resemble the production environment.
- **CI/CD:** Set up pipelines to automate testing and deployment processes with `Jenkins`.
- **Container Orchestration:** Explore container orchestration platforms like `Kubernetes`.
