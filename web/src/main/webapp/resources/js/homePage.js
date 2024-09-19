const itemsPerPage = 8; // Số lượng khóa học trên mỗi trang
let currentPage = 1;

function showPage(page) {
    const courses = document.querySelectorAll('#coursesContainer > div');
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;

    courses.forEach((course, index) => {
        if (index >= start && index < end) {
            course.style.display = 'block';
        } else {
            course.style.display = 'none';
        }
    });
}

function setupPagination() {
    const courses = document.querySelectorAll('#coursesContainer > div');
    const pageCount = Math.ceil(courses.length / itemsPerPage);
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    function createPageItem(page) {
        const pageItem = document.createElement('li');
        pageItem.className = 'page-item' + (page === currentPage ? ' active' : '');
        pageItem.innerHTML = `<a class="page-link" href="#">${page}</a>`;
        pageItem.addEventListener('click', function(event) {
            event.preventDefault();
            currentPage = page;
            showPage(currentPage);
            setupPagination(); // Cập nhật lại phân trang
        });
        return pageItem;
    }

    // Nút Previous
    const prevItem = document.createElement('li');
    prevItem.className = 'page-item' + (currentPage === 1 ? ' disabled' : '');
    prevItem.innerHTML = `<a class="page-link" href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>`;
    prevItem.addEventListener('click', function(event) {
        event.preventDefault();
        if (currentPage > 1) {
            currentPage--;
            showPage(currentPage);
            setupPagination();
        }
    });
    pagination.appendChild(prevItem);

    // Nút trang đầu
    pagination.appendChild(createPageItem(1));

    // Nút trang hiện tại và 1 trang bên cạnh
    if (currentPage > 2) {
        const dots = document.createElement('li');
        dots.className = 'page-item disabled';
        dots.innerHTML = `<a class="page-link" href="#">...</a>`;
        pagination.appendChild(dots);
    }

    for (let i = Math.max(2, currentPage - 1); i <= Math.min(pageCount - 1, currentPage + 1); i++) {
        pagination.appendChild(createPageItem(i));
    }

    if (currentPage < pageCount - 1) {
        const dots = document.createElement('li');
        dots.className = 'page-item disabled';
        dots.innerHTML = `<a class="page-link" href="#">...</a>`;
        pagination.appendChild(dots);
    }

    // Nút trang cuối
    if (pageCount > 1) {
        pagination.appendChild(createPageItem(pageCount));
    }

    // Nút Next
    const nextItem = document.createElement('li');
    nextItem.className = 'page-item' + (currentPage === pageCount ? ' disabled' : '');
    nextItem.innerHTML = `<a class="page-link" href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>`;
    nextItem.addEventListener('click', function(event) {
        event.preventDefault();
        if (currentPage < pageCount) {
            currentPage++;
            showPage(currentPage);
            setupPagination();
        }
    });
    pagination.appendChild(nextItem);
}

// Hiển thị trang đầu tiên và thiết lập phân trang khi trang được tải
document.addEventListener('DOMContentLoaded', function() {
    showPage(currentPage);
    setupPagination();
});

document.querySelectorAll('#coursesContainer > div').forEach(item => {
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

document.querySelectorAll('#featuredCoursesCarousel .carousel-item').forEach(item => {
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