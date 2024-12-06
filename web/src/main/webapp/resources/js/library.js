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
            const courseId = this.getAttribute('data-course-id');
            window.location.href = _ctx + `course/${courseId}`;
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


document.addEventListener('DOMContentLoaded', function() {
    const inProgressTab = document.getElementById('in-progress-courses-tab');
    
    inProgressTab.addEventListener('shown.bs.tab', function() {
        const courseItems = document.querySelectorAll('#in-progress-courses .course-data');
        
        courseItems.forEach(function(courseItem) {
            const courseId = courseItem.getAttribute('data-course-id');
            
            if (!courseId) {
                console.error('No course ID found for item');
                return;
            }
            
            fetch(`${_ctx}library/course/progress?courseId=${courseId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Progress data:', data);
                    
                    // Find the appropriate container more flexibly
                    const cardBody = courseItem.querySelector('.card-body') || 
                                     courseItem.closest('.card')?.querySelector('.card-body') || 
                                     courseItem;
                    
                    if (!cardBody) {
                        console.error('Could not find card body for course item');
                        return;
                    }
                    
                    // Create progress container
                    const progressDiv = document.createElement('div');
                    progressDiv.className = 'progress-container';
                    progressDiv.innerHTML = `
                        <div class="progress mb-2" style="height: 20px;">
                            <div class="progress-bar ${getProgressBarClass(data.completionPercentage)}" 
                                 role="progressbar" 
                                 style="width: ${data.completionPercentage}%" 
                                 aria-valuenow="${data.completionPercentage}" 
                                 aria-valuemin="0" 
                                 aria-valuemax="100">
                                ${data.completionPercentage}%
                            </div>
                        </div>
                    `;
                    
                    // Remove existing progress container
                    const existingProgressContainer = cardBody.querySelector('.progress-container');
                    if (existingProgressContainer) {
                        existingProgressContainer.remove();
                    }
                    
                    // Insert new progress container before the first child
                    cardBody.insertBefore(progressDiv, cardBody.firstChild);
                })
                .catch(error => {
                    console.error('Error fetching course progress:', error);
                });
        });
    });

    function getProgressBarClass(percentage) {
        if (percentage < 25) return 'bg-danger';
        if (percentage < 50) return 'bg-warning';
        if (percentage < 75) return 'bg-info';
        return 'bg-success';
    }
});