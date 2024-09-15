document.getElementById('searchInput').addEventListener('focus', function() {
    this.placeholder = '';
});
document.getElementById('searchInput').addEventListener('blur', function() {
    this.placeholder = 'Bạn đang tìm kiếm điều gì';
});

const itemsPerPage = 8; // Số lượng khóa học trên mỗi trang
let currentPage = 1;

// Dữ liệu mẫu cho các chương trình khuyến mãi
const promotions = [
    { name: 'Tên chương trình 1', time: '11/10-22/12', progress: 60 },
    { name: 'Tên chương trình 2', time: '11/10-22/12', progress: 70 },
    { name: 'Tên chương trình 3', time: '11/10-22/12', progress: 40 },
    { name: 'Tên chương trình 4', time: '11/10-22/12', progress: 50 },
    { name: 'Tên chương trình 5', time: '11/10-22/12', progress: 80 },
    { name: 'Tên chương trình 6', time: '11/10-22/12', progress: 90 },
    // Thêm nhiều chương trình khuyến mãi hơn nếu cần
];

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

/*function displayPromotions() {
    const promotionsContainer = document.getElementById('promotionCarousel').querySelector('.carousel-inner');
    promotionsContainer.innerHTML = '';

    for (let i = 0; i < promotions.length; i += 3) {
        const activeClass = i === 0 ? 'active' : '';
        const promotionItem = document.createElement('div');
        promotionItem.className = `carousel-item ${activeClass}`;
        promotionItem.innerHTML = `
            <div class="row">
                ${promotions.slice(i, i + 3).map(promotion => `
                    <div class="col-12 mb-3">
                        <div class="promotion-item">
                            <p>${promotion.name}</p>
                            <p>Thời gian: ${promotion.time}</p>
                            <div class="progress">
                                <div class="progress-bar" style="width: ${promotion.progress}%;"></div>
                            </div>
                        </div>
                    </div>
                `).join('')}
            </div>
        `;
        promotionsContainer.appendChild(promotionItem);
    }
}*/


// Hiển thị trang đầu tiên và thiết lập phân trang khi trang được tải
document.addEventListener('DOMContentLoaded', function() {
    showPage(currentPage);
    setupPagination();
});

document.querySelectorAll('#coursesContainer > div').forEach(item => {
    item.addEventListener('click', function() {
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
    });
});

document.querySelectorAll('#featuredCoursesCarousel .carousel-item').forEach(item => {
    item.addEventListener('click', function() {
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