// Java des Héros — Scripts principaux

// Confirmation avant suppression
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', (e) => {
            if (!confirm('Confirmer la suppression ?')) {
                e.preventDefault();
            }
        });
    });
});
