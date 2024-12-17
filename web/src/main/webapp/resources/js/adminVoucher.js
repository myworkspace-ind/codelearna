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
*/
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

