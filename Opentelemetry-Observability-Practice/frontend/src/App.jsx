import { useState } from "react";
import { SpanStatusCode } from "@opentelemetry/api";
import { getTracer } from "./telemetry.js";

/**
 * Tiny dashboard UI: three actions that hit the FastAPI backend.
 * Each click opens a parent span; fetch instrumentation nests the HTTP call under it.
 */
export default function App() {
  const [output, setOutput] = useState("Click a button to generate a distributed trace.");
  const [busy, setBusy] = useState(false);

  /**
   * Wrap an async API call in a named UI span so Jaeger shows a clear root
   * on the dashboard-ui service, with the HTTP + backend work underneath.
   */
  async function runAction(spanName, requestFn) {
    const tracer = getTracer();
    setBusy(true);

    // startActiveSpan keeps this span current for nested fetch spans.
    await tracer.startActiveSpan(spanName, async (span) => {
      try {
        const result = await requestFn();
        span.setAttribute("ui.result", JSON.stringify(result));
        setOutput(JSON.stringify(result, null, 2));
      } catch (err) {
        // Mark the UI span as errored when the backend returns 5xx / network fails.
        span.recordException(err);
        span.setStatus({ code: SpanStatusCode.ERROR, message: String(err) });
        setOutput(`Error: ${err.message || err}`);
      } finally {
        span.end();
        setBusy(false);
      }
    });
  }

  /** Button 1 — Load Metrics (GET). */
  function loadMetrics() {
    return runAction("ui.load_metrics", async () => {
      const res = await fetch("/api/metrics");
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      return res.json();
    });
  }

  /** Button 2 — Run Report (POST with nested backend spans). */
  function runReport() {
    return runAction("ui.run_report", async () => {
      const res = await fetch("/api/report", { method: "POST" });
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      return res.json();
    });
  }

  /** Button 3 — Trigger Alert (POST; backend randomly fails ~50%). */
  function triggerAlert() {
    return runAction("ui.trigger_alert", async () => {
      const res = await fetch("/api/alert", { method: "POST" });
      const body = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(body.detail || `HTTP ${res.status}`);
      return body;
    });
  }

  return (
    <div className="page">
      <header className="header">
        <p className="eyebrow">OpenTelemetry practice</p>
        <h1>Ops Dashboard</h1>
        <p className="lede">
          Each button creates a browser span, calls the API, and continues the
          same trace into FastAPI — then Jaeger stitches it together.
        </p>
      </header>

      <section className="actions" aria-label="Dashboard actions">
        <button type="button" disabled={busy} onClick={loadMetrics}>
          Load Metrics
        </button>
        <button type="button" disabled={busy} onClick={runReport}>
          Run Report
        </button>
        <button type="button" disabled={busy} onClick={triggerAlert}>
          Trigger Alert
        </button>
      </section>

      <section className="panel">
        <h2>Last response</h2>
        <pre>{output}</pre>
      </section>

      <p className="hint">
        After clicking, open{" "}
        <a href="http://localhost:16686" target="_blank" rel="noreferrer">
          Jaeger UI
        </a>{" "}
        and search for service <code>dashboard-ui</code> or{" "}
        <code>dashboard-api</code>.
      </p>
    </div>
  );
}
