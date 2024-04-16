$(document).ready(function() {
    $('form').on('submit', function(e) {
        e.preventDefault(); // Запобігаємо стандартній поведінці форми

        var email = $('#email').val(); // Отримуємо значення поля email
        $.ajax({
            url: '/forgot-password', // URL, на який буде відправлено запит
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email }), // Перетворюємо дані у JSON
            success: function(response) {
                alert('Інструкції з відновлення паролю відправлені на ваш email.');
                window.location.href = '/login'; // Перенаправляємо користувача на сторінку логіну
            },
            error: function(xhr, status, error) {
                alert('Помилка при відправленні email: ' + xhr.responseText);
            }
        });
    });
});