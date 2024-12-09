const paginationStates = {
	courses: {
		currentPage: 1,
		itemsPerPage: 10,
		totalPages: 1
	},
	lessons: {
		currentPage: 1,
		itemsPerPage: 5,
		totalPages: 1
	},
	parameters: {
		currentPage: 1,
		itemsPerPage: 7,
		totalPages: 1
	}
};

// Hàm cập nhật state phân trang
function updatePaginationState(type, currentPage, totalItems) {
	const state = paginationStates[type];
	state.currentPage = currentPage;
	state.totalPages = Math.ceil(totalItems / state.itemsPerPage);
}

// Hàm hiển thị các item theo trang
function showPage(type, page) {
	const tableBody = document.querySelector(`#${type}Container tbody`);
	if (!tableBody) return;

	const tableRows = tableBody.querySelectorAll('tr');
	const state = paginationStates[type];
	const start = (page - 1) * state.itemsPerPage;
	const end = start + state.itemsPerPage;

	tableRows.forEach((row, index) => {
		if (index >= start && index < end) {
			row.style.display = '';
		} else {
			row.style.display = 'none';
		}
	});
}

// Hàm tạo nút phân trang
function createPaginationButton(type, page, isActive = false, isDisabled = false) {
	const li = document.createElement('li');
	li.className = `page-item ${isActive ? 'active' : ''} ${isDisabled ? 'disabled' : ''}`;

	const a = document.createElement('a');
	a.className = 'page-link';
	a.href = '#';
	a.textContent = page;

	if (!isDisabled) {
		a.addEventListener('click', (e) => {
			e.preventDefault();
			if (typeof page === 'number') {
				paginationStates[type].currentPage = page;
				updatePagination(type);
			}
		});
	}

	li.appendChild(a);
	return li;
}

// Hàm tạo nút Previous/Next
function createNavigationButton(type, navType) {
	const state = paginationStates[type];
	const li = document.createElement('li');
	const isDisabled = navType === 'prev'
		? state.currentPage === 1
		: state.currentPage === state.totalPages;

	li.className = `page-item ${isDisabled ? 'disabled' : ''}`;

	const a = document.createElement('a');
	a.className = 'page-link';
	a.href = '#';
	a.setAttribute('aria-label', navType === 'prev' ? 'Previous' : 'Next');

	const span = document.createElement('span');
	span.setAttribute('aria-hidden', 'true');
	span.innerHTML = navType === 'prev' ? '&laquo;' : '&raquo;';

	a.appendChild(span);

	if (!isDisabled) {
		a.addEventListener('click', (e) => {
			e.preventDefault();
			if (navType === 'prev' && state.currentPage > 1) {
				state.currentPage--;
			} else if (navType === 'next' && state.currentPage < state.totalPages) {
				state.currentPage++;
			}
			updatePagination(type);
		});
	}

	li.appendChild(a);
	return li;
}

// Hàm cập nhật UI phân trang
function updatePagination(type) {
	const pagination = document.getElementById(`${type}Pagination`);
	if (!pagination) return;

	const state = paginationStates[type];

	// Xóa tất cả các nút phân trang hiện tại
	pagination.innerHTML = '';

	// Thêm nút Previous
	pagination.appendChild(createNavigationButton(type, 'prev'));

	// Tính toán các trang cần hiển thị
	let startPage = Math.max(1, state.currentPage - 2);
	let endPage = Math.min(state.totalPages, startPage + 4);

	// Điều chỉnh lại startPage nếu endPage đã đạt giới hạn
	startPage = Math.max(1, endPage - 4);

	// Thêm nút trang đầu và dấu ...
	if (startPage > 1) {
		pagination.appendChild(createPaginationButton(type, 1));
		if (startPage > 2) {
			pagination.appendChild(createPaginationButton(type, '...', false, true));
		}
	}

	// Thêm các nút số trang
	for (let i = startPage; i <= endPage; i++) {
		pagination.appendChild(createPaginationButton(type, i, i === state.currentPage));
	}

	// Thêm dấu ... và nút trang cuối
	if (endPage < state.totalPages) {
		if (endPage < state.totalPages - 1) {
			pagination.appendChild(createPaginationButton(type, '...', false, true));
		}
		pagination.appendChild(createPaginationButton(type, state.totalPages));
	}

	// Thêm nút Next
	pagination.appendChild(createNavigationButton(type, 'next'));

	// Hiển thị các item của trang hiện tại
	showPage(type, state.currentPage);
}

// Hàm khởi tạo phân trang cho một bảng cụ thể
function initializePagination(type) {
	const container = document.getElementById(`${type}Container`);
	if (!container) return;

	const totalItems = container.querySelectorAll('tbody tr').length;
	updatePaginationState(type, 1, totalItems);
	updatePagination(type);
}

// Export các hàm cần thiết
export {
	initializePagination,
	updatePagination,
	paginationStates
};