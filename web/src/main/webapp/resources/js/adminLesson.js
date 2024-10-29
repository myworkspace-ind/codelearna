
function loadCourseLessons(courseId) {
	fetch(`${_ctx}admin/courses/${courseId}/lessons`)
		.then(response => response.text())
		.then(html => {

			document.getElementById('dynamic-content').innerHTML = html;
			initializePagination('lessons');
		})
		.catch(error => console.error('Lỗi khi tải danh sách bài học:', error));
}





function loadAddLessonForm(courseId) {
	fetch(`${_ctx}admin/courses/${courseId}/lessons/add`)
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to load add lesson form');
			}
			return response.text();
		})
		.then(html => {
			document.getElementById('dynamic-content').innerHTML = html;
			initializeAddLessonForm();
		})
		.catch(error => {
			console.error('Error loading add lesson form:', error);
			showErrorToast('Failed to load add lesson form');
		});
}

function initializeAddLessonForm() {
	const form = document.querySelector('#lessonForm');
	if (form) {
		form.addEventListener('submit', submitLessonForm);
	}
}

function submitLessonForm(event) {
	event.preventDefault();

	const form = event.target;
	const formData = new FormData(form);
	const courseId = form.getAttribute('data-course-id');

	// Basic form validation
	const title = formData.get('title');
	if (!title || title.trim() === '') {
		showErrorToast('Please enter a lesson title');
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
			if (data.status === 'success') {
				showSuccessToast(data.message || 'Lesson added successfully!');
				if (data.courseId) {
					loadCourseLessons(data.courseId);
				}
			} else {
				throw new Error(data.message || 'Failed to add lesson');
			}
		})
		.catch(error => {
			console.error('Error adding lesson:', error);
			showErrorToast(error.message || 'An error occurred while adding the lesson');
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
	event.preventDefault();
	const form = event.target;
	const formData = new FormData(form);

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
				showSuccessToast(data.message || 'Lesson updated successfully!');
				const modal = bootstrap.Modal.getInstance(document.getElementById('editLessonModal'));
				if (modal) {
					modal.hide();
				}
				if (data.courseId) {
					loadCourseLessons(data.courseId);
				}
			} else {
				showErrorToast(data.message || 'Failed to update lesson');
			}
		})
		.catch(error => {
			console.error('Error updating lesson:', error);
			showErrorToast(error.message || 'An error occurred while updating the lesson');
		});
}

function deleteLesson(lessonId, courseId) {
	fetch(`${_ctx}admin/lessons/delete/${lessonId}`, {
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
				const targetCourseId = data.courseId || courseId;
				loadCourseLessons(targetCourseId);

				// Đóng modal nếu nó đang mở
				const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
				if (deleteModal) {
					deleteModal.hide();
				}
			} else {
				throw new Error(data.message || 'Failed to delete lesson');
			}
		})
		.catch(error => {
			console.error('Error deleting lesson:', error);
			showErrorToast(error.message || 'An error occurred while deleting the lesson');
		});
}


function showLessonDeleteConfirmModal(lessonId, courseId) {
	const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	const confirmBtn = document.getElementById('confirmDeleteBtn');

	confirmBtn.replaceWith(confirmBtn.cloneNode(true));

	document.getElementById('confirmDeleteBtn').addEventListener('click', () => {
		deleteLesson(lessonId, courseId);
	});

	modal.show();
}

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



document.addEventListener('DOMContentLoaded', function() {
	// Kiểm tra xem đang ở trang nào để khởi tạo phân trang phù hợp
	if (document.getElementById('lessonsContainer')) {
		initializePagination('lessons');
	}
});

