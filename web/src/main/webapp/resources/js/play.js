// Lấy các tham chiếu đến các phần tử DOM
const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('embedded-content');
const videoTitle = document.querySelector('.title');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');

// Lấy danh sách bài học từ dữ liệu server-rendered
const lessons = Array.from(document.querySelectorAll('.video-list-content .vid'));
let currentLessonIndex = 0;

function loadLesson(index) {
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

        // Cập nhật URL để phản ánh bài học hiện tại
        history.pushState(null, '', `/codelearna-web/play/${courseId}?lessonId=${lessonId}`);

        // Tải lại bình luận cho bài học mới
        loadCommentsHTML(courseId, lessonId);
    }
}

function loadCommentsHTML(courseId, lessonId) {
    fetch(`/codelearna-web/play/${courseId}/${lessonId}/comments`)
        .then(response => response.text()) // Lấy dữ liệu dạng HTML
        .then(html => {
            console.log("Comments HTML received:", html); // Debug
            const commentSection = document.getElementById('comment-list');
            if (commentSection) {
                commentSection.innerHTML = html; // Cập nhật phần bình luận
            } else {
                console.error('Comment section not found in the DOM');
            }
        })
        .catch(error => {
            console.error('Error loading comments:', error);
            document.getElementById('comment-list').innerHTML = `<p>Không thể tải bình luận. Lỗi: ${error.message}</p>`;
        });
}

// Sự kiện chuyển đổi giữa các bài học
lessons.forEach((lesson, index) => {
    lesson.addEventListener('click', function () {
        currentLessonIndex = index;
        loadLesson(currentLessonIndex);
    });
});

// Khởi tạo trang
window.addEventListener('load', function () {
    if (lessons.length > 0) {
        loadLesson(currentLessonIndex); // Load the first lesson initially
    }
});

toggleBtn.addEventListener('click', function () {
    videoList.classList.toggle('hidden');
    container.classList.toggle('expanded');
});

prevBtn.addEventListener('click', function () {
    if (currentLessonIndex > 0) {
        currentLessonIndex--;
        loadLesson(currentLessonIndex);
    }
});

nextBtn.addEventListener('click', function () {
    if (currentLessonIndex < lessons.length - 1) {
        currentLessonIndex++;
        loadLesson(currentLessonIndex);
    }
});

// Handle browser back/forward buttons
window.addEventListener('popstate', function(event) {
    const urlParams = new URLSearchParams(window.location.search);
    const lessonId = urlParams.get('lessonId');
    if (lessonId) {
        const lessonIndex = lessons.findIndex(lesson => lesson.getAttribute('data-lesson-id') === lessonId);
        if (lessonIndex !== -1) {
            currentLessonIndex = lessonIndex;
            loadLesson(currentLessonIndex);
        }
    }
});
