import requests
import os
import argparse
from dotenv import load_dotenv

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--dashboard-uid', required=True)
    parser.add_argument('--panel-id', required=True)
    parser.add_argument('--from', dest='time_from', required=True)
    parser.add_argument('--to', dest='time_to', required=True)
    parser.add_argument('--output', required=True)
    args = parser.parse_args()

    load_dotenv()
    base_url = os.getenv('GRAFANA_URL', 'http://grafana:3000')
    api_key = os.getenv('GRAFANA_API_KEY')

    os.makedirs(args.output, exist_ok=True)

    render_url = f"{base_url}/render/d-solo/{args.dashboard_uid}/?orgId=1&panelId={args.panel_id}"
    params = {
        'from': args.time_from,
        'to': args.time_to,
        'width': 1000,
        'height': 500,
        'tz': 'UTC'
    }

    response = requests.get(
        render_url,
        params=params,
        headers={'Authorization': f'Bearer {api_key}'},
        stream=True
    )

    output_file = os.path.join(args.output, f"panel_{args.panel_id}.png")
    with open(output_file, 'wb') as f:
        for chunk in response.iter_content(1024):
            f.write(chunk)

    print(f"Saved: {output_file}")

if __name__ == "__main__":
    main()