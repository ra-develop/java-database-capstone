# Smart Clinic Management System: 

## Architecture Design Document

### 1. Architecture summary
--------------------

This Spring Boot application combines MVC and REST controllers. Thymeleaf templates power the Admin and Doctor dashboards, while REST APIs handle all other modules. The system integrates two databases—MySQL (for structured patient, doctor, appointment, and admin data) and MongoDB (for flexible prescription records). Controllers forward requests to a shared service layer, which coordinates with JPA repositories (MySQL) and document-based repositories (MongoDB). MySQL relies on JPA entities, while MongoDB uses schema-free document models.

**Three-Tier Spring Boot Application**

1.  Presentation Tier:
    
    *   **Thymeleaf** for Admin/Doctor dashboards (server-rendered HTML).
        
    *   **REST APIs** for other modules (JSON responses).
        
2.  Application Tier:
    
    *   **Controllers**: MVC (Thymeleaf) + REST endpoints.
        
    *   **Service Layer**: Unified business logic (integrates both databases).
        
3.  Data Tier:
    
    *   **MySQL** (JPA): Structured data (Patient, Doctor, Appointment).
        
    *   **MongoDB**: Document-based prescriptions.
        

**Key Features**:

*   Hybrid MVC/REST design.
    
*   Dual-database support via shared service layer.
    
*   Scalable, containerized, and CI/CD-ready.



[Top](#architecture-design-document)

---

### 2. Numbered flow of data and control

1.  User accesses AdminDashboard/DoctorDashboard (Thymeleaf) or Appointment/Patient modules (REST API).**
    
2.  The request is routed to either Thymeleaf MVC controllers (for dashboards) or REST controllers (for API modules).**
    
3.  The controller validates the request and calls the appropriate service layer method.**
    
4.  Service layer executes business logic and interacts with:**
    
    *   MySQL repositories (JPA) for patient/doctor/appointment data
        
    *   MongoDB repository for prescription records
        
5.  Databases process queries and return results to repositories.**
    
6.  Service layer receives data and transforms/combines it as needed.**
    
7.  Final response is returned:**
    
    *   Thymeleaf controllers render HTML templates with model data
        
    *   REST controllers return JSON responses to API clients

**Key Notes**:

*   Steps 1–2: Entry points (UI/API).
    
*   Steps 3–5: Backend processing (controllers → services → databases).
    
*   Steps 6–7: Data transformation and output.

