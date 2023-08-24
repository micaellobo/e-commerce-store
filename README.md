# e-commerce-store

Spring microservice-based application to manage orders, products, inventory and product reviews.

# Microservices Overview

- **API Gateway:** Routes requests to appropriate microservices.
- **Eureka Service:** Service discovery and registration.
- **User Service:** Handles user management.
- **Auth Service:** Provides authentication.
- **Inventory Service:** Manages product inventory and availability.
- **Order Service:** Manage and process customer orders.
- **Reviews Service:** Gather and display product reviews and ratings from users.

# How to run

### Docker Compose

This option uses pre-built Docker images from a Docker Hub registry. Simpler and faster way to run the application.

Clone the repository or just download the
[docker-compose.yaml](https://github.com/micaellobo/e-commerce-store/raw/master/deployment/docker-compose.yaml) file and
run the following command:

        docker-compose up -d

### Local Development with Docker Compose

This option builds new Docker images and packages the application to an JAR file from your current local codebase, can
be a bit slower.

1. Clone the repository.
2. Run the following command:

       docker-compose -f docker-compose-dev.yaml up -d

For local development is preferable to have installed `Java 17`, `Maven` and run the necessary databases with
docker([docker-compose-dbs.yaml](https://githubcom/micaellobo/e-commerce-store/raw/master/deployment/docker-compose-dbs.yaml)). \
And run each service individually with the following command:

        mvn spring-boot:run

### Exploring and Interacting with API

[//]: # (- **Swagger:** http://localhost:8080/swagger-ui.html)
- **Postman Collection:** [postman_collection.json](https://githubcom/micaellobo/e-commerce-store/raw/master/documentation/postman_collection.json)

# Next Steps and Improvements

As the project is actively being developed, there are several possibilities for future enhancements and progress. Here
are a few ideas:

- **API Documentation:** API documentation using `Swagger`.
- **Security:** Transitioning from `JWT` to `OAuth` 2.0 with `Keycloak` for more robust authentication and authorization
  mechanisms.
- **Testing:** Implement integration tests using `testContainers` to closely resemble the production environment.
- **CI/CD:** Set up pipelines to automate testing and deployment processes with `Jenkins`.
- **Container Orchestration:** Explore container orchestration platforms like `Kubernetes`.
