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
            maxValueInput.value = discountValueInput.value;
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

// Phần bên dưới copy tham khảo, chưa dùng được
/*function loadEditCourseForm(courseId) {
	fetch(`${_ctx}admin/courses/edit/${courseId}`)
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to load edit course form');
			}
			return response.text();
		})
		.then(html => {
			if (!document.getElementById('editCourseModal')) {
				document.body.insertAdjacentHTML('beforeend', html);
			} else {
				document.getElementById('editCourseModal').outerHTML = html;
			}

			const editCourseModal = new bootstrap.Modal(document.getElementById('editCourseModal'));
			editCourseModal.show();

			document.getElementById('editCourseForm').addEventListener('submit', function(event) {
				event.preventDefault();
				submitEditCourseForm(event, courseId);
			});
		})
		.catch(error => {
			console.error('Error loading edit course form:', error);
			showErrorToast('Error loading course form');
		});
}
function showDeleteConfirmModal(courseId) {
	const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	const confirmBtn = document.getElementById('confirmDeleteBtn');

	// Xóa event listener cũ (nếu có)
	const newConfirmBtn = confirmBtn.cloneNode(true);
	confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

	// Thêm event listener mới
	newConfirmBtn.addEventListener('click', () => {
		deleteCourse(courseId, modal);
	});

	modal.show();
}
function deleteCourse(courseId, modal) {
	fetch(`${_ctx}admin/courses/delete/${courseId}`, {
		method: 'POST', // Chuyển từ DELETE sang POST
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
				showSuccessToast(data.message || 'Lesson deleted successfully');
				loadCoursesSection(null);;

				// Đóng modal nếu nó đang mở
				const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
				if (deleteModal) {
					deleteModal.hide();
				}
			} else {
				throw new Error(data.message || 'Failed to delete course');
			}
		})
		.catch(error => {
			console.error('Error deleting course:', error);
			showErrorToast(error.message || 'An error occurred while deleting the course');
		});
}
*/