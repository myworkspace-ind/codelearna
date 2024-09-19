const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('main-video-player');
const videoTitle = document.querySelector('.title');
const playPauseBtn = document.getElementById('play-pause-btn');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');

const videos = [
	{ url: "http://media.w3.org/2010/05/sintel/trailer.mp4", title: "Sintel Trailer" },
	{ url: "http://media.w3.org/2010/05/bunny/trailer.mp4", title: "Bunny Trailer" },
	{ url: "http://media.w3.org/2010/05/bunny/movie.mp4", title: "Bunny Movie" }
];
let currentVideoIndex = 0;
function loadVideo(index) {
	videoPlayer.src = videos[index].url;
	videoPlayer.play();
	videoTitle.textContent = videos[index].title;
}

toggleBtn.addEventListener('click', function() {
	videoList.classList.toggle('hidden');
	container.classList.toggle('expanded');
});

playPauseBtn.addEventListener('click', function() {
	if (videoPlayer.paused) {
		videoPlayer.play();
		playPauseBtn.textContent = "Tạm dừng";
	} else {
		videoPlayer.pause();
		playPauseBtn.textContent = "Tiếp tục";
	}
});

prevBtn.addEventListener('click', function() {
	if (currentVideoIndex > 0) {
		currentVideoIndex--;
		loadVideo(currentVideoIndex);
	}
});

nextBtn.addEventListener('click', function() {
	if (currentVideoIndex < videos.length - 1) {
		currentVideoIndex++;
		loadVideo(currentVideoIndex);
	}
});

videoPlayer.addEventListener('ended', function() {
	if (currentVideoIndex < videos.length - 1) {
		currentVideoIndex++;
		loadVideo(currentVideoIndex);
	}
});

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