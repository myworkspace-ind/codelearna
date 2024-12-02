import { showSuccessToast, showErrorToast } from './toast.js';

function initializeReviewForm(formSelector) {
	const form = document.querySelector(formSelector);
	if (!form) return;

	const stars = form.querySelectorAll('.rating-stars .star');
	const ratingInput = form.querySelector('input[name="ratingStar"]');
	const ratingError = form.querySelector('.rating-error');
	const reviewContent = form.querySelector('.review-content');
	const charCount = form.querySelector('.char-count');


	stars.forEach(star => {
		star.addEventListener('click', function() {
			const rating = this.getAttribute('data-rating');
			ratingInput.value = rating;
			updateStars(stars, rating);
			if (ratingError) {
				ratingError.style.display = "none";
			}
		});
	});

	if (reviewContent && charCount) {
		reviewContent.addEventListener('input', function() {
			updateCharacterCount(this, charCount);
		});
	}

	form.addEventListener('submit', function(e) {
		if (ratingInput.value === "0") {
			e.preventDefault();
			if (ratingError) {
				ratingError.style.display = "block";
			}
		}
	});
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


document.addEventListener('DOMContentLoaded', function() {
	handleSuccessMessage();

	initializeReviewForms();

	handleDeleteReview();

	handleAddReview();
	
	/*handleEditReview();*/

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

	document.querySelectorAll('.add-review-btn').forEach(button => {
		button.addEventListener('click', function() {
			courseInfor.courseId = this.getAttribute('data-course-id');
		});
	});

	document.getElementById('reviewForm').addEventListener('submit', function(event) {
		event.preventDefault();
		if (courseInfor.courseId) {
			submitReview(courseInfor.courseId, this);
		}
	});
}

function submitReview(courseId, form) {
	const formData = new FormData(form);
	fetch(_ctx + `/course/${courseId}/review`, {
		method: 'POST',
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			handleServerResponse(data);
		})
		.catch(error => {
			console.error('Error submitting review:', error);
			showErrorToast('An error occurred. Please try again.');
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
		fetch(_ctx + `/course/${courseInfor.courseId}/review/${courseInfor.reviewId}/delete`, {
			method: 'DELETE',
		})
			.then(response => response.json())
			.then(data => {
				handleServerResponse(data);
			})
			.catch(error => {
				console.error('Error:', error);
				showErrorToast('An error occurred. Please try again later.');
			});
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



