"""OpenTelemetry setup for the FastAPI backend.

Exports spans over OTLP/HTTP to Jaeger (default: http://localhost:4318/v1/traces).
"""

from __future__ import annotations

import os

from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor


def setup_telemetry() -> None:
    """
    Configure the global TracerProvider once at process start.

    SERVICE_NAME is how this process shows up in the Jaeger service dropdown.
    """
    # Resource = identity metadata attached to every span from this process.
    resource = Resource.create(
        {
            "service.name": "dashboard-api",
            "service.version": "0.1.0",
        }
    )

    provider = TracerProvider(resource=resource)

    # Where Jaeger listens for OTLP HTTP (docker-compose maps 4318).
    endpoint = os.getenv(
        "OTEL_EXPORTER_OTLP_TRACES_ENDPOINT",
        "http://localhost:4318/v1/traces",
    )

    # BatchSpanProcessor buffers spans and ships them in the background
    # so request handlers stay fast.
    exporter = OTLPSpanExporter(endpoint=endpoint)
    provider.add_span_processor(BatchSpanProcessor(exporter))

    # Register as the process-wide provider used by get_tracer() / instrumentors.
    trace.set_tracer_provider(provider)
