document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const errorElement = document.getElementById('error');
    errorElement.textContent = '';

    const formData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
    };

    try {
        const response = await fetch('/auth/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || result);
        }

        localStorage.setItem('jwtToken', result.token);
        window.location.href = '/auth/static/services.html';

    } catch (error) {
        errorElement.textContent = error.message || 'Ошибка авторизации';
        console.error('Auth error:', error);
    }
});