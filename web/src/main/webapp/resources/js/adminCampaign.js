function loadCampaignsSection(event) {
	if (event) {
		event.preventDefault();
	}
	const dynamicContent = document.getElementById('dynamic-content');
	if (!dynamicContent) {
		console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
		return;
	}

	fetch(`${_ctx}admin/listCampaign`)
		.then(response => response.text())
		.then(html => {
			dynamicContent.innerHTML = html;
			initializePagination("campaigns");
		})
		.catch(error => console.error('Error loading campaigns section:', error));
}
function submitCampaignForm(event) {
	event.preventDefault(); // Ngăn form submit mặc định

	// Lấy form và dữ liệu form
	const form = document.querySelector('#campaignForm');
	const formData = new FormData(form);

	// Gửi yêu cầu fetch đến endpoint thêm chiến dịch
	fetch(`${_ctx}admin/addCampaign`, {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (!response.ok) {
				return response.json().then(data => {
					throw new Error(data.message || 'Unknown error occurred');
				});
			}
			return response.json();
		})
		.then(data => {
			if (data.status === "success") {
				// Hiển thị thông báo thành công
				showSuccessToast('Campaign added successfully!');
				/*loadCoursesSection(event);*/ // Load lại danh sách Campaign
				/* phải load lại trang Campaign, code trên load lại trang Courses*/
				loadCampaignsSection(event);
			} else {
				throw new Error(data.message); // Ném lỗi nếu không thành công
			}
		})
		.catch(error => {
			// Xử lý lỗi
			console.error('Error adding campaign:', error);
			document.getElementById('error-text-campaign').innerText = error.message;
			document.getElementById('error-message-campaign').style.display = 'block';
		});
}

function submitCampaignData(event) {
	event.preventDefault();

	const form = document.querySelector('#campaignForm');
	if (!form) {
		console.error("Form 'campaignForm' không tồn tại trên trang.");
		return;
	}

	const formData = new FormData(form);

	fetch(`${_ctx}admin/addCampaign`, {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (!response.ok) {
				return response.json().then(data => {
					throw new Error(data.message || 'Unknown error occurred');
				});
			}
			return response.json();
		})
		.then(data => {
			if (data.status === "success") {
				showSuccessToast('Campaign added successfully!');
				loadCampaignsSection(event); // Hàm này để tải lại danh sách campaign
			} else {
				throw new Error(data.message);
			}
		})
		.catch(error => {
			console.error('Error adding campaign:', error);
			const errorText = document.getElementById('error-text-campaign');
			const errorMessage = document.getElementById('error-message-campaign');
			if (errorText && errorMessage) {
				errorText.innerText = error.message;
				errorMessage.style.display = 'block';
			} else {
				console.error("Các phần tử thông báo lỗi không tồn tại trên trang.");
			}
		});
}

function fetchAddCampaignPage(event) {
    if (event) {
        event.preventDefault(); // Ngừng hành động mặc định của sự kiện
    }
    const dynamicContent = document.getElementById('dynamic-content');
    if (!dynamicContent) {
        console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
        return;
    }

    fetch(`${_ctx}admin/addCampaign`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            dynamicContent.innerHTML = html;
        })
        .catch(error => console.error('Error loading add campaign page: ', error));
}

function fetchAddCampaignHandsontablePage(event) {
	event.preventDefault();

	fetch(`${_ctx}admin/addCampaignHandsontable`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;


			initializeCourseHandsontable();
		})
		.catch(error => console.error('Error loading add course page:', error));
}

document.addEventListener('DOMContentLoaded', function() {
	// Kiểm tra xem đang ở trang nào để khởi tạo phân trang phù hợp
	if (document.getElementById('campaignsContainer')) {
		initializePagination('campaigns');
	}
});

function showDeleteConfirmModal_campaign(campaignId) {
	const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	const confirmBtn = document.getElementById('confirmDeleteBtn');

	// Xóa event listener cũ (nếu có)
	const newConfirmBtn = confirmBtn.cloneNode(true);
	confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

	// Thêm event listener mới
	newConfirmBtn.addEventListener('click', () => {
		deleteCampaign(campaignId, modal);
	});

	modal.show();
}

function deleteCampaign(campaignId, modal) {
	fetch(`${_ctx}admin/campaigns/delete/${campaignId}`, {
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
				showSuccessToast(data.message || 'Campaign deleted successfully');
				loadCampaignsSection(event);

				// Đóng modal nếu nó đang mở
				const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
				if (deleteModal) {
					deleteModal.hide();
				}
			} else {
				throw new Error(data.message || 'Failed to delete Campaign');
			}
		})
		.catch(error => {
			console.error('Error deleting course:', error);
			showErrorToast(error.message || 'An error occurred while deleting the course');
		});
}