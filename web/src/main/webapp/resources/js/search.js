 function setSortOrder(element) {
	const sortOrder = element.getAttribute('data-sort-order');
	const sortField = element.getAttribute('data-sort-field');
	document.querySelector('button#sortDropdown').setAttribute('data-sort-order', sortOrder);
	document.querySelector('button#sortDropdown').setAttribute('data-sort-field', sortField);
	applyFilter();
}

function applyFilter() {
	const form = document.createElement('form');
	form.method = 'GET';


	
	const subcategoryId = document.getElementById('hiddenSubcategory').value;

	if (subcategoryId) {
		form.action = _ctx + `subcategory/${subcategoryId}`;
	} else {
		form.action = _ctx + `search`;
	}

	// Input cho keyword
	const keywordInput = document.createElement('input');
	keywordInput.type = 'hidden';
	keywordInput.name = 'keyword';
	keywordInput.value = document.getElementById('hiddenKeyword').value || '';
	form.appendChild(keywordInput);

	// Input cho sortOrder
	const sortOrderInput = document.createElement('input');
	sortOrderInput.type = 'hidden';
	sortOrderInput.name = 'sortOrder';
	sortOrderInput.value = document.querySelector('button#sortDropdown').getAttribute('data-sort-order') || 'asc';
	form.appendChild(sortOrderInput);

	// Input cho sortField
	const sortFieldInput = document.createElement('input');
	sortFieldInput.type = 'hidden';
	sortFieldInput.name = 'sortField';
	sortFieldInput.value = document.querySelector('button#sortDropdown').getAttribute('data-sort-field') || 'createdDate';
	form.appendChild(sortFieldInput);

	// Input cho level
	const level = document.querySelector('input[name="level"]:checked');
	if (level) {
		const levelInput = document.createElement('input');
		levelInput.type = 'hidden';
		levelInput.name = 'level';
		levelInput.value = level.value;
		form.appendChild(levelInput);
	}

	document.body.appendChild(form);
	form.submit();
}
