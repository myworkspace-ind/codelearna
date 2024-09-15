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

// Tự động phát video tiếp theo khi video hiện tại kết thúc
videoPlayer.addEventListener('ended', function () {
    if (currentVideoIndex < videos.length - 1) {
        currentVideoIndex++;
        loadVideo(currentVideoIndex);
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
        avatar.classList.add('avatar'); // Thêm class avatar để dễ styling
        
        // Tạo phần tử nội dung bình luận
        const commentContent = document.createElement('div');
        commentContent.classList.add('comment-content');
        commentContent.textContent = item.comment;
        
        // Thêm avatar và bình luận vào phần tử li
        newComment.appendChild(avatar);
        newComment.appendChild(commentContent);
        commentList.appendChild(newComment);
    });

    // Tải video đầu tiên khi trang load
    loadVideo(currentVideoIndex);
};

// Thêm bình luận mới từ người dùng
submitCommentBtn.addEventListener('click', function () {
    const commentValue = commentText.value.trim();

    if (commentValue !== '') {
        const newComment = document.createElement('li');
        
        // Avatar ngẫu nhiên cho bình luận mới từ 1-70
        const avatar = document.createElement('img');
        avatar.src = `https://i.pravatar.cc/40?img=${Math.floor(Math.random() * 70) + 1}`; 
        avatar.classList.add('avatar'); // Thêm class avatar
        
        // Nội dung bình luận mới
        const commentContent = document.createElement('div');
        commentContent.classList.add('comment-content');
        commentContent.textContent = commentValue;

        // Thêm avatar và nội dung bình luận vào phần tử li
        newComment.appendChild(avatar);
        newComment.appendChild(commentContent);
        commentList.appendChild(newComment);

        commentText.value = '';  // Reset nội dung bình luận
    } else {
        alert('Vui lòng nhập nội dung bình luận!');
    }
});
