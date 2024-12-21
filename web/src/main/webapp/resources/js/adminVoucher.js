/**
 * 
 */
function loadVouchersSection(event) {
    if (event) {
        event.preventDefault();
    }
    const dynamicContent = document.getElementById('dynamic-content');
    if (!dynamicContent) {
        console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
        return;
    }

    fetch(`${_ctx}admin/listVoucher`)
        .then(response => response.text())
        .then(html => {
            dynamicContent.innerHTML = html;
        })
        .catch(error => console.error('Error loading vouchers section:', error));
}


function fetchAddVoucherPage(event) {
	if (event) {
		event.preventDefault();
	}
	const dynamicContent = document.getElementById('dynamic-content');
	if (!dynamicContent) {
		console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
		return;
	}

	fetch(`${_ctx}admin/addVoucher`)
		.then(response => response.text())
		.then(html => {
			dynamicContent.innerHTML = html;
		})
		.catch(error => console.error('Error loading add course page: ', error));
}

function toggleDiscountValue() {
    const valueType = document.getElementById("valueType").value; // Lấy giá trị của Value Type
    const discountValueInput = document.getElementById("discountValue"); // Ô Discount Value
    const maxValueInput = document.getElementById("maxValue"); // Ô Max Value

    if (valueType) {
        // Nếu có giá trị trong Value Type, enable Discount Value
        discountValueInput.disabled = false;

        if (valueType === "percentage") {
            // Nếu Value Type là Percentage
            discountValueInput.max = 100;

            // Kiểm tra và đặt lại Discount Value nếu vượt quá 100
            if (parseFloat(discountValueInput.value) > 100) {
                discountValueInput.value = 100;
            }

            // Enable ô Max Value
            maxValueInput.disabled = false;
        } else if (valueType === "fixed") {
            // Nếu Value Type là Fixed
            discountValueInput.removeAttribute("max");

            // Disable ô Max Value và gán giá trị bằng Discount Value
            maxValueInput.disabled = true;
			maxValueInput.value = '';
        }
    } else {
        // Nếu không chọn Value Type, disable Discount Value và Max Value
        discountValueInput.disabled = true;
        discountValueInput.value = "";
        discountValueInput.removeAttribute("max");

        maxValueInput.disabled = true;
        maxValueInput.value = "";
    }
}
function validateDiscountValue() {
    const valueType = document.getElementById('valueType').value;
    const discountValue = document.getElementById('discountValue');

    // Nếu valueType là "percentage", kiểm tra giá trị discountValue
    if (valueType === "percentage") {
        let value = parseFloat(discountValue.value);

        // Kiểm tra xem giá trị có trong khoảng từ 1 đến 100 không
        if (value < 1 || value > 100 || isNaN(value)) {
            if (value < 1) {
				discountValue.value = 1;
			}
			else if (value > 100) {
				discountValue.value = 100;
			}
        } else {
            discountValue.setCustomValidity(""); // Nếu hợp lệ, loại bỏ lỗi
        }
    } else {
        // Nếu không phải percentage, không có kiểm tra giá trị
        discountValue.setCustomValidity(""); // Loại bỏ lỗi nếu có
    }
}
function submitVoucherForm(event) {
    event.preventDefault(); // Ngăn form reload trang

    // Tạo đối tượng FormData để lấy toàn bộ dữ liệu từ form
    const formData = new FormData(document.getElementById('voucherForm'));

    // Chuẩn bị dữ liệu JSON để gửi đi
    const data = {
        name: formData.get('name'),
        campaign: formData.get('campaign'),
        discountValue: formData.get('discountValue'),
        valueType: formData.get('valueType'),
        maxValue: formData.get('maxValue'),
        quantity: formData.get('quantity'),
        startDate: formData.get('startDate'),
        endDate: formData.get('endDate'),
        description: formData.get('description'),
    };
    // Gửi dữ liệu đến API (controller)
    fetch('/codelearna-web/admin/addVoucher', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
    .then(response => {
        if (response.ok) {
            alert('Voucher added successfully!');
            loadVouchersSection(); // Gọi hàm load lại danh sách vouchers
        } else {
            return response.json().then(errorData => {
                throw new Error(errorData.message || 'Something went wrong!');
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('error-text-voucher').innerText = error.message;
        document.getElementById('error-message-voucher').style.display = 'block';
    });
}


function showError(message) {
    const errorMessage = document.getElementById('error-message-voucher');
    const errorText = document.getElementById('error-text-voucher');
    errorText.textContent = message;
    errorMessage.style.display = 'block';
}

function loadEditVoucherForm(voucherId) {
	fetch(`${_ctx}admin/vouchers/edit/${voucherId}`)
		.then(response => response.text())
		.then(html => {
			if (!document.getElementById('editVoucherModal')) {
				document.body.insertAdjacentHTML('beforeend', html);
			} else {
				document.getElementById('editVoucherModal').outerHTML = html;
			}
			const editVoucherModal = new bootstrap.Modal(document.getElementById('editVoucherModal'));
			editVoucherModal.show();
			document.getElementById('editVoucherForm').addEventListener('submit', function(event) {
				event.preventDefault();
				submitEditVoucherForm(event, voucherId);
			});
			document.getElementById('valueType').addEventListener('change', function () {
			    var valueType = this.value;
			    var maxValueInput = document.getElementById('maxValue');
			    
			    if (valueType === 'percentage') {
			        maxValueInput.disabled = false;
			    } else {
			        maxValueInput.disabled = true;
					maxValueInput.value = '';
			    }
			});
		})
		.catch(error => console.error('Error loading edit lesson form:', error));
}

function submitEditVoucherForm(event, voucherId) {
	event.preventDefault();
	const form = event.target;
	const formData = new FormData(form);
	// Chuẩn bị dữ liệu JSON để gửi đi
	const data = {
	    name: formData.get('name'),
	    campaign: formData.get('campaign'),
	    discountValue: formData.get('discountValue'),
	    valueType: formData.get('valueType'),
	    maxValue: formData.get('maxValue'),
	    quantity: formData.get('quantity'),
	    startDate: formData.get('startDate'),
	    endDate: formData.get('endDate'),
	    description: formData.get('description'),
	};
	fetch(form.action, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data),
	})
		.then(response => {
			if (!response.ok) {
				return response.json().then(data => Promise.reject(data));
			}
			return response.json();
		})
		.then(data => {
			if (data.status === "success") {
				showSuccessToast(data.message || 'Voucher updated successfully!');
				const modal = bootstrap.Modal.getInstance(document.getElementById('editVoucherModal'));
				if (modal) {
					modal.hide();
				}
				loadVouchersSection();
			} else {
				showErrorToast(data.message || 'Failed to update voucher');
			}
		})
		.catch(error => {
			console.error('Error updating voucher:', error);
			showErrorToast(error.message || 'An error occurred while updating the voucher');
		});
}
function showDeleteConfirmModal_voucher(voucherId) {
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    // Xóa event listener cũ (nếu có)
    const newConfirmBtn = confirmBtn.cloneNode(true);
    confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

    // Thêm event listener mới
    newConfirmBtn.addEventListener('click', () => {
        deleteVoucher(voucherId, modal);
    });

    modal.show();
}

function deleteVoucher(voucherId, modal) {
    fetch(`${_ctx}admin/vouchers/delete/${voucherId}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => Promise.reject(data));
        }
        return response.json();
    })
    .then(data => {
        if (data.status === 'success') {
            showSuccessToast(data.message || 'Voucher deleted successfully');
            loadVouchersSection(null); // Reload danh sách voucher


            // Đóng modal nếu nó đang mở
            const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
            if (deleteModal) {
                deleteModal.hide();
            }
        } else {
            throw new Error(data.message || 'Failed to delete voucher');
        }
    })
    .catch(error => {
        console.error('Error deleting voucher:', error);
        showErrorToast(error.message || 'An error occurred while deleting the voucher');
    });
}

function loadCampaignDates(event) {
    const campaignId = event.target.value;
    if (!campaignId) return;

    fetch(`${_ctx}admin/getCampaignDates?id=${campaignId}`)
        .then(response => response.json())
        .then(data => {
            if (data && data.startDate && data.endDate) {
                document.getElementById('startDate').value = data.startDate;
                document.getElementById('endDate').value = data.endDate;
            } else {
                console.error("Invalid campaign data:", data);
            }
        })
        .catch(error => console.error('Error fetching campaign dates:', error));
}
