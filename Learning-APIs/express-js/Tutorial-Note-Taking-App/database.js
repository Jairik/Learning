// Declaring a mock notes array
const notes = [{
    id: 1,
    title: "My First Note",
    timestamp: Date.now(),
    contents: "Hello this is my first note"
}, 
{
    id: 2,
    title: "Second note",
    timestamp: Date.now(),
    contents: "Second note contents here and stuff"
}];

function getNotes(){
    return notes;
}
exports.getNotes = getNotes

function getNote(id){
    return notes.find(note => note.id === id);
}
exports.getNote = getNote

function addNote(note){

}
exports.addNote = addNote

function deleteNote(id){

}
exports.deleteNote = deleteNote
