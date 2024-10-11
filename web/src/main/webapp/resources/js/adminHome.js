function loadCoursesSection(event) {
    event.preventDefault();  // Ngăn chặn hành vi mặc định của liên kết

    // Sử dụng fetch để gọi API và tải nội dung "Quản lý khóa học"
    fetch(`${_ctx}admin/listCourse`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
            setupPagination(); // Gọi setupPagination chỉ khi tải danh sách khóa học
        })
        .catch(error => console.error('Error loading courses section:', error));
}

function loadEditCourseForm(courseId) {
    fetch(`${_ctx}admin/courses/edit/${courseId}`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
            // Không gọi setupPagination ở đây vì fragment editCourse không cần phân trang
        })
        .catch(error => console.error('Error loading edit course form:', error));
}


function filterSubcategories(categoryId) {
    const subcategorySelect = document.getElementById('subcategory');
    
    // Nếu người dùng chọn một category, enable subcategory
    if (categoryId) {
        subcategorySelect.disabled = false;
        
        // Lọc và hiển thị các subcategory phù hợp
        const allSubcategories = subcategorySelect.querySelectorAll('option');
        
        allSubcategories.forEach(option => {
            if (option.value === "" || option.getAttribute('data-category-id') === categoryId) {
                option.style.display = '';  // Hiển thị subcategory phù hợp
            } else {
                option.style.display = 'none';  // Ẩn các subcategory không phù hợp
            }
        });

        // Reset lại lựa chọn subcategory
        subcategorySelect.value = "";
    } else {
        // Nếu không có category nào được chọn, disable subcategory
        subcategorySelect.disabled = true;
        subcategorySelect.value = "";
    }
}


