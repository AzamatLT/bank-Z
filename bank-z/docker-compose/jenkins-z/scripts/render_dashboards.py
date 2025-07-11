import requests
import os
import zipfile
from datetime import datetime, timedelta

GRAFANA_URL = "http://grafana:3000"
API_KEY = "xxx"
OUTPUT_DIR = "/var/jenkins_home/dashboard_screenshots"

def get_dashboards():
    headers = {"Authorization": f"Bearer {API_KEY}"}
    response = requests.get(f"{GRAFANA_URL}/api/search", headers=headers)
    return [{"uid": db["uid"], "title": db["title"]} for db in response.json()]

def render_dashboard(uid, title, time_from, time_to):
    headers = {"Authorization": f"Bearer {API_KEY}"}
    params = {
        "orgId": 1,
        "from": time_from,
        "to": time_to,
        "width": 1200,
        "height": 800,
        "timeout": 60
    }
    response = requests.get(
        f"{GRAFANA_URL}/render/d-solo/{uid}/{title}",
        headers=headers,
        params=params,
        stream=True
    )
    filename = f"{OUTPUT_DIR}/{title}_{time_from}_{time_to}.png"
    with open(filename, "wb") as f:
        for chunk in response.iter_content(1024):
            f.write(chunk)
    return filename

def create_zip():
    zip_filename = f"{OUTPUT_DIR}/dashboards_{datetime.now().strftime('%Y%m%d_%H%M%S')}.zip"
    with zipfile.ZipFile(zip_filename, "w") as zipf:
        for file in os.listdir(OUTPUT_DIR):
            if file.endswith(".png"):
                zipf.write(os.path.join(OUTPUT_DIR, file), file)
    return zip_filename

if __name__ == "__main__":
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    
    # Пример параметров (можно заменить на аргументы скрипта)
    dashboards = get_dashboards()
    time_from = (datetime.now() - timedelta(hours=1)).isoformat() + "Z"
    time_to = datetime.now().isoformat() + "Z"
    
    for db in dashboards[:3]:  # Рендерим первые 3 дашборда для примера
        render_dashboard(db["uid"], db["title"], time_from, time_to)
    
    zip_path = create_zip()
    print(f"ZIP archive created: {zip_path}")