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

function fetchAddCourseHandsontablePage(event) {
	event.preventDefault();
	fetch(`${_ctx}admin/addCourseHandsontable`)
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



// Cấu hình Handsontable
let hot;
document.addEventListener('DOMContentLoaded', function() {
	const containerHandsontable = document.getElementById('handsontable-container');
	if (containerHandsontable) {
		hot = new Handsontable(containerHandsontable, {
			data: [],
			colHeaders: ['Course Name', 'Original Price', 'Discounted Price', 'Description'],
			columns: [
				{ data: 'name', type: 'text' },
				{ data: 'originalPrice', type: 'numeric' },
				{ data: 'discountedPrice', type: 'numeric' },
				{ data: 'description', type: 'text' }
				/*	{
						data: 'difficultyLevel',
						type: 'dropdown',
						source: ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT', 'MASTER']
					},
					{
						data: 'lessonType',
						type: 'dropdown',
						source: ['VIDEO', 'INTERACTIVE']
					},
					{ data: 'isFree', type: 'checkbox' }*/
			],
			minRows: 1,
			minCols: 4,
			rowHeaders: true,
			contextMenu: true,
			licenseKey: 'non-commercial-and-evaluation'
		});
	} else {
		console.error('Container for Handsontable not found.');
	}
});



function submitCourseData(event) {
	event.preventDefault();
	const rawData = hot.getData();
	const courseData = rawData.filter(row => row.some(cell => cell !== null && cell !== '')).map(row => ({
		name: row[0],
		originalPrice: parseFloat(row[1]),
		discountedPrice: parseFloat(row[2]),
		description: row[3],
		difficultyLevel: row[4] || 'BEGINNER',
		lessonType: row[5] || 'VIDEO'
		
	}));

	console.log('Course data to be sent:', courseData);

	fetch(`${_ctx}admin/saveCoursesHandsontable`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(courseData)
	})
		.then(response => {
			console.log('Response status:', response.status);
			if (!response.ok) {
				return response.json().then(data => {
					throw new Error(data.message || 'Unknown error occurred');
				});
			}
			return response.json();
		})
		.then(data => {
			if (data.status === "success") {
				alert(data.message);
				loadCoursesSection(event);
			} else {
				throw new Error(data.message);
			}
		})
		.catch(error => {
			console.error('Error adding courses:', error);
			document.getElementById('error-text').innerText = error.message;
			document.getElementById('error-message').style.display = 'block';
		});
}





const itemsPerPageAdmin = 8; // Số lượng khóa học trên mỗi trang
let currentPageAdmin = 1;

function showPage(page) {
	const courses = document.querySelectorAll('tbody tr');  // Chọn các hàng trong bảng
	const start = (page - 1) * itemsPerPageAdmin;
	const end = start + itemsPerPageAdmin;

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
	const pageCount = Math.ceil(courses.length / itemsPerPageAdmin);  // Tính số trang
	const pagination = document.getElementById('pagination');
	pagination.innerHTML = '';

	function createPageItem(page) {
		const pageItem = document.createElement('li');
		pageItem.className = 'page-item' + (page === currentPageAdmin ? ' active' : '');
		pageItem.innerHTML = `<a class="page-link" href="#">${page}</a>`;
		pageItem.addEventListener('click', function(event) {
			event.preventDefault();
			currentPageAdmin = page;
			showPage(currentPageAdmin);
			setupPagination(); // Cập nhật lại phân trang
		});
		return pageItem;
	}

	// Nút Previous
	const prevItem = document.createElement('li');
	prevItem.className = 'page-item' + (currentPageAdmin === 1 ? ' disabled' : '');
	prevItem.innerHTML = `<a class="page-link" href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>`;
	prevItem.addEventListener('click', function(event) {
		event.preventDefault();
		if (currentPageAdmin > 1) {
			currentPageAdmin--;
			showPage(currentPageAdmin);
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
	nextItem.className = 'page-item' + (currentPageAdmin === pageCount ? ' disabled' : '');
	nextItem.innerHTML = `<a class="page-link" href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>`;
	nextItem.addEventListener('click', function(event) {
		event.preventDefault();
		if (currentPageAdmin < pageCount) {
			currentPageAdmin++;
			showPage(currentPageAdmin);
			setupPagination();
		}
	});
	pagination.appendChild(nextItem);
}
document.addEventListener('DOMContentLoaded', function() {
	showPage(currentPageAdmin);
	setupPagination();
});
