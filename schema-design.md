# Database Schema Design for Smart Clinic Management System

## MySQL Database Design (Relational)

### 1. Table: `patients`
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **name**: VARCHAR(100), NOT NULL  
- **email**: VARCHAR(100), UNIQUE  
- **phone**: VARCHAR(20), NOT NULL  
- **address**: TEXT  
- **date_of_birth**: DATE  
- **created_at**: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP  

**Notes**:  
- `email` is validated via application logic (regex).  
- `phone` is required for appointment reminders.  

---

### 2. Table: `doctors`  
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **name**: VARCHAR(100), NOT NULL  
- **specialization**: VARCHAR(100)  
- **email**: VARCHAR(100), UNIQUE  
- **phone**: VARCHAR(20)  
- **working_hours**: JSON (*Stores flexible schedule, e.g., `{"mon": ["09:00-17:00"]}`*)  

**Notes**:  
- `working_hours` uses JSON for variable schedules.  

---

### 3. Table: `appointments`  
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **patient_id**: INT, Foreign Key → `patients(id)`, ON DELETE CASCADE  
- **doctor_id**: INT, Foreign Key → `doctors(id)`  
- **appointment_time**: DATETIME, NOT NULL  
- **status**: ENUM('Scheduled', 'Completed', 'Cancelled')  
- **notes**: TEXT  

**Notes**:  
- Deleting a patient cascades to their appointments.  
- `status` ensures data integrity.  

---

### 4. Table: `admin`  
- **id**: INT, Primary Key, AUTO_INCREMENT  
- **username**: VARCHAR(50), UNIQUE  
- **password_hash**: VARCHAR(255) (*encrypted*)  
- **role**: ENUM('SuperAdmin', 'Staff')  

---

## MongoDB Collection Design (Document-Based)

### Collection: `prescriptions`  
```json
{
  "_id": ObjectId("64abc123456"),
  "patientId": 101,  // Reference to MySQL `patients.id`
  "appointmentId": 51,  // Reference to MySQL `appointments.id`
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "Every 6 hours",
      "duration": "7 days"
    }
  ],
  "doctorNotes": "Avoid alcohol during medication.",
  "pharmacy": {
    "name": "Walgreens",
    "location": "Market Street"
  },
  "createdAt": ISODate("2025-07-13T10:00:00Z")
}
```

