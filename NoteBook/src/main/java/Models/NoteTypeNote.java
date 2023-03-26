
package Models;

import java.util.LinkedList;


public class NoteTypeNote {
    private NoteType noteType;
    private LinkedList<Note> notes;

    public NoteTypeNote(NoteType noteType, LinkedList<Note> notes) {
        this.noteType = noteType;
        this.notes = notes;
    }
    
    public NoteTypeNote(NoteTypeNote noteTypeNote) {
        this.noteType = noteTypeNote.noteType;
        this.notes = noteTypeNote.notes;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public LinkedList<Note> getNotes() {
        return notes;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public void setNotes(LinkedList<Note> notes) {
        this.notes = notes;
    }
}
