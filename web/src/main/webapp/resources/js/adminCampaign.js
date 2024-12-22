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
	const startTime = formData.get('startTime');
	    const endTime = formData.get('endTime');
	    
	    if (startTime) {
	        formData.set('startTime', new Date(startTime).toISOString());
	    }
	    if (endTime) {
	        formData.set('endTime', new Date(endTime).toISOString());
	    }
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

function loadEditCampaignForm(campaignId) {
    fetch(`${_ctx}admin/campaigns/edit/${campaignId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to load edit campaign form');
            }
            return response.text();
        })
        .then(html => {
            if (!document.getElementById('editCampaignModal')) {
                document.body.insertAdjacentHTML('beforeend', html);
            } else {
                document.getElementById('editCampaignModal').outerHTML = html;
            }

            const editCampaignModal = new bootstrap.Modal(document.getElementById('editCampaignModal'));
            editCampaignModal.show();

            document.getElementById('editCampaignForm').addEventListener('submit', function(event) {
                event.preventDefault(); 
                submitEditCampaignForm(event, campaignId); 
            });
        })
        .catch(error => {
            console.error('Error loading edit campaign form:', error);
            showErrorToast('Error loading campaign form'); 
        });
}

function submitEditCampaignForm(event, campaignId) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    // Basic validation
    const name = formData.get('name');
    if (!name || name.trim() === '') {
        showErrorToast('Please enter a campaign name');
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
                showSuccessToast(data.message || 'Campaign updated successfully!');
                const modal = bootstrap.Modal.getInstance(document.getElementById('editCampaignModal'));
                if (modal) {
                    modal.hide();
                }
                loadCampaignsSection(); // Reload danh sách campaign
            } else {
                throw new Error(data.message || 'Failed to update campaign');
            }
        })
        .catch(error => {
            console.error('Error updating campaign:', error);
            showErrorToast(error.message || 'An error occurred while updating the campaign');
        });
}

function addVoucherById(campaignId) {
    const dynamicContent = document.getElementById('dynamic-content');
    if (!dynamicContent) {
        console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
        return;
    }

    fetch(`${_ctx}admin/addVoucher?campaignId=${campaignId}`)
        .then(response => response.text())
        .then(html => {
            dynamicContent.innerHTML = html;

            fetch(`${_ctx}admin/getCampaignDates?id=${campaignId}`)
                .then(response => response.json())
                .then(data => {
                    if (data.startDate && data.endDate) {
                        document.getElementById("startDate").value = data.startDate;
                        document.getElementById("endDate").value = data.endDate;
                    } else {
                        console.warn("Không thể tải ngày bắt đầu và kết thúc.");
                    }
                })
                .catch(error => console.error('Error fetching campaign dates: ', error));
        })
        .catch(error => console.error('Error loading add voucher page: ', error));
}

function showCampaignStatusChangeModal(campaignId, currentStatus) {
    const modal = new bootstrap.Modal(document.getElementById('statusChangeModal'));
    const confirmBtn = document.getElementById('confirmStatusChangeBtn');

    // Xóa event listener cũ (nếu có)
    const newConfirmBtn = confirmBtn.cloneNode(true);
    confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

    // Thêm event listener mới
    newConfirmBtn.addEventListener('click', () => {
        toggleCampaignStatus(campaignId, currentStatus, modal);
    });

    modal.show();
}

function toggleCampaignStatus(campaignId, currentStatus, modal) {
    const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';

    fetch(`${_ctx}admin/campaigns/toggleStatus/${campaignId}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ status: newStatus })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => Promise.reject(data));
        }
        return response.json();
    })
    .then(data => {
        if (data.status === 'success') {
            showSuccessToast(data.message || 'Campaign status updated successfully');
            loadCampaignsSection();

            // Đóng modal nếu nó đang mở
            if (modal) {
                modal.hide();
            }
        } else {
            throw new Error(data.message || 'Failed to update campaign status');
        }
    })
    .catch(error => {
        console.error('Error updating campaign status:', error);
        showErrorToast(error.message || 'An error occurred while updating the campaign status');
    });
}
