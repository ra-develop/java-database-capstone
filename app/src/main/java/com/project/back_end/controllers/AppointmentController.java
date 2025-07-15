package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.HealthcareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final HealthcareService healthcareService;

    public AppointmentController(AppointmentService appointmentService,
            HealthcareService healthcareService) {
        this.appointmentService = appointmentService;
        this.healthcareService = healthcareService;
    }

    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable LocalDate date,
            @PathVariable String patientName,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = healthcareService.validateToken(token, "doctor");

        if (validationResponse.getStatusCode().isError()) {
            return ResponseEntity.status(validationResponse.getStatusCode())
                    .body(Map.of("error", "Invalid or expired token"));
        }

        return ResponseEntity.ok(appointmentService.getAppointments(patientName, date, token));
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = healthcareService.validateToken(token, "patient");

        if (validationResponse.getStatusCode().isError()) {
            return validationResponse;
        }

        int validationResult = healthcareService.validateAppointment(appointment);
        if (validationResult == -1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Doctor not found"));
        } else if (validationResult == 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Time slot not available"));
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            return ResponseEntity.status(201)
                    .body(Map.of("message", "Appointment booked successfully"));
        } else {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to book appointment"));
        }
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = healthcareService.validateToken(token, "patient");

        if (validationResponse.getStatusCode().isError()) {
            return validationResponse;
        }

        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> validationResponse = healthcareService.validateToken(token, "patient");

        if (validationResponse.getStatusCode().isError()) {
            return validationResponse;
        }

        return appointmentService.cancelAppointment(id, token);
    }
}

// package com.project.back_end.controllers;

// public class AppointmentController {

// 1. Set Up the Controller Class:
// - Annotate the class with `@RestController` to define it as a REST API
// controller.
// - Use `@RequestMapping("/appointments")` to set a base path for all
// appointment-related endpoints.
// - This centralizes all routes that deal with booking, updating, retrieving,
// and canceling appointments.

// 2. Autowire Dependencies:
// - Inject `AppointmentService` for handling the business logic specific to
// appointments.
// - Inject the general `Service` class, which provides shared functionality
// like token validation and appointment checks.

// 3. Define the `getAppointments` Method:
// - Handles HTTP GET requests to fetch appointments based on date and patient
// name.
// - Takes the appointment date, patient name, and token as path variables.
// - First validates the token for role `"doctor"` using the `Service`.
// - If the token is valid, returns appointments for the given patient on the
// specified date.
// - If the token is invalid or expired, responds with the appropriate message
// and status code.

// 4. Define the `bookAppointment` Method:
// - Handles HTTP POST requests to create a new appointment.
// - Accepts a validated `Appointment` object in the request body and a token as
// a path variable.
// - Validates the token for the `"patient"` role.
// - Uses service logic to validate the appointment data (e.g., check for doctor
// availability and time conflicts).
// - Returns success if booked, or appropriate error messages if the doctor ID
// is invalid or the slot is already taken.

// 5. Define the `updateAppointment` Method:
// - Handles HTTP PUT requests to modify an existing appointment.
// - Accepts a validated `Appointment` object and a token as input.
// - Validates the token for `"patient"` role.
// - Delegates the update logic to the `AppointmentService`.
// - Returns an appropriate success or failure response based on the update
// result.

// 6. Define the `cancelAppointment` Method:
// - Handles HTTP DELETE requests to cancel a specific appointment.
// - Accepts the appointment ID and a token as path variables.
// - Validates the token for `"patient"` role to ensure the user is authorized
// to cancel the appointment.
// - Calls `AppointmentService` to handle the cancellation process and returns
// the result.

// }
