function loadCertificateSection(event) {
	if (event) {
		event.preventDefault();
	}
	const dynamicContent = document.getElementById('dynamic-content');
	if (!dynamicContent) {
		console.error("Phần tử 'dynamic-content' không tồn tại trên trang.");
		return;
	}

	fetch(`${_ctx}admin/createCertificate`)
		.then(response => response.text())
		.then(html => {
			dynamicContent.innerHTML = html;
		})
		.catch(error => console.error('Error loading courses section:', error));
}
// Show popup function
function showPopup() {
	document.getElementById('popup').style.display = 'block';
	document.getElementById('overlay').style.display = 'block';
}

// Close popup function
function closePopup() {
	document.getElementById('popup').style.display = 'none';
	document.getElementById('overlay').style.display = 'none';
}
async function downloadPDF() {
	const { jsPDF } = window.jspdf;
	const certificate = document.getElementById('certificate');
	const canvas = await html2canvas(certificate);
	const imgData = canvas.toDataURL('image/png');

	const pdf = new jsPDF();
	pdf.addImage(imgData, 'PNG', 10, 10, 190, 0);
	pdf.save('certificate.pdf');
}

function updateQR() {
	const fileInput = document.getElementById('qr-upload');
	const qrContainer = document.getElementById('qr-container');

	if (fileInput.files && fileInput.files[0]) {
		const reader = new FileReader();

		reader.onload = function(e) {
			qrContainer.innerHTML = `<img src="${e.target.result}" alt="QR Code">`;
		};

		reader.readAsDataURL(fileInput.files[0]);
	}
}
function triggerQRUpload() {
	const fileInput = document.getElementById('qr-upload');
	fileInput.click();
}