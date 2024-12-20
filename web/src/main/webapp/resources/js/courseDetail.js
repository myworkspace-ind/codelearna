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


function selectVoucher(voucherId) {
    document.getElementById(voucherId).checked = true;
}

function loadVouchersSection() {
    fetch( `${_ctx}voucher/applyVoucher`)
        .then(response => response.text())
		.then(html => {
			if (!document.getElementById('voucherModal')) {
				document.body.insertAdjacentHTML('beforeend', html);
			} else {
				document.getElementById('voucherModal').outerHTML = html;
			}
			var voucherModal = new bootstrap.Modal(document.getElementById('voucherModal'));
			voucherModal.show();
			/*document.getElementById('voucherModal').addEventListener('submit', function(event) {
				event.preventDefault();
				submitEditLessonForm(event, lessonId);
			});*/
		})
        .catch(error => console.error('Error loading vouchers section:', error));
}






function getCourseIdFromUrl() {
    const url = window.location.href; // Lấy URL hiện tại
    const match = url.match(/\/course\/(\d+)/); // Tìm ID trong URL
    return match ? match[1] : null;
}

function applySelectedVoucher() {
    const voucherIdInput = document.querySelector('input[name="voucherId"]:checked');
    if (!voucherIdInput) {
        alert('Vui lòng chọn một voucher!');
        return;
    }
    const voucherId = voucherIdInput.value;

    // Lấy courseId từ URL
    const courseId = document.querySelector('input[name="voucherId"]');
    if (!courseId) {
        alert('Không tìm thấy Course ID từ URL!');
        return;
    }
	alert(courseId)

    fetch('voucher/apply', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `voucherId=${voucherId}&courseId=${courseId}`,
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to apply voucher');
            }
            return response.text();
        })
        .then(() => {
            alert('Voucher đã được áp dụng thành công!');
            location.reload(); // Reload trang để cập nhật giá
        })
        .catch(error => {
            console.error('Error applying voucher:', error);
            alert('Không thể áp dụng voucher!');
        });
}
