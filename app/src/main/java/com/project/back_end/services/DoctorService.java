package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
// import com.project.back_end.models.Login;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final @Lazy TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        // Get doctor's working hours (assuming 9AM-5PM as default)
        List<String> allSlots = generateTimeSlots(LocalTime.of(9, 0), LocalTime.of(17, 0), 30);

        // Get booked appointments for the doctor on this date
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<LocalTime> bookedTimes = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end)
                .stream()
                .map(a -> a.getAppointmentTime().toLocalTime())
                .collect(Collectors.toList());

        // Filter out booked slots
        return allSlots.stream()
                .filter(slot -> !isSlotBooked(slot, bookedTimes))
                .collect(Collectors.toList());
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                throw new ValidationException("Doctor with this email already exists");
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (ConstraintViolationException e) { // Catch ConstraintViolationException directly
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

            List<String> errorMessages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.toList());

            throw new RuntimeException("Validation failed: " + String.join(", ", errorMessages));
        } catch (ValidationException e) {
            throw new RuntimeException("Validation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save doctor: " + e.getMessage());
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) {
                throw new ValidationException("Doctor not found");
            }
            doctorRepository.save(doctor);
            return 1;
        } catch (ConstraintViolationException e) { // Catch ConstraintViolationException directly
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

            List<String> errorMessages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.toList());

            throw new RuntimeException("Validation failed: " + String.join(", ", errorMessages));
        } catch (ValidationException e) {
            throw new RuntimeException("Validation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save doctor: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(long id) {
        try {
            if (!doctorRepository.existsById(id)) {
                throw new ValidationException("Doctor not found");
            }
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save doctor: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());

        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }

        String token = tokenService.generateToken(doctor.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        response.put("doctors", doctors);
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        response.put("doctors", filtered);
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        response.put("doctors", filtered);
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        response.put("doctors", doctors);
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        response.put("doctors", filtered);
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        response.put("doctors", doctors);
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> filtered = filterDoctorByTime(doctors, amOrPm);
        response.put("doctors", filtered);
        return response;
    }

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(doctor -> isAvailableDuringPeriod(doctor, amOrPm))
                .collect(Collectors.toList());
    }

    // Helper methods
    private List<String> generateTimeSlots(LocalTime start, LocalTime end, int durationMinutes) {
        List<String> slots = new ArrayList<>();
        while (start.isBefore(end)) {
            slots.add(start.toString());
            start = start.plusMinutes(durationMinutes);
        }
        return slots;
    }

    private boolean isSlotBooked(String slot, List<LocalTime> bookedTimes) {
        LocalTime slotTime = LocalTime.parse(slot);
        return bookedTimes.stream().anyMatch(booked -> booked.equals(slotTime));
    }

    // private boolean isAvailableDuringPeriod(Doctor doctor, String amOrPm) {
    // // Implementation depends on how available times are stored in Doctor entity
    // // This is a placeholder implementation
    // return true;
    // }

    /**
     * Checks if the doctor is available during the specified period (AM/PM)
     * 
     * @param doctor The doctor to check availability for
     * @param amOrPm The period to check ("AM" or "PM")
     * @return true if available during the specified period, false otherwise
     */
    private boolean isAvailableDuringPeriod(Doctor doctor, String amOrPm) {
        if (doctor == null || amOrPm == null) {
            return false;
        }

        // Convert input to uppercase for case-insensitive comparison
        String period = amOrPm.toUpperCase();
        if (!period.equals("AM") && !period.equals("PM")) {
            throw new IllegalArgumentException("Period must be either 'AM' or 'PM'");
        }

        // Check both availableTimes list and availability map
        if (doctor.getAvailableTimes() != null && !doctor.getAvailableTimes().isEmpty()) {
            // Implementation for availableTimes (List<String>)
            return doctor.getAvailableTimes().stream()
                    .anyMatch(timeSlot -> isTimeSlotInPeriod(timeSlot, period));
        } else if (doctor.getAvailability() != null && !doctor.getAvailability().isEmpty()) {
            // Implementation for availability (Map<DayOfWeek, Set<LocalTime>>)
            return doctor.getAvailability().values().stream()
                    .flatMap(Set::stream)
                    .anyMatch(time -> isLocalTimeInPeriod(time, period));
        }

        return false;
    }

    // Helper method for List<String> availableTimes
    private boolean isTimeSlotInPeriod(String timeSlot, String period) {
        try {
            String[] parts = timeSlot.split("-");
            if (parts.length != 2)
                return false;

            LocalTime start = LocalTime.parse(parts[0].trim());
            LocalTime end = LocalTime.parse(parts[1].trim());

            // AM period: before 12:00
            if (period.equals("AM")) {
                return start.isBefore(LocalTime.NOON) || end.isBefore(LocalTime.NOON);
            }
            // PM period: 12:00 or later
            else {
                return !start.isBefore(LocalTime.NOON) || !end.isBefore(LocalTime.NOON);
            }
        } catch (Exception e) {
            return false;
        }
    }

    // Helper method for Map<DayOfWeek, Set<LocalTime>> availability
    private boolean isLocalTimeInPeriod(LocalTime time, String period) {
        if (period.equals("AM")) {
            return time.isBefore(LocalTime.NOON);
        } else {
            return !time.isBefore(LocalTime.NOON);
        }
    }
}

// package com.project.back_end.services;

// public class DoctorService {

// 1. **Add @Service Annotation**:
// - This class should be annotated with `@Service` to indicate that it is a
// service layer class.
// - The `@Service` annotation marks this class as a Spring-managed bean for
// business logic.
// - Instruction: Add `@Service` above the class declaration.

// 2. **Constructor Injection for Dependencies**:
// - The `DoctorService` class depends on `DoctorRepository`,
// `AppointmentRepository`, and `TokenService`.
// - These dependencies should be injected via the constructor for proper
// dependency management.
// - Instruction: Ensure constructor injection is used for injecting
// dependencies into the service.

// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database
// Data**:
// - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`,
// `filterDoctorsBy*` should be annotated with `@Transactional`.
// - The `@Transactional` annotation ensures that database operations are
// consistent and wrapped in a single transaction.
// - Instruction: Add the `@Transactional` annotation above the methods that
// perform database operations or queries.

// 4. **getDoctorAvailability Method**:
// - Retrieves the available time slots for a specific doctor on a particular
// date and filters out already booked slots.
// - The method fetches all appointments for the doctor on the given date and
// calculates the availability by comparing against booked slots.
// - Instruction: Ensure that the time slots are properly formatted and the
// available slots are correctly filtered.

// 5. **saveDoctor Method**:
// - Used to save a new doctor record in the database after checking if a doctor
// with the same email already exists.
// - If a doctor with the same email is found, it returns `-1` to indicate
// conflict; `1` for success, and `0` for internal errors.
// - Instruction: Ensure that the method correctly handles conflicts and
// exceptions when saving a doctor.

// 6. **updateDoctor Method**:
// - Updates an existing doctor's details in the database. If the doctor doesn't
// exist, it returns `-1`.
// - Instruction: Make sure that the doctor exists before attempting to save the
// updated record and handle any errors properly.

// 7. **getDoctors Method**:
// - Fetches all doctors from the database. It is marked with `@Transactional`
// to ensure that the collection is properly loaded.
// - Instruction: Ensure that the collection is eagerly loaded, especially if
// dealing with lazy-loaded relationships (e.g., available times).

// 8. **deleteDoctor Method**:
// - Deletes a doctor from the system along with all appointments associated
// with that doctor.
// - It first checks if the doctor exists. If not, it returns `-1`; otherwise,
// it deletes the doctor and their appointments.
// - Instruction: Ensure the doctor and their appointments are deleted properly,
// with error handling for internal issues.

// 9. **validateDoctor Method**:
// - Validates a doctor's login by checking if the email and password match an
// existing doctor record.
// - It generates a token for the doctor if the login is successful, otherwise
// returns an error message.
// - Instruction: Make sure to handle invalid login attempts and password
// mismatches properly with error responses.

// 10. **findDoctorByName Method**:
// - Finds doctors based on partial name matching and returns the list of
// doctors with their available times.
// - This method is annotated with `@Transactional` to ensure that the database
// query and data retrieval are properly managed within a transaction.
// - Instruction: Ensure that available times are eagerly loaded for the
// doctors.

// 11. **filterDoctorsByNameSpecilityandTime Method**:
// - Filters doctors based on their name, specialty, and availability during a
// specific time (AM/PM).
// - The method fetches doctors matching the name and specialty criteria, then
// filters them based on their availability during the specified time period.
// - Instruction: Ensure proper filtering based on both the name and specialty
// as well as the specified time period.

// 12. **filterDoctorByTime Method**:
// - Filters a list of doctors based on whether their available times match the
// specified time period (AM/PM).
// - This method processes a list of doctors and their available times to return
// those that fit the time criteria.
// - Instruction: Ensure that the time filtering logic correctly handles both AM
// and PM time slots and edge cases.

// 13. **filterDoctorByNameAndTime Method**:
// - Filters doctors based on their name and the specified time period (AM/PM).
// - Fetches doctors based on partial name matching and filters the results to
// include only those available during the specified time period.
// - Instruction: Ensure that the method correctly filters doctors based on the
// given name and time of day (AM/PM).

// 14. **filterDoctorByNameAndSpecility Method**:
// - Filters doctors by name and specialty.
// - It ensures that the resulting list of doctors matches both the name
// (case-insensitive) and the specified specialty.
// - Instruction: Ensure that both name and specialty are considered when
// filtering doctors.

// 15. **filterDoctorByTimeAndSpecility Method**:
// - Filters doctors based on their specialty and availability during a specific
// time period (AM/PM).
// - Fetches doctors based on the specified specialty and filters them based on
// their available time slots for AM/PM.
// - Instruction: Ensure the time filtering is accurately applied based on the
// given specialty and time period (AM/PM).

// 16. **filterDoctorBySpecility Method**:
// - Filters doctors based on their specialty.
// - This method fetches all doctors matching the specified specialty and
// returns them.
// - Instruction: Make sure the filtering logic works for case-insensitive
// specialty matching.

// 17. **filterDoctorsByTime Method**:
// - Filters all doctors based on their availability during a specific time
// period (AM/PM).
// - The method checks all doctors' available times and returns those available
// during the specified time period.
// - Instruction: Ensure proper filtering logic to handle AM/PM time periods.

// }
