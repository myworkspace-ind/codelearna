const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('main-video-player');
const videoTitle = document.querySelector('.title');  // Thêm dòng này để cập nhật tiêu đề

const playPauseBtn = document.getElementById('play-pause-btn');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');

const videos = [
    { url: "http://media.w3.org/2010/05/sintel/trailer.mp4", title: "Sintel Trailer" },
    { url: "http://media.w3.org/2010/05/bunny/trailer.mp4", title: "Bunny Trailer" },
    { url: "http://media.w3.org/2010/05/bunny/movie.mp4", title: "Bunny Movie" }
];
let currentVideoIndex = 0;

// Hiển thị video và tiêu đề của video hiện tại
function loadVideo(index) {
    videoPlayer.src = videos[index].url;
    videoPlayer.play();
    videoTitle.textContent = videos[index].title;  // Cập nhật tiêu đề video
}

// Điều khiển video toggle
toggleBtn.addEventListener('click', function () {
    if (videoList.classList.contains('hidden')) {
        videoList.classList.remove('hidden');
        container.classList.remove('expanded');
    } else {
        videoList.classList.add('hidden');
        container.classList.add('expanded');
    }
});

playPauseBtn.addEventListener('click', function () {
    if (videoPlayer.paused) {
        videoPlayer.play();
        playPauseBtn.textContent = "Tạm dừng";
    } else {
        videoPlayer.pause();
        playPauseBtn.textContent = "Tiếp tục";
    }
});

prevBtn.addEventListener('click', function () {
    if (currentVideoIndex > 0) {
        currentVideoIndex--;
        loadVideo(currentVideoIndex);  // Cập nhật bằng hàm loadVideo
    }
});

nextBtn.addEventListener('click', function () {
    if (currentVideoIndex < videos.length - 1) {
        currentVideoIndex++;
        loadVideo(currentVideoIndex);  // Cập nhật bằng hàm loadVideo
    }
});

videoPlayer.addEventListener('ended', function () {
    if (currentVideoIndex < videos.length - 1) {
        currentVideoIndex++;
        loadVideo(currentVideoIndex);
    }
});

// Bình luận mẫu
const comments = [
    { user: { avatarUrl: "avatar1.png", name: "User 1" }, content: "Bình luận 1" },
    { user: { avatarUrl: "avatar2.png", name: "User 2" }, content: "Bình luận 2" },
    { user: { avatarUrl: "avatar3.png", name: "User 3" }, content: "Bình luận 3" },
    { user: { avatarUrl: "avatar4.png", name: "User 4" }, content: "Bình luận 4" },
    { user: { avatarUrl: "avatar5.png", name: "User 5" }, content: "Bình luận 5" },
    { user: { avatarUrl: "avatar6.png", name: "User 6" }, content: "Bình luận 6" },
    { user: { avatarUrl: "avatar7.png", name: "User 7" }, content: "Bình luận 7" },
    { user: { avatarUrl: "avatar8.png", name: "User 8" }, content: "Bình luận 8" },
    { user: { avatarUrl: "avatar9.png", name: "User 9" }, content: "Bình luận 9" },
    { user: { avatarUrl: "avatar10.png", name: "User 10" }, content: "Bình luận 10" },
    { user: { avatarUrl: "avatar11.png", name: "User 11" }, content: "Bình luận 11" }
];

let currentPage = 0; // Trang hiện tại
const commentsPerPage = 10; // Số lượng bình luận mỗi trang
const totalPages = Math.ceil(comments.length / commentsPerPage); // Tính tổng số trang

// Hàm để tải bình luận cho một trang cụ thể
function loadComments(page) {
    // Xóa các bình luận hiện tại
    const commentList = document.getElementById('comment-list');
    commentList.innerHTML = ''; 

    // Xác định phạm vi bình luận sẽ hiển thị trên trang hiện tại
    const start = page * commentsPerPage;
    const end = Math.min(start + commentsPerPage, comments.length);

    // Lặp qua từng bình luận và hiển thị
    for (let i = start; i < end; i++) {
        const comment = comments[i];
        const newComment = document.createElement('li');
        
        const avatar = document.createElement('img');
        avatar.src = comment.user.avatarUrl;
        avatar.classList.add('avatar');

        const commentContent = document.createElement('div');
        commentContent.classList.add('comment-content');
        commentContent.textContent = comment.content;

        newComment.appendChild(avatar);
        newComment.appendChild(commentContent);
        commentList.appendChild(newComment);
    }

    // Cập nhật hiển thị chỉ số phân trang
    document.getElementById('page-indicator').textContent = `Trang ${currentPage + 1} / ${totalPages}`;

    // Kiểm tra nếu đang ở trang đầu hoặc cuối để vô hiệu hóa nút
    document.getElementById('prev-page-btn').disabled = currentPage === 0;
    document.getElementById('next-page-btn').disabled = currentPage >= totalPages - 1;
}

// Xử lý sự kiện khi nhấn nút "Trang sau"
document.getElementById('next-page-btn').addEventListener('click', function () {
    if (currentPage < totalPages - 1) {
        currentPage++;
        loadComments(currentPage);
    }
});

// Xử lý sự kiện khi nhấn nút "Trang trước"
document.getElementById('prev-page-btn').addEventListener('click', function () {
    if (currentPage > 0) {
        currentPage--;
        loadComments(currentPage);
    }
});

// Tải bình luận cho trang đầu tiên khi trang được tải
window.onload = function () {
    loadComments(currentPage);
};
