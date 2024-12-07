// Loading type constants
const LOADING_TYPES = {
    SPINNER: 'spinner',
    BAR: 'bar'
};

// Loading configuration
const LOADING_CONFIG = {
    spinner: {
        html: '<div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div>',
        class: 'loading-spinner'
    },
    bar: {
        html: `
            <div class="progress" style="height: 5px;">
                <div class="progress-bar progress-bar-striped progress-bar-animated" style="width: 100%;" role="progressbar"></div>
            </div>
        `,
        class: 'loading-bar'
    }
};

// Function to show loading
function showLoading(type = LOADING_TYPES.SPINNER) {
    const config = LOADING_CONFIG[type];
    const loadingId = 'loading_' + Date.now();

    const loadingHTML = `
        <div id="${loadingId}" class="${config.class}" role="alert">
            ${config.html}
        </div>
    `;

    const loadingContainer = document.getElementById('loadingContainer');
    
    // Kiểm tra xem loadingContainer có tồn tại không
    if (loadingContainer) {
        loadingContainer.insertAdjacentHTML('beforeend', loadingHTML);
    } else {
        console.error('loadingContainer không tồn tại trong DOM');
    }

    // Optional: You can add a timeout to hide the loading after a specific duration if necessary.
    setTimeout(() => {
        hideLoading(loadingId);
    }, 5000); // Example: remove loading after 5 seconds
}


// Function to hide loading
function hideLoading(loadingId) {
    const loadingElement = document.getElementById(loadingId);
    if (loadingElement) {
        loadingElement.remove();
    }
}

// Utility functions for different loading types
function showSpinnerLoading() {
    showLoading(LOADING_TYPES.SPINNER);
}

function showBarLoading() {
    showLoading(LOADING_TYPES.BAR);
}

function hideAllLoading() {
    const loadingElements = document.querySelectorAll('.loading-spinner, .loading-bar');
    loadingElements.forEach(element => element.remove());
}

// Export các hàm và constants cần thiết
export {
    showSpinnerLoading,
    showBarLoading,
    hideAllLoading,
    LOADING_TYPES
};
