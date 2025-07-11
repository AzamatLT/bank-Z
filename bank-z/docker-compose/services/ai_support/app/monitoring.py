from prometheus_client import start_http_server, Counter, Histogram
import time

# Метрики
REQUEST_COUNT = Counter(
    'bank_ai_request_count',
    'Total number of requests',
    ['endpoint', 'method']
)

REQUEST_LATENCY = Histogram(
    'bank_ai_request_latency_seconds',
    'Request latency in seconds',
    ['endpoint']
)

ERROR_COUNT = Counter(
    'bank_ai_error_count',
    'Total number of errors',
    ['error_type']
)

# Размер очереди Kafka
KAFKA_QUEUE_SIZE = Gauge(
    'bank_ai_kafka_queue_size',
    'Current number of messages in Kafka queue'
)

# Время ответа AI
AI_RESPONSE_TIME = Histogram(
    'bank_ai_response_time_seconds',
    'Time taken to generate AI response'
)

def monitor_requests(endpoint: str, method: str):
    """Декоратор для мониторинга запросов"""
    def decorator(func):
        def wrapper(*args, **kwargs):
            start_time = time.time()
            REQUEST_COUNT.labels(endpoint, method).inc()
            
            try:
                result = func(*args, **kwargs)
                latency = time.time() - start_time
                REQUEST_LATENCY.labels(endpoint).observe(latency)
                return result
            except Exception as e:
                ERROR_COUNT.labels(type(e).__name__).inc()
                raise

        return wrapper
    return decorator

def start_metrics_server(port: int = 8000):
    """Запуск сервера метрик"""
    start_http_server(port)
    
from prometheus_client import make_asgi_app, Counter, Histogram

REQUEST_COUNT = Counter(
    'http_requests_total',
    'Total HTTP Requests',
    ['method', 'endpoint']
)

def setup_monitoring(app):
    metrics_app = make_asgi_app()
    app.mount("/metrics", metrics_app)
    
    @app.middleware("http")
    async def count_requests(request, call_next):
        REQUEST_COUNT.labels(
            method=request.method,
            endpoint=request.url.path
        ).inc()
        response = await call_next(request)
        return response