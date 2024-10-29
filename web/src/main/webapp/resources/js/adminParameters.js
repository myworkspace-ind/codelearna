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
document.addEventListener('DOMContentLoaded', function() {
	if (document.getElementById('parametersContainer')) {
		initializePagination('parameters');
	}
});