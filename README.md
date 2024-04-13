# e-commerce-store

Spring Boot microservice-based application to manage orders, products, inventory and product reviews.

# Microservices Overview

- **API Gateway:** Routes requests to appropriate microservices.
- **Eureka Service:** Service discovery and registration.
- **User Service:** Handles user management.
- **Auth Service:** Provides authentication.
- **Inventory Service:** Manages product inventory and availability.
- **Order Service:** Manage and process customer orders.
- **Reviews Service:** Gather and display product reviews and ratings from users.
- **Notification Service:** Sends notifications to users.

# How to run

### Docker Compose

Use pre-built Docker images for a fast and straightforward way to run the application:

1. Clone the repository or download the
   [docker-compose.yaml](https://github.com/micaellobo/e-commerce-store/raw/master/deployment/docker-compose.yaml)
2. Run the following command:

        cd deployment && docker-compose up -d

### Local Development with Docker

Build new Docker images and package the application into a JAR file from your local codebase, although it may be a bit
slower.

1. Clone the repository.
2. Run the following command:

       docker-compose -f docker-compose-dev.yaml up -d --build

For smoother local development, it's recommended to have `Java 17` or higher and `Maven` installed. You can also
configure the essential infrastructure
using [docker-compose-infra.yaml](https://github.com/micaellobo/e-commerce-store/raw/master/deployment/docker-compose-infra.yaml). \
Run each service individually with `mvn spring-boot:run`.

### Exploring and Interacting with API

- **Swagger:** http://localhost:8080/swagger
- **Postman Collection:** [postman.json](https://github.com/micaellobo/e-commerce-store/raw/master/documentation/postman.json)

# Next Steps and Improvements

As the project is actively being developed, there are several possibilities for future enhancements and progress. Here
are a few ideas:

- [ ] **Testing:** Implement integration tests using `testContainers` to closely resemble the production environment.
- [ ] **Security:** Transitioning from `JWT` to `OAuth` 2.0 with `Keycloak` for more robust authentication and authorization
  mechanisms.
- [ ] **CI/CD:** Set up pipelines to automate testing and deployment processes with `Jenkins`. Currently
  using `GitHub Actions` to build and push microservices Docker images to `Docker Hub`.
- [ ] **Container Orchestration:** Explore container orchestration platforms like `Kubernetes`.
- [x] **Event-Driven:** Explore asynchronous communication with `Kafka`/ `RabbitMQ`.
- [x] **Observability:**
    - **Grafana:** Data visualization.
    - **OpenTelemetry:** Collect metrics, traces, and logs.
    - **Grafana Loki:** Centralized logging.
    - **Grafana Tempo and Zipkin:** Distributed tracing.
    - **Prometheus:** Metrics monitoring and alerting.