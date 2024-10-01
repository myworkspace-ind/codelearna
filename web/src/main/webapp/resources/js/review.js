// Select star rating
const stars = document.querySelectorAll('.rating-stars i');
const ratingInput = document.getElementById('ratingStar');
const ratingError = document.getElementById('rating-error');
const reviewForm = document.querySelector('.review-form');


stars.forEach(star => {
	star.addEventListener('click', function() {
		const rating = this.getAttribute('data-rating');
		ratingInput.value = rating;
		updateStars(rating);
		ratingError.style.display = "none";

	});
});

function updateStars(rating) {
	stars.forEach(star => {
		if (star.getAttribute('data-rating') <= rating) {
			star.classList.add('checked');
		} else {
			star.classList.remove('checked');
		}
	});
}

reviewForm.addEventListener('submit', function(e) {
	if (ratingInput.value == "0") {
		e.preventDefault();
		ratingError.style.display = "block";

	}
});

// Count limit character review
function updateCharacterCount() {
	var maxChars = 200;
	var currentLength = document.getElementById("content").value.length;
	document.getElementById("charCount").innerText = (maxChars - currentLength) + " ký tự còn lại";
}


