const BASE_URL = "http://localhost:8081";

async function apiRequest(path, method = "GET", body = null) {
    const options = {
        method,
        headers: {
            "Content-Type": "application/json"
        }
    };
    if (body) {
        options.body = JSON.stringify(body);
    }
    
    const response = await fetch(BASE_URL + path, options);
    if (!response.ok) {
        const errorMsg = await response.text();
        throw new Error(errorMsg || "API Error");
    }
    
    // Handle empty responses
    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

// Multi-Tab Role-Aware User State Management
function setCurrentUser(user) {
    if (user) {
        const str = JSON.stringify(user);
        sessionStorage.setItem("currentUser", str);
        localStorage.setItem("currentUser", str);
        const role = (user.primaryRole || user.role || 'FARMER').toUpperCase();
        localStorage.setItem("khamarbd_user_" + role, str);
    }
}

function getCurrentUser() {
    // 1. Priority 1: Current tab's isolated sessionStorage
    const sessionStr = sessionStorage.getItem("currentUser");
    if (sessionStr) {
        try {
            return JSON.parse(sessionStr);
        } catch (e) {}
    }

    // 2. Priority 2: Context-aware role recovery based on the current portal path
    const path = window.location.pathname.toLowerCase();
    let targetRole = null;
    if (path.includes('/pages/supplier/')) targetRole = 'SUPPLIER';
    else if (path.includes('/pages/buyer/')) targetRole = 'BUYER';
    else if (path.includes('/pages/specialist/')) targetRole = 'SPECIALIST';
    else if (path.includes('/pages/admin/')) targetRole = 'ADMIN';
    else if (path.includes('/pages/farmer/') || path.endsWith('/pages/dashboard.html')) targetRole = 'FARMER';

    if (targetRole) {
        const roleSpecificStr = localStorage.getItem("khamarbd_user_" + targetRole);
        if (roleSpecificStr) {
            try {
                const parsed = JSON.parse(roleSpecificStr);
                sessionStorage.setItem("currentUser", roleSpecificStr);
                return parsed;
            } catch (e) {}
        }
    }

    // 3. Priority 3: Fallback to global localStorage
    const localStr = localStorage.getItem("currentUser");
    if (localStr) {
        try {
            const parsed = JSON.parse(localStr);
            sessionStorage.setItem("currentUser", localStr);
            return parsed;
        } catch (e) {}
    }
    return null;
}

function logoutUser() {
    try {
        const u = getCurrentUser();
        if (u) {
            const role = (u.primaryRole || u.role || 'FARMER').toUpperCase();
            localStorage.removeItem("khamarbd_user_" + role);
        }
    } catch (e) {}
    sessionStorage.removeItem("currentUser");
    sessionStorage.removeItem("khamarbd_redirect");
    localStorage.removeItem("currentUser");
    window.location.href = "/pages/auth/login.html";
}

// Upload file to local server storage
async function uploadImageFile(file) {
    const formData = new FormData();
    formData.append("file", file);

    const response = await fetch(BASE_URL + "/api/upload", {
        method: "POST",
        body: formData
    });

    if (!response.ok) {
        const errorMsg = await response.text();
        throw new Error(errorMsg || "Upload Failed");
    }

    const data = await response.json();
    return data.url; // e.g. /uploads/img_172830000_abc123.jpg
}
