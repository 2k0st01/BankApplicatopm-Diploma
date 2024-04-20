function submitForm() {
    const form = document.getElementById('transactionForm');
    const formData = {
        sum: form.sum.value,
        comment: form.comment.value,
        toAccount: form.toAccount.value,
        currency: form.currency.value,
    };

    fetch('/transaction/send', {
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
                alert('Transaction successful!');
                window.location.href = '/transaction';
            } else {
                throw new Error(data.message);
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Error in transaction: ' + error.message);
        });
}