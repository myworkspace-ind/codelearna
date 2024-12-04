// Toast type constants
const TOAST_TYPES = {
    SUCCESS: 'success',
    ERROR: 'error',
    WARNING: 'warning',
    INFO: 'info'
};

// Toast configuration
const TOAST_CONFIG = {
    success: {
        bgClass: 'bg-success',
        icon: '<i class="bi bi-check-circle-fill"></i>'
    },
    error: {
        bgClass: 'bg-danger',
        icon: '<i class="bi bi-x-circle-fill"></i>'
    },
    warning: {
        bgClass: 'bg-warning',
        icon: '<i class="bi bi-exclamation-triangle-fill"></i>'
    },
    info: {
        bgClass: 'bg-info',
        icon: '<i class="bi bi-info-circle-fill"></i>'
    }
};

// Function to show toast
function showToast(message, type = TOAST_TYPES.INFO) {
    const config = TOAST_CONFIG[type];
    const toastId = 'toast_' + Date.now();
    
    const toastHTML = `
        <div id="${toastId}" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header ${config.bgClass} text-white">
                <span class="me-2">${config.icon}</span>
                <strong class="me-auto">${type.charAt(0).toUpperCase() + type.slice(1)}</strong>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                ${message}
            </div>
        </div>
    `;
    
    const toastContainer = document.getElementById('toastContainer');
    toastContainer.insertAdjacentHTML('beforeend', toastHTML);
    
    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, {
        animation: true,
        autohide: true,
        delay: 3000
    });
    
    toast.show();
    
    // Remove toast element after it's hidden
    toastElement.addEventListener('hidden.bs.toast', () => {
        toastElement.remove();
    });
}

// Utility functions for different toast types
function showSuccessToast(message) {
    showToast(message, TOAST_TYPES.SUCCESS);
}

function showErrorToast(message) {
    showToast(message, TOAST_TYPES.ERROR);
}

function showWarningToast(message) {
    showToast(message, TOAST_TYPES.WARNING);
}

function showInfoToast(message) {
    showToast(message, TOAST_TYPES.INFO);
}

// Export các hàm và constants cần thiết
export {
    showSuccessToast,
    showErrorToast,
    showWarningToast,
    showInfoToast,
    TOAST_TYPES
};