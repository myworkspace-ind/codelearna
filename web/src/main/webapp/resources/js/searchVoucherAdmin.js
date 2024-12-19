function searchVouchers() {
    const searchValue = document.getElementById('voucherSearchInput').value.toLowerCase();
    const rows = document.querySelectorAll('#loadingContainer tbody tr');

    rows.forEach(row => {
        const campaignName = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
     

        if (campaignName.includes(searchValue)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function handleSearchVoucherKeyPress(event) {
    if (event.key === 'Enter') {
        searchVouchers();
    }
}
