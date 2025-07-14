/**
 * Doctor Card Component
 * Creates a dynamic card displaying doctor information with role-specific actions
 * 
 * Dependencies:
 * - deleteDoctor() from '/js/services/doctorServices.js'
 * - getPatientData() from '/js/services/patientServices.js'
 * - showBookingOverlay() from '/js/loggedPatient.js'
 */

import { deleteDoctor } from '/js/services/doctorServices.js';
import { getPatientData } from '/js/services/patientServices.js';
import { showBookingOverlay } from '/js/loggedPatient.js';

/**
 * Creates and returns a doctor card element with role-specific functionality
 * @param {Object} doctor - Doctor object containing:
 *   @property {string} id - Doctor's unique ID
 *   @property {string} name - Doctor's full name
 *   @property {string} specialty - Medical specialty
 *   @property {string} email - Contact email
 *   @property {string[]} availableTimes - Array of available time slots
 * @returns {HTMLElement} Fully constructed doctor card element
 */
export function createDoctorCard(doctor) {
    // Validate doctor object
    if (!doctor || typeof doctor !== 'object') {
        console.error('Invalid doctor data provided');
        return document.createElement('div'); // Return empty div as fallback
    }

    // Create card container
    const card = document.createElement('div');
    card.className = 'doctor-card';
    card.dataset.doctorId = doctor.id;

    // Get current user role
    const role = localStorage.getItem('userRole') || 'guest';

    // Create information section
    const infoSection = createInfoSection(doctor);
    
    // Create actions section based on role
    const actionsSection = createActionsSection(doctor, role);

    // Assemble card
    card.append(infoSection, actionsSection);
    return card;
}

/**
 * Creates the information section of the doctor card
 * @param {Object} doctor - Doctor data
 * @returns {HTMLElement} Information section element
 */
function createInfoSection(doctor) {
    const infoDiv = document.createElement('div');
    infoDiv.className = 'doctor-info';

    const elements = [
        createElement('h3', 'doctor-name', doctor.name),
        createElement('p', 'doctor-specialty', `Specialty: ${doctor.specialty}`),
        createElement('p', 'doctor-email', `Email: ${doctor.email}`),
        createElement('p', 'doctor-availability', 
                     `Available: ${formatAvailability(doctor.availableTimes)}`)
    ];

    infoDiv.append(...elements);
    return infoDiv;
}

/**
 * Creates the actions section based on user role
 * @param {Object} doctor - Doctor data
 * @param {string} role - User role
 * @returns {HTMLElement} Actions section element
 */
function createActionsSection(doctor, role) {
    const actionsDiv = document.createElement('div');
    actionsDiv.className = 'card-actions';

    switch (role) {
        case 'admin':
            actionsDiv.appendChild(createDeleteButton(doctor));
            break;
        case 'loggedPatient':
            actionsDiv.appendChild(createBookButton(doctor, true));
            break;
        case 'patient':
            actionsDiv.appendChild(createBookButton(doctor, false));
            break;
        default:
            // No actions for guests
            break;
    }

    return actionsDiv;
}

/**
 * Creates a standardized DOM element
 * @param {string} tag - Element tag name
 * @param {string} className - CSS class name
 * @param {string} text - Text content
 * @returns {HTMLElement} Created element
 */
function createElement(tag, className, text) {
    const el = document.createElement(tag);
    el.className = className;
    el.textContent = text;
    return el;
}

/**
 * Formats availability times for display
 * @param {string[]} times - Array of time slots
 * @returns {string} Formatted availability string
 */
function formatAvailability(times) {
    return Array.isArray(times) ? times.join(', ') : 'Not available';
}

/**
 * Creates a delete button (admin only)
 * @param {Object} doctor - Doctor data
 * @returns {HTMLButtonElement} Configured delete button
 */
function createDeleteButton(doctor) {
    const button = document.createElement('button');
    button.className = 'btn btn-danger';
    button.textContent = 'Delete';
    
    button.addEventListener('click', async () => {
        if (!confirm(`Permanently delete Dr. ${doctor.name}?`)) return;
        
        try {
            const token = localStorage.getItem('token');
            if (!token) throw new Error('Authentication required');
            
            await deleteDoctor(doctor.id, token);
            button.closest('.doctor-card').remove();
        } catch (error) {
            console.error('Delete failed:', error.message);
            alert(`Delete failed: ${error.message}`);
        }
    });
    
    return button;
}

/**
 * Creates a book appointment button
 * @param {Object} doctor - Doctor data
 * @param {boolean} isLoggedIn - Whether user is authenticated
 * @returns {HTMLButtonElement} Configured book button
 */
function createBookButton(doctor, isLoggedIn) {
    const button = document.createElement('button');
    button.className = 'btn btn-primary';
    button.textContent = 'Book Now';
    
    button.addEventListener('click', async () => {
        if (!isLoggedIn) {
            alert('Please login to book an appointment');
            return;
        }

        try {
            const token = localStorage.getItem('token');
            if (!token) throw new Error('Session expired');
            
            const patientData = await getPatientData(token);
            showBookingOverlay(doctor, patientData);
        } catch (error) {
            console.error('Booking error:', error.message);
            alert(`Booking failed: ${error.message}`);
        }
    });
    
    return button;
}

/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
