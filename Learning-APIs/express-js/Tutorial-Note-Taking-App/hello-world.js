/* Hello World example using Express.js to get started */
const express = require('express');  // Import the express module
const app = express();  // Create an instance of an Express application
const PORT = 3000;  // Define the port number

// Accepting HTTP GET requests to the root URL
app.get('/', (req, res) => {  // Define a route for the root URL
  res.send('Hello, World!');  // Send a response to the client
});

// Accepting a new HTTP GET request
// req = request, res = response
app.get("/goodbye", (req, res) => {
    res.send("goodbye world!");
});

// Waiting for a request to the root URL
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);  // Callback function when successfully called
});