package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;


@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final @Lazy TokenService tokenService;

    public AppointmentService(AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            // Validate patient exists
            Optional<Patient> patient = patientRepository.findById(appointment.getPatient().getId());
            if (patient.isEmpty()) {
                return 0; // Patient not found
            }

            // Validate doctor exists and is available
            Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctor().getId());
            if (doctor.isEmpty()) {
                return 0; // Doctor not found
            }

            // Check if doctor is available at the requested time
            // if (appointmentRepository.existsByDoctorIdAndAppointmentTime(
            //         appointment.getDoctor().getId(), appointment.getAppointmentTime())) {
            //     return 0; // Time slot already booked
            // }

            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> existingAppointment = appointmentRepository.findById(appointment.getId());
        if (existingAppointment.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate patient exists
        if (!patientRepository.existsById(appointment.getPatient().getId())) {
            response.put("message", "Patient not found");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate doctor exists
        if (!doctorRepository.existsById(appointment.getDoctor().getId())) {
            response.put("message", "Doctor not found");
            return ResponseEntity.badRequest().body(response);
        }

        // // Check if new time slot is available
        // if (appointmentRepository.existsByDoctorIdAndAppointmentTimeAndIdNot(
        //         appointment.getDoctor().getId(), appointment.getAppointmentTime(), appointment.getId())) {
        //     response.put("message", "Doctor already has an appointment at this time");
        //     return ResponseEntity.badRequest().body(response);
        // }

        try {
            appointmentRepository.save(appointment);
            response.put("message", "Appointment updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to update appointment: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
            // throw new RuntimeException("Failed to save doctor: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }

        // Verify patient exists and owns the appointment
        String userEmail = tokenService.extractIdentifier(token);
        Optional<Patient> patient = Optional.ofNullable(patientRepository.findByEmail(userEmail));
        if (patient.isEmpty() || !appointment.get().getPatient().getId().equals(patient.get().getId())) {
            response.put("message", "You can only cancel your own appointments");
            return ResponseEntity.badRequest().body(response);
        }


        try {
            appointmentRepository.delete(appointment.get());
            response.put("message", "Appointment cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to cancel appointment");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAppointments(String pname, LocalDate date, String token) {
        Map<String, Object> response = new HashMap<>();

        // Verify doctor exists
        String userEmail = tokenService.extractIdentifier(token);
        Optional<Doctor> doctor = Optional.ofNullable(doctorRepository.findByEmail(userEmail));
        if (doctor.isEmpty()) {
            response.put("message", "Doctor not found");
            return response;
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments;
        if (pname.equals("null") || pname.isEmpty()) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctor.get().getId(), start, end);
        } else {
            appointments = appointmentRepository
                    .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                            doctor.get().getId(), pname, start, end);
        }

        response.put("appointments", appointments);
        return response;
    }

    @Transactional
    public void changeStatus(long id, int status) {
        appointmentRepository.updateStatus(status, id);
    }

    // private boolean validateAppointment(Appointment appointment) {
    //     // Additional validation logic can be added here
    //     return patientRepository.existsById(appointment.getPatient().getId()) &&
    //             doctorRepository.existsById(appointment.getDoctor().getId());
    // }
}

// package com.project.back_end.services;

// public class AppointmentService {
// 1. **Add @Service Annotation**:
// - To indicate that this class is a service layer class for handling business
// logic.
// - The `@Service` annotation should be added before the class declaration to
// mark it as a Spring service component.
// - Instruction: Add `@Service` above the class definition.

// 2. **Constructor Injection for Dependencies**:
// - The `AppointmentService` class requires several dependencies like
// `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and
// `DoctorRepository`.
// - These dependencies should be injected through the constructor.
// - Instruction: Ensure constructor injection is used for proper dependency
// management in Spring.

// 3. **Add @Transactional Annotation for Methods that Modify Database**:
// - The methods that modify or update the database should be annotated with
// `@Transactional` to ensure atomicity and consistency of the operations.
// - Instruction: Add the `@Transactional` annotation above methods that
// interact with the database, especially those modifying data.

// 4. **Book Appointment Method**:
// - Responsible for saving the new appointment to the database.
// - If the save operation fails, it returns `0`; otherwise, it returns `1`.
// - Instruction: Ensure that the method handles any exceptions and returns an
// appropriate result code.

// 5. **Update Appointment Method**:
// - This method is used to update an existing appointment based on its ID.
// - It validates whether the patient ID matches, checks if the appointment is
// available for updating, and ensures that the doctor is available at the
// specified time.
// - If the update is successful, it saves the appointment; otherwise, it
// returns an appropriate error message.
// - Instruction: Ensure proper validation and error handling is included for
// appointment updates.

// 6. **Cancel Appointment Method**:
// - This method cancels an appointment by deleting it from the database.
// - It ensures the patient who owns the appointment is trying to cancel it and
// handles possible errors.
// - Instruction: Make sure that the method checks for the patient ID match
// before deleting the appointment.

// 7. **Get Appointments Method**:
// - This method retrieves a list of appointments for a specific doctor on a
// particular day, optionally filtered by the patient's name.
// - It uses `@Transactional` to ensure that database operations are consistent
// and handled in a single transaction.
// - Instruction: Ensure the correct use of transaction boundaries, especially
// when querying the database for appointments.

// 8. **Change Status Method**:
// - This method updates the status of an appointment by changing its value in
// the database.
// - It should be annotated with `@Transactional` to ensure the operation is
// executed in a single transaction.
// - Instruction: Add `@Transactional` before this method to ensure atomicity
// when updating appointment status.

// }
