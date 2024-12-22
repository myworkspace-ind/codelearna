document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.learn-now-btn').forEach(item => {
        item.addEventListener('click', function () {
            const courseId = this.getAttribute('data-course-id');
            window.location.href = _ctx + `play/${courseId}`;
        });
    });

    const priceElement = document.getElementById('finalPrice');
    if (priceElement) {
        originalPrice = parseFloat(priceElement.getAttribute('data-original-price'));
    }

    const voucherButton = document.getElementById('selectVoucherBtn');
    const voucherModal = new bootstrap.Modal(document.getElementById('voucherModal'), {
        backdrop: 'static', // Ngăn click bên ngoài để đóng modal
    });

    if (voucherButton) {
        voucherButton.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
			voucherModal.show();
            loadVouchers();
        });
    }
});

if (document.getElementById('confirmPaymentBtn') != null) {
    document.getElementById('confirmPaymentBtn').addEventListener('click', function () {
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

// Biến lưu trữ thông tin giá và voucher
let originalPrice = 0;
let selectedVoucher = null;

// Khởi tạo khi trang được load


function createVoucherElement(voucher) {
    const div = document.createElement('div');
    div.className = 'voucher-item p-3 border rounded mb-2 hover:bg-gray-50';
    
    // Tính toán số tiền giảm
    let discountText = '';
    if (voucher.valueType === 'PERCENTAGE') {
        discountText = `Giảm ${voucher.discountValue}% `;
        if (voucher.maxValue) {
            discountText += `(tối đa ${formatCurrency(voucher.maxValue)})`;
        }
    } else {
        discountText = `Giảm ${formatCurrency(voucher.discountValue)}`;
    }

    div.innerHTML = `
        <div class="d-flex justify-content-between align-items-center">
            <div class="flex-grow-1">
                <h6 class="mb-1 font-semibold">${voucher.name}</h6>
                <p class="mb-1 text-success">${discountText}</p>
                <small class="text-muted d-block">HSD: ${formatDate(voucher.endDate)}</small>
                ${voucher.condition ? `<small class="text-muted d-block">Điều kiện: ${voucher.condition}</small>` : ''}
            </div>
            <button class="btn btn-outline-primary ms-3" data-voucher-id="${voucher.id}">
                Áp dụng
            </button>
        </div>
    `;

    // Add click event listener properly
    const applyButton = div.querySelector('button');
    applyButton.addEventListener('click', () => applyVoucher(voucher));
    
    return div;
}
// Hàm load danh sách voucher
function loadVouchers() {
    const courseId = document.querySelector('input[name="courseId"]').value;
    
    fetch(`${_ctx}api/vouchers/available`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            courseId: courseId,
            price: originalPrice
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(vouchers => {
	
        const voucherList = document.querySelector('.voucher-list');
        if (!voucherList) {
            console.error('Voucher list container not found');
		
            return;
        }
        
        voucherList.innerHTML = '';
		
        if (vouchers.length === 0) {
			
            voucherList.innerHTML = '<div class="text-center p-3">Không có voucher khả dụng</div>';
            return;
        }
		console.log("Number of vouchers:", vouchers.length);
		        vouchers.forEach(voucher => {
		            console.log("Creating voucher element for:", voucher);
		            const voucherElement = createVoucherElement(voucher);
		            voucherList.appendChild(voucherElement);
		        });
		        
		        // Add this debug line
		        console.log("Final voucher list HTML:", voucherList.innerHTML);
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('Không thể tải danh sách voucher', 'error');
    });
}


function applyVoucher(voucher) {
    selectedVoucher = voucher;
	let discountText = '';
    let discountAmount = 0;
    if (voucher.valueType === 'PERCENTAGE') {
        discountAmount = (originalPrice * voucher.discountValue) / 100;
        if (voucher.maxValue && discountAmount > voucher.maxValue) {
            discountAmount = voucher.maxValue;
        }
		discountText = `${voucher.discountValue}%`;
    } else {
        discountAmount = voucher.discountValue;
		discountText = formatCurrency(voucher.discountValue);
    }
    
    const finalPrice = Math.max(0, originalPrice - discountAmount);
    
    // Update displayed price
    const finalPriceElement = document.getElementById('finalPrice');
    if (finalPriceElement) {
        finalPriceElement.textContent = '₫ ' + formatDecimal(finalPrice);
    }
    
    // Update ALL hidden finalPrice inputs in ALL forms
    const finalPriceInputs = document.querySelectorAll('input[name="finalPrice"]');
    finalPriceInputs.forEach(input => {
        input.value = finalPrice;
    });
	const appliedVoucherElement = document.getElementById('appliedVoucher');
	    appliedVoucherElement.textContent = `${voucher.name} (${discountText})`;
    // Update voucher ID in ALL forms
    const voucherInputs = document.querySelectorAll('input[name="voucherId"]');
    voucherInputs.forEach(input => {
        input.value = voucher.id;
    });
    
    // Close modal
	const voucherModalInstance = bootstrap.Modal.getInstance(voucherModal);
	        if (voucherModalInstance) {
	            voucherModalInstance.hide();
	            // Khôi phục scroll cho payment modal
	            setTimeout(() => {
	                paymentModal.style.overflow = '';
	                // Xóa tất cả backdrop dư thừa
	                document.querySelectorAll('.modal-backdrop').forEach((backdrop, index) => {
	                    if (index > 0) backdrop.remove();
	                });
	            }, 200);
	        }
    
    showToast('Áp dụng voucher thành công!', 'success');
}

// Helper function to format decimal numbers similar to Thymeleaf's formatDecimal
function formatDecimal(number) {
    return number.toLocaleString('vi-VN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    });
}

// Update the removeVoucher function to also reset all form inputs
function removeVoucher() {
    selectedVoucher = null;
    
    // Reset displayed price
    const finalPriceElement = document.getElementById('finalPrice');
    if (finalPriceElement) {
        finalPriceElement.textContent = '₫ ' + formatDecimal(originalPrice);
    }
    
    // Reset ALL hidden finalPrice inputs
    const finalPriceInputs = document.querySelectorAll('input[name="finalPrice"]');
    finalPriceInputs.forEach(input => {
        input.value = originalPrice;
    });
    
    // Reset ALL voucher IDs
    const voucherInputs = document.querySelectorAll('input[name="voucherId"]');
    voucherInputs.forEach(input => {
        input.value = '';
    });
    
    showToast('Đã xóa voucher', 'info');
}

// Format tiền tệ
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Format ngày
function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { 
        year: 'numeric', 
        month: '2-digit', 
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    };
    return date.toLocaleDateString('vi-VN', options);
}

// Hiển thị toast message
function showToast(message, type = 'info') {
    // Implement toast message theo UI framework của bạn
    // Ví dụ sử dụng Bootstrap toast
    const toastContainer = document.getElementById('toastContainer');
    if (toastContainer) {
        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${type} border-0`;
        toast.setAttribute('role', 'alert');
        toast.setAttribute('aria-live', 'assertive');
        toast.setAttribute('aria-atomic', 'true');
        
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;
        
        toastContainer.appendChild(toast);
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        // Tự động xóa toast sau khi ẩn
        toast.addEventListener('hidden.bs.toast', function() {
            toast.remove();
        });
    }
}