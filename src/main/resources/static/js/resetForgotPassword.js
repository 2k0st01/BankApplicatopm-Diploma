$(document).ready(function() {
    // Отримання токена з URL
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    // Перевірка наявності токена
    if (!token) {
        window.location.href = '/login'; // Переадресація на сторінку входу
    }

    // Обробка відправлення форми
    $('form').on('submit', function(e) {
        e.preventDefault(); // Зупинка стандартного відправлення форми

        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();

        // Перевірка співпадіння паролів
        if (newPassword !== confirmPassword) {
            alert('Паролі не співпадають!');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/change-password?token=' + token,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                newPassword: newPassword,
                confirmPassword: confirmPassword
            }),
            success: function(response) {
                alert('Пароль успішно змінено!');
                window.location.href = '/login'; // Переадресація на сторінку входу
            },
            error: function(xhr, status, error) {
                alert('Помилка: ' + xhr.responseText);
            }
        });
    });
});