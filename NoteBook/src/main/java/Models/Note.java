
package Models;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Note {
    private int noteId;
    private String title; 
    private Date dateCreate;
    private String password;
    private NoteType noteType;  
    private boolean pin;
    private Content content;
    private LinkedList<TodoList> todoList;
    private LinkedList<Photo> photos;

    public Note(int noteId, String title, Date dateCreate, String password, 
            NoteType noteType, boolean pin, Content content, LinkedList<TodoList> todoList, LinkedList<Photo> photos) {
        this.noteId = noteId;
        this.title = title;
        this.dateCreate = dateCreate;
        this.password = password;
        this.noteType = noteType;
        this.pin = pin;
        this.content = content;
        this.todoList = new LinkedList<>();
        this.photos = new LinkedList<>();
    }
    
    public Note(Note note) {
        this.noteId = note.noteId;
        this.title = note.title;
        this.dateCreate = note.dateCreate;
        this.password = note.password;
        this.noteType = note.noteType;
        this.pin = note.pin;
        this.content = new Content(note.content);
        this.todoList = new LinkedList<>(note.todoList);
        this.photos = new LinkedList<>(note.photos);
    }

    //get
    public int getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public String getPassword() {
        return password;
    }

    public NoteType getType() {
        return noteType;
    }

    // set
    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(NoteType noteType) {
        this.noteType = noteType;
    }
    
    public void setPin(boolean pin) {
        this.pin = pin;
    }

    public boolean isPin() {
        return pin;
    }

    public Content getContent() {
        return content;
    }

    public LinkedList<TodoList> getTodoList() {
        return todoList;
    }

    public LinkedList<Photo> getPhotos() {
        return photos;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void setTodoList(LinkedList<TodoList> todoList) {
        this.todoList = todoList;
    }

    public void setPhotos(LinkedList<Photo> photos) {
        this.photos = photos;
    }
    
    // them 1 todo vào danh sách
    public void addTodo(TodoList todo) {
        this.todoList.addLast(todo);
    }
    
    // thêm danh sách todoList vào cuối todoList hiện tại
    public void addTodoList(LinkedList<TodoList> todos) {
        this.todoList.addAll(todos);
    }
    
    // thêm 1 tấm ảnh vào danh sách
    public void addPhoto(Photo photo) {
        this.photos.addLast(photo);
    }
    
    // xóa 1 tấm ảnh khỏi danh sách
    public void deletePhoto(int i) {
        this.photos.remove(i);
    }
    
    // xóa 1 todo item
    public void deleteTodoItem(int i) {
        this.todoList.remove(i);
    }
    
    // xóa todolist
    public void clearTodoList(){
        this.todoList.clear();
    }
    
    // xóa content
    public void deleteContent() {
        this.content.clearData();
        this.content.setContentId(-1);
    }
    
    // xóa danh sách hình ảnh
    public void clearPhotos() {
        this.photos.clear();
    }
}
