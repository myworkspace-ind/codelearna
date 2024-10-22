// List Course
function loadCoursesSection(event) {
	if (event) {
		event.preventDefault();
	}
	const dynamicContent = document.getElementById('dynamic-content');
	if (!dynamicContent) {
		console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
		return;
	}

	fetch(`${_ctx}admin/listCourse`)
		.then(response => response.text())
		.then(html => {
			dynamicContent.innerHTML = html;
		})
		.catch(error => console.error('Error loading courses section:', error));
}

function loadCourseLessons(courseId) {
	fetch(`${_ctx}admin/courses/${courseId}/lessons`)
		.then(response => response.text())
		.then(html => {

			document.getElementById('dynamic-content').innerHTML = html;
		})
		.catch(error => console.error('Lỗi khi tải danh sách bài học:', error));
}
function loadEditCourseForm(courseId) {
	fetch(`${_ctx}admin/courses/edit/${courseId}`)
		.then(response => response.text())
		.then(html => {
			if (!document.getElementById('editCourseModal')) {
				document.body.insertAdjacentHTML('beforeend', html);
			} else {
				document.getElementById('editCourseModal').outerHTML = html;
			}

			var editCourseModal = new bootstrap.Modal(document.getElementById('editCourseModal'));
			editCourseModal.show();

			document.getElementById('editCourseForm').addEventListener('submit', function(event) {
				event.preventDefault();
				submitEditCourseForm(event, courseId);
			});
		})
		.catch(error => console.error('Error loading edit course form:', error));
}



function submitEditCourseForm(event, courseId) {
	const form = event.target;
	const formData = new FormData(form);

	fetch(form.action, {
		method: 'POST',
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				bootstrap.Modal.getInstance(document.getElementById('editCourseModal')).hide();
				loadCoursesSection();
			} else {
				alert("Error: " + data.message);
			}
		})
		.catch(error => {
			console.error('Error updating course:', error);
			alert("An error occurred while updating the course.");
		});
}

function deleteCourse(courseId) {
	if (confirm('Are you sure you want to delete this course?')) {
		fetch(`${_ctx}admin/courses/delete/${courseId}`, {
			method: 'DELETE'
		})
			.then(response => {
				if (response.ok) {
					alert('Course deleted successfully');

					loadCoursesSection(null);
				} else {
					alert('Failed to delete course');
				}
			})
			.catch(error => {
				console.error('Error deleting course:', error);
			});
	}
}

// Course - lessons
function loadAddLessonForm(courseId) {
	fetch(`${_ctx}admin/courses/${courseId}/lessons/add`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;
		})
		.catch(error => console.error('Error loading add lesson form:', error));
}

function submitLessonForm(event) {
	event.preventDefault();

	const form = document.querySelector('#lessonForm');
	const formData = new FormData(form);
	const courseId = form.getAttribute('data-course-id');

	fetch(`${_ctx}admin/courses/${courseId}/lessons/add`, {
		method: 'POST',
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {

				loadCourseLessons(courseId);
			} else {
				const errorMessageDiv = document.getElementById('error-message');
				errorMessageDiv.innerText = data.message;
				errorMessageDiv.style.display = 'block';
			}
		})
		.catch(error => {
			console.error('Lỗi khi thêm bài học:', error);
			const errorMessageDiv = document.getElementById('error-message');
			errorMessageDiv.innerText = "Có lỗi xảy ra khi thêm bài học.";
			errorMessageDiv.style.display = 'block';
		});
}

function loadEditLessonForm(lessonId) {
	fetch(`${_ctx}admin/lessons/edit/${lessonId}`)
		.then(response => response.text())
		.then(html => {
			if (!document.getElementById('editLessonModal')) {
				document.body.insertAdjacentHTML('beforeend', html);
			} else {
				document.getElementById('editLessonModal').outerHTML = html;
			}
			var editLessonModal = new bootstrap.Modal(document.getElementById('editLessonModal'));
			editLessonModal.show();
			document.getElementById('editLessonForm').addEventListener('submit', function(event) {
				event.preventDefault();
				submitEditLessonForm(event, lessonId);
			});
		})
		.catch(error => console.error('Error loading edit lesson form:', error));
}

function submitEditLessonForm(event, lessonId) {
	const form = event.target;
	const formData = new FormData(form);

	fetch(form.action, {
		method: 'POST',
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				bootstrap.Modal.getInstance(document.getElementById('editLessonModal')).hide();
				loadCourseLessons(data.courseId);
			} else {
				alert("Error: " + data.message);
			}
		})
		.catch(error => {
			console.error('Error updating lesson:', error);
			alert("An error occurred while updating the lesson.");
		});
}


function deleteLesson(lessonId, courseId) {
	if (confirm('Are you sure you want to delete this lesson?')) {
		fetch(`${_ctx}admin/lessons/delete/${lessonId}`, {
			method: 'DELETE'
		})
			.then(response => response.json())
			.then(data => {
				if (data.status === "success") {
					alert('Lesson deleted successfully');

					loadCourseLessons(courseId);
				} else {
					alert('Failed to delete lesson: ' + data.message);
				}
			})
			.catch(error => {
				console.error('Error deleting lesson:', error);
				alert('An error occurred while deleting the lesson.');
			});
	}
}



// Add course 
function fetchAddCoursePage(event) {
	event.preventDefault();
	fetch(`${_ctx}admin/addCourse`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;
		})
		.catch(error => console.error('Error loading add course page:', error));
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

// Add course with Handsontable
function fetchAddCourseHandsontablePage(event) {
	event.preventDefault();

	fetch(`${_ctx}admin/addCourseHandsontable`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;


			initializeCourseHandsontable();
		})
		.catch(error => console.error('Error loading add course page:', error));
}


function handleFileCourse(event) {
	const input = event.target;
	const file = input.files[0];

	if (!file) {
		console.error('No file selected');
		return;
	}

	const reader = new FileReader();
	reader.onload = function(e) {
		try {
			const data = new Uint8Array(e.target.result);
			const workbook = XLSX.read(data, { type: 'array' });

			const firstSheetName = workbook.SheetNames[0];
			const worksheet = workbook.Sheets[firstSheetName];

			const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

			console.log('Excel Data:', jsonData);

			const handsontableData = jsonData
				.filter(row => row.length >= 7)
				.map(row => ({
					name: row[0] !== undefined ? row[0].toString() : undefined,
					originalPrice: row[1] !== undefined ? parseFloat(row[1]) : undefined,
					discountedPrice: row[2] !== undefined ? parseFloat(row[2]) : undefined,
					description: row[3] !== undefined ? row[3].toString() : undefined,
					difficultyLevel: row[4] !== undefined ? row[4].toString() : undefined,
					lessonType: row[5] !== undefined ? row[5].toString() : undefined,
					isFree: row[6] !== undefined ? row[6].toString().toUpperCase() === 'TRUE' : undefined
				}));

			if (hot) {
				hot.loadData(handsontableData);
				hot.render();
				console.log('Data loaded into Handsontable');
			} else {
				console.error('Handsontable instance not initialized');
			}
		} catch (error) {
			console.error('Error processing Excel file:', error);
		}
	};

	reader.onerror = function(ex) {
		console.error('Error reading file:', ex);
	};

	reader.readAsArrayBuffer(file);
}

function initializeCourseHandsontable() {
	const containerHandsontable = document.getElementById('handsontable-container');

	if (containerHandsontable) {
		hot = new Handsontable(containerHandsontable, {
			data: [],
			colHeaders: ['Course Name', 'Original Price', 'Discounted Price', 'Description', 'Difficulty Level', 'Lesson Type', 'Is Free'],
			columns: [
				{ data: 'name', type: 'text' },
				{ data: 'originalPrice', type: 'numeric' },
				{ data: 'discountedPrice', type: 'numeric' },
				{ data: 'description', type: 'text' },
				{
					data: 'difficultyLevel',
					type: 'dropdown',
					source: ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT', 'MASTER']
				},
				{
					data: 'lessonType',
					type: 'dropdown',
					source: ['VIDEO', 'INTERACTIVE']
				},
				{ data: 'isFree', type: 'checkbox' }
			],
			minRows: 1,
			rowHeaders: true,
			contextMenu: true,
			licenseKey: 'non-commercial-and-evaluation'
		});
		console.log('Handsontable initialized');
	} else {
		console.error('Error: Handsontable container not found.');
	}
}


function submitCourseData(event) {
	event.preventDefault();

	const rawData = hot.getData();
	console.log("rawData:",rawData)

	const courseData = rawData
		.map(row => ({
			name: row[0] !== undefined ? row[0].toString() : null,
			originalPrice: row[1] !== undefined ? parseFloat(row[1]) : null,
			discountedPrice: row[2] !== undefined ? parseFloat(row[2]) : null,
			description: row[3] !== undefined ? row[3].toString() : null,
			difficultyLevel: row[4] !== undefined ? row[4].toString() : null,
			lessonType: row[5] !== undefined ? row[5].toString() : null,
			isFree: row[6] !== undefined ? row[6].toString().toUpperCase() === 'TRUE' : false
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

				loadCoursesSection();
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


// Add Lessons with Handsontable
let hotLessons;

function fetchAddLessonHandsontablePage(event, courseId) {
	event.preventDefault();
	fetch(`${_ctx}admin/addLessonsHandsontable/${courseId}`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;
			initializeHandsontable();
		})
		.catch(error => console.error('Error loading add lessons with Handsontable page:', error));
}

function initializeHandsontable() {
	const container = document.getElementById('handsontable-container-lessons');
	if (container) {
		hotLessons = new Handsontable(container, {
			data: [],
			colHeaders: ['Lesson Title', 'Video URL'],
			columns: [
				{ data: 'title', type: 'text' },
				{ data: 'videoUrl', type: 'text' }
			],
			minRows: 1,
			rowHeaders: true,
			contextMenu: true,
			licenseKey: 'non-commercial-and-evaluation'
		});
		console.log('Handsontable for lessons initialized.');
	} else {
		console.error('Error: Cannot find Handsontable container for lessons.');
	}
}

function handleFile(event) {
	const input = event.target;
	const file = input.files[0];

	if (!file) {
		console.error('No file selected');
		return;
	}

	const reader = new FileReader();

	reader.onload = function(e) {
		try {
			const data = new Uint8Array(e.target.result);
			const workbook = XLSX.read(data, { type: 'array' });

			const firstSheetName = workbook.SheetNames[0];
			const worksheet = workbook.Sheets[firstSheetName];

			const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1, range: 3 });

			console.log('Raw Excel data:', jsonData);

			const handsontableData = jsonData
				.filter(row => row.length >= 2 && row[0] && row[1])
				.map(row => ({
					title: row[0].toString(),
					videoUrl: row[1].toString()
				}));

			console.log('Processed Handsontable data:', handsontableData);

			if (hotLessons) {
				hotLessons.loadData(handsontableData);
				hotLessons.render();
				console.log('Data loaded into Handsontable');
			} else {
				console.error('Handsontable instance not initialized');
			}
		} catch (error) {
			console.error('Error processing Excel file:', error);
		}
	};

	reader.onerror = function(ex) {
		console.error('Error reading file:', ex);
	};

	reader.readAsArrayBuffer(file);
}


function submitLessonData(event, courseId) {
	event.preventDefault();

	if (!hotLessons) {
		console.error('Handsontable not initialized');
		return;
	}

	const rawData = hotLessons.getData();
	const lessonData = rawData
		.filter(row => row[0] && row[1])
		.map(row => ({
			title: row[0].toString(),
			videoUrl: row[1].toString()
		}));

	console.log('Lesson data to be submitted:', lessonData);

	fetch(`${_ctx}admin/saveLessonsHandsontable/${courseId}`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(lessonData)
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
				alert(data.message);
				loadCourseLessons(courseId);
			} else {
				throw new Error(data.message);
			}
		})
		.catch(error => {
			console.error('Error submitting lesson data:', error);
			document.getElementById('error-text').innerText = error.message;
			document.getElementById('error-message').style.display = 'block';
		});
}


// Pagination

const itemsPerPageAdmin = 8;
let currentPageAdmin = 1;

function showPage(page) {
	const courses = document.querySelectorAll('tbody tr');
	const start = (page - 1) * itemsPerPageAdmin;
	const end = start + itemsPerPageAdmin;

	courses.forEach((course, index) => {
		if (index >= start && index < end) {
			course.style.display = '';
		} else {
			course.style.display = 'none';
		}
	});
}


function setupPagination() {
	const courses = document.querySelectorAll('tbody tr');
	const pageCount = Math.ceil(courses.length / itemsPerPageAdmin);
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
			setupPagination();
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