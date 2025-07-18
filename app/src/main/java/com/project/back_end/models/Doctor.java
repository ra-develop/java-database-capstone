package com.project.back_end.models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3-100 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name can only contain letters and spaces")
    private String name;

    @NotNull
    @NotBlank(message = "Specialty is required")
    @Size(min = 3, max = 50, message = "Specialty must be between 3-50 characters")
    private String specialty;

    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least 1 uppercase, 1 lowercase, 1 digit and 1 special character")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> availableTimes;

    @ElementCollection
    @CollectionTable(name = "doctor_availability", joinColumns = @JoinColumn(name = "doctor_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "time_slots")
    @JsonIgnore
    private Map<DayOfWeek, Set<LocalTime>> availability;

    // Constructors
    public Doctor() {
    }

    public Doctor(String name, String specialty, String email, String password, String phone) {
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.password = password;
        this.phone = phone;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
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

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public Map<DayOfWeek, Set<LocalTime>> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<DayOfWeek, Set<LocalTime>> availability) {
        this.availability = availability;
    }

    // Business Methods
    @Transient
    @JsonIgnore
    public boolean isAvailable(DayOfWeek day, LocalTime time) {
        return availability != null &&
                availability.containsKey(day) &&
                availability.get(day).contains(time);
    }

    // toString() - excludes sensitive data
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
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
// - Represents the unique identifier for each doctor.
// - The @Id annotation marks it as the primary key.
// - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation
// auto-generates the ID value when a new record is inserted into the database.

// 2. 'name' field:
// - Type: private String
// - Description:
// - Represents the doctor's name.
// - The @NotNull annotation ensures that the doctor's name is required.
// - The @Size(min = 3, max = 100) annotation ensures that the name length is
// between 3 and 100 characters.
// - Provides validation for correct input and user experience.

// 3. 'specialty' field:
// - Type: private String
// - Description:
// - Represents the medical specialty of the doctor.
// - The @NotNull annotation ensures that a specialty must be provided.
// - The @Size(min = 3, max = 50) annotation ensures that the specialty name is
// between 3 and 50 characters long.

// 4. 'email' field:
// - Type: private String
// - Description:
// - Represents the doctor's email address.
// - The @NotNull annotation ensures that an email address is required.
// - The @Email annotation validates that the email address follows a valid
// email format (e.g., doctor@example.com).

// 5. 'password' field:
// - Type: private String
// - Description:
// - Represents the doctor's password for login authentication.
// - The @NotNull annotation ensures that a password must be provided.
// - The @Size(min = 6) annotation ensures that the password must be at least 6
// characters long.
// - The @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) annotation
// ensures that the password is not serialized in the response (hidden from the
// frontend).

// 6. 'phone' field:
// - Type: private String
// - Description:
// - Represents the doctor's phone number.
// - The @NotNull annotation ensures that a phone number must be provided.
// - The @Pattern(regexp = "^[0-9]{10}$") annotation validates that the phone
// number must be exactly 10 digits long.

// 7. 'availableTimes' field:
// - Type: private List<String>
// - Description:
// - Represents the available times for the doctor in a list of time slots.
// - Each time slot is represented as a string (e.g., "09:00-10:00",
// "10:00-11:00").
// - The @ElementCollection annotation ensures that the list of time slots is
// stored as a separate collection in the database.

// 8. Getters and Setters:
// - Standard getter and setter methods are provided for all fields: id, name,
// specialty, email, password, phone, and availableTimes.
