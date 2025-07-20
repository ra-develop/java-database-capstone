package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.HealthcareService;
import com.project.back_end.services.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final HealthcareService healthcareService;

    public PatientController(PatientService patientService,
            HealthcareService healthcareService) {
        this.patientService = patientService;
        this.healthcareService = healthcareService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = healthcareService.validateToken(token, "patient");

        if (validationResponse.getStatusCode().isError()) {
            return ResponseEntity.status(validationResponse.getStatusCode())
                    .body(Map.of("error", "Invalid or expired token"));
        }

        return patientService.getPatientDetails(token);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {

        int result = 0;
        try {
            result = patientService.createPatient(patient);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to save patient data: " + e.getMessage()));
        }
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Signup successful"));
        } else {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Some internal error occurred"));
        }


        // if (!healthcareService.validatePatient(patient)) {
        //     return ResponseEntity.badRequest()
        //             .body(Map.of("error", "Patient with email or phone already exists"));
        // }

        // int result = patientService.createPatient(patient);
        // if (result == 1) {
        //     return ResponseEntity.ok(Map.of("message", "Signup successful"));
        // } else {
        //     return ResponseEntity.internalServerError()
        //             .body(Map.of("error", "Internal server error"));
        // }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login login) {
        return healthcareService.validatePatientLogin(login);
    }

    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = healthcareService.validateToken(token, "patient");

        if (validationResponse.getStatusCode().isError()) {
            return ResponseEntity.status(validationResponse.getStatusCode())
                    .body(Map.of("error", "Invalid or expired token"));
        }

        return patientService.getPatientAppointment(id, token);
    }

    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = healthcareService.validateToken(token, "patient");

        if (validationResponse.getStatusCode().isError()) {
            return ResponseEntity.status(validationResponse.getStatusCode())
                    .body(Map.of("error", "Invalid or expired token"));
        }

        return healthcareService.filterPatient(condition, name, token);
    }
}

// package com.project.back_end.controllers;

// public class PatientController {

// 1. Set Up the Controller Class:
// - Annotate the class with `@RestController` to define it as a REST API
// controller for patient-related operations.
// - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`,
// grouping all patient functionalities under a common route.

// 2. Autowire Dependencies:
// - Inject `PatientService` to handle patient-specific logic such as creation,
// retrieval, and appointments.
// - Inject the shared `Service` class for tasks like token validation and login
// authentication.

// 3. Define the `getPatient` Method:
// - Handles HTTP GET requests to retrieve patient details using a token.
// - Validates the token for the `"patient"` role using the shared service.
// - If the token is valid, returns patient information; otherwise, returns an
// appropriate error message.

// 4. Define the `createPatient` Method:
// - Handles HTTP POST requests for patient registration.
// - Accepts a validated `Patient` object in the request body.
// - First checks if the patient already exists using the shared service.
// - If validation passes, attempts to create the patient and returns success or
// error messages based on the outcome.

// 5. Define the `login` Method:
// - Handles HTTP POST requests for patient login.
// - Accepts a `Login` DTO containing email/username and password.
// - Delegates authentication to the `validatePatientLogin` method in the shared
// service.
// - Returns a response with a token or an error message depending on login
// success.

// 6. Define the `getPatientAppointment` Method:
// - Handles HTTP GET requests to fetch appointment details for a specific
// patient.
// - Requires the patient ID, token, and user role as path variables.
// - Validates the token using the shared service.
// - If valid, retrieves the patient's appointment data from `PatientService`;
// otherwise, returns a validation error.

// 7. Define the `filterPatientAppointment` Method:
// - Handles HTTP GET requests to filter a patient's appointments based on
// specific conditions.
// - Accepts filtering parameters: `condition`, `name`, and a token.
// - Token must be valid for a `"patient"` role.
// - If valid, delegates filtering logic to the shared service and returns the
// filtered result.

// }
