const IS_SAKAI_ENVIRONMENT = window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1';

document.addEventListener('DOMContentLoaded', function() {

	const userEidElement = document.getElementById('userEid');
	const userEid = userEidElement.getAttribute('data-user-eid');
	console.log("User EID from div:", userEid);
	const userEmailElement = document.getElementById('userEmail');
	const userEmail = userEmailElement.getAttribute('data-user-email');
    const toggleBtn = document.getElementById('toggle-btn');
    const videoList = document.getElementById('video-list');
    const container = document.querySelector('.container');
    const videoPlayer = document.getElementById('embedded-content');
    const videoTitle = document.querySelector('.title');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const submitCommentBtn = document.getElementById('submit-comment-btn');
    const commentTextArea = document.getElementById('comment-text');
	const searchInput = document.getElementById('search');
	   searchInput.addEventListener('keyup', function() {
	       searchLessons(this.value);
	   });

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
    const lessons = Array.from(document.querySelectorAll('.video-list-content .vid'));
    let currentLessonIndex = 0;
	function convertToEmbeddableUrl(url) {
	       // Check if it's a YouTube URL
	       const youtubeRegex = /(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/;
	       const match = url.match(youtubeRegex);

	       if (match) {
	           // YouTube URL
	           const videoId = match[1];
	           return `https://www.youtube.com/embed/${videoId}`;
	       }
		   
		   const facebookRegex = /(?:https?:\/\/)?(?:www\.)?(?:facebook\.com\/(?:watch\?v=|[^\/]+\/videos\/)([0-9]+)|fb\.watch\/[a-zA-Z0-9]+\/?)/;
   	       const match_1 = url.match(facebookRegex);

   	       if (match_1) {
   	           return `https://www.facebook.com/plugins/video.php?href=${url}`;
   	       }

	       // If not a YouTube URL, return the original URL
	       return url;
	   }
    function generatePlayURL(courseId, lessonId) {
        if (IS_SAKAI_ENVIRONMENT) {
            return `${_ctx}play/${courseId}?lessonId=${lessonId}`;
        } else {
            return `${_ctx}play/${courseId.split('-')[0]}?lessonId=${lessonId}`;
        }
    }

    function getCurrentCourseId() {
        const pathParts = window.location.pathname.split('/');
        if (IS_SAKAI_ENVIRONMENT) {
            for (let i = 0; i < pathParts.length; i++) {
                if (pathParts[i] === 'site' && i + 1 < pathParts.length) {
                    return pathParts[i + 1];
                }
            }
        } else {
            return pathParts[pathParts.length - 1].split('-')[0];
        }
        console.warn('Could not determine courseId from URL');
        return null;
    }

    function getCurrentLessonId() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('lessonId');
    }

    function findLessonIndex(lessonId) {
        return lessons.findIndex(lesson => lesson.getAttribute('data-lesson-id') === lessonId);
    }

	function encode(value) {
	    var result = encodeURIComponent(value).replace(/'/g, "%27").replace(/"/g, "%22");
	    return result;
	}

	/**
	 * Encode string to Base64
	 * @param {string} input - String to encode
	 * @returns {string} - Base64 encoded string
	 */
	function encodeBase64(input) {
	    const utf8Bytes = new TextEncoder().encode(input); // Convert to UTF-8
	    const base64String = btoa(String.fromCharCode(...utf8Bytes));
	    return base64String;
	}

	function generateBasicAuth() {
	    // Data to encode
	    const username = userEid;
	
	    const email = userEmail;
		
	    // String to encode
	    const valueToEncode = `${username}:${email}`;
	  

	    // Base64 encoding
	    const encodedValue = encodeBase64(valueToEncode);
	 

	    // Create Basic Authorization header
	    const authHeader = `Basic ${encodedValue}`;
	 

	    return authHeader;
	}
	
	function openCourse(courseUrl, activityId) {
	    const auth2 = generateBasicAuth();


	    var actor = `{"name":["${userEid}"],"mbox":["${userEmail}"],"objectType":"Agent"}`;
	  

	    var endPoint = `https://mksol.vn/xapi-lrs/${userEid}/`;
	 

	    var auth = auth2;

	    var params = 'actor=' + encode(actor) + 
	                '&endpoint=' + encode(endPoint) + 
	                '&auth=' + encode(auth) + 
	                '&activity_id=' + encode(activityId);
	    
	    const embeddableUrl = convertToEmbeddableUrl(courseUrl);
	    var iframeHTML = `<iframe src="${embeddableUrl}?${params}" width="100%" height="900px" frameborder="0" allowfullscreen></iframe>`;

	    const courseDiv = document.getElementById("course");
	    if (courseDiv) {
	        courseDiv.innerHTML = iframeHTML;

	        // Get course and lesson IDs from the current lesson
	        const currentLesson = lessons[currentLessonIndex];
	        const courseId = currentLesson.getAttribute('data-course-id');
	        const lessonId = currentLesson.getAttribute('data-lesson-id');

	        // Send tracking data to the server
	        fetch(`${_ctx}api/lesson-tracking`, {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify({
	                userEid: userEid,
	                courseId: courseId,
	                lessonId: lessonId,
	                courseUrl: courseUrl,
	                activityId: activityId
	            })
	        })
	        .then(response => {
	            if (!response.ok) {
	                throw new Error('Failed to save lesson tracking');
	            }
	            console.log('Lesson tracking saved successfully');
	        })
	        .catch(error => {
	            console.error('Error saving lesson tracking:', error);
	        });
	    } else {
	        console.error("Element with id 'course' not found");
	    }
	}

	function convertYoutubeLinkToEmbed(youtubeLink) {
	    try {
	        const url = new URL(youtubeLink);
			
			if (url.hostname === "www.youtube.com" && url.pathname.startsWith("/embed")) {
	            return youtubeLink;
	        }

	        if (url.hostname === "youtu.be") {
	            return `https://www.youtube.com/embed/${url.pathname.slice(1)}`;
	        }

	        if (url.hostname === "www.youtube.com" && url.pathname === "/watch") {
	            const videoId = url.searchParams.get("v");
	            if (videoId) {
	                return `https://www.youtube.com/embed/${videoId}`;
	            }
	        }

	        console.error("URL không đúng định dạng hợp lệ của YouTube.");
	        return null;
	    } catch (error) {
	        console.error("Định dạng URL không hợp lệ:", error);
	        return null;
	    }
	}
	
	function loadLesson(index) {
	    const lesson = lessons[index];
	    if (lesson) {
	        const videoUrl = lesson.querySelector('video').src; 
	        const title = lesson.querySelector('.title').textContent; 
	        const courseId = lesson.getAttribute('data-course-id'); 
	        const lessonId = lesson.getAttribute('data-lesson-id');
	        const activityId = lesson.getAttribute('data-activity-id'); 

	       
	        videoTitle.textContent = title;

	        
	        openCourse(videoUrl, activityId);

	       
	        lessons.forEach(l => l.classList.remove('active'));
	        lesson.classList.add('active');

	        
	        lesson.scrollIntoView({ behavior: 'smooth', block: 'nearest' });

	  
	        const newUrl = generatePlayURL(courseId, lessonId);
	        history.replaceState(null, '', newUrl);

	        // Load comments
	        loadComments(courseId, lessonId);
	    } else {
	        console.warn('No lesson found at index:', index); 
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
    // Thêm sự kiện click cho nút toggle-btn
	toggleBtn.addEventListener('click', () => {
	    const container = document.querySelector('.container-play');
	    if (container.classList.contains('expanded')) {
	        container.classList.remove('expanded');
	        toggleBtn.textContent = '☰'; 
	    } else {
	        container.classList.add('expanded');
	        toggleBtn.textContent = '✖';
	    }
	});
    // Xử lý sự kiện click cho bài học
    lessons.forEach((lesson, index) => {
        lesson.addEventListener('click', function () {
            currentLessonIndex = index;
            loadLesson(currentLessonIndex);
        });
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
        } else {
            console.log('Already at the last lesson');
        }
    });

    // Xử lý nút back/forward của trình duyệt
    window.addEventListener('popstate', function(event) {
        const lessonId = getCurrentLessonId();
        if (lessonId) {
            const lessonIndex = findLessonIndex(lessonId);
            if (lessonIndex !== -1) {
                currentLessonIndex = lessonIndex;
                loadLesson(currentLessonIndex, false);
            }
        }
    });

    submitCommentBtn.addEventListener('click', function () {
        const commentContent = commentTextArea.value.trim();
        const courseId = lessons[currentLessonIndex].getAttribute('data-course-id');
        const lessonId = lessons[currentLessonIndex].getAttribute('data-lesson-id');

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
                loadComments(courseId, lessonId, 0);
            })
            .catch(error => {
                console.error('Lỗi:', error);
            });
        } else {
            alert('Vui lòng nhập bình luận trước khi gửi.');
        }
    });

    // Khởi tạo trang
    const lessonId = getCurrentLessonId();
    const courseId = getCurrentCourseId();
    
    console.log('Initial lessonId:', lessonId);
    console.log('Initial courseId:', courseId);
    console.log('Initial lessons:', lessons);

    if (!lessonId && lessons.length > 0) {
        const firstLessonId = lessons[0].getAttribute('data-lesson-id');
        const newUrl = generatePlayURL(courseId, firstLessonId);
        history.replaceState(null, '', newUrl);
    }

    if (lessonId) {
        const lessonIndex = findLessonIndex(lessonId);
        if (lessonIndex !== -1) {
            currentLessonIndex = lessonIndex;
        }
    }
    
    if (lessons.length > 0) {
        if (currentLessonIndex >= 0 && currentLessonIndex < lessons.length) {
            loadLesson(currentLessonIndex, false);
        } else {
            console.warn('Invalid currentLessonIndex:', currentLessonIndex);
            loadLesson(0, false); // Load bài học đầu tiên nếu index không hợp lệ
        }
    } else {
        console.warn('No lessons found');
    }
	
});