const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('main-video-player');
const videoTitle = document.querySelector('.title');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');

// Lấy danh sách các bài học từ dữ liệu được truyền từ server
const lessons = JSON.parse(document.getElementById('lessons-data').value);
let currentVideoIndex = parseInt(document.getElementById('current-lesson-index').value);

// Hàm để tải bài học theo index
function loadLesson(index) {
    if (index >= 0 && index < lessons.length) {
        currentVideoIndex = index;
        videoPlayer.src = lessons[index].videoUrl;
        videoTitle.textContent = lessons[index].title;
        videoPlayer.play();
    }
}

// Sự kiện khi nhấn nút bài học trước
prevBtn.addEventListener('click', function() {
    if (currentVideoIndex > 0) {
        loadLesson(currentVideoIndex - 1);
    }
});

// Sự kiện khi nhấn nút bài học tiếp theo
nextBtn.addEventListener('click', function() {
    if (currentVideoIndex < lessons.length - 1) {
        loadLesson(currentVideoIndex + 1);
    }
});

// Khởi động với bài học đầu tiên hoặc bài học hiện tại
loadLesson(currentVideoIndex);

// Sự kiện để hiển thị và ẩn danh sách video
toggleBtn.addEventListener('click', function() {
    videoList.classList.toggle('hidden');
    container.classList.toggle('expanded');
});

// Sự kiện khi video kết thúc, chuyển sang bài học tiếp theo nếu có
videoPlayer.addEventListener('ended', function() {
    if (currentVideoIndex < lessons.length - 1) {
        loadLesson(currentVideoIndex + 1);
    }
});


// Chức năng bình luận
let currentPage = 0;
let totalPages = 0;

function loadComments(page) {
    const courseId = document.querySelector('#course-id').value;

    fetch(`/play/${courseId}/comments?page=${page}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Invalid page');
            }
            return response.json();
        })
        .then(data => {
            displayComments(data.comments);
            updatePagination(data.currentPage, data.totalPages);
            currentPage = data.currentPage;
            totalPages = data.totalPages;
        })
        .catch(error => {
            console.error('Error loading comments:', error);
        });
}

function displayComments(comments) {
    const commentList = document.getElementById('comment-list');
    commentList.innerHTML = '';

    comments.forEach(comment => {
        const commentItem = document.createElement('li');
        commentItem.classList.add('comment-item', 'parent-comment');

        commentItem.innerHTML = `
            <div class="comment-content">
                <img src="${comment.user.avatarUrl}" alt="Avatar" class="avatar parent-avatar">
                <div class="comment-text">
                    <span class="comment-username">${comment.user.name}</span>
                    <p>${comment.content}</p>
                </div>
            </div>
            ${renderChildComments(comment.childComments)}
        `;

        commentList.appendChild(commentItem);
    });
}

function renderChildComments(childComments) {
    if (!childComments || childComments.length === 0) return '';

    let childHtml = '<ul class="child-comments">';
    childComments.forEach(child => {
        childHtml += `
            <li class="comment-item child-comment">
                <div class="comment-content">
                    <img src="${child.user.avatarUrl}" alt="Avatar" class="avatar child-avatar">
                    <div class="comment-text">
                        <span class="comment-username">${child.user.name}</span>
                        <p>${child.content}</p>
                    </div>
                </div>
            </li>
        `;
    });
    childHtml += '</ul>';
    return childHtml;
}

function updatePagination(currentPage, totalPages) {
    const pagination = document.querySelector('.pagination');

    pagination.innerHTML = `
        <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage > 0 ? currentPage - 1 : 0}" aria-label="Trang trước" ${currentPage === 0 ? 'tabindex="-1"' : ''}>
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
        <li class="page-item disabled">
            <span class="page-link">Trang ${currentPage + 1} / ${totalPages}</span>
        </li>
        <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1}" aria-label="Trang sau" ${currentPage === totalPages - 1 ? 'tabindex="-1"' : ''}>
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    `;

    pagination.querySelectorAll('.page-link').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            if (!this.parentElement.classList.contains('disabled')) {
                const pageNumber = parseInt(this.getAttribute('data-page'));
                loadComments(pageNumber);
            }
        });
    });
}

window.addEventListener('load', function() {
    loadComments(0);
});
