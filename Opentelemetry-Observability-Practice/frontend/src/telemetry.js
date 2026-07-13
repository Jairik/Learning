/**
 * Browser OpenTelemetry setup.
 *
 * Creates spans for UI actions and outbound fetch() calls, then exports them
 * to Jaeger via the Vite /otlp proxy. Fetch instrumentation also injects
 * W3C traceparent headers so the FastAPI spans become children of the UI span.
 */

import { ZoneContextManager } from "@opentelemetry/context-zone";
import { OTLPTraceExporter } from "@opentelemetry/exporter-trace-otlp-http";
import { registerInstrumentations } from "@opentelemetry/instrumentation";
import { FetchInstrumentation } from "@opentelemetry/instrumentation-fetch";
import { Resource } from "@opentelemetry/resources";
import { BatchSpanProcessor } from "@opentelemetry/sdk-trace-base";
import { WebTracerProvider } from "@opentelemetry/sdk-trace-web";
import { ATTR_SERVICE_NAME } from "@opentelemetry/semantic-conventions";
import { trace } from "@opentelemetry/api";

let ready = false;

/** Initialize the web tracer once; safe to call from main.jsx before render. */
export function setupTelemetry() {
  if (ready) return;

  // service.name is what appears in Jaeger's service filter for the frontend.
  const resource = new Resource({
    [ATTR_SERVICE_NAME]: "dashboard-ui",
  });

  // Batch + send to Jaeger through the Vite proxy (avoids browser CORS).
  const provider = new WebTracerProvider({ resource });
  provider.addSpanProcessor(
    new BatchSpanProcessor(
      new OTLPTraceExporter({
        url: "/otlp/v1/traces",
      })
    )
  );

  provider.register({
    // Zone.js context manager keeps the active span across async/await.
    contextManager: new ZoneContextManager(),
  });

  registerInstrumentations({
    instrumentations: [
      new FetchInstrumentation({
        // Propagate trace context to our API (and any localhost URL).
        propagateTraceHeaderCorsUrls: [/.*/],
        // Don't create extra spans for the OTLP exporter's own POSTs.
        ignoreUrls: [/\/otlp\//],
      }),
    ],
  });

  ready = true;
}

/** Helper so App.jsx can open named UI spans around button handlers. */
export function getTracer() {
  return trace.getTracer("dashboard-ui");
}
