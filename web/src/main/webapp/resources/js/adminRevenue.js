var currentPagee = 1;
var pageSize = 5;
var totalPages = 1;

function loadRevenueSection(event) {
    if (event) {
        event.preventDefault();
    }
    const dynamicContent = document.getElementById('dynamic-content-revenue');
    if (!dynamicContent) {
        console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
        return;
    }

    fetch(`${_ctx}admin/revenue`)
        .then(response => response.text())
        .then(html => {
            dynamicContent.innerHTML = html;
            
            fetchRevenueStatistics();
			initializeRevenueSection(); // Thêm hàm khởi tạo
        })
        .catch(error => console.error('Error loading courses section:', error));
}

// Hàm khởi tạo cho phần revenue
function initializeRevenueSection() {
    const timePeriodSelect = document.getElementById('time-period');
    const customRangeDiv = document.getElementById('custom-range');
    
    if (timePeriodSelect && customRangeDiv) {
        timePeriodSelect.addEventListener('change', function() {
            if (this.value === 'custom') {
                customRangeDiv.style.display = 'block';
            } else {
                customRangeDiv.style.display = 'none';
            }
        });
    }

    // Thêm sự kiện cho nút fetch
    const fetchButton = document.getElementById('fetch-button');
    if (fetchButton) {
        fetchButton.addEventListener('click', fetchRevenueStatistics);
    }
}

// Đảm bảo initializeRevenueSection được gọi khi DOMContentLoaded
document.addEventListener('DOMContentLoaded', function() {
    const dynamicContent = document.getElementById('dynamic-content');
    if (dynamicContent) {
        initializeRevenueSection();
    }
});

// Các hàm còn lại giữ nguyên như ban đầu
function formatCurrency(amount) {
    if (amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            maximumFractionDigits: 0
        }).format(amount);
    }
    return 'N/A';
}

function fetchRevenueStatistics() {
    const timePeriodElement = document.getElementById('time-period');
    const startDateElement = document.getElementById('start-date');
    const endDateElement = document.getElementById('end-date');
    const totalRevenueElement = document.getElementById('total-revenue');
    const tempRevenueElement = document.getElementById('temp-revenue');
    const revenueDataElement = document.getElementById('revenue-data');

    // Comprehensive null checks
    if (!timePeriodElement || !startDateElement || !endDateElement || 
        !totalRevenueElement || !tempRevenueElement || !revenueDataElement) {
        console.error('One or more required elements are missing');
        return;
    }

    var timePeriod = timePeriodElement.value;
    var startDate = startDateElement.value;
    var endDate = endDateElement.value;

    var url = `${_ctx}/api/revenue/statistics?page=${currentPagee}&size=${pageSize}`;

    if (timePeriod === 'custom' && startDate && endDate) {
        url += `&startDate=${startDate}&endDate=${endDate}`;
    } else {
        url += `&timePeriod=${timePeriod}`;
    }

    fetch(url)
        .then(response => response.json())
        .then(data => {
            // Update total revenue
            const totalRevenue = formatCurrency(data.totalRevenue);
            totalRevenueElement.innerText = totalRevenue;

            // Update temporary revenue
            const tempRevenue = formatCurrency(data.tempRevenue);
            tempRevenueElement.innerText = tempRevenue;
            
            // Update revenue data table
            const revenueData = data.revenueByCourse;
            revenueDataElement.innerHTML = '';

            if (revenueData && revenueData.length > 0) {
                revenueData.forEach(item => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${item.courseName}</td>
                        <td>${item.purchaseCount}</td>
                        <td>${formatCurrency(item.revenue)}</td>
                    `;
                    revenueDataElement.appendChild(row);
                });
            } else {
                revenueDataElement.innerHTML = `<tr><td colspan="3" class="text-center">No revenue data available</td></tr>`;
            }
				
            // Update pagination
            totalPages = data.totalPages;
            updatePagination();
			createChart(revenueData);
			createChartRevenue(revenueData);
        })
        .catch(error => {
            console.error('Error fetching revenue statistics:', error);
            // Update elements with error state
            totalRevenueElement.innerText = 'Error loading data';
            tempRevenueElement.innerText = 'Error loading data';
            revenueDataElement.innerHTML = `<tr><td colspan="3" class="text-center">Failed to load revenue data</td></tr>`;
        });
}

function updatePagination() {
    const paginationContainer = document.getElementById('pagination');
    if (!paginationContainer) {
        console.error('Pagination container not found');
        return;
    }

    paginationContainer.innerHTML = '';

    if (currentPagee > 1) {
        const prevButton = document.createElement('button');
        prevButton.classList.add('btn', 'btn-primary');
        prevButton.innerText = 'Previous';
        prevButton.onclick = function() {
            currentPagee--;
            fetchRevenueStatistics();
        };
        paginationContainer.appendChild(prevButton);
    }

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.classList.add('btn', 'btn-secondary');
        pageButton.innerText = i;
        if (i === currentPagee) {
            pageButton.disabled = true;
            pageButton.classList.add('active');
        }
        pageButton.onclick = function() {
            currentPagee = i;
            fetchRevenueStatistics();
        };
        paginationContainer.appendChild(pageButton);
    }

    if (currentPagee < totalPages) {
        const nextButton = document.createElement('button');
        nextButton.classList.add('btn', 'btn-primary');
        nextButton.innerText = 'Next';
        nextButton.onclick = function() {
            currentPagee++;
            fetchRevenueStatistics();
        };
        paginationContainer.appendChild(nextButton);
    }
}
function createChart(revenueData)
{
	const courseNames = [];
    const purchaseCounts = [];
	
	const courseCountMap = {}; // Object để đếm số lượng

    // Duyệt qua revenueData và cập nhật số lượng từng courseName
    revenueData.forEach(item => {
        if (courseCountMap[item.courseName]) {
            courseCountMap[item.courseName] += item.purchaseCount;
        } else {
            courseCountMap[item.courseName] = item.purchaseCount;
        }
    });
	Object.entries(courseCountMap).forEach(([courseName, totalCount]) => {
	        courseNames.push(courseName);
	        purchaseCounts.push(totalCount);
	    });
		// Tạo biểu đồ tròn
		    const ctx = document.getElementById('visitorChart').getContext('2d');
		    new Chart(ctx, {
		        type: 'pie', // Loại biểu đồ tròn
		        data: {
		            labels: courseNames, // Tên khóa học
		            datasets: [{
		                label: 'Tỷ lệ đơn hàng',
		                data: purchaseCounts, // Số lượng đơn hàng
		                backgroundColor: [
		                    'rgba(255, 99, 132, 0.5)',
		                    'rgba(54, 162, 235, 0.5)',
		                    'rgba(255, 206, 86, 0.5)',
		                    'rgba(75, 192, 192, 0.5)',
		                    'rgba(153, 102, 255, 0.5)',
		                    'rgba(255, 159, 64, 0.5)'
		                ], // Màu sắc khác nhau cho từng phần
		                borderColor: [
		                    'rgba(255, 99, 132, 1)',
		                    'rgba(54, 162, 235, 1)',
		                    'rgba(255, 206, 86, 1)',
		                    'rgba(75, 192, 192, 1)',
		                    'rgba(153, 102, 255, 1)',
		                    'rgba(255, 159, 64, 1)'
		                ],
		                borderWidth: 1
		            }]
		        },
		        options: {
		            responsive: true,
		            plugins: {
		                legend: {
		                    position: 'top', // Vị trí của chú thích
		                },
		                tooltip: {
		                    callbacks: {
		                        label: function (tooltipItem) {
		                            const total = purchaseCounts.reduce((sum, value) => sum + value, 0);
		                            const percentage = ((tooltipItem.raw / total) * 100).toFixed(2);
		                            return `${tooltipItem.label}: ${tooltipItem.raw} courses (${percentage}%)`;
		                        }
		                    }
		                }
		            }
		        }
		    });
			
}
function createChartRevenue(revenueData){
	const courseNames = [];
	    const revenues = [];

	    // Xử lý dữ liệu
	    const courseRevenueMap = {};
	    revenueData.forEach(item => {
	        if (courseRevenueMap[item.courseName]) {
	            courseRevenueMap[item.courseName] += item.revenue;
	        } else {
	            courseRevenueMap[item.courseName] = item.revenue;
	        }
	    });

	    // Chuyển dữ liệu vào mảng
	    Object.entries(courseRevenueMap).forEach(([courseName, totalRevenue]) => {
	        courseNames.push(courseName);
	        revenues.push(totalRevenue);
	    });

	    // Tạo biểu đồ tròn thống kê doanh thu
	    const ctx = document.getElementById('revenueChart').getContext('2d');
	    new Chart(ctx, {
	        type: 'pie', // Loại biểu đồ tròn
	        data: {
	            labels: courseNames, // Tên khóa học
	            datasets: [{
	                label: 'Doanh thu',
	                data: revenues, // Doanh thu của từng khóa học
	                backgroundColor: [
	                    'rgba(255, 99, 132, 0.5)',
	                    'rgba(54, 162, 235, 0.5)',
	                    'rgba(255, 206, 86, 0.5)',
	                    'rgba(75, 192, 192, 0.5)',
	                    'rgba(153, 102, 255, 0.5)',
	                    'rgba(255, 159, 64, 0.5)'
	                ], // Màu sắc khác nhau cho từng phần
	                borderColor: [
	                    'rgba(255, 99, 132, 1)',
	                    'rgba(54, 162, 235, 1)',
	                    'rgba(255, 206, 86, 1)',
	                    'rgba(75, 192, 192, 1)',
	                    'rgba(153, 102, 255, 1)',
	                    'rgba(255, 159, 64, 1)'
	                ],
	                borderWidth: 1
	            }]
	        },
	        options: {
	            responsive: true,
	            plugins: {
	                legend: {
	                    position: 'top', // Vị trí của chú thích
	                },
	                tooltip: {
	                    callbacks: {
	                        label: function (tooltipItem) {
	                            const totalRevenue = revenues.reduce((sum, value) => sum + value, 0);
	                            const percentage = ((tooltipItem.raw / totalRevenue) * 100).toFixed(2);
	                            return `${tooltipItem.label}: ${formatCurrency(tooltipItem.raw)} (${percentage}%)`;
	                        }
	                    }
	                }
	            }
	        }
	    });
}
