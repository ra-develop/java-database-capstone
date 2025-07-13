package com.project.back_end.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;

    @NotBlank(message = "Patient name is required")
    @Size(min = 3, max = 100, message = "Patient name must be 3-100 characters")
    @Field("patient_name")
    private String patientName;

    @NotNull(message = "Appointment reference is required")
    @Field("appointment_id")
    private Long appointmentId;

    @NotBlank(message = "Doctor ID is required")
    @Field("doctor_id")
    private String doctorId;

    @NotNull(message = "Medications are required")
    @Size(min = 1, max = 100, message = "At least one medication must be specified")
    @NotNull
    private String medication;

    @NotNull(message = "Medications are required")
    @Size(min = 1, message = "At least one medication must be specified")
    private List<Medication> medications;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Field("doctor_notes")
    private String doctorNotes;

    @Field("creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Field("is_active")
    private boolean active = true;

    // Nested Medication class
    public static class Medication {
        @NotBlank(message = "Medication name is required")
        @Size(min = 3, max = 100, message = "Medication name cannot exceed 100 characters")
        private String name;

        // @NotBlank(message = "Dosage is required")
        // @Size(max = 50, message = "Dosage instructions cannot exceed 50 characters")
        private String dosage;

        // @NotBlank(message = "Frequency is required")
        // @Size(max = 50, message = "Frequency cannot exceed 50 characters")
        private String frequency;

        // @Min(value = 1, message = "Duration must be at least 1 day")
        private int durationDays;

        // Constructors, Getters and Setters
        public Medication() {}

        public Medication(String name, String dosage, String frequency, int durationDays) {
            this.name = name;
            this.dosage = dosage;
            this.frequency = frequency;
            this.durationDays = durationDays;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }
        public int getDurationDays() { return durationDays; }
        public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    }

    // Temporary function to support simple medication functionality
    private void medicationToMedications(String medication) {
        Medication medicationExt = new Medication();
        medicationExt.setName(medication);
        this.medications.clear();
        this.medications.add(medicationExt);
    }

    // Constructors
    public Prescription() {}

    public Prescription(String patientName, Long appointmentId, String doctorId, String medication) {
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.medication = medication;
        this.medicationToMedications(medication);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getMedication() { return medication; }
    public void setMedication(String medication) { 
      this.medication = medication; 
      this.medicationToMedications(medication);
    }
    
    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }
    public String getDoctorNotes() { return doctorNotes; }
    public void setDoctorNotes(String doctorNotes) { this.doctorNotes = doctorNotes; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // Business Methods
    public boolean containsMedication(String medicationName) {
        return medications.stream()
                .anyMatch(med -> med.getName().equalsIgnoreCase(medicationName));
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id='" + id + '\'' +
                ", patientName='" + patientName + '\'' +
                ", appointmentId=" + appointmentId +
                ", creationDate=" + creationDate +
                '}';
    }
}

// @Document annotation:
  // - Marks the class as a MongoDB document (a collection in MongoDB).
  // - The collection name is specified as "prescriptions" to map this class to
  // the "prescriptions" collection in MongoDB.

  // 1. 'id' field:
  // - Type: private String
  // - Description:
  // - Represents the unique identifier for each prescription.
  // - The @Id annotation marks it as the primary key in the MongoDB collection.
  // - The id is of type String, which is commonly used for MongoDB's ObjectId as
  // it stores IDs as strings in the database.

  // 2. 'patientName' field:
  // - Type: private String
  // - Description:
  // - Represents the name of the patient receiving the prescription.
  // - The @NotNull annotation ensures that the patient name is required.
  // - The @Size(min = 3, max = 100) annotation ensures that the name length is
  // between 3 and 100 characters, ensuring a reasonable name length.

  // 3. 'appointmentId' field:
  // - Type: private Long
  // - Description:
  // - Represents the ID of the associated appointment where the prescription was
  // given.
  // - The @NotNull annotation ensures that the appointment ID is required for the
  // prescription.

  // 4. 'medication' field:
  // - Type: private String
  // - Description:
  // - Represents the medication prescribed to the patient.
  // - The @NotNull annotation ensures that the medication name is required.
  // - The @Size(min = 3, max = 100) annotation ensures that the medication name
  // is between 3 and 100 characters, which ensures meaningful medication names.

  // 5. 'dosage' field:
  // - Type: private String
  // - Description:
  // - Represents the dosage information for the prescribed medication.
  // - The @NotNull annotation ensures that the dosage information is provided.

  // 6. 'doctorNotes' field:
  // - Type: private String
  // - Description:
  // - Represents any additional notes or instructions from the doctor regarding
  // the prescription.
  // - The @Size(max = 200) annotation ensures that the doctor's notes do not
  // exceed 200 characters, providing a reasonable limit for additional notes.

  // 7. Constructors:
  // - The class includes a no-argument constructor (default constructor) and a
  // parameterized constructor that initializes the fields: patientName,
  // medication, dosage, doctorNotes, and appointmentId.

  // 8. Getters and Setters:
  // - Standard getter and setter methods are provided for all fields: id,
  // patientName, medication, dosage, doctorNotes, and appointmentId.
  // - These methods allow access and modification of the fields of the
  // Prescription class.