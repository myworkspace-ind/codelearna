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
            initializePagination("courses"); 
        })
        .catch(error => console.error('Error loading courses section:', error));
}

function loadCourseLessons(courseId) {
	fetch(`${_ctx}admin/courses/${courseId}/lessons`)
		.then(response => response.text())
		.then(html => {

			document.getElementById('dynamic-content').innerHTML = html;
			initializePagination('lessons');
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
	if (event) {
		event.preventDefault();
	}
	const dynamicContent = document.getElementById('dynamic-content');
	if (!dynamicContent) {
		console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
		return;
	}

	fetch(`${_ctx}admin/addCourse`)
		.then(response => response.text())
		.then(html => {
			dynamicContent.innerHTML = html;
		})
		.catch(error => console.error('Error loading add course page: ', error));
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
				.filter(row => row.length >= 9)
				.map(row => ({
					name: row[0] !== undefined ? row[0].toString() : undefined,
					originalPrice: row[1] !== undefined ? parseFloat(row[1]) : undefined,
					discountedPrice: row[2] !== undefined ? parseFloat(row[2]) : undefined,
					imageUrl: row[3] !== undefined ? row[3].toString() : undefined,
					description: row[4] !== undefined ? row[4].toString() : undefined,
					difficultyLevel: row[5] !== undefined ? row[5].toString() : undefined,
					lessonType: row[6] !== undefined ? row[6].toString() : undefined,
					subcategory: row[7] !== undefined ? row[7].toString() : undefined,
					isFree: row[8] !== undefined ? row[8].toString().toUpperCase() === 'TRUE' : undefined
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
			colHeaders: ['Course Name', 'Original Price', 'Discounted Price','Image URL', 'Description', 'Difficulty Level', 'Lesson Type', 'Subcategory', 'Is Free'],
			columns: [
				{ data: 'name', type: 'text' },
				{ data: 'originalPrice', type: 'numeric' },
				{ data: 'discountedPrice', type: 'numeric' },
				{ data: 'imageUrl', type: 'text' },
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
				{
					data: 'subcategory',
					type: 'dropdown',
					source: ['Python', 'Java', 'Javascript', 'React', 'Machine Learning', 'IOS Development', 'Digital Marketing Strategy', 'Finance & Accounting', 'AI', 'Image Processing', 'Social Media', 'Flutter']
				},
				{ data: 'isFree', type: 'checkbox' }
			],
			minRows: 1,
			rowHeaders: true,
			contextMenu: true,
			height: 200,
			stretchH: 'all',
			colWidths: [, , , 100],
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
	console.log("rawData:", rawData);

	const courseData = rawData
		.map(row => ({
			name: row[0] !== undefined ? row[0].toString() : null,
			originalPrice: row[1] !== undefined ? parseFloat(row[1]) : null,
			discountedPrice: row[2] !== undefined ? parseFloat(row[2]) : null,
			imageUrl: row[3] !== undefined ? row[3].toString() : null,
			description: row[4] !== undefined ? row[4].toString() : null,
			difficultyLevel: row[5] !== undefined ? row[5].toString() : null,
			lessonType: row[6] !== undefined ? row[6].toString() : null,
			subcategory: row[7] !== undefined ? row[7].toString() : null,
			isFree: row[8] !== undefined ? row[8].toString().toUpperCase() === 'TRUE' : false
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
			document.getElementById('error-message-handsontable').style.display = 'block';
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



// Add parameter value with Handsontable
function loadParametersManagePage(event) {
	if (event) {
		event.preventDefault();
	}
	const dynamicContent = document.getElementById('dynamic-content');
	if (!dynamicContent) {
		console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
		return;
	}

	fetch(`${_ctx}admin/listParameters`)
		.then(response => response.text())
		.then(html => {
			dynamicContent.innerHTML = html;
		})
		.catch(error => console.error('Error loading courses section:', error));
}

function fetchAddParamValueHandsontablePage(event){
	if (event) {
			event.preventDefault();
		}
	fetch(`${_ctx}admin/addParameterHandsontable`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;

			initializeParameterHandsontable();
		})
		.catch(error => console.error('Error loading add parameter page:', error));
}



function handleFileParameter(event) {
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
				.filter(row => row.length >= 2)
				.map(row => ({
					paramKey: row[0] !== undefined ? row[0].toString() : undefined,
					paramValue: row[1] !== undefined ? row[1].toString() : undefined,
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



function initializeParameterHandsontable() {
	const containerHandsontable = document.getElementById('handsontable-parameter-container');

	if (containerHandsontable) {
		hot = new Handsontable(containerHandsontable, {
			data: [],
			colHeaders: ['Parameter Key', 'Parameter Value'],
			columns: [		
				{
					data: 'paramKey',
					type: 'dropdown',
					source: ['category', 'subcategory', 'difficulty_level', 'lesson_type']
				},
				{ data: 'paramValue', type: 'text' }
			],
			minRows: 1,
			rowHeaders: true,
			contextMenu: true,
			height: 200,
			stretchH: 'all',
			licenseKey: 'non-commercial-and-evaluation'
		});
		console.log('Handsontable initialized');
	} else {
		console.error('Error: Handsontable container not found.');
	}
}




function submitParameterData(event) {
	event.preventDefault();

	const rawData = hot.getData();
	console.log("rawData:", rawData);

	const parameterData = rawData
		.map(row => ({
			paramKey: row[0] !== undefined ? row[0].toString() : null,
			paramValue: row[1] !== undefined ? row[1].toString() : null,
			
		}));

	console.log('Parameter data to be sent:', parameterData);

	fetch(`${_ctx}admin/saveParametersHandsontable`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(parameterData)
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
				loadParametersManagePage(event);
			} else {
				throw new Error(data.message);
			}
		})
		.catch(error => {
			console.error('Error adding parameters:', error);
			document.getElementById('error-text').innerText = error.message;
			document.getElementById('error-message-parameter-handsontable').style.display = 'block';
		});
}

function deleteParameter(parameterId) {
	if (confirm('Are you sure you want to delete this parameter?')) {
		fetch(`${_ctx}admin/parameter/delete/${parameterId}`, {
			method: 'DELETE'
		})
			.then(response => {
				if (response.ok) {
					alert('Parameter deleted successfully');

					loadParametersManagePage(null);
				} else {
					alert('Failed to delete parameter');
				}
			})
			.catch(error => {
				console.error('Error deleting parameter:', error);
			});
	}
}

function loadEditParameterForm(parameterId) {
	fetch(`${_ctx}admin/parameter/edit/${parameterId}`)
		.then(response => response.text())
		.then(html => {
			if (!document.getElementById('editParameterModal')) {
				document.body.insertAdjacentHTML('beforeend', html);
			} else {
				document.getElementById('editParameterModal').outerHTML = html;
			}

			var editParameterModal = new bootstrap.Modal(document.getElementById('editParameterModal'));
			editParameterModal.show();

			document.getElementById('editParameterForm').addEventListener('submit', function(event) {
				event.preventDefault();
				submitEditParameterForm(event, parameterId);
			});
		})
		.catch(error => console.error('Error loading edit parameter form:', error));
}



function submitEditParameterForm(event, parameterId) {
	const form = event.target;
	const formData = new FormData(form);

	fetch(form.action, {
		method: 'POST',
		body: formData
	})
		.then(response => response.json())
		.then(data => {
			if (data.status === "success") {
				bootstrap.Modal.getInstance(document.getElementById('editParameterModal')).hide();
				loadParametersManagePage();
			} else {
				alert("Error: " + data.message);
			}
		})
		.catch(error => {
			console.error('Error updating parameter:', error);
			alert("An error occurred while updating the parameter.");
		});
}


// Định nghĩa state riêng cho từng loại bảng
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
    }
};

// Hàm cập nhật state phân trang
function updatePaginationState(type, currentPage, totalItems) {
    const state = paginationStates[type];
    state.currentPage = currentPage;
    state.totalPages = Math.ceil(totalItems / state.itemsPerPage);

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


// Khởi tạo khi DOM load
document.addEventListener('DOMContentLoaded', function() {
    // Kiểm tra xem đang ở trang nào để khởi tạo phân trang phù hợp
    if (document.getElementById('coursesContainer')) {
        initializePagination('courses');
    } else if (document.getElementById('lessonsContainer')) {
        initializePagination('lessons');
    }
});
