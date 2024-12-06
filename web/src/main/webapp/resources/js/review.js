import { showSuccessToast, showErrorToast } from './toast.js';


function initializeReviewForm(formSelector) {
    const form = document.querySelector(formSelector);
    if (!form) {
        console.warn(`Form not found for selector: ${formSelector}`);
        return; 
    }

    const stars = form.querySelectorAll('.rating-stars .star');
    const ratingInput = form.querySelector('input[name="ratingStar"]');
    const ratingError = form.querySelector('.rating-error');
    const reviewContent = form.querySelector('.review-content');
    const charCount = form.querySelector('.char-count');

    if (!stars || !ratingInput) {
        console.warn(`Required elements not found in form: ${formSelector}`);
        return;
    }

    form.addEventListener('submit', function (e) {
        if (!ratingInput.value || ratingInput.value === "0") {
            e.preventDefault();
            if (ratingError) {
                ratingError.style.display = "block";
            }
        }
    });

    stars.forEach(star => {
        star.addEventListener('click', function () {
            const rating = this.getAttribute('data-rating');
            ratingInput.value = rating;
            updateStars(stars, rating);
            if (ratingError) {
                ratingError.style.display = "none";
            }
        });
    });

    if (reviewContent && charCount) {
        reviewContent.addEventListener('input', function () {
            updateCharacterCount(this, charCount);
        });
    }
}


function updateStars(stars, rating) {
	stars.forEach(star => {
		const starRating = star.getAttribute('data-rating');
		if (starRating <= rating) {
			star.classList.add('checked');
		} else {
			star.classList.remove('checked');
		}
	});
}

function updateCharacterCount(textarea, countElement) {
	const maxChars = 200;
	const currentLength = textarea.value.length;
	countElement.textContent = `${maxChars - currentLength} characters remaining`;
}

document.querySelectorAll('.btn-warning').forEach(button => {
	button.addEventListener('click', function() {
		loadEditReviewForm(this.dataset.reviewId, this.dataset.courseId);
	});
});

document.addEventListener('DOMContentLoaded', function() {
	handleSuccessMessage();

	initializeReviewForms();

	handleDeleteReview();

	handleAddReview();
});

function handleSuccessMessage() {
	const successMessage = sessionStorage.getItem('SuccessMessage');
	if (successMessage) {
		showSuccessToast(successMessage);
		sessionStorage.removeItem('SuccessMessage');
	}
}

function initializeReviewForms() {
	initializeReviewForm('.review-form');
	document.querySelectorAll('.modal form').forEach(editForm => {
		initializeReviewForm(`#${editForm.closest('.modal').id} form`);
	});
}


function handleAddReview() {
	let courseInfor = { courseId: null };

	const addReviewButtons = document.querySelectorAll('.add-review-btn');
	if (addReviewButtons.length > 0) {
		addReviewButtons.forEach(button => {
			button.addEventListener('click', function() {
				courseInfor.courseId = this.getAttribute('data-course-id');
			});
		});
	}

	const reviewForm = document.getElementById('reviewForm');
	if (reviewForm) {
		reviewForm.addEventListener('submit', function(event) {
			event.preventDefault();
			if (courseInfor.courseId) {
				submitReview(courseInfor.courseId, this);
			}
		});
	}
}


function submitReview(courseId, form) {
	const formData = new FormData(form);
	fetch(`${_ctx}course/${courseId}/review`, {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const contentType = response.headers.get('Content-Type');
			if (contentType && contentType.includes('application/json')) {
				return response.json();
			} else {
				throw new Error('Response is not JSON');
			}
		})
		.then(data => {
			handleServerResponse(data);
		})
		.catch(error => {
			console.error('Error submitting review:', error);
			showErrorToast(error.message || 'An error occurred. Please try again.');
		});
}

function handleDeleteReview() {
	let courseInfor = { courseId: null, reviewId: null };

	document.querySelectorAll('.delete-review-btn').forEach(button => {
		button.addEventListener('click', function() {
			courseInfor.courseId = this.getAttribute('data-course-id');
			courseInfor.reviewId = this.getAttribute('data-review-id');
			showDeleteModal(courseInfor);
		});
	});
}

function showDeleteModal(courseInfor) {
	let deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	deleteModal.show();

	document.getElementById('confirmDeleteBtn').addEventListener('click', function() {
		deleteReview(courseInfor);
	});
}

function deleteReview(courseInfor) {
	if (courseInfor.courseId && courseInfor.reviewId) {
		fetch(`${_ctx}course/${courseInfor.courseId}/review/${courseInfor.reviewId}/delete`, {
			method: 'POST',
		})
			.then(response => {
				if (!response.ok) {
					console.error('Response status:', response.status);
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				const contentType = response.headers.get('Content-Type');
				if (contentType && contentType.includes('application/json')) {
					return response.json();
				} else {
					console.error('Invalid Content-Type:', contentType);
					throw new Error('Response is not JSON');
				}
			})
			.then(data => {
				console.log('Server response:', data);
				handleServerResponse(data);
			})
			.catch(error => {
				console.error('Error:', error);
				showErrorToast(error.message || 'An error occurred. Please try again later.');
			});
	} else {
		console.error('Invalid course information:', courseInfor);
		showErrorToast('Invalid course information. Please try again.');
	}
}

function handleServerResponse(data) {
	if (data.success) {
		sessionStorage.setItem('SuccessMessage', data.message);
		location.reload();
	} else {
		showErrorToast('An error occurred. Please try again.');
	}
}

function loadEditReviewForm(reviewId, courseId) {
	fetch(`${_ctx}course/${courseId}/review/edit/${reviewId}`)
		.then(response => response.text())
		.then(html => {
			let editReviewModal = document.getElementById('editReviewModal');
			if (!editReviewModal) {
				document.body.insertAdjacentHTML('beforeend', html);
			} else {
				editReviewModal.outerHTML = html;
			}

			editReviewModal = new bootstrap.Modal(document.getElementById('editReviewModal'));
			editReviewModal.show();

			initializeReviewForm('#editReviewForm');

			document.getElementById('editReviewForm').addEventListener('submit', function(event) {
				event.preventDefault();
				submitEditReviewForm(event, reviewId);
			});
		})
		.catch(error => console.error('Error loading edit review form:', error));
}

function submitEditReviewForm(event, reviewId) {
	event.preventDefault();
	const form = event.target;
	const formData = new FormData(form);

	const ratingInput = form.querySelector('input[name="ratingStar"]');
	const ratingError = form.querySelector('.rating-error');

	if (ratingInput.value === "0") {
		if (ratingError) {
			ratingError.style.display = "block";
		}
		return;
	}

	fetch(form.action, {
		method: 'POST',
		body: formData,
		headers: {
			'Accept': 'application/json'
		}
	})
		.then(response => {
			if (!response.ok) {
				console.error('Response status:', response.status);
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const contentType = response.headers.get('Content-Type');
			if (contentType && contentType.includes('application/json')) {
				return response.json();
			} else {
				console.error('Invalid Content-Type:', contentType);
				throw new Error('Response is not JSON');
			}
		})
		.then(data => {
			console.log('Server response:', data);
			handleServerResponse(data);
		})
		.catch(error => {
			console.error('Error:', error);
			showErrorToast(error.message || 'An error occurred. Please try again later.');
		});
}



