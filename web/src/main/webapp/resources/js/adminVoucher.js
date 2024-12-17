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
function submitEditCourseForm(event, courseId) {
	event.preventDefault();
	const form = event.target;
	const formData = new FormData(form);

	// Basic validation
	const name = formData.get('name');
	if (!name || name.trim() === '') {
		showErrorToast('Please enter a course name');
		return;
	}

	fetch(form.action, {
		method: 'POST',
		body: formData,
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
			if (data.status === "success") {
				showSuccessToast(data.message || 'Course updated successfully!');
				const modal = bootstrap.Modal.getInstance(document.getElementById('editCourseModal'));
				if (modal) {
					modal.hide();
				}
				loadCoursesSection();
			} else {
				throw new Error(data.message || 'Failed to update course');
			}
		})
		.catch(error => {
			console.error('Error updating course:', error);
			showErrorToast(error.message || 'An error occurred while updating the course');
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