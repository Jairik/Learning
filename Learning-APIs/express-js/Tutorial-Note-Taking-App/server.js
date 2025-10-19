/* Server.js - The entry point to the application */
const express = require('express');  // Requiring the express dependency
const app = express();  // Creating an express object "app"
const database = require("./database")
const port = 8080;  // Constant, unprivaledged port
app.set("view engine", "ejs"); // Telling express to use ejs as templating 

// GET example of returning data from server
app.get('/', (req, res) => {
    res.render("index.ejs", {
        numberOfIterations: 50  // Object literal to pass to ejs file
    })
});


// Starting a new route that shows all of the notes
app.get("/notes", (req, res) => {
    const notes = database.getNotes()
    res.render("notes.ejs", {
        notes,
    })
});

// New http request to return note
// Note: the :id refers to whatever is referenced in the request kiond of
app.get("/notes/:id", (req, res) => {
    const id = +req.params.id;  // + prefix makes js try to treat it as a number
    const note = database.getNote(id);
    if(!note){
        res.status(404).render("note404.ejs");
        return;
    }
    res.render("singleNote.ejs", {
        note, 
    });
})

// Telling express to serve these static files when requested for them
app.use(express.static("public"));

// Listens for a request
app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`);
})