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

function confirmDelete() {
	return confirm("Are you sure you want to delete this review?");
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

	initializeReviewForm('.review-form');

	document.querySelectorAll('.modal form').forEach(editForm => {
		initializeReviewForm(`#${editForm.closest('.modal').id} form`);
	});

	// review delete handle
	let reviewToDelete = {
	       courseId: null,
	       reviewId: null
	   };
		
	   // when user click delete button
	   document.querySelectorAll('.delete-review-btn').forEach(button => {
	       button.addEventListener('click', function() {
	           reviewToDelete.courseId = this.getAttribute('data-course-id');
	           reviewToDelete.reviewId = this.getAttribute('data-review-id');
	           
	           // modal display
	           let deleteModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	           deleteModal.show();
	       });
	   });
		
	   // when user confirm delete
	   document.getElementById('confirmDeleteBtn').addEventListener('click', function() {
	       if (reviewToDelete.courseId && reviewToDelete.reviewId) {
	           const form = document.createElement('form');
	           form.method = 'POST';
	           form.action = _ctx + `/course/${reviewToDelete.courseId}/review/${reviewToDelete.reviewId}/delete`;
	           
	           const methodInput = document.createElement('input');
	           methodInput.type = 'hidden';
	           methodInput.name = '_method';
	           methodInput.value = 'delete';
	           form.appendChild(methodInput);
			   
	           document.body.appendChild(form);
	           form.submit();
	       }
	   });
});