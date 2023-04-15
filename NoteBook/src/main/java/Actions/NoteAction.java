
package Actions;

import Models.Note;
import Models.NoteType;
import java.util.LinkedList;

public class NoteAction {
    public static void changeType(LinkedList<Note> curNotes, LinkedList<Note> notes, NoteType type, Note note) {
//        note.setTitle("KKK");
        note.getType().setTypeName(type.getTypeName());
        System.out.println(note);
        System.out.println(type.getTypeName());
    }
}
