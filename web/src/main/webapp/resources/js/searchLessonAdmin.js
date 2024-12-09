function searchLessons() {
    const searchValue = document.getElementById('lessonSearchInput').value.toLowerCase();
    const rows = document.querySelectorAll('#lessonsContainer tbody tr');

    rows.forEach(row => {
        const lessonName = row.querySelector('td:nth-child(3)').textContent.toLowerCase();

        if (lessonName.includes(searchValue)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function handleSearchLessonKeyPress(event) {
    if (event.key === 'Enter') {
        searchLessons();
    }
}
