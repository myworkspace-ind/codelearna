
import { initializePagination } from './pagination.js';
import { showSuccessToast, showErrorToast } from './toast.js';


window.initializePagination = initializePagination;
window.showSuccessToast = showSuccessToast;
window.showErrorToast = showErrorToast;


document.addEventListener("DOMContentLoaded", function () {
    const toggleSidebarBtn = document.querySelector(".toggle-sidebar");
    const sidebar = document.querySelector(".sidebar");

    toggleSidebarBtn.addEventListener("click", function () {
        sidebar.classList.toggle("collapsed");
    });
});


document.querySelectorAll('.sidebar nav a').forEach(link => {
    link.addEventListener('click', function () {
        document.querySelectorAll('.sidebar nav a').forEach(el => el.classList.remove('active'));
        this.classList.add('active');
    });
})