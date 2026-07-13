# FastAPI demo API with OpenTelemetry tracing.
# Telemetry must be configured before the app is created so auto-instrumentation can wrap ASGI.

from __future__ import annotations

import random
import time
from typing import Any

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from opentelemetry import trace
from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor

from telemetry import setup_telemetry

# Wire up the tracer provider / OTLP exporter before creating routes.
setup_telemetry()

# Named tracer for hand-written spans (business logic we care about in Jaeger).
tracer = trace.get_tracer("dashboard-api")

app = FastAPI(title="OpenTelemetry Dashboard API")

# Browser (Vite on :5173) calls this API; CORS must allow those origins.
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://127.0.0.1:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Auto-instrument FastAPI so each HTTP request becomes a server span,
# and inbound W3C traceparent headers continue the browser's parent span.
FastAPIInstrumentor.instrument_app(app)


@app.get("/api/health")
def health() -> dict[str, str]:
    """Simple liveness check — still appears as a span via FastAPIInstrumentor."""
    return {"status": "ok"}


@app.get("/api/metrics")
def get_metrics() -> dict[str, Any]:
    """
    Mimics a dashboard 'Load Metrics' action.
    Creates a child span for the 'query' work so Jaeger shows nested timing.
    """
    with tracer.start_as_current_span("query_metrics_store") as span:
        # Fake DB latency so the span has a visible duration in Jaeger.
        time.sleep(0.15)
        values = {
            "active_users": random.randint(40, 120),
            "requests_per_min": random.randint(200, 900),
            "error_rate": round(random.uniform(0.0, 2.5), 2),
        }
        # Attributes show up as searchable tags on the span in Jaeger.
        span.set_attribute("metrics.active_users", values["active_users"])
        span.set_attribute("metrics.error_rate", values["error_rate"])
        return values


@app.post("/api/report")
def run_report() -> dict[str, Any]:
    """
    Mimics a dashboard 'Run Report' action with several nested steps.
    Each step is its own span so the flame graph / timeline is easy to read.
    """
    with tracer.start_as_current_span("run_report") as root:
        root.set_attribute("report.type", "weekly_summary")

        # Step 1: gather raw rows
        with tracer.start_as_current_span("fetch_raw_rows") as fetch_span:
            time.sleep(0.2)
            rows = random.randint(50, 200)
            fetch_span.set_attribute("report.rows_fetched", rows)

        # Step 2: transform / aggregate
        with tracer.start_as_current_span("aggregate_rows") as agg_span:
            time.sleep(0.25)
            total = rows * random.randint(3, 9)
            agg_span.set_attribute("report.total", total)

        # Step 3: pretend to persist the result
        with tracer.start_as_current_span("persist_report"):
            time.sleep(0.1)

        return {"status": "ready", "rows": rows, "total": total}


@app.post("/api/alert")
def trigger_alert() -> dict[str, str]:
    """
    Mimics a dashboard 'Trigger Alert' action that sometimes fails.
    Failed spans are marked ERROR so they stand out in Jaeger.
    """
    with tracer.start_as_current_span("evaluate_alert_rules") as span:
        time.sleep(0.1)
        # Roughly half the time we fail on purpose — useful for seeing error traces.
        if random.random() < 0.5:
            span.set_attribute("alert.triggered", False)
            span.set_status(trace.Status(trace.StatusCode.ERROR, "threshold exceeded"))
            span.record_exception(RuntimeError("alert threshold exceeded"))
            raise HTTPException(status_code=500, detail="Alert threshold exceeded")

        span.set_attribute("alert.triggered", True)
        return {"status": "alert_sent", "channel": "ops-slack"}
