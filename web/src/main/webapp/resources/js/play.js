const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('main-video-player');

const playPauseBtn = document.getElementById('play-pause-btn');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');

const videos = [
    "http://media.w3.org/2010/05/sintel/trailer.mp4",
    "http://media.w3.org/2010/05/bunny/trailer.mp4",
    "http://media.w3.org/2010/05/bunny/movie.mp4"
];
let currentVideoIndex = 0;

// Điều khiển video
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
        videoPlayer.src = videos[currentVideoIndex];
        videoPlayer.play();
    }
});

nextBtn.addEventListener('click', function () {
    if (currentVideoIndex < videos.length - 1) {
        currentVideoIndex++;
        videoPlayer.src = videos[currentVideoIndex];
        videoPlayer.play();
    }
});

// Các bình luận mẫu với avatar
const demoComments = [
    { avatar: 'https://i.pravatar.cc/40?img=1', comment: "Video này rất hữu ích!" },
    { avatar: 'https://i.pravatar.cc/40?img=2', comment: "Cảm ơn bạn đã chia sẻ!" },
    { avatar: 'https://i.pravatar.cc/40?img=3', comment: "Có thể giải thích rõ hơn phần cuối video không?" },
    { avatar: 'https://i.pravatar.cc/40?img=4', comment: "Video này rất hay, mong chờ video tiếp theo!" }
];

// Xử lý phần bình luận
const commentList = document.getElementById('comment-list');
const submitCommentBtn = document.getElementById('submit-comment-btn');
const commentText = document.getElementById('comment-text');

// Hiển thị các bình luận mẫu khi trang tải
window.onload = function() {
    demoComments.forEach(function(item) {
        const newComment = document.createElement('li');
        
        // Tạo phần tử avatar
        const avatar = document.createElement('img');
        avatar.src = item.avatar;
        
        // Tạo phần tử nội dung bình luận
        const commentContent = document.createElement('div');
        commentContent.classList.add('comment-content');
        commentContent.textContent = item.comment;
        
        // Thêm avatar và bình luận vào phần tử li
        newComment.appendChild(avatar);
        newComment.appendChild(commentContent);
        commentList.appendChild(newComment);
    });
};

// Thêm bình luận mới từ người dùng
submitCommentBtn.addEventListener('click', function () {
    const commentValue = commentText.value.trim();

    if (commentValue !== '') {
        const newComment = document.createElement('li');
        
        // Avatar mặc định cho bình luận mới
        const avatar = document.createElement('img');
        avatar.src = 'https://i.pravatar.cc/40?img=5'; // Avatar mặc định

        // Nội dung bình luận mới
        const commentContent = document.createElement('div');
        commentContent.classList.add('comment-content');
        commentContent.textContent = commentValue;

        // Thêm avatar và nội dung bình luận vào phần tử li
        newComment.appendChild(avatar);
        newComment.appendChild(commentContent);
        commentList.appendChild(newComment);

        commentText.value = '';
    } else {
        alert('Vui lòng nhập nội dung bình luận!');
    }
});

