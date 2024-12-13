document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.learn-now-btn').forEach(item => {
        item.addEventListener('click', function() {
            const courseId = this.getAttribute('data-course-id');
            window.location.href = _ctx + `play/${courseId}`;
        });
    });
});

if (document.getElementById('confirmPaymentBtn') != null) {
    document.getElementById('confirmPaymentBtn').addEventListener(
        'click', function() {
            // Gửi form khi nhấn "Confirm"
            document.getElementById('paymentForm').submit();
        });
}


// payment
function selectPayment(method) {
    document.querySelectorAll('.payment-method').forEach(el => {
        el.classList.remove('selected');
    });

    const selectedMethod = document.querySelector(`#${method}`);
    selectedMethod.checked = true;
    selectedMethod.closest('.payment-method').classList.add('selected');
}

function updateFormAction() {
    const form = document.getElementById('paymentForm');
    const selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked');

    if (selectedPaymentMethod && selectedPaymentMethod.value === 'ewallet') {
        form.action = `${_ctx}payment/pay`;
    } else {
        form.action = `${_ctx}orders`;
    }
    return true;
}

//filter reviews
function applySorting() {
    var sortValue = document.getElementById('reviewSortSelect').value;
    window.location.href = window.location.pathname + '?sortBy='
        + sortValue;
}