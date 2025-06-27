# Employee Service API

This project is a RESTful employee management service developed with Spring Boot. It provides CRUD (Create, Read, Update, Delete) operations for employee records and uses MySQL as the database, with a Docker-ready configuration.

## 🚀 Technologies Used

* **Java 11**
* **Spring Boot 2.7.5**
* **Apache Maven**
* **MySQL 8+** (via Docker)
* **Spring Data JPA**
* **Lombok**
* **MapStruct**
* **JUnit 5**
* **Mockito**
* **SpringDoc OpenAPI (Swagger UI)**
* **Docker & Docker Compose**
* **HTTPS** (configured with PKCS12 certificate)

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

* **Java Development Kit (JDK) 11**
* **Apache Maven 3.6+**
* **Docker Desktop** (includes Docker Engine and Docker Compose)
* **Git** (to clone the repository)

## 💻 Development Environment Setup

### 1. Clone the Repository

First, clone this repository to your local machine:

```bash
git clone [https://github.com/ozzielAZ/employee-service.git](https://github.com/ozzielAZ/employee-service.git)

2. Database Configuration (with Docker Compose)
The project uses Docker Compose to spin up a MySQL instance and your Spring Boot application

3. Log Volumes
The docker-compose.yml mounts a volume for application logs. It's important that the logs directory exists at the same level as your employee-service project root folder. The directory structure should look like this:

logs/
   service-employee/  # Application logs will be stored here
employee-service/      # Your cloned GitHub project

Installation and Compilation
Once in the root of the project (employee-service):

Compile the project and resolve dependencies:

Bash

mvn clean install
This command will download all necessary dependencies, run the tests, and build the .jar file in the target/ directory.

🚀 Running the Application
You have two primary ways to run the application:

Option 1: Run with Docker Compose (Recommended for Development)
This is the easiest way, as it brings up both the database and your application in containers, isolated from your host system.

Ensure Docker Desktop is running.

From the root of your project (employee-service), execute:

Bash

docker-compose up --build
--build: Rebuilds the application image if there are code changes. If you haven't made changes and have already built the image, you can omit this flag.

The first time, this might take a while as Docker downloads images and builds your application image.

Once the containers are up, the application will be accessible at https://localhost:8881.

Option 2: Run from Maven (Application Only)
You can run the application directly using Maven, provided you have a running MySQL instance available (either locally or by running only the db service from Docker Compose).

If you intend to use the MySQL database from Docker Compose, ensure the db service is running:

Bash

docker-compose up db
(You can open another terminal for the application).

Run the Spring Boot application:

Bash

mvn spring-boot:run
The application will start and be accessible at https://localhost:8881.

🌐 API Endpoints
The API will be available at https://localhost:8881.

You can access the interactive Swagger UI documentation at:

Swagger UI: https://localhost:8881/swagger-ui.html

API Docs (JSON): https://localhost:8881/v3/api-docs

Here are some of the main endpoints:

Method

Path

Description

POST

/api/employees

Creates one or more new employees.

GET

/api/employees

Retrieves a list of all employees.

GET

/api/employees/{id}

Retrieves an employee by their ID.

PUT

/api/employees/{id}

Updates an existing employee by their ID.

DELETE

/api/employees/{id}

Deletes an employee by their ID.

GET

/api/employees/search?name={term}

Searches for employees by full-text in first and last names.


✅ Running Tests
The project includes unit and integration tests that you can execute using Maven.

From the root of your project, run all tests:

Bash

mvn test
This command will execute the tests defined in the src/test/java directory.
You will see a summary of the test results in the console output.

🤝 Contribution
This project follows the Git Flow branching model. If you wish to contribute, please follow these guidelines:

Clone the repository and ensure you have main and develop branches updated locally.

Start a new feature branch from develop:

Bash

git flow feature start my-new-feature
Develop your changes in this branch, making meaningful and descriptive commits.

Ensure all tests pass (mvn test). If you add new functionality, write tests for it.

Finish your feature when it's complete and ready to be integrated into develop:

Bash

git flow feature finish my-new-feature
This will merge your changes into develop and delete your local feature branch.

Push your develop branch changes to GitHub:

Bash

git push origin develop
For topics related to releases (deployments to production) or hotfixes (quick fixes in production), please consult the official Git Flow documentation or the project maintainer.