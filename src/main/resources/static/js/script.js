document.addEventListener('DOMContentLoaded', function() {
    fetch('/account/data')
        .then(response => response.json())
        .then(data => {
            document.getElementById('firstName').textContent = data.firstName;
            document.getElementById('lastName').textContent = data.lastName;
            document.getElementById('phone').textContent = data.phone;
            document.getElementById('email').textContent = data.email;
            document.getElementById('IBAN').textContent = data.iban;
            document.getElementById('specialName').textContent = data.specialName;
            document.getElementById('dateOfBirth').textContent = data.dateOfBirth;
            document.getElementById('address').textContent = data.address;
        })
        .catch(error => {
            console.error('Ошибка при получении данных:', error);
        });
});
