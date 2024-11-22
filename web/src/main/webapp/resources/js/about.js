/**
 * Display dialog About
 */
$(function () {
    $('#about').click(function() {
        const dlgId = $('#dlg_about');
        const bootstrapModal = new bootstrap.Modal(dlgId);
        
        bootstrapModal.show();
    });
});

