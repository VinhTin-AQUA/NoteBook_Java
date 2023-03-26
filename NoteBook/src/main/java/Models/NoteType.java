
package Models;


public class NoteType {
    private String typeName;
    private int id;

    public NoteType(int id,String typeName ) {
        this.typeName = typeName;
        this.id = id;
    }
    
    public NoteType(NoteType type) {
        this.typeName = type.typeName;
        this.id = type.id;
    }

    public NoteType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getId() {
        return id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setId(int id) {
        this.id = id;
    }
}
