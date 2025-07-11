from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from app.agent import BankAIAgent

app = FastAPI()

class Message(BaseModel):
    text: str
    user_id: str

@app.post("/chat")
async def chat(message: Message):
    try:
        agent = BankAIAgent()
        response_text = agent.generate_response(message.text)
        return {"response": response_text}
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Internal Server Error: {str(e)}"
        )