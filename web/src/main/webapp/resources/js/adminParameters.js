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
			initializePagination("parameters");
		})
		.catch(error => console.error('Error loading courses section:', error));
}

function fetchAddParamValueHandsontablePage(event) {
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
					paramKey: row[0] !== undefined ? row[0].toString() : null,
					paramValue: row[1] !== undefined ? row[1].toString() : null,
					seqno: row[2] !== undefined && !isNaN(parseInt(row[2]))
						? parseInt(row[2])
						: null
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
			colHeaders: ['Parameter Key', 'Parameter Value', 'Sequence Number'],
			columns: [
				{
					data: 'paramKey',
					type: 'dropdown',
					source: ['category', 'subcategory', 'difficulty_level', 'lesson_type']
				},
				{ data: 'paramValue', type: 'text' },
				{
					data: 'seqno',
					type: 'numeric',
					validator: (value) => {
						return value === null || (!isNaN(value) && Number.isInteger(Number(value)));
					},
					allowInvalid: false
				}
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
			paramKey: row[0] !== null ? row[0].toString() : null,
			paramValue: row[1] !== null ? row[1].toString() : null,
			seqno: row[2] !== null && !isNaN(parseInt(row[2])) ? parseInt(row[2]) : null 
		}))
		.filter(row => row.paramKey !== null || row.paramValue !== null);
	if (parameterData.length === 0) {
		showErrorToast('Please enter at least one parameter key and value.');
		return;
	}

	console.log('Parameter data to be sent:', parameterData);

	fetch(`${_ctx}admin/saveParametersHandsontable`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Accept': 'application/json'
		},
		body: JSON.stringify(parameterData)
	})
		.then(response => {
			// Kiểm tra content-type của response
			const contentType = response.headers.get('content-type');
			if (contentType && contentType.includes('application/json')) {
				return response.json().then(data => {
					if (!response.ok) {
						throw new Error(data.message || 'Server error occurred');
					}
					return data;
				});
			} else {
				throw new Error('Invalid response format from server');
			}
		})
		.then(data => {
			if (data.status === "success") {
				showSuccessToast(data.message || 'Parameters added successfully');
				loadParametersManagePage(event);
			} else {
				throw new Error(data.message || 'Unknown error occurred');
			}
		})
		.catch(error => {
			console.error('Error adding parameters:', error);
			showErrorToast(error.message || 'Error occurred while saving parameters');
		});
}


function deleteParameter(parameterId, modal) {
	fetch(`${_ctx}admin/parameter/delete/${parameterId}`, {
		method: 'POST',
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
				showSuccessToast(data.message || 'Parameter deleted successfully');

				loadParametersManagePage(null);

				// Đóng modal nếu nó đang mở
				const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
				if (deleteModal) {
					deleteModal.hide();
				}
			} else {
				throw new Error(data.message || 'Failed to delete parameter');
			}
		})
		.catch(error => {
			console.error('Error deleting lesson:', error);
			showErrorToast(error.message || 'An error occurred while deleting the parameter');
		});
}
function showDeleteParameterConfirmModal(parameterId) {
	const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
	const confirmBtn = document.getElementById('confirmDeleteBtn');

	// Xóa event listener cũ (nếu có)
	const newConfirmBtn = confirmBtn.cloneNode(true);
	confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);

	// Thêm event listener mới
	newConfirmBtn.addEventListener('click', () => {
		deleteParameter(parameterId, modal);
	});

	modal.show();
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
				showSuccessToast(data.message || 'Parameter updated successfully!');
				const editParameterModalInstance = bootstrap.Modal.getInstance(document.getElementById('editParameterModal'));
				if (editParameterModalInstance) {
					editParameterModalInstance.hide();
				}
				loadParametersManagePage(event);
			} else {
				throw new Error(data.message);
			}
		})
		.catch(error => {
			console.error('Error submitting parameter data:', error);
			document.getElementById('error-text-parameter-handsontable').innerText = error.message;
			document.getElementById('error-message-parameter-handsontable').style.display = 'block';
		});
}
document.addEventListener('DOMContentLoaded', function() {
	if (document.getElementById('parametersContainer')) {
		initializePagination('parameters');
	}
});

function initializeFilterKeyListener() {
    const filterKey = document.getElementById('filterKey');
    if (!filterKey) {
        console.warn("Element with ID 'filterKey' not found. Retrying...");
        setTimeout(initializeFilterKeyListener, 100);
        return;
    }

    console.log("FilterKey found in DOM. Adding event listener.");
    
    const parameterRows = document.querySelectorAll('#parametersContainer tbody tr');
    console.log('Parameter rows:', parameterRows);

    filterKey.addEventListener('change', function () {
        const selectedKey = filterKey.value;
        console.log('Selected Key:', selectedKey);

        parameterRows.forEach(row => {
            const parameterKey = row.querySelector('td:nth-child(2)').innerText.trim();
            console.log('Row Parameter Key:', parameterKey);

            if (selectedKey === "" || parameterKey === selectedKey) {
                row.style.display = "";
            } else {
                row.style.display = "none"; 
            }
        });
    });
}

document.addEventListener('DOMContentLoaded', initializeFilterKeyListener);