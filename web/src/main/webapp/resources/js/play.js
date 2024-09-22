// Lấy các tham chiếu đến các phần tử DOM
const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('embedded-content');
const videoTitle = document.querySelector('.title');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');
const submitCommentBtn = document.getElementById('submit-comment-btn');
const commentTextArea = document.getElementById('comment-text');


// Lấy danh sách bài học từ dữ liệu server-rendered
const lessons = Array.from(document.querySelectorAll('.video-list-content .vid'));
let currentLessonIndex = 0;

// Giữ nguyên các phần code hiện có...

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
            // Gọi lại hàm để tải bình luận ngay khi URL thay đổi
            loadCommentsHTML(courseId, lessonId);
        } else {
            loadCommentsHTML(courseId, lessonId); // Gọi lại bình luận nếu không cập nhật URL
        }
    }
}


function loadCommentsHTML(courseId, lessonId) {
    console.log(`Đang tải bình luận cho khóa học ${courseId}, bài học ${lessonId}`);
    fetch(`/codelearna-web/play/${courseId}/${lessonId}/comments`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Lỗi HTTP! Trạng thái: ${response.status}`);
            }
            return response.text(); // Chuyển đổi sang HTML
        })
        .then(html => {
            const commentSection = document.querySelector('.comment-section .comment-list');
            if (commentSection) {
                commentSection.innerHTML = html; // Cập nhật HTML cho phần bình luận
                console.log("Đã cập nhật phần bình luận");
            } else {
                console.error('Không tìm thấy phần tử chứa bình luận');
            }
        })
        .catch(error => {
            console.error('Lỗi khi tải bình luận:', error);
        });
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

// Cập nhật các sự kiện lắng nghe
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
            commentTextArea.value = ''; // Xóa nội dung textarea
            loadCommentsHTML(courseId, lessonId); // Cập nhật danh sách bình luận
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