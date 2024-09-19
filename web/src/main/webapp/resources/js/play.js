// Get references to DOM elements
const toggleBtn = document.getElementById('toggle-btn');
const videoList = document.getElementById('video-list');
const container = document.querySelector('.container');
const videoPlayer = document.getElementById('embedded-content');
const videoTitle = document.querySelector('.title');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');

// Get the list of lessons from the server-rendered data
const lessons = Array.from(document.querySelectorAll('.video-list-content .vid'));
let currentLessonIndex = 0;

function loadLesson(index) {
    const lesson = lessons[index];
    if (lesson) {
        const videoUrl = lesson.querySelector('video').src;
        const title = lesson.querySelector('.title').textContent;
        
        videoPlayer.src = videoUrl;
        videoTitle.textContent = title;
        
        // Update active class
        lessons.forEach(l => l.classList.remove('active'));
        lesson.classList.add('active');
		
		// Scroll the active lesson into view
		lesson.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
}

toggleBtn.addEventListener('click', function() {
    videoList.classList.toggle('hidden');
    container.classList.toggle('expanded');
});

prevBtn.addEventListener('click', function() {
    if (currentLessonIndex > 0) {
        currentLessonIndex--;
        loadLesson(currentLessonIndex);
    }
});

nextBtn.addEventListener('click', function() {
    if (currentLessonIndex < lessons.length - 1) {
        currentLessonIndex++;
        loadLesson(currentLessonIndex);
    }
});

// Add click event listeners to each lesson in the list
lessons.forEach((lesson, index) => {
    lesson.addEventListener('click', function() {
        currentLessonIndex = index;
        loadLesson(currentLessonIndex);
    });
});

// Comment system
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

// Initialize the page
window.addEventListener('load', function() {
    loadComments(0);
});

// Add event listener for submitting new comments
const submitCommentBtn = document.getElementById('submit-comment-btn');
const commentText = document.getElementById('comment-text');

submitCommentBtn.addEventListener('click', function() {
    const content = commentText.value.trim();
    if (content) {
        // You'll need to implement this function to send the comment to the server
        submitComment(content);
    }
});

function submitComment(content) {
    // Implement the logic to send the comment to the server
    // After successful submission, reload the comments
    // For example:
    // fetch('/submit-comment', {
    //     method: 'POST',
    //     body: JSON.stringify({ content: content }),
    //     headers: { 'Content-Type': 'application/json' }
    // })
    // .then(response => response.json())
    // .then(data => {
    //     commentText.value = '';
    //     loadComments(0);
    // })
    // .catch(error => console.error('Error submitting comment:', error));
}