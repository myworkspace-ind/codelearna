document.addEventListener("DOMContentLoaded", function() {
    const itemsPerPage = 8; // Số lượng courseItem mỗi trang

    function paginate(tabId) {
        const container = document.getElementById(tabId);
        const items = container.getElementsByClassName('course-data');
        const pagination = document.getElementById('pagination-' + tabId);

        if (!items.length) return;

        const totalPages = Math.ceil(items.length / itemsPerPage);

        function showPage(page) {
            for (let i = 0; i < items.length; i++) {
                items[i].style.display = 'none';
            }
            const start = (page - 1) * itemsPerPage;
            const end = start + itemsPerPage;
            for (let i = start; i < end && i < items.length; i++) {
                items[i].style.display = 'block';
            }
        }

        function createPagination() {
            pagination.innerHTML = '';
            for (let i = 1; i <= totalPages; i++) {
                const li = document.createElement('li');
                li.className = 'page-item';
                const a = document.createElement('a');
                a.className = 'page-link';
                a.href = '#';
                a.textContent = i;
                a.addEventListener('click', function(e) {
                    e.preventDefault();
                    showPage(i);
                });
                li.appendChild(a);
                pagination.appendChild(li);
            }
        }

        showPage(1);
        createPagination();
    }

    const tabIds = ['all-courses', 'purchased-courses', 'trial-courses', 'in-progress-courses', 'completed-courses'];
    tabIds.forEach(tabId => paginate(tabId));

    document.querySelectorAll('.course-data').forEach(item => {
        item.addEventListener('click', function() {
            const isFree = this.getAttribute('data-is-free') === 'true';
            const courseId = this.getAttribute('data-course-id');
            
            if (isFree) {
                window.location.href = _ctx + `/play/${courseId}`;
            } else {
                const course = {
                    name: this.getAttribute('data-name'),
                    imageUrl: this.getAttribute('data-image-url'),
                    description: this.getAttribute('data-description'),
                    difficultyLevel: this.getAttribute('data-difficulty-level'),
                    lessonType: this.getAttribute('data-lesson-type'),
                    originalPrice: parseFloat(this.getAttribute('data-original-price')),
                    discountedPrice: parseFloat(this.getAttribute('data-discounted-price'))
                };
                openPopup(course);
            }
        });
    });
});

function openPopup(course) {
    document.getElementById('courseName').innerText = course.name || 'N/A';
    document.getElementById('courseImage').src = course.imageUrl || '';
    document.getElementById('courseDescription').innerText = course.description || 'N/A';
    document.getElementById('courseDifficulty').innerText = course.difficultyLevel || 'N/A';
    document.getElementById('courseLessonType').innerText = course.lessonType || 'N/A';
    document.getElementById('courseOriginalPrice').innerText = '₫ ' + (course.originalPrice ? course.originalPrice.toLocaleString() : '0');
    document.getElementById('courseDiscountedPrice').innerText = '₫ ' + (course.discountedPrice ? course.discountedPrice.toLocaleString() : '0');
    
    document.getElementById('courseDetailPopup').style.display = 'flex';
    document.getElementById('blurOverlay').style.display = 'block';
}

function closePopup() {
    document.getElementById('courseDetailPopup').style.display = 'none';
    document.getElementById('blurOverlay').style.display = 'none';
}

document.getElementById('courseDetailPopup').addEventListener('click', function(event) {
    if (event.target === this) {
        closePopup();
    }
});

