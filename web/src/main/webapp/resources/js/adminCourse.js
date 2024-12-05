/*import { initializePagination } from './pagination.js';
import { 
	showSuccessToast, 
	showErrorToast 
} from './toast.js';
*/
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

function loadEditCourseForm(courseId) {
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

function showDeleteConfirmModal(courseId) {
	const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	const confirmBtn = document.getElementById('confirmDeleteBtn');

	// Xóa event listener cũ (nếu có)
	const newConfirmBtn = confirmBtn.cloneNode(true);
	confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

	// Thêm event listener mới
	newConfirmBtn.addEventListener('click', () => {
		deleteCourse(courseId, modal);
	});

	modal.show();
}
function deleteCourse(courseId, modal) {
	fetch(`${_ctx}admin/courses/delete/${courseId}`, {
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
				showSuccessToast(data.message || 'Lesson deleted successfully');
				loadCoursesSection(null);;

				// Đóng modal nếu nó đang mở
				const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
				if (deleteModal) {
					deleteModal.hide();
				}
			} else {
				throw new Error(data.message || 'Failed to delete course');
			}
		})
		.catch(error => {
			console.error('Error deleting course:', error);
			showErrorToast(error.message || 'An error occurred while deleting the course');
		});
}

function toggleCourseStatus(courseId) {
    showSpinnerLoading(); // Hiển thị spinner khi bắt đầu yêu cầu

    fetch(`${_ctx}admin/courses/toggleCourseStatus/${courseId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ courseId: courseId })
    })
    .then(response => response.json())
    .then(data => {
        hideAllLoading(); // Ẩn spinner sau khi nhận được phản hồi

        if (data.status === 'success') {
            showSuccessToast(data.message || 'Thay đổi trạng thái khóa học thành công');
            loadCoursesSection(); // Tải lại danh sách khóa học sau khi thay đổi trạng thái

            const statusChangeModal = bootstrap.Modal.getInstance(document.getElementById('statusChangeModal'));
            if (statusChangeModal) {
                statusChangeModal.hide(); // Ẩn modal nếu có
            }
        } else {
            alert('Lỗi: ' + data.message);
        }
    })
    .catch(error => {
        hideAllLoading(); // Đảm bảo spinner được ẩn ngay cả khi có lỗi

        console.error('Lỗi:', error);
        alert("Đã xảy ra lỗi trong khi thay đổi trạng thái khóa học.");
    });
}


function showCourseStatusChangeModal(courseId, currentStatus) {

    const modalElement = document.getElementById('statusChangeModal');
    if (!modalElement) {
        console.error("Modal element not found.");
        return;
    }

    const modal = new bootstrap.Modal(modalElement);

    document.getElementById('confirmStatusChangeBtn').dataset.courseId = courseId;
    document.getElementById('confirmStatusChangeBtn').dataset.currentStatus = currentStatus;
	
	document.getElementById('confirmStatusChangeBtn').addEventListener('click', () => {
			toggleCourseStatus(courseId);
		});

    modal.show();
}
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
				showSuccessToast('Course added successfully!');
				loadCoursesSection(event);
			} else {
				throw new Error(data.message);
			}
		})
		.catch(error => {
			console.error('Error adding course:', error);
			document.getElementById('error-text-course').innerText = error.message;
			document.getElementById('error-message-course').style.display = 'block';
		});
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
					name: row[0] !== undefined ? row[0].toString() : null,
					originalPrice: row[1] !== undefined ? parseFloat(row[1]) : null,
					discountedPrice: row[2] !== undefined ? parseFloat(row[2]) : null,
					imageUrl: row[3] !== undefined ? row[3].toString() : null,
					description: row[4] !== undefined ? row[4].toString() : null,
					difficultyLevel: row[5] !== undefined ? row[5].toString() : null,
					lessonType: row[6] !== undefined ? row[6].toString() : null,
					subcategory: row[7] !== undefined ? row[7].toString() : null,
					isFree: row[8] !== undefined ? row[8].toString().toUpperCase() === 'TRUE' : null
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
			colHeaders: ['Course Name', 'Original Price', 'Discounted Price', 'Image URL', 'Description', 'Difficulty Level', 'Lesson Type', 'Subcategory', 'Is Free'],
			columns: [
				{ data: 'name', type: 'text' },
				{ data: 'originalPrice', type: 'numeric' },
				{ data: 'discountedPrice', type: 'numeric' },
				{ data: 'imageUrl', type: 'text' },
				{ data: 'description', type: 'text' },
				{
					data: 'difficultyLevel',
					type: 'dropdown',
					source: ['BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT', 'MASTER'],
					
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
            name: row[0] !== null ? row[0].toString() : null,
            // Chuyển đổi giá trị sang số thập phân
            originalPrice: row[1] !== null ? parseFloat(row[1]).toFixed(2) : null,
            discountedPrice: row[2] !== null ? parseFloat(row[2]).toFixed(2) : null,
            imageUrl: row[3] !== null ? row[3].toString() : null,
            description: row[4] !== null ? row[4].toString() : null,
            difficultyLevel: row[5] !== null ? row[5].toString() : null,
            lessonType: row[6] !== null ? row[6].toString() : null,
            subcategory: row[7] !== null ? row[7].toString() : null,
            isFree: row[8] !== null ? row[8].toString().toUpperCase() === 'TRUE' : false
        }))
        .filter(row => row.name !== null || row.originalPrice !== null || 
                row.discountedPrice !== null || row.difficultyLevel !== null ||
                row.lessonType !== null || row.subcategory !== null);

    if (courseData.length === 0) {
        document.getElementById('error-text-course-handsontable').innerText = 'Please enter at least one course value.';
        document.getElementById('error-message-course-handsontable').style.display = 'block';
        return;
    }

    // Validate numeric values
    for (const course of courseData) {
        if (course.originalPrice && isNaN(parseFloat(course.originalPrice))) {
            showErrorToast('Original price must be a valid number');
            return;
        }
        if (course.discountedPrice && isNaN(parseFloat(course.discountedPrice))) {
            showErrorToast('Discounted price must be a valid number');
            return;
        }
    }

    console.log('Course data to be sent:', courseData);

    fetch(`${_ctx}admin/saveCoursesHandsontable`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(courseData)
    })
    .then(response => {
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return response.json().then(data => {
                if (!response.ok) {
                    throw new Error(data.message || 'Unknown error occurred');
                }
                return data;
            });
        } else {
            return response.text().then(text => {
                throw new Error(`Server returned non-JSON response: ${text}`);
            });
        }
    })
    .then(data => {
        if (data.status === "success") {
            showSuccessToast(data.message || 'Course added successfully');
            loadCoursesSection(event);
        } else {
            throw new Error(data.message || 'Unknown error occurred');
        }
    })
    .catch(error => {
        console.error('Error adding courses:', error);
        document.getElementById('error-text-course-handsontable').innerText = error.message;
        document.getElementById('error-message-course-handsontable').style.display = 'block';
    });
}

document.addEventListener('DOMContentLoaded', function() {
	// Kiểm tra xem đang ở trang nào để khởi tạo phân trang phù hợp
	if (document.getElementById('coursesContainer')) {
		initializePagination('courses');
	}
});

