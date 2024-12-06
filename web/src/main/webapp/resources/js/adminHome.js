
import { initializePagination } from './pagination.js';
import { showSuccessToast, showErrorToast } from './toast.js';
import { showSpinnerLoading, showBarLoading, hideAllLoading } from './loading.js'

window.initializePagination = initializePagination;
window.showSuccessToast = showSuccessToast;
window.showErrorToast = showErrorToast;


document.addEventListener("DOMContentLoaded", function() {
	const toggleSidebarBtn = document.querySelector(".toggle-sidebar");
	const sidebar = document.querySelector(".sidebar");

	toggleSidebarBtn.addEventListener("click", function() {
		sidebar.classList.toggle("collapsed");
	});
});


document.querySelectorAll('.sidebar nav a').forEach(link => {
	link.addEventListener('click', function() {
		document.querySelectorAll('.sidebar nav a').forEach(el => el.classList.remove('active'));
		this.classList.add('active');
	});
})

// check dynamic-content to display the welcome page
document.addEventListener('DOMContentLoaded', function() {
	const dynamicContent = document.getElementById('dynamic-content');
	const welcomeSection = document.getElementById('welcomeSection');

	welcomeSection.style.display = 'block';

	function updateVisibility() {
		if (dynamicContent.innerHTML.trim() !== '') {
			welcomeSection.style.display = 'none';
		} else {
			welcomeSection.style.display = 'block';
		}
	}

	const observer = new MutationObserver(updateVisibility);

	observer.observe(dynamicContent, {
		childList: true,
		characterData: true,
		subtree: true
	});

	updateVisibility();
});

window.showSpinnerLoading = showSpinnerLoading;
window.showBarLoading = showBarLoading;
window.hideAllLoading = hideAllLoading;

