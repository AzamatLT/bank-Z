<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Банковские сервисы</title>
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            if (!localStorage.getItem('jwtToken')) {
                window.location.href = '/auth/static/index.html';
                return;
            }

            document.getElementById('services').innerHTML = `
                <h2>Доступные сервисы</h2>
                <ul>
                    <li><a href="#">Переводы между счетами</a></li>
                    <li><a href="#">Оплата услуг</a></li>
                    <li><a href="#">История операций</a></li>
                </ul>
                <button onclick="logout()">Выйти</button>
            `;
        });

        function logout() {
            localStorage.removeItem('jwtToken');
            window.location.href = '/auth/static/index.html';
        }
    </script>
</head>
<body>
    <div id="services"></div>
</body>
</html>