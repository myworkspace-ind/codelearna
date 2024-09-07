document.getElementById('searchInput').addEventListener('focus', function() {
    this.placeholder = '';
});
document.getElementById('searchInput').addEventListener('blur', function() {
    this.placeholder = 'Bạn đang tìm kiếm điều gì';
});

// Dữ liệu mẫu cho các khóa học
const courses = [
    { name: 'Tên khóa học 1', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#ff9999' },
    { name: 'Tên khóa học 2', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#99ccff' },
    { name: 'Tên khóa học 3', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#99ff99' },
    { name: 'Tên khóa học 4', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#ffcc99' },
    { name: 'Tên khóa học 5', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#ff9999' },
    { name: 'Tên khóa học 6', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#99ccff' },
    { name: 'Tên khóa học 7', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#99ff99' },
    { name: 'Tên khóa học 8', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#ffcc99' },
    { name: 'Tên khóa học 9', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#ff9999' },
    { name: 'Tên khóa học 10', price: '3.000.000 đ', discount: '2.500.000 đ', color: '#99ccff' },
    // Thêm nhiều khóa học hơn nếu cần
];

// Dữ liệu mẫu cho các khóa học nổi bật
const featuredCourses = [
    { name: 'Tên khóa học 1', description: 'Mô tả khóa học 1', color: '#ff9999' },
    { name: 'Tên khóa học 2', description: 'Mô tả khóa học 2', color: '#99ccff' },
    { name: 'Tên khóa học 3', description: 'Mô tả khóa học 3', color: '#99ff99' },
    // Thêm nhiều khóa học nổi bật hơn nếu cần
];

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

const itemsPerPage = 8; // Tăng số lượng khóa học hiển thị trên mỗi trang
let currentPage = 1;

function displayCourses(page) {
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const paginatedCourses = courses.slice(start, end);

    const coursesContainer = document.getElementById('coursesContainer');
    coursesContainer.innerHTML = '';

    paginatedCourses.forEach(course => {
        const courseItem = document.createElement('div');
        courseItem.className = 'col-md-3 mb-4'; // Thêm khoảng cách dưới cho mỗi khóa học
        courseItem.innerHTML = `
            <div class="course-item">
                <a href="javascript:void(0);" class="course-link" style="text-decoration: none; color: inherit;">
                    <div class="course-placeholder" style="background-color: ${course.color}; height: 200px;"></div>
                    <h3>${course.name}</h3>
                    <p>${course.price}</p>
                    <p>${course.discount}</p>
                </a>
            </div>
        `;
        coursesContainer.appendChild(courseItem);
    });
}

function setupPagination() {
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
            displayCourses(currentPage);
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
            displayCourses(currentPage);
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
            displayCourses(currentPage);
            setupPagination();
        }
    });
    pagination.appendChild(nextItem);
}

function displayFeaturedCourses() {
    const featuredCoursesContainer = document.getElementById('featuredCoursesCarousel').querySelector('.carousel-inner');
    const indicatorsContainer = document.getElementById('featuredCoursesCarousel').querySelector('.carousel-indicators');
    featuredCoursesContainer.innerHTML = '';
    indicatorsContainer.innerHTML = '';

    featuredCourses.forEach((course, index) => {
        const activeClass = index === 0 ? 'active' : '';
        const courseItem = document.createElement('div');
        courseItem.className = `carousel-item ${activeClass}`;
        courseItem.innerHTML = `
            <div class="carousel-placeholder" style="background-color: ${course.color}; height: 400px;"></div>
            <div class="carousel-caption d-none d-md-block">
                <h3>${course.name}</h3>
                <p>${course.description}</p>
            </div>
        `;
        featuredCoursesContainer.appendChild(courseItem);

        const indicatorItem = document.createElement('button');
        indicatorItem.type = 'button';
        indicatorItem.dataset.bsTarget = '#featuredCoursesCarousel';
        indicatorItem.dataset.bsSlideTo = index;
        indicatorItem.className = activeClass;
        indicatorItem.setAttribute('aria-current', activeClass ? 'true' : 'false');
        indicatorItem.setAttribute('aria-label', `Slide ${index + 1}`);
        indicatorsContainer.appendChild(indicatorItem);
    });
}

function displayPromotions() {
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
}

// Khởi tạo hiển thị khóa học, khóa học nổi bật và chương trình khuyến mãi
displayCourses(currentPage);
setupPagination();
displayFeaturedCourses();
displayPromotions();