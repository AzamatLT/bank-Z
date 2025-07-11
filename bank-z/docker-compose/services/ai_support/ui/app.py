import streamlit as st
import requests
from datetime import datetime

# Настройка страницы
st.set_page_config(page_title="Банковский чат-бот", layout="wide")

# Инициализация сессии
if "messages" not in st.session_state:
    st.session_state.messages = []

# Стили для сообщений
st.markdown("""
<style>
.user-message {
    background-color: #f0f2f6;
    padding: 10px;
    border-radius: 10px;
    margin: 5px 0;
}
.bot-message {
    background-color: #e3f2fd;
    padding: 10px;
    border-radius: 10px;
    margin: 5px 0;
}
</style>
""", unsafe_allow_html=True)

# Отображение истории сообщений
for msg in st.session_state.messages:
    if msg["role"] == "user":
        st.markdown(f"""
        <div class="user-message">
            <strong>Вы</strong> ({msg['time']}): {msg['content']}
        </div>
        """, unsafe_allow_html=True)
    else:
        st.markdown(f"""
        <div class="bot-message">
            <strong>Бот</strong> ({msg['time']}): {msg['content']}
        </div>
        """, unsafe_allow_html=True)

# Функция отправки сообщения
def send_message():
    user_input = st.session_state.user_input.strip()
    if user_input:
        # Добавляем сообщение пользователя
        st.session_state.messages.append({
            "role": "user",
            "content": user_input,
            "time": datetime.now().strftime("%H:%M:%S")
        })
        
        try:
            # Отправляем запрос к бекенду
            response = requests.post(
                "http://backend:48091/chat",
                json={"text": user_input, "user_id": "user"}
            )
            response.raise_for_status()
            bot_response = response.json()["response"]
        except requests.exceptions.RequestException as e:
            bot_response = f"Ошибка соединения с сервером: {str(e)}"
        except Exception as e:
            bot_response = f"Ошибка обработки запроса: {str(e)}"
        
        # Добавляем ответ бота
        st.session_state.messages.append({
            "role": "assistant",
            "content": bot_response,
            "time": datetime.now().strftime("%H:%M:%S")
        })
        
        # Очищаем поле ввода
        st.session_state.user_input = ""

# Поле ввода и кнопка
col1, col2 = st.columns([5, 1])
with col1:
    st.text_input(
        "Введите сообщение",
        key="user_input",
        on_change=send_message,
        label_visibility="collapsed",
        placeholder="Напишите ваш вопрос здесь..."
    )
with col2:
    st.button("Отправить", on_click=send_message)