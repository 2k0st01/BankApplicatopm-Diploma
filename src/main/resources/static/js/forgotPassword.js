$(document).ready(function() {

    $('form').on('submit', function(e) {
        e.preventDefault(); // Запобігаємо стандартній поведінці форми


        $.ajax({
            url: '/forgot-password', // URL, на який буде відправлено запит
            method: 'POST',
            headers: { // Исправлено с header на headers
                'X-XSRF-TOKEN': csrfToken,
                'Content-Type': 'application/json' // Убедитесь, что Content-Type правильно установлен
            },
            contentType: 'application/json',
            data: JSON.stringify({ email: email }), // Перетворюємо дані у JSON
            success: function(response) {
                alert('Інструкції з відновлення паролю відправлені на ваш email.');
            },
            error: function(xhr, status, error) {
                alert('Помилка при відправленні email: ' + xhr.responseText);
            }
        });
    });
});

