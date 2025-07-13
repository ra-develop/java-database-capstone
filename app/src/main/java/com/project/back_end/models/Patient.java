package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3-100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "Name can only contain letters, spaces, hyphens, and apostrophes")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$", message = "Password must contain at least 1 uppercase and 1 lowercase letter and 1 digit")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    // @NotNull(message = "Date of birth is required")
    // @Past(message = "Date of birth must be in the past")
    // private LocalDate dateOfBirth;

    // @Column(updatable = false)
    // private LocalDate registrationDate = LocalDate.now();

    // private boolean active = true;

    // Constructors
    public Patient() {
    }

    public Patient(String name, String email, String password, String phone, String address, LocalDate dateOfBirth) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        // this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // public LocalDate getDateOfBirth() {
    //     return dateOfBirth;
    // }

    // public void setDateOfBirth(LocalDate dateOfBirth) {
    //     this.dateOfBirth = dateOfBirth;
    // }

    // public LocalDate getRegistrationDate() {
    //     return registrationDate;
    // }

    // public boolean isActive() {
    //     return active;
    // }

    // public void setActive(boolean active) {
    //     this.active = active;
    // }

    // Business Methods
    // @Transient
    // public int getAge() {
    //     return Period.between(dateOfBirth, LocalDate.now()).getYears();
    // }

    // Authentication Support
    @Transient
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }

    // Reporting
    // @Transient
    // @JsonIgnore
    // public String getDemographics() {
    //     return String.format("%s | %d years | %s", name, getAge(), address);
    // }

    // toString() - excludes sensitive data
    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                // ", registrationDate=" + registrationDate +
                '}';
    }
}

// @Entity annotation:
// - Marks the class as a JPA entity, meaning it represents a table in the
// database.
// - Required for persistence frameworks (e.g., Hibernate) to map the class to a
// database table.

// 1. 'id' field:
// - Type: private Long
// - Description:
// - Represents the unique identifier for each patient.
// - The @Id annotation marks it as the primary key.
// - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation
// auto-generates the ID value when a new record is inserted into the database.

// 2. 'name' field:
// - Type: private String
// - Description:
// - Represents the patient's full name.
// - The @NotNull annotation ensures that the patient's name is required.
// - The @Size(min = 3, max = 100) annotation ensures that the name length is
// between 3 and 100 characters.
// - Provides validation for correct input and user experience.

// 3. 'email' field:
// - Type: private String
// - Description:
// - Represents the patient's email address.
// - The @NotNull annotation ensures that an email address must be provided.
// - The @Email annotation validates that the email address follows a valid
// email format (e.g., patient@example.com).

// 4. 'password' field:
// - Type: private String
// - Description:
// - Represents the patient's password for login authentication.
// - The @NotNull annotation ensures that a password must be provided.
// - The @Size(min = 6) annotation ensures that the password must be at least 6
// characters long.

// 5. 'phone' field:
// - Type: private String
// - Description:
// - Represents the patient's phone number.
// - The @NotNull annotation ensures that a phone number must be provided.
// - The @Pattern(regexp = "^[0-9]{10}$") annotation validates that the phone
// number must be exactly 10 digits long.

// 6. 'address' field:
// - Type: private String
// - Description:
// - Represents the patient's address.
// - The @NotNull annotation ensures that the address must be provided.
// - The @Size(max = 255) annotation ensures that the address does not exceed
// 255 characters in length, providing validation for the address input.

// 7. Getters and Setters:
// - Standard getter and setter methods are provided for all fields: id, name,
// email, password, phone, and address.
// - These methods allow access and modification of the fields of the Patient
// class.
