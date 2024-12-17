function searchCampaigns() {
    const searchValue = document.getElementById('campaignSearchInput').value.toLowerCase();
    const rows = document.querySelectorAll('#campaignsContainer tbody tr');

    rows.forEach(row => {
        const campaignName = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
        const shortDescription = row.querySelector('td:nth-child(7)').textContent.toLowerCase();

        if (campaignName.includes(searchValue) || shortDescription.includes(searchValue)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

function handleSearchCampaignKeyPress(event) {
    if (event.key === 'Enter') {
        searchCampaigns();
    }
}
