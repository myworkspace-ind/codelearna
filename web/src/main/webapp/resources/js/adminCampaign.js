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