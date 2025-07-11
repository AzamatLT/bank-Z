from pydantic import BaseModel
from datetime import datetime
from typing import Optional

# Сообщение в чате
class Message(BaseModel):
    user_id: str
    text: str
    timestamp: datetime = datetime.now()
    is_bot: bool = False

# Диалог с пользователем
class Conversation(BaseModel):
    conversation_id: str
    user_id: str
    messages: list[Message] = []
    created_at: datetime = datetime.now()
    updated_at: datetime = datetime.now()

# Клиент банка (из таблицы clients)
class Client(BaseModel):
    id: int
    inn: str
    last_name: str
    first_name: str
    middle_name: Optional[str] = None