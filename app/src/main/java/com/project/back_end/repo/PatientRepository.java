package com.project.back_end.repo;

import com.project.back_end.models.Patient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Finds a patient by their exact name
     * @param name The name to search for
     * @return The Patient entity matching the name, or null if not found
     */
    @Query("SELECT p FROM Doctor p WHERE p.name LIKE CONCAT('%', :name, '%')")
    List<Patient> findByNameLike(String name);

    /**
     * Finds a patient by their exact email address
     * @param email The email address to search for
     * @return The Patient entity matching the email, or null if not found
     */
    Patient findByEmail(String email);

    /**
     * Finds a patient by either their email address or phone number
     * @param email The email address to search for
     * @param phone The phone number to search for
     * @return The Patient entity matching either the email or phone, or null if not found
     */
    Patient findByEmailOrPhone(String email, String phone);
}

// package com.project.back_end.repo;

// public interface PatientRepository {
    // 1. Extend JpaRepository:
//    - The repository extends JpaRepository<Patient, Long>, which provides basic CRUD functionality.
//    - This allows the repository to perform operations like save, delete, update, and find without needing to implement these methods manually.
//    - JpaRepository also includes features like pagination and sorting.

// Example: public interface PatientRepository extends JpaRepository<Patient, Long> {}

// 2. Custom Query Methods:

//    - **findByEmail**:
//      - This method retrieves a Patient by their email address.
//      - Return type: Patient
//      - Parameters: String email

//    - **findByEmailOrPhone**:
//      - This method retrieves a Patient by either their email or phone number, allowing flexibility for the search.
//      - Return type: Patient
//      - Parameters: String email, String phone

// 3. @Repository annotation:
//    - The @Repository annotation marks this interface as a Spring Data JPA repository.
//    - Spring Data JPA automatically implements this repository, providing the necessary CRUD functionality and custom queries defined in the interface.


// }

