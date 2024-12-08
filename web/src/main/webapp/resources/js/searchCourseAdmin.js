function searchCourses() {
    const searchValue = document.getElementById('courseSearchInput').value.toLowerCase();
    const rows = document.querySelectorAll('#coursesContainer tbody tr');

    rows.forEach(row => {
        const courseName = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
        const courseDescription = row.querySelector('td:nth-child(4)').textContent.toLowerCase();

        if (courseName.includes(searchValue) || courseDescription.includes(searchValue)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function handleSearchCourseKeyPress(event) {
    if (event.key === 'Enter') {
        searchCourses();
    }
}
