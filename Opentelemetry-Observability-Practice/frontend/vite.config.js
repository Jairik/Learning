import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// Vite proxies keep the browser same-origin for API + OTLP, so we avoid CORS
// headaches while still shipping traces to Jaeger and calls to FastAPI.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Frontend fetch("/api/...") -> FastAPI on :8000
      "/api": {
        target: "http://localhost:8000",
        changeOrigin: true,
      },
      // Browser OTLP export -> Jaeger OTLP HTTP on :4318
      "/otlp": {
        target: "http://localhost:4318",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/otlp/, ""),
      },
    },
  },
});
