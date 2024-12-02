document.addEventListener('DOMContentLoaded', function() {
	document.querySelectorAll('.learn-now-btn').forEach(item => {
		item.addEventListener('click', function() {
			const courseId = this.getAttribute('data-course-id');
			window.location.href = _ctx + `play/${courseId}`;
		});
	});
});

document.getElementById('confirmPaymentBtn').addEventListener(
	'click', function() {
		// Gửi form khi nhấn "Confirm"
		document.getElementById('paymentForm').submit();
	});