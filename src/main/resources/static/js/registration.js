function submitForm() {
    const form = document.getElementById('registrationForm');
    const formData = {
        firstName: form.firstName.value,
        lastName: form.lastName.value,
        email: form.email.value,
        password: form.password.value,
        phone: form.phone.value,
        dateOfBirth: form.dateOfBirth.value
    };

    fetch('/api/registration', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Check your data');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                console.log('Success:', data.message);
                alert('Registration successful!');
                window.location.href = '/login';
            } else {
                throw new Error(data.message);
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Error in registration: ' + error.message);
        });
}
