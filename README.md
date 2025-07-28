# Java Database Capstone: Smart Clinic Management System

## Overview
This is the capstone project for the [IBM Java Developer Certification Course on Coursera](https://www.coursera.org/professional-certificates/ibm-full-stack-software-developer). It is a healthcare application that manages doctors, patients, appointments, and prescriptions using a Java/Spring Boot backend and a relational database. The project showcases my skills in building scalable backend solutions with REST APIs and front-end integration, relevant for enterprise roles like Solutions Architect or Android Developer.

## Features
- **Java Backend**: Implements logic for managing healthcare data using Spring Boot.
- **Database Integration**: Supports CRUD operations (Create, Read, Update, Delete) with a relational database.
- **REST API**: Provides endpoints for appointments, doctors, patients, and prescriptions.
- **Front-End**: Includes HTML, CSS, and JavaScript for user dashboards (admin, doctor, patient).
- **Security**: Uses authentication and authorization (e.g., `SecurityConfig.java`, `TokenService.java`).
- **Clean Code**: Follows MVC architecture and object-oriented principles.

## Technologies Used
- **Java**: Core language for backend logic.
- **Spring Boot**: Framework for REST APIs and MVC architecture.
- **JDBC/JPA**: For database connectivity and queries.
- **Maven**: For project management and dependencies.
- **Database**: Relational database (e.g., MySQL, PostgreSQL, or H2).
- **HTML/CSS/JavaScript**: For front-end dashboards and user interfaces.
- **Docker**: For containerization (via `Dockerfile`).

## Project Structure
```
java-database-capstone/
├── .github/                    # GitHub workflows for CI/CD
│   └── workflows/             # Workflow files for compilation and linting
│       ├── compile-backend.yml
│       ├── lint-backend.yml
│       ├── lint-docker.yml
│       ├── lint-frontend.yml
├── app/                       # Main application directory
│   ├── src/                   # Source code
│   │   ├── main/             # Main application code
│   │   │   ├── java/         # Java source files
│   │   │   │   └── com/project/back_end/
│   │   │   │       ├── config/             # Configuration classes
│   │   │   │       ├── controllers/         # REST API controllers
│   │   │   │       ├── DTO/                # Data Transfer Objects
│   │   │   │       ├── models/             # Entity models
│   │   │   │       ├── mvc/                # MVC controllers
│   │   │   │       ├── repo/               # Repository interfaces
│   │   │   │       ├── services/           # Service layer logic
│   │   │   │       ├── BackEndApplication.java
│   │   │   ├── resources/    # Front-end and configuration files
│   │   │   │   ├── static/   # Static assets (CSS, JS, images)
│   │   │   │   ├── templates/ # HTML templates for dashboards
│   │   │   │   ├── application.properties
│   │   ├── test/             # Test files
│   │   │   ├── java/com/project/back_end/
│   │   │   │   ├── BackEndApplicationTests.java
│   ├── target/               # Compiled output
│   ├── Dockerfile            # Docker configuration
│   ├── HELP-DB.md            # Database help documentation
│   ├── mvnw                  # Maven wrapper (Unix)
│   ├── mvnw.cmd              # Maven wrapper (Windows)
│   ├── pom.xml               # Maven configuration
├── curl-tests/               # Curl test scripts
│   ├── apointments.json
│   ├── doctor-filter.json
│   ├── doctors.json
│   ├── patient-data.json
│   ├── patient-login.json
├── sql-tests/                # SQL test scripts
│   ├── GetDailyAppointmentReportByDoctor.sql
│   ├── GetDoctorWithMostPatientsByMonth.sql
│   ├── GetDoctorWithMostPatientsByYear.sql
│   ├── patients.sql
│   ├── tables.sql
├── user_stories/             # User story documentation
│   ├── user_stories_admin.md
│   ├── user_stories_doctor.md
│   ├── user_stories_patient.md
│   ├── user_stories_template.md
├── .DS_Store                 # macOS system file
├── .gitignore                # Git ignore file
├── doctor-filter.json        # JSON test data
├── LICENSE                   # MIT License file
├── README.md                 # Project documentation
├── schema-architecture.md    # Architecture documentation
└── schema-design.md          # Database schema design
```

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ra-develop/java-database-capstone.git
   cd java-database-capstone
   ```
2. **Install Dependencies**:
   - Install Java (JDK 8 or higher) and Maven.
   - Run `./mvnw install` (or `mvnw.cmd install` on Windows) to download dependencies.
3. **Configure Database**:
   - Set up a relational database (e.g., MySQL, PostgreSQL, or H2).
   - Update `app/src/main/resources/application.properties` with database URL, username, and password.
   - See `HELP-DB.md` for database setup details.
4. **Run the Application**:
   - Build the project: `./mvnw clean package`.
   - Run the application: `java -jar app/target/java-database-capstone.jar`.
5. **Test the API**:
   - Use `curl-tests/` scripts (e.g., `apointments.json`) with curl or Postman to test endpoints (e.g., `GET /api/appointments`).
   - Check `sql-tests/` for database query tests.
6. **Run with Docker**:
   - Build the Docker image: `docker build -t java-database-capstone .`.
   - Run the container: `docker run -p 8080:8080 java-database-capstone`.

## Learning Outcomes
- Mastered Java and Spring Boot for backend development.
- Learned JDBC/JPA for database operations.
- Designed RESTful APIs for healthcare data management.
- Built front-end dashboards with HTML, CSS, and JavaScript.
- Applied Docker for containerization and CI/CD with GitHub Actions.
- Used clean coding and MVC principles for scalability.

## About Me
I’m Rashid Amanzholov, a software developer with over 10 years of experience in mobile (Flutter, Kotlin) and backend development. This project is part of my IBM Java Developer certification, showcasing my skills in building enterprise backend solutions. Visit my [LinkedIn](https://www.linkedin.com/in/rashid-amanzholov) and [GitHub](https://github.com/ra-develop) for more projects.

## License
This project is licensed under the Apache License. See the `LICENSE` file for details.