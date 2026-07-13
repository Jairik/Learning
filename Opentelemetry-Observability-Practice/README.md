# OpenTelemetry Observability Practice

A small **React + FastAPI** demo that shows how OpenTelemetry traces flow from a browser UI into a Python API, then land in **Jaeger** for visualization.

The business logic is intentionally trivial. The point is the **trace**: click a button → UI span → HTTP span → API spans (with nested work) → Jaeger timeline.

## What you get

| Piece | Role |
| --- | --- |
| `frontend/` | Vite + React “ops dashboard” with 3 buttons |
| `backend/` | FastAPI endpoints those buttons call |
| `docker-compose.yml` | Jaeger all-in-one (OTLP receiver + UI) |

**Services in Jaeger**

- `dashboard-ui` — browser spans (button clicks + `fetch`)
- `dashboard-api` — FastAPI request spans + hand-written child spans

## Architecture (how it fits together)

```text
┌─────────────────┐     fetch + traceparent      ┌─────────────────┐
│  React (Vite)   │ ───────────────────────────► │  FastAPI (:8000) │
│  dashboard-ui   │                              │  dashboard-api   │
└────────┬────────┘                              └────────┬────────┘
         │ OTLP /v1/traces (via Vite /otlp proxy)          │ OTLP /v1/traces
         ▼                                                 ▼
                              ┌─────────────────┐
                              │ Jaeger (:4318)  │
                              │ UI  (:16686)    │
                              └─────────────────┘
```

1. **Click** a button → the UI opens a named span (`ui.load_metrics`, etc.).
2. **`fetch` instrumentation** creates an HTTP client span and injects the W3C `traceparent` header.
3. **FastAPI instrumentation** reads that header and continues the **same trace** as a server span.
4. **Manual spans** in Python (`query_metrics_store`, `fetch_raw_rows`, …) nest under the request.
5. Both apps **export** spans to Jaeger over **OTLP HTTP**.
6. The Vite dev server **proxies** `/api` → FastAPI and `/otlp` → Jaeger so the browser avoids CORS issues.

## Prerequisites

- Docker (for Jaeger)
- Python 3.10+
- Node.js 18+

## Run it

### One command (recommended)

From this folder, sync deps and start Jaeger + API + UI:

```bash
chmod +x start.sh   # once
./start.sh
```

That runs `uv sync`, `npm install`, `docker compose up -d`, then FastAPI (`:8000`) and Vite (`:5173`). Ctrl+C stops the API/UI; Jaeger keeps running until `docker compose down`.

| URL | What |
| --- | --- |
| http://localhost:5173 | Dashboard |
| http://localhost:8000/docs | API docs |
| http://localhost:16686 | Jaeger UI |

### Manual (optional)

```bash
docker compose up -d
uv sync && cd backend && uv run --project .. uvicorn main:app --reload --port 8000
cd frontend && npm install && npm run dev
```

### 4. Generate traces

Click:

- **Load Metrics** — simple GET + one child span
- **Run Report** — POST with several nested backend spans (clearest flame graph)
- **Trigger Alert** — POST that fails ~50% of the time (look for red/error spans)

### 5. View the graph in Jaeger

OpenTelemetry does the instrumentation. Jaeger **captures** those spans and turns them into interactive views.

1. Open [http://localhost:16686](http://localhost:16686)
2. **Service:** `dashboard-ui` (or `dashboard-api`) → **Find Traces**
3. Click a trace — you get several ways to “see the graph”:

| View | What it shows |
| --- | --- |
| **Timeline** (default) | Gantt-style bars: who called whom, and how long each span took |
| **Trace Graph** | Node graph of spans in that one request (parent → child edges) |
| **System Architecture** / **DAG** (search page / compare) | Higher-level service-to-service graph across traces |

Best first click: **Run Report**, then open the trace and switch to **Trace Graph**. You should see one **trace ID** linking `dashboard-ui` → HTTP → `dashboard-api` → nested steps (`fetch_raw_rows`, `aggregate_rows`, …).

## What each button does

| Button | API | Trace highlight |
| --- | --- | --- |
| Load Metrics | `GET /api/metrics` | `ui.load_metrics` → HTTP → `GET /api/metrics` → `query_metrics_store` |
| Run Report | `POST /api/report` | Nested `fetch_raw_rows` → `aggregate_rows` → `persist_report` |
| Trigger Alert | `POST /api/alert` | Random success/failure; failures mark span status `ERROR` |

## Key files

```text
backend/
  telemetry.py   # TracerProvider + OTLP exporter (service.name = dashboard-api)
  main.py        # Routes + manual spans + FastAPIInstrumentor

frontend/
  src/telemetry.js   # WebTracerProvider + FetchInstrumentation (service.name = dashboard-ui)
  src/App.jsx        # Buttons wrap handlers in UI spans
  vite.config.js     # Proxies /api → :8000 and /otlp → Jaeger :4318
```

## Concepts this demo illustrates

- **Span** — a timed unit of work (button click, HTTP call, DB-ish step).
- **Trace** — a tree of spans sharing one `trace_id` (end-to-end request story).
- **Context propagation** — `traceparent` header links browser and backend.
- **Instrumentation** — libraries create spans for frameworks (`fetch`, FastAPI) so you don’t hand-wrap every call.
- **OTLP** — the wire protocol both apps use to send spans to Jaeger (Jaeger’s old proprietary exporters are deprecated).
- **Service name** — how Jaeger groups spans (`dashboard-ui` vs `dashboard-api`).

## Troubleshooting

| Symptom | Check |
| --- | --- |
| No traces in Jaeger | Is `docker compose` up? Ports `4318` / `16686` free? |
| Only API spans, no UI | Frontend running? Vite proxy `/otlp` working? Wait a few seconds for batch export. |
| UI spans but not linked to API | Confirm you’re clicking through the Vite app (`:5173`), not calling `:8000` directly in a way that skips the UI span. |
| CORS / failed OTLP from browser | Use the Vite proxy URL `/otlp/v1/traces` (already configured). Don’t point the browser straight at `:4318` unless CORS is set. |
| Backend can’t reach Jaeger | Exporter default is `http://localhost:4318/v1/traces`. Override with `OTEL_EXPORTER_OTLP_TRACES_ENDPOINT` if needed. |

## Tear down

```bash
docker compose down
# stop uvicorn + npm with Ctrl+C
```

## Skipped on purpose

- OpenTelemetry Collector (apps talk to Jaeger OTLP directly)
- Metrics / logs pipelines (traces only)
- Auth, databases, production hardening

Add those when you outgrow this sandbox.
