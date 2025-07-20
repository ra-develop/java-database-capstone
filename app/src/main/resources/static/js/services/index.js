import { openModal } from '../components/modals.js';
import { API_BASE_URL } from '../config/config.js';

const API_ENDPOINTS = {
    admin: `${API_BASE_URL}/admin/login`,
    doctor: `${API_BASE_URL}/doctor/login`,
};

// window.onload = function() {
//     document.getElementById('adminBtn').addEventListener('click', () => openModal('adminLogin'));
//     document.getElementById('doctorBtn').addEventListener('click', () => openModal('doctorLogin'));
// };

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    setupEventListeners();
});

/**
 * Sets up all event listeners
 */
function setupEventListeners() {
    // Add Admin button
    const adminBtn = document.getElementById('adminBtn');
    if (adminBtn) {
        adminBtn.addEventListener('click', () => openModal('adminLogin'));
    }
    // Add Doctor button
    const doctorBtn = document.getElementById('doctorBtn');
    if (doctorBtn) {
        doctorBtn.addEventListener('click', () => openModal('doctorLogin'));
    }
}


window.adminLoginHandler = async function(event) {
    event.preventDefault();
    try {
        const response = await authenticate({
            username: document.getElementById('adminUsername').value,
            password: document.getElementById('adminPassword').value
        }, 'admin');
        
        handleLoginSuccess(response, 'admin');
    } catch (error) {
        showError('adminError', error.message);
    }
};

window.doctorLoginHandler = async function(event) {
    event.preventDefault();
    try {
        const response = await authenticate({
            identifier: document.getElementById('doctorEmail').value,
            password: document.getElementById('doctorPassword').value
        }, 'doctor');
        
        handleLoginSuccess(response, 'doctor');
    } catch (error) {
        showError('doctorError', error.message);
    }
};

async function authenticate(credentials, role) {
    const response = await fetch(API_ENDPOINTS[role], {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': getCSRFToken()
        },
        body: JSON.stringify(credentials)
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Login failed');
    }

    return response;
}

function handleLoginSuccess(response, role) {
    response.json().then(data => {
        if (!data.token) throw new Error('No token received');
        localStorage.setItem('token', data.token);
        localStorage.setItem('userRole', role);

        // Redirect
        window.location.href = `/${role}/dashboard?token=${encodeURIComponent(data.token)}`;

        // Immediately clear from URL after loading
        setTimeout(() => {
            window.history.replaceState({}, document.title, window.location.pathname);
        }, 100);
    });
}

export function showError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }

    // Auto-hide after 5 seconds
    setTimeout(() => {
        errorElement.style.display = 'none';
    }, 5000);
}

function getCSRFToken() {
    return document.querySelector('meta[name="_csrf"]')?.content || '';
}


// /**
//  * Role-Based Login Handling Service
//  * Manages admin and doctor login functionality
//  */

// import { openModal } from '../components/modals.js';
// import { API_BASE_URL } from '../config/config.js';

// // API Endpoints
// const ADMIN_API = `${API_BASE_URL}/admin`;
// const DOCTOR_API = `${API_BASE_URL}/doctor/login`;

// // Initialize on page load
// window.onload = function() {
//     // Admin login button
//     const adminBtn = document.getElementById('adminBtn');
//     if (adminBtn) {
//         adminBtn.addEventListener('click', () => {
//             openModal('adminLogin');
//         });
//     }

//     // Doctor login button
//     const doctorBtn = document.getElementById('doctorBtn');
//     if (doctorBtn) {
//         doctorBtn.addEventListener('click', () => {
//             openModal('doctorLogin');
//         });
//     }
// };

// /**
//  * Handles admin login form submission
//  * @global
//  */
// window.adminLoginHandler = async function() {
//     try {
//         // Get form values
//         const username = document.getElementById('adminUsername').value.trim();
//         const password = document.getElementById('adminPassword').value;

//         if (!username || !password) {
//             alert('Please enter both username and password');
//             return;
//         }

//         // Create request payload
//         const admin = { username, password };

//         // Send login request
//         const response = await fetch(ADMIN_API, {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(admin)
//         });

//         if (!response.ok) {
//             throw new Error('Invalid credentials');
//         }

//         // Process successful login
//         const data = await response.json();
//         localStorage.setItem('token', data.token);
//         localStorage.setItem('userRole', 'admin');
        
//         // Redirect or update UI
//         handleLoginSuccess('admin');
        
//     } catch (error) {
//         console.error('Admin login failed:', error);
//         alert(error.message || 'Login failed. Please try again.');
//     }
// };

// /**
//  * Handles doctor login form submission
//  * @global
//  */
// window.doctorLoginHandler = async function() {
//     try {
//         // Get form values
//         const email = document.getElementById('doctorEmail').value.trim();
//         const password = document.getElementById('doctorPassword').value;

//         if (!email || !password) {
//             alert('Please enter both email and password');
//             return;
//         }

//         // Create request payload
//         const doctor = { email, password };

//         // Send login request
//         const response = await fetch(DOCTOR_API, {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(doctor)
//         });

//         if (!response.ok) {
//             throw new Error('Invalid credentials');
//         }

//         // Process successful login
//         const data = await response.json();
//         localStorage.setItem('token', data.token);
//         localStorage.setItem('userRole', 'doctor');
        
//         // Redirect or update UI
//         handleLoginSuccess('doctor');
        
//     } catch (error) {
//         console.error('Doctor login failed:', error);
//         alert(error.message || 'Login failed. Please try again.');
//     }
// };

// /**
//  * Role selection handler (defined in render.js)
//  * @param {string} role - Selected role (admin|doctor)
//  */
// function handleLoginSuccess(role) {
//     // Implementation from render.js
//     // Sets role in localStorage and updates UI
//     console.log(`Role selected: ${role}`);
//     // Redirect or update UI as needed
//     window.location.href = `${API_BASE_URL}`;
//     // window.location.href = `${API_BASE_URL}/${role}Dashboard.html`;
// }

/*
  Import the openModal function to handle showing login popups/modals
  Import the base API URL from the config file
  Define constants for the admin and doctor login API endpoints using the base URL

  Use the window.onload event to ensure DOM elements are available after page load
  Inside this function:
    - Select the "adminLogin" and "doctorLogin" buttons using getElementById
    - If the admin login button exists:
        - Add a click event listener that calls openModal('adminLogin') to show the admin login modal
    - If the doctor login button exists:
        - Add a click event listener that calls openModal('doctorLogin') to show the doctor login modal


  Define a function named adminLoginHandler on the global window object
  This function will be triggered when the admin submits their login credentials

  Step 1: Get the entered username and password from the input fields
  Step 2: Create an admin object with these credentials

  Step 3: Use fetch() to send a POST request to the ADMIN_API endpoint
    - Set method to POST
    - Add headers with 'Content-Type: application/json'
    - Convert the admin object to JSON and send in the body

  Step 4: If the response is successful:
    - Parse the JSON response to get the token
    - Store the token in localStorage
    - Call selectRole('admin') to proceed with admin-specific behavior

  Step 5: If login fails or credentials are invalid:
    - Show an alert with an error message

  Step 6: Wrap everything in a try-catch to handle network or server errors
    - Show a generic error message if something goes wrong


  Define a function named doctorLoginHandler on the global window object
  This function will be triggered when a doctor submits their login credentials

  Step 1: Get the entered email and password from the input fields
  Step 2: Create a doctor object with these credentials

  Step 3: Use fetch() to send a POST request to the DOCTOR_API endpoint
    - Include headers and request body similar to admin login

  Step 4: If login is successful:
    - Parse the JSON response to get the token
    - Store the token in localStorage
    - Call selectRole('doctor') to proceed with doctor-specific behavior

  Step 5: If login fails:
    - Show an alert for invalid credentials

  Step 6: Wrap in a try-catch block to handle errors gracefully
    - Log the error to the console
    - Show a generic error message
*/
