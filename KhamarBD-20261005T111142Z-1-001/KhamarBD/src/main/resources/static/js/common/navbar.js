/**
 * KhamarBD - Common Navigation & Authorization Controller
 * Loaded on all pages to synchronize user info, sidebar navigation, and enforce Role Authorization.
 */

// 1. Role-Based Route Segregation Guard
function getRoleDashboard(role) {
    const r = (role || 'FARMER').toUpperCase();
    if (r === 'SUPPLIER') return '/pages/supplier/dashboard.html';
    if (r === 'BUYER') return '/pages/buyer/dashboard.html';
    if (r === 'SPECIALIST' || r === 'VET' || r === 'DOCTOR') return '/pages/specialist/dashboard.html';
    if (r === 'ADMIN') return '/pages/admin/dashboard.html';
    return '/pages/dashboard.html'; // Farmer default
}

function getRoleName(role) {
    const r = (role || 'FARMER').toUpperCase();
    if (r === 'SUPPLIER') return 'Input Supplier';
    if (r === 'BUYER') return 'Commercial Buyer (Wholesale / Hotel / Hall)';
    if (r === 'SPECIALIST' || r === 'VET' || r === 'DOCTOR') return 'Veterinary Specialist / Doctor';
    if (r === 'ADMIN') return 'System Administrator';
    return 'Farmer';
}

function enforceRoleAccess() {
    const path = window.location.pathname.toLowerCase();

    // Public Pages - No auth needed
    const isPublic = path.includes('/pages/auth/') ||
                     path.includes('/pages/public/') ||
                     path.endsWith('/index.html') ||
                     path === '/' ||
                     path === '';

    if (isPublic) return;

    // Shared Multi-Role Pages (e.g. Chat)
    const isShared = path.includes('/chat.html');

    const user = typeof getCurrentUser === 'function' ? getCurrentUser() : null;

    // Protection 1: Must be logged in to view private dashboards
    if (!user) {
        if (typeof sessionStorage !== 'undefined') {
            sessionStorage.setItem('khamarbd_redirect', window.location.href);
        }
        alert('Authentication Required: Please sign in to access this portal.');
        window.location.href = '/pages/auth/login.html';
        return;
    }

    const role = (user.primaryRole || user.role || 'FARMER').toUpperCase();

    // Protection 2: Strict Role Segregation
    const isFarmerRoute = (path.includes('/pages/farmer/') || path.endsWith('/pages/dashboard.html')) && !isShared;
    const isSupplierRoute = path.includes('/pages/supplier/');
    const isBuyerRoute = path.includes('/pages/buyer/');
    const isSpecialistRoute = path.includes('/pages/specialist/') && !isShared;
    const isAdminRoute = path.includes('/pages/admin/');

    let isDenied = false;
    let targetPortal = '';

    if (isAdminRoute && role !== 'ADMIN') {
        isDenied = true;
        targetPortal = 'Admin Portal';
    } else if (isSupplierRoute && role !== 'SUPPLIER' && role !== 'ADMIN') {
        isDenied = true;
        targetPortal = 'Supplier Portal';
    } else if (isBuyerRoute && role !== 'BUYER' && role !== 'ADMIN') {
        isDenied = true;
        targetPortal = 'Commercial Buyer Portal';
    } else if (isSpecialistRoute && role !== 'SPECIALIST' && role !== 'VET' && role !== 'DOCTOR' && role !== 'ADMIN') {
        isDenied = true;
        targetPortal = 'Veterinary Specialist Portal';
    } else if (isFarmerRoute && role !== 'FARMER' && role !== 'ADMIN') {
        isDenied = true;
        targetPortal = 'Farmer Portal';
    }

    if (isDenied) {
        const homeDashboard = getRoleDashboard(role);
        const roleTitle = getRoleName(role);
        alert(`⛔ Access Denied!\n\nYour active account role is "${roleTitle}". You do not have permission to access the ${targetPortal}.\n\nYou are being redirected to your own dashboard.`);
        window.location.href = homeDashboard;
        return;
    }
}

// 2. Initialize on load
document.addEventListener('DOMContentLoaded', () => {
    enforceRoleAccess();

    // Sync user details to topbar if elements exist
    const user = typeof getCurrentUser === 'function' ? getCurrentUser() : null;
    if (user) {
        const userNameEls = document.querySelectorAll('.profile-btn span, #topbarUserName, .topbar-user-name, .user-name, .farmer-profile-name, .user-display-name');
        userNameEls.forEach(el => {
            if (user.name) el.textContent = user.name;
        });
        const userRoleEls = document.querySelectorAll('.user-role-badge, .topbar-user-role, .user-role');
        userRoleEls.forEach(el => {
            el.textContent = `Role: ${getRoleName(user.primaryRole || user.role)}`;
        });
    }

    // Attach logout confirmation to all sidebar logout buttons
    document.querySelectorAll('a, button').forEach(el => {
        const text = (el.textContent || '').trim().toLowerCase();
        const href = (el.getAttribute('href') || '').toLowerCase();
        if (text.includes('logout') || href.includes('logout')) {
            el.onclick = function(e) {
                e.preventDefault();
                if (confirm('Are you sure you want to sign out?')) {
                    if (typeof logoutUser === 'function') {
                        logoutUser();
                    } else {
                        sessionStorage.removeItem('currentUser');
                        localStorage.removeItem('currentUser');
                        window.location.href = '/pages/auth/login.html';
                    }
                }
            };
        }
    });
});

// Run immediate check
if (document.readyState === 'complete' || document.readyState === 'interactive') {
    enforceRoleAccess();
}
