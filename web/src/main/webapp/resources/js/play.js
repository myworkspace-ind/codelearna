// Lấy các tham chiếu đến các phần tử DOM
console.log('Script đang chạy');


const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('embedded-content');
const videoTitle = document.querySelector('.title');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');
const submitCommentBtn = document.querySelector('#submit-comment-btn');
const commentTextArea = document.getElementById('comment-text');

console.log('Nút gửi bình luận:', submitCommentBtn);
submitCommentBtn.onclick = function(event) {
    event.preventDefault();
    console.log('Nút gửi bình luận được nhấn');
};
// Lấy danh sách bài học từ dữ liệu server-rendered
const lessons = Array.from(document.querySelectorAll('.video-list-content .vid'));
let currentLessonIndex = 0;

let initialLoad = true;

function loadLesson(index, updateUrl = true) {
    const lesson = lessons[index];
    if (lesson) {
        const videoUrl = lesson.querySelector('video').src;
        const title = lesson.querySelector('.title').textContent;
        const courseId = lesson.getAttribute('data-course-id');
        const lessonId = lesson.getAttribute('data-lesson-id');

        // Cập nhật video và tiêu đề
        videoPlayer.src = videoUrl;
        videoTitle.textContent = title;

        // Cập nhật class 'active' cho bài học
        lessons.forEach(l => l.classList.remove('active'));
        lesson.classList.add('active');

        // Cuộn bài học đang active vào tầm nhìn
        lesson.scrollIntoView({ behavior: 'smooth', block: 'nearest' });

        // Cập nhật URL nếu cần
        if (updateUrl) {
            history.replaceState(null, '', `/codelearna-web/play/${courseId}?lessonId=${lessonId}`);
        }
        
        // Tải bình luận cho bài học mới
        loadComments(courseId, lessonId);
    }
}

function loadComments(courseId, lessonId, page = 0) {
    console.log(`Đang tải bình luận cho khóa học ${courseId}, bài học ${lessonId}, trang ${page}`);
    fetch(`/codelearna-web/play/${courseId}/${lessonId}/comments?page=${page}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            const commentSection = document.querySelector('.comment-section');
            if (commentSection) {
                commentSection.innerHTML = html;
                attachPaginationListeners();
                attachReplyListeners();
            } else {
                console.error('Comment section element not found');
            }
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
            const courseId = getCurrentCourseId();
            const lessonId = getCurrentLessonId();
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
        const courseId = getCurrentCourseId();
        const lessonId = getCurrentLessonId();
        fetch(`/codelearna-web/play/comments/${commentId}/reply`, {
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

function getCurrentCourseId() {
    return window.location.pathname.split('/')[3]; // Adjust based on your URL structure
}

function getCurrentLessonId() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('lessonId');
}

// Hàm để lấy lessonId từ URL
function getLessonIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('lessonId');
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

// Cập nhật sự kiện click cho bài học
lessons.forEach((lesson, index) => {
    lesson.addEventListener('click', function () {
        currentLessonIndex = index;
        loadLesson(currentLessonIndex);
    });
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
    const courseId = lessons[currentLessonIndex].getAttribute('data-course-id');
    const lessonId = lessons[currentLessonIndex].getAttribute('data-lesson-id');

    if (commentContent) {
        // Gửi bình luận đến server
        fetch(`/codelearna-web/play/${courseId}/${lessonId}/comments`, {
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

// Tải bài học ban đầu
window.addEventListener('load', function() {
    const lessonId = getLessonIdFromUrl();
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