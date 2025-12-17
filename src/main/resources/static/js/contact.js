function copyEmail() {
    const email = 'support@projectmanager.com';

    // Copy to clipboard
    navigator.clipboard.writeText(email).then(function() {
        showToast('Email copied to clipboard: ' + email);
    }).catch(function(err) {
        console.error('Could not copy email: ', err);
        showToast('Failed to copy email');
    });
}

function showToast(message) {
    const toast = document.getElementById('toast');
    if (!toast) {
        console.error('Toast element not found');
        return;
    }

    toast.textContent = message;
    toast.classList.add('show');

    // Hide after 3 seconds
    setTimeout(function() {
        toast.classList.remove('show');
    }, 3000);
}
