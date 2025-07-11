document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = {
        username: document.getElementById('regUsername').value,
        password: document.getElementById('regPassword').value,
        confirmPassword: document.getElementById('confirmPassword').value
    };

    try {
        const response = await fetch('/auth/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Registration failed');
        }

        window.location.href = '/auth/static/index.html?registered=true';
    } catch (error) {
        document.getElementById('error').textContent = error.message;
    }
});