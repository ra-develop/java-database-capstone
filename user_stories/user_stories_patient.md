# Patient User Stories

**1. Browse Doctors (Public)**
As a patient, I want to view doctors list without logging in, so I can explore options before registering. 

**Acceptance Criteria**:

    1. Show specialty filters (Cardiology, Pediatrics, etc.)

    2. Display doctor names, photos, and ratings

    3. Paginate results (10 doctors/page)  
**Priority**: High  
**Story Points**: 3  
**Notes**:
* Cache results for 1 hour to reduce DB load
    
[Top](#patient-user-stories)

---

**2. Patient Registration**
As a patient, I want to sign up with email/password, so I can book appointments. 

**Acceptance Criteria**:

    1.  Validate email format and password strength

    2.  Send confirmation email with verification link
        
    3.  Create patient profile in MySQL  
   
**Priority**: High  
**Story Points**: 5  
**Notes**:
* Password must be 8+ chars with special characters

[Top](#patient-user-stories)

---

**3. Patient Login**
As a patient, I want to log in securely, so I can manage my bookings.  

**Acceptance Criteria**:

    1. Authenticate via email/password
      
    2. Show "Forgot Password" option
      
    3. Redirect to dashboard on success  

**Priority**: High  
**Story Points**: 3  
**Notes**:
*   Lock account after 5 failed attempts
    

[Top](#patient-user-stories)

---

**4. Book Appointment**
As a patient, I want to book a 1-hour appointment, so I can consult a doctor. 

**Acceptance Criteria**:

    1.  Show available slots in calendar UI
        
    2.  Prevent double-booking of slots
        
    3.  Send confirmation email with ICS attachment  
   
**Priority**: High  
**Story Points**: 8  
**Notes**:
*   Integrate with doctor's working hours
    

[Top](#patient-user-stories)

---

**5. View Upcoming Appointments**
As a patient, I want to see my scheduled visits, so I can prepare.  

**Acceptance Criteria**:

    1.  List appointments sorted by date
        
    2.  Show doctor details and purpose of visit
        
    3.  Include "Cancel" button (24h+ in advance)  

**Priority**: Medium  
**Story Points**: 5  
**Notes**:
*   Gray out past appointments
    

[Top](#patient-user-stories)

---

**6. Patient Logout**
As a patient, I want to log out, so my account stays secure.  

**Acceptance Criteria**:

    1.  Invalidate session immediately
        
    2.  Redirect to public doctor search page
        
    3.  Show "Logged out successfully" toast  

**Priority**: Medium  
**Story Points**: 2  
**Notes**:
*   Clear client-side JWT tokens
    

[Top](#patient-user-stories)

---

**7. View Prescription History**
As a patient, I want to see past prescriptions, so I can track medications.  

**Acceptance Criteria**:

    1.  Fetch MongoDB prescription records
        
    2.  Display in reverse chronological order
        
    3.  Allow PDF download  

**Priority**: Medium  
**Story Points**: 5  
**Notes**:
*   Blur sensitive data in preview mode
    

[Top](#patient-user-stories)

---

**8. Telemedicine Join**

**Title**: As a patient, I want to join virtual consultations, so I can attend appointments remotely.

**Acceptance Criteria**:

    1.  "Join Call" button appears 5 mins before appointment
        
    2.  WebRTC video connection establishes successfully
        
    3.  Show connection quality indicators  

**Priority**: High  
**Story Points**: 8  
**Notes**:
*   Fallback to audio-only if bandwidth low
    

[Top](#patient-user-stories)

---

**9. Reschedule Appointments**

**Title**: As a patient, I want to reschedule bookings, so I can adjust my availability. 

**Acceptance Criteria**:

    1.  Display available alternative slots
        
    2.  Send notification to doctor about change
        
    3.  Update original appointment in database  

**Priority**: Medium  
**Story Points**: 5  
**Notes**:
*   Limit to 2 reschedules per appointment
    

[Top](#patient-user-stories)

---

**10. Health Records Upload**

**Title**: As a patient, I want to upload medical reports, so doctors can review them pre-consultation. 

**Acceptance Criteria**:

    1.  Accept PDF/JPG/PNG (max 10MB)
        
    2.  Show upload progress indicator
        
    3.  Auto-categorize by document type  

**Priority**: Medium  
**Story Points**: 5  
**Notes**:
*   OCR for lab result extraction
    

[Top](#patient-user-stories)

---

**11. Prescription Refill Request**

**Title**: As a patient, I want to request medication refills, so I don't run out of essentials.  

**Acceptance Criteria**:

    1.  Button on past prescriptions
        
    2.  Form pre-filled with medication details
        
    3.  Send notification to prescribing doctor  

**Priority**: Low  
**Story Points**: 3  
**Notes**:
*   Auto-approve for chronic medications
    

[Top](#patient-user-stories)

---

**12. Emergency Contact Setup**

**Title**: As a patient, I want to designate emergency contacts, so staff can notify them if needed. 

**Acceptance Criteria**:

    1.  Add multiple contacts with relationships
        
    2.  Verify contact phone/email
        
    3.  Badge in patient profile  

**Priority**: Low  
**Story Points**: 3  
**Notes**:

*   GDPR consent required
    
[Top](#patient-user-stories)

---