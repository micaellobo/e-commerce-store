# e-commerce-store

Spring microservice-based application to manage orders, products, inventory and product reviews.

## Microservices Overview

- **Eureka Service:** Service discovery and registration.
- **API Gateway:** Routes requests to appropriate microservices.
- **User Service:** Handles user management.
- **Auth Service:** Provides authentication.
- **Inventory Service:** Manages product inventory and availability.
- **Reviews Service:** Gather and display product reviews and ratings from users.
- **Reviews Service:** Manages product reviews and ratings.
- **Order Service:** Manage and process customer orders.

## How to run

Clone the repository or just download the
[docker-compose.yaml](https://github.com/micaellobo/e-commerce-store/raw/master/docker-compose.yaml) file and run:

        docker-compose up -d

## Next Steps and Improvements

As the project is actively being developed, there are several possibilities for future enhancements and progress. Here are a few ideas:

- **API Documentation:** API documentation using `Swagger`.
- **Security:** Use OAuth 2.0 with `Keycloak` instead of JWT.
- **Testing:** Implement integration tests using `testContainers` to closely resemble the production environment.
- **CI/CD:** Set up pipelines to automate testing and deployment processes with `Jenkins`.
- **Container Orchestration:** Explore container orchestration platforms like `Kubernetes`.
