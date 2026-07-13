#!/usr/bin/env bash
# Start Jaeger + FastAPI + React for the OpenTelemetry practice demo.
# Installs/syncs dependencies first, then runs everything until Ctrl+C.

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"

BACKEND_PID=""
FRONTEND_PID=""

# Stop child processes (and leave Jaeger running — use docker compose down to stop it).
cleanup() {
  echo ""
  echo "Stopping backend and frontend..."
  [[ -n "$BACKEND_PID" ]] && kill "$BACKEND_PID" 2>/dev/null || true
  [[ -n "$FRONTEND_PID" ]] && kill "$FRONTEND_PID" 2>/dev/null || true
  wait 2>/dev/null || true
}
trap cleanup EXIT INT TERM

echo "==> Syncing Python deps (uv)"
if ! command -v uv >/dev/null 2>&1; then
  echo "uv is required. Install: https://docs.astral.sh/uv/"
  exit 1
fi
uv sync

echo "==> Installing frontend deps (npm)"
if ! command -v npm >/dev/null 2>&1; then
  echo "npm / Node.js is required."
  exit 1
fi
(cd frontend && npm install)

echo "==> Starting Jaeger (docker compose)"
if ! command -v docker >/dev/null 2>&1; then
  echo "docker is required for Jaeger."
  exit 1
fi
docker compose up -d

echo "==> Starting FastAPI on :8000"
(
  cd backend
  # Use the project-root venv from uv; app-dir is this backend folder.
  uv run --project "$ROOT" uvicorn main:app --reload --host 127.0.0.1 --port 8000
) &
BACKEND_PID=$!

echo "==> Starting React (Vite) on :5173"
(cd frontend && npm run dev -- --host 127.0.0.1 --port 5173) &
FRONTEND_PID=$!

echo ""
echo "Ready:"
echo "  Dashboard  http://127.0.0.1:5173"
echo "  API        http://127.0.0.1:8000/docs"
echo "  Jaeger     http://127.0.0.1:16686"
echo ""
echo "Ctrl+C stops the API and UI. Jaeger keeps running (docker compose down to stop)."
echo ""

# Keep the script alive while either child is still running.
wait
