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

	initializeReviewForm('.review-form');
	
	document.querySelectorAll('.modal form').forEach(editForm => {
		initializeReviewForm(`#${editForm.closest('.modal').id} form`);
	});
});