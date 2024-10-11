function loadCoursesSection(event) {
	event.preventDefault();
	fetch(`${_ctx}admin/listCourse`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;
		})
		.catch(error => console.error('Error loading courses section:', error));
}

function fetchAddCoursePage(event) {
	event.preventDefault();
	fetch(`${_ctx}admin/addCourse`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;
		})
		.catch(error => console.error('Error loading add course page:', error));
}

function submitCourseForm(event) {
    event.preventDefault();

    const form = document.querySelector('#courseForm');
    const formData = new FormData(form);

    fetch(`${_ctx}admin/addCourse`, {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())  
    .then(data => {
		const errorMessageDiv = document.getElementById('error-message');
        if (data.status === "success") {
            loadCoursesSection(event);
			errorMessageDiv.style.display = 'none';
        } else {
            errorMessageDiv.innerText = data.message;
			errorMessageDiv.style.display = 'block';
        }
    })
    .catch(error => console.error('Error adding course:', error));
}


function filterSubcategories(categoryId) {
	const subcategorySelect = document.getElementById('subcategory');

	if (categoryId) {
		subcategorySelect.disabled = false;

		const allSubcategories = subcategorySelect.querySelectorAll('option');

		allSubcategories.forEach(option => {
			if (option.value === "" || option.getAttribute('data-category-id') === categoryId) {
				option.style.display = '';
			} else {
				option.style.display = 'none';
			}
		});

		subcategorySelect.value = "";
	} else {
		subcategorySelect.disabled = true;
		subcategorySelect.value = "";
	}
}




const itemsPerPage = 8; // Số lượng khóa học trên mỗi trang
let currentPage = 1;

function showPage(page) {
	const courses = document.querySelectorAll('tbody tr');  // Chọn các hàng trong bảng
	const start = (page - 1) * itemsPerPage;
	const end = start + itemsPerPage;

	courses.forEach((course, index) => {
		if (index >= start && index < end) {
			course.style.display = '';  // Hiển thị hàng
		} else {
			course.style.display = 'none';  // Ẩn hàng
		}
	});
}


function setupPagination() {
	const courses = document.querySelectorAll('tbody tr');  // Chọn các hàng trong bảng
	const pageCount = Math.ceil(courses.length / itemsPerPage);  // Tính số trang
	const pagination = document.getElementById('pagination');
	pagination.innerHTML = '';

	function createPageItem(page) {
		const pageItem = document.createElement('li');
		pageItem.className = 'page-item' + (page === currentPage ? ' active' : '');
		pageItem.innerHTML = `<a class="page-link" href="#">${page}</a>`;
		pageItem.addEventListener('click', function(event) {
			event.preventDefault();
			currentPage = page;
			showPage(currentPage);
			setupPagination(); // Cập nhật lại phân trang
		});
		return pageItem;
	}

	// Nút Previous
	const prevItem = document.createElement('li');
	prevItem.className = 'page-item' + (currentPage === 1 ? ' disabled' : '');
	prevItem.innerHTML = `<a class="page-link" href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>`;
	prevItem.addEventListener('click', function(event) {
		event.preventDefault();
		if (currentPage > 1) {
			currentPage--;
			showPage(currentPage);
			setupPagination();
		}
	});
	pagination.appendChild(prevItem);

	// Tạo các nút phân trang
	for (let i = 1; i <= pageCount; i++) {
		pagination.appendChild(createPageItem(i));
	}

	// Nút Next
	const nextItem = document.createElement('li');
	nextItem.className = 'page-item' + (currentPage === pageCount ? ' disabled' : '');
	nextItem.innerHTML = `<a class="page-link" href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>`;
	nextItem.addEventListener('click', function(event) {
		event.preventDefault();
		if (currentPage < pageCount) {
			currentPage++;
			showPage(currentPage);
			setupPagination();
		}
	});
	pagination.appendChild(nextItem);
}
document.addEventListener('DOMContentLoaded', function() {
	showPage(currentPage);
	setupPagination();
});
