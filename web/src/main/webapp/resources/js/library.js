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



document.addEventListener('DOMContentLoaded', function() {
	const userEid = document.getElementById('user_eid').textContent;
            // Hàm thêm nút chứng chỉ vào mỗi khóa học hoàn thành
            function addCertificateButton(courseItem) {
                const cardBody = courseItem.querySelector('.card-body') || 
                                 courseItem.closest('.card')?.querySelector('.card-body') || 
                                 courseItem;

                if (!cardBody) return;

                const certificateButton = document.createElement('button');
                certificateButton.className = 'btn btn-outline-success w-100 mt-2';
                certificateButton.innerHTML = `
                    <i class="bi bi-trophy-fill me-2"></i>
                    Xem chứng chỉ
                `;

                certificateButton.addEventListener('click', function(e) {
                    e.stopPropagation(); 
            
                    const courseName = courseItem.getAttribute('data-name') || 'Khóa học';
                    const userName = userEid;

                    const today = new Date();
                    const formattedDate = today.toLocaleDateString('vi-VN');

                    if (window.openCertificatePopup) {
                        window.openCertificatePopup(userName, courseName, formattedDate);
                    } else {
                        console.error('Certificate popup function not found');
                    }
                });

                const existingCertButton = cardBody.querySelector('.btn-outline-success');
                if (existingCertButton) {
                    existingCertButton.remove();
                }

                cardBody.appendChild(certificateButton);
            }

            const completedTab = document.getElementById('completed-courses-tab');
            completedTab.addEventListener('shown.bs.tab', function() {
                const completedCourses = document.querySelectorAll('#completed-courses .course-data');

                const completedCoursesCountBadge = document.getElementById('completedCoursesCount');
                if (completedCoursesCountBadge) {
                    completedCoursesCountBadge.textContent = completedCourses.length;
                }

                completedCourses.forEach(addCertificateButton);
            });
        });

        // Mở popup chứng chỉ
		function openCertificatePopup(userName, courseName, issueDate) {
		    const nameElement = document.getElementById('display_name');
		    const courseNameElement = document.getElementById('course_name');
		    const issueDateElement = document.getElementById('issue_date');

		    if (nameElement) {
		        nameElement.textContent = userName;
		    } else {
		        console.error('Element #display_name not found');
		    }

		    if (courseNameElement) {
		        courseNameElement.textContent = courseName;
		    } else {
		        console.error('Element #course_name not found');
		    }

		    if (issueDateElement) {
		        issueDateElement.textContent = issueDate;
		    } else {
		        console.error('Element #issue_date not found');
		    }

		    document.getElementById('certificatePopupOverlay').style.display = 'flex';
		}

        // Đóng popup chứng chỉ
        function closeCertificatePopup() {
            document.getElementById('certificatePopupOverlay').style.display = 'none';
        }

		async function downloadCertificatePDF() {
		    const certificate = document.getElementById('certificate');
			if (!certificate) {
			    console.error('Element #certificate not found');
			    return;
			}
		    try {
		        // Chuyển DOM thành ảnh
		        const dataUrl = await domtoimage.toPng(certificate);

		        // Khởi tạo jsPDF
		        const { jsPDF } = window.jspdf;
		        const pdf = new jsPDF({ 
		            orientation: 'landscape', 
		            unit: 'px', 
		            format: [certificate.offsetWidth, certificate.offsetHeight] 
		        });

		        // Thêm ảnh vào PDF
		        pdf.addImage(dataUrl, 'PNG', 0, 0, certificate.offsetWidth, certificate.offsetHeight);

		        // Tải PDF
		        pdf.save('certificate.pdf');
		    } catch (error) {
		        console.error('Error generating PDF:', error);
		        alert('Có lỗi xảy ra khi tạo PDF: ' + error.message);
		    }
		}

