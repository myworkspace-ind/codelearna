const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('embedded-content');
const videoTitle = document.querySelector('.title');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');
const submitCommentBtn = document.getElementById('submit-comment-btn');
const commentTextArea = document.getElementById('comment-text');

const lessons = Array.from(document.querySelectorAll('.video-list-content .vid'));
let currentLessonIndex = 0;

let initialLoad = true;

// Hàm tạo URL
function generatePlayURL(courseId, lessonId) {
    return `${_ctx}play/${courseId}?lessonId=${lessonId}`;
}

// Hàm lấy courseId từ URL
function getCourseIdFromUrl() {
    const pathParts = window.location.pathname.split('/');
    return pathParts[pathParts.indexOf('play') + 1];
}

// Hàm lấy lessonId từ URL
function getLessonIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('lessonId');
}

// Thêm sự kiện click cho nút toggle-btn
toggleBtn.addEventListener('click', () => {
    if (videoList.style.display === 'none' || !videoList.style.display) {
        videoList.style.display = 'block';
        container.classList.remove('expanded'); 
        toggleBtn.textContent = '☰'; 
    } else {
        videoList.style.display = 'none';
        container.classList.add('expanded');
        toggleBtn.textContent = '✖';
    }
});

// Hàm tìm kiếm bài học theo tên trong danh sách hiện tại
function searchLessons(searchTerm) {
    lessons.forEach(lesson => {
        const title = lesson.querySelector('.title').textContent.toLowerCase();
        if (title.includes(searchTerm.toLowerCase())) {
            lesson.style.visibility = 'visible';
            lesson.style.height = ''; 
            lesson.style.opacity = '1'; 
            lesson.style.pointerEvents = 'auto';
        } else {
            lesson.style.visibility = 'hidden'; 
            lesson.style.height = '0'; 
            lesson.style.opacity = '0'; 
            lesson.style.pointerEvents = 'none';
        }
    });
}

// Xử lý sự kiện click cho bài học
lessons.forEach((lesson, index) => {
    lesson.addEventListener('click', function () {
        currentLessonIndex = index;
        loadLesson(currentLessonIndex);
    });
});

function loadLesson(index, updateUrl = true) {
    const lesson = lessons[index];
    if (lesson) {
        const videoUrl = lesson.querySelector('video').src;
        const title = lesson.querySelector('.title').textContent;
        const courseId = getCourseIdFromUrl();
        const lessonId = lesson.getAttribute('data-lesson-id');

        videoPlayer.src = videoUrl;
        videoTitle.textContent = title;

        lessons.forEach(l => l.classList.remove('active'));
        lesson.classList.add('active');

        lesson.scrollIntoView({ behavior: 'smooth', block: 'nearest' });

        if (updateUrl) {
            const newUrl = generatePlayURL(courseId, lessonId);
            history.replaceState(null, '', newUrl);
        }
        
        loadComments(courseId, lessonId);
    }
}

function loadComments(courseId, lessonId, page = 0) {
    fetch(`${_ctx}play/${courseId}/${lessonId}/comments?page=${page}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;

            const newCommentList = tempDiv.querySelector('#comment-list');
            const newPagination = tempDiv.querySelector('.pagination');

            const commentListContainer = document.querySelector('#comment-list');
            const paginationContainer = document.querySelector('.pagination');

            if (commentListContainer && newCommentList) {
                commentListContainer.innerHTML = newCommentList.innerHTML;
            }

            if (paginationContainer && newPagination) {
                paginationContainer.innerHTML = newPagination.innerHTML;
                attachPaginationListeners();
            } else if (newPagination) {
                const commentSection = document.querySelector('.comment-section');
                if (commentSection) {
                    commentSection.appendChild(newPagination);
                    attachPaginationListeners();
                }
            } else {
                console.warn('Pagination element not found in the loaded content.');
            }

            attachReplyListeners();
        })
        .catch(error => {
            console.error('Error loading comments:', error);
        });
}

function attachPaginationListeners() {
    const paginationButtons = document.querySelectorAll('.pagination .page-btn');
    paginationButtons.forEach(button => {
        button.addEventListener('click', function() {
            const page = this.getAttribute('data-page');
            const courseId = getCourseIdFromUrl();
            const lessonId = getLessonIdFromUrl();
            loadComments(courseId, lessonId, page);
        });
    });
}

function attachReplyListeners() {
    const replyButtons = document.querySelectorAll('.reply-btn');
    replyButtons.forEach(button => {
        button.addEventListener('click', function() {
            const commentId = this.getAttribute('data-comment-id');
            const replySection = document.querySelector(`.reply-section[data-comment-id="${commentId}"]`);
            replySection.style.display = replySection.style.display === 'none' ? 'block' : 'none';
        });
    });

    const submitReplyButtons = document.querySelectorAll('.submit-reply-btn');
    submitReplyButtons.forEach(button => {
        button.addEventListener('click', function() {
            const replySection = this.closest('.reply-section');
            const commentId = replySection.getAttribute('data-comment-id');
            const replyContent = replySection.querySelector('.reply-textarea').value.trim();
            submitReply(commentId, replyContent);
        });
    });
}

function submitReply(commentId, content) {
    if (content) {
        const courseId = getCourseIdFromUrl();
        const lessonId = getLessonIdFromUrl();
        fetch(`${_ctx}play/comments/${commentId}/reply`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                content: content
            })
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error(`Server returned error: ${response.status}`);
            }
        })
        .then(data => {
            console.log('Reply sent:', data);
            loadComments(courseId, lessonId);
        })
        .catch(error => {
            console.error('Error submitting reply:', error);
        });
    } else {
        alert('Please enter a reply before submitting.');
    }
}

// Hàm để tìm index của bài học dựa trên lessonId
function findLessonIndex(lessonId) {
    return lessons.findIndex(lesson => lesson.getAttribute('data-lesson-id') === lessonId);
}

prevBtn.addEventListener('click', function () {
    if (currentLessonIndex > 0) {
        currentLessonIndex--;
        loadLesson(currentLessonIndex);
    }
});

nextBtn.addEventListener('click', function () {
    console.log('Next button clicked');
    if (currentLessonIndex < lessons.length - 1) {
        currentLessonIndex++;
        loadLesson(currentLessonIndex);
    } else {
        console.log('Already at the last lesson');
    }
});

// Xử lý nút back/forward của trình duyệt
window.addEventListener('popstate', function(event) {
    const lessonId = getLessonIdFromUrl();
    if (lessonId) {
        const lessonIndex = findLessonIndex(lessonId);
        if (lessonIndex !== -1) {
            currentLessonIndex = lessonIndex;
            loadLesson(currentLessonIndex, false);
        }
    }
});

// Hàm gửi bình luận
submitCommentBtn.addEventListener('click', function () {
    console.log('Submit button clicked');
    const commentContent = commentTextArea.value.trim();
    const courseId = getCourseIdFromUrl();
    const lessonId = getLessonIdFromUrl();

    if (commentContent) {
        fetch(`${_ctx}play/${courseId}/${lessonId}/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                content: commentContent
            })
        })
        .then(response => response.text())
        .then(message => {
            console.log(message);
            commentTextArea.value = '';
            loadComments(courseId, lessonId, 0); // Luôn tải trang đầu tiên sau khi gửi bình luận mới
        })
        .catch(error => {
            console.error('Lỗi:', error);
        });
    } else {
        alert('Vui lòng nhập bình luận trước khi gửi.');
    }
});

// Khởi tạo khi trang load
window.addEventListener('load', function() {
    const courseId = getCourseIdFromUrl();
    let lessonId = getLessonIdFromUrl();
    
    if (!lessonId && lessons.length > 0) {
        lessonId = lessons[0].getAttribute('data-lesson-id');
        const newUrl = generatePlayURL(courseId, lessonId);
        history.replaceState(null, '', newUrl);
    }

    if (lessonId) {
        const lessonIndex = findLessonIndex(lessonId);
        if (lessonIndex !== -1) {
            currentLessonIndex = lessonIndex;
        }
    }
    
    if (lessons.length > 0) {
        loadLesson(currentLessonIndex, false);
    }
});