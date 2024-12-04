
import { initializePagination } from './pagination.js';
import { showSuccessToast, showErrorToast } from './toast.js';
import {showSpinnerLoading, showBarLoading, hideAllLoading} from './loading.js'

window.initializePagination = initializePagination;
window.showSuccessToast = showSuccessToast;
window.showErrorToast = showErrorToast;
window.showSpinnerLoading = showSpinnerLoading;
window.showBarLoading = showBarLoading;
window.hideAllLoading = hideAllLoading;