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
        const userId = 1; // Giả sử userId là 1, bạn có thể thay đổi theo logic của bạn

        if (isFree) {
            // Gửi yêu cầu AJAX để thêm khóa học vào thư viện
            $.ajax({
                url: _ctx + `library/add`,
                type: 'POST',
                data: {
                    userId: userId,
                    courseId: courseId
                },
                success: function(response) {
                    console.log(response);
                },
                error: function(xhr, status, error) {
                    console.error('Lỗi:', error);
                }
            });

            // Chuyển hướng đến trang play
            window.location.href = _ctx + `play/${courseId}`;
        } else {
			/*
            const course = {
                name: this.getAttribute('data-name'),
                imageUrl: this.getAttribute('data-image-url'),
                description: this.getAttribute('data-description'),
                difficultyLevel: this.getAttribute('data-difficulty-level'),
                lessonType: this.getAttribute('data-lesson-type'),
                originalPrice: parseFloat(this.getAttribute('data-original-price')),
                discountedPrice: parseFloat(this.getAttribute('data-discounted-price'))
            };*/
			window.location.href = _ctx + `course/${courseId}`;
            /*openPopup(course);*/
        }
    });
});

document.querySelectorAll('#featuredCoursesCarousel .carousel-item').forEach(item => {
    item.addEventListener('click', function() {
        const isFree = this.getAttribute('data-is-free') === 'true';
        const courseId = this.getAttribute('data-course-id');
        
        if (isFree) {
            window.location.href = _ctx + `play/${courseId}`;
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

document.querySelectorAll('#campaignCarousel .carousel-item').forEach(item => {
    item.addEventListener('click', function() {
        const startDate = new Date(this.getAttribute('data-start-time'));
        const endDate = new Date(this.getAttribute('data-end-time'));
        const today = new Date();
        const daysLeft = Math.ceil((endDate - today) / (1000 * 60 * 60 * 24));

        const campaign = {
            name: this.getAttribute('data-name'),
            description: this.getAttribute('data-description'),
            startDate: this.getAttribute('data-start-time'),
            endDate: this.getAttribute('data-end-time'),
            daysLeft: daysLeft,
            imageUrl: this.getAttribute('data-image-url')
        };
        openCampaignPopup(campaign);
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

function openCampaignPopup(campaign) {
    document.getElementById('campaignName').innerText = campaign.name || 'N/A';
    document.getElementById('campaignDescription').innerText = campaign.description || 'N/A';
    document.getElementById('campaignImage').src = campaign.imageUrl || '';

    const startDate = new Date(campaign.startDate);
    const endDate = new Date(campaign.endDate);
    const startDateString = `${startDate.getDate().toString().padStart(2, '0')}/${(startDate.getMonth() + 1).toString().padStart(2, '0')}`;
    const endDateString = `${endDate.getDate().toString().padStart(2, '0')}/${(endDate.getMonth() + 1).toString().padStart(2, '0')}`;
    const timeRangeString = `${startDateString} - ${endDateString}`;
    const daysLeftString = `còn ${campaign.daysLeft} ngày`;

    document.getElementById('campaignTimeRange').innerText = timeRangeString;
    document.getElementById('campaignDaysLeft').innerText = daysLeftString;

    document.getElementById('campaignDetailPopup').style.display = 'flex';
    document.getElementById('blurOverlay').style.display = 'block';
}

function closeCampaignPopup() {
    document.getElementById('campaignDetailPopup').style.display = 'none';
    document.getElementById('blurOverlay').style.display = 'none';
}

document.getElementById('campaignDetailPopup').addEventListener('click', function(event) {
    if (event.target === this) {
        closeCampaignPopup();
    }
});