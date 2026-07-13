import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { setupTelemetry } from "./telemetry.js";
import "./App.css";

// Start tracing before React mounts so the first button click is covered.
setupTelemetry();

createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
