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

function deleteCourse(courseId) {
    if (confirm('Are you sure you want to delete this course?')) {
        fetch(`${_ctx}admin/courses/delete/${courseId}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert('Course deleted successfully');
                // Reload or update the course list
                loadCoursesSection(); 
            } else {
                alert('Failed to delete course');
            }
        })
        .catch(error => {
            console.error('Error deleting course:', error);
        });
    }
}


function loadCourseLessons(courseId) {
    fetch(`${_ctx}admin/courses/${courseId}/lessons`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
        })
        .catch(error => console.error('Error loading course lessons:', error));
}


function loadEditCourseForm(courseId) {
	fetch(`${_ctx}admin/courses/edit/${courseId}`)
		.then(response => response.text())
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;
			var editCourseModal = new bootstrap.Modal(document.getElementById('editCourseModal'));
			editCourseModal.show(); 
		})
		.catch(error => console.error('Error loading edit course form:', error));
}

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


    fetch(`${_ctx}admin/courses/${courseId}/lessons/add`, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
           
            loadCourseLessons(courseId);  
        } else {
            // Xử lý lỗi
            return response.json().then(data => {
                const errorMessageDiv = document.getElementById('error-message');
                errorMessageDiv.innerText = data.message;
                errorMessageDiv.style.display = 'block';
            });
        }
    })
    .catch(error => console.error('Error adding lesson:', error));
}



function loadEditLessonForm(lessonId) {
    fetch(`${_ctx}admin/lessons/edit/${lessonId}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html; 
        })
        .catch(error => console.error('Error loading edit lesson form:', error));
}


function deleteLesson(lessonId) {
    if (confirm('Are you sure you want to delete this lesson?')) {
        fetch(`${_ctx}admin/lessons/delete/${lessonId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    alert('Lesson deleted successfully');
                   
                    loadCourseLessons(courseId);
                } else {
                    alert('Failed to delete lesson');
                }
            })
            .catch(error => console.error('Error deleting lesson:', error));
    }
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



let hotLessons;
function fetchAddLessonHandsontablePage(event, courseId) {
    event.preventDefault();
    fetch(`${_ctx}admin/addLessonsHandsontable/${courseId}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
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
        })
        .catch(error => console.error('Error loading add lessons with Handsontable page:', error));
}


function submitLessonData(event, courseId) {
    event.preventDefault();

    const rawData = hotLessons.getData();
    const lessonData = rawData.filter(row => row.some(cell => cell !== null && cell !== '')).map(row => ({
        title: row[0],
        videoUrl: row[1]
    }));

    console.log('Lesson data to be sent:', lessonData);

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
            console.error('Error adding lessons:', error);
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