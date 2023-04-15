package Controllers;

import DataConnection.Data;
import Models.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class NoteController {

    // load danh sách notes
    public static LinkedList<Note> loadNotes(LinkedList<NoteType> noteTypes) {
        LinkedList<Note> notes = new LinkedList<>();
        Content content = new Content(-1, null);
        LinkedList<Photo> photos = new LinkedList<>();
        LinkedList<TodoList> todoList = new LinkedList<>();

        // note
        int noteId;
        String title;
        Date dateCreate;
        String password;
        boolean pin;

        // Photo
        int photoId;
        byte[] data;

        // todoList
        int todoListId;
        String item;
        boolean check;

        try {
            for (NoteType noteType : noteTypes) {
                String query = "SELECT * FROM note WHERE note.TypeId = " + noteType.getId();
                PreparedStatement ps = Data.con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    // giá trị thuộc tính của mỗi note
                    noteId = rs.getInt("NoteId");
                    title = rs.getString("Title");
                    dateCreate = rs.getDate("DateCreate");
                    password = rs.getString("Password");
                    pin = rs.getBoolean("pin");
                    Note note = new Note(noteId, title, dateCreate, password, noteType, pin, null, null, null);

                    notes.add(note);
                }
            }
            
            for (Note note : notes) {
                    // kiểm tra Note này có content không
                    String query = "SELECT * FROM Content WHERE Content.NoteId = " + note.getNoteId();
                    PreparedStatement ps = Data.con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        content.setContentId(rs.getInt("ContentId"));
                        content.setText(rs.getBytes("Text"));
                    }

                    // kiểm tra note có chứa hình ảnh không
                    query = "SELECT * FROM Photo WHERE Photo.NoteId = " + note.getNoteId();
                    ps = Data.con.prepareStatement(query);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        photoId = rs.getInt("PhotoId");
                        data = rs.getBytes("Data");
                        Photo photo = new Photo(photoId, data);
                        photos.add(photo);
                    }

                    // kiểm tra có todolist không
                    query = "SELECT * FROM TodoList WHERE TodoList.NoteId = " + note.getNoteId();
                    ps = Data.con.prepareStatement(query);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        todoListId = rs.getInt("TodoListId");
                        item = rs.getString("Item");
                        check = rs.getBoolean("Check");
                        TodoList todo = new TodoList(todoListId, item, check);
                        todoList.add(todo);
                    }
                    note.setContent(new Content(content));

                    // tạo ra vùng nhớ mới để lưu danh sách todoList và Photos 
                    // để khi xóa danh sách cũ thì giá trị trong note không bị biến mất
                    LinkedList<Photo> tempPhotos = new LinkedList<>(photos);
                    note.setPhotos(tempPhotos);
                    LinkedList<TodoList> tempTodoList = new LinkedList<>(todoList);
                    note.setTodoList(tempTodoList);

                    // xóa danh sách cũ để thêm danh sách mới vào
                    photos.clear();
                    todoList.clear();
                    content = new Content(-1, null);
                }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return notes;
    }

    // tạo note                                                                                                                                                     
    public static int createNote(Note note, String typeName) {
        String query;
        int generatedId;
        try {
            // luu note
            query = "INSERT INTO Note(Title,DateCreate,`Password`, TypeId, pin) "
                    + "VALUES(?, CURDATE(), ?,?,?);";
            PreparedStatement ps = Data.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, note.getTitle());

            ps.setString(2, note.getPassword());
            ps.setInt(3, getTypeId(typeName));
            ps.setBoolean(4, note.isPin());
            ps.executeUpdate();

            // getGeneratedKeys() sẽ trả về một đối tượng ResultSet chứa ID của note vừa được thêm vào database
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
                if (!note.getTodoList().isEmpty()) {
                    LinkedList<TodoList> countTodoList = note.getTodoList();

                    for (TodoList item : countTodoList) {
                        query = "INSERT INTO TodoList(TodoList.Item,TodoList.`check`,TodoList.NoteId) "
                                + "VALUES (?,?,?);";
                        ps = Data.con.prepareStatement(query);
                        ps.setString(1, item.getItem());
                        ps.setBoolean(2, item.isCheck());
                        ps.setInt(3, generatedId);
                        ps.executeUpdate();
                    }
                } // kiểm tra có nội dung kèm theo thì lưu nội dung
                else if (note.getContent().getText().equals("") == false) {
                    query = "INSERT INTO Content(`Text`,NoteId) VALUES (?, ?);";
                    ps = Data.con.prepareStatement(query);
                    ps.setBytes(1, note.getContent().getText());
                    ps.setInt(2, generatedId);
                    ps.executeUpdate();
                } // nếu k có nội dung thì kiểm tra xem co todo list không

                // kiểm tra xem có hình ảnh kèm theo để lưu không
                if (note.getPhotos() != null) {
                    LinkedList<Photo> countPhotos = note.getPhotos();
                    for (Photo item : countPhotos) {
                        query = "INSERT INTO Photo(`Data`,NoteId) VALUES(?,?);";
                        ps = Data.con.prepareStatement(query);
                        ps.setBytes(1, item.getData());
                        ps.setInt(2, generatedId);
                        ps.executeUpdate();
                    }
                }
//                JOptionPane.showMessageDialog(null, "create note successfully");
                return generatedId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "note creation failed");
        }
        return -1;
    }

    // cập nhật note
    public static void updateNote(Note note, String typeName) {
        try {
            // lưu note
            String query = "UPDATE Note SET Title=?,DateCreate=curdate(),TypeId=?                                                                     "
                    + "WHERE NoteId=?";
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setString(1, note.getTitle());
            ps.setInt(2, getTypeId(typeName));
            ps.setInt(3, note.getNoteId());
            ps.executeUpdate();

            // xóa hết content, photos, todoList cũ
            query = "DELETE FROM TodoList WHERE NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            query = "DELETE FROM Content WHERE NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            query = "DELETE FROM Photo WHERE NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();

            // kiểm tra có nội dung kèm theo thì lưu nội dung
            if (note.getContent().getText() != null) {
                query = "INSERT INTO Content(`Text`,NoteId) VALUES (?, ?);";
                ps = Data.con.prepareStatement(query);
                ps.setBytes(1, note.getContent().getText());
                ps.setInt(2, note.getNoteId());
                ps.executeUpdate();
            } // nếu k có nội dung thì kiểm tra xem co todo list không
            else if (note.getTodoList() != null) {
                LinkedList<TodoList> countTodoList = note.getTodoList();
                for (TodoList item : countTodoList) {
                    query = "INSERT INTO TodoList(TodoList.Item,TodoList.`check`,TodoList.NoteId) "
                            + "VALUES (?,?,?);";
                    ps = Data.con.prepareStatement(query);
                    ps.setString(1, item.getItem());
                    ps.setBoolean(2, item.isCheck());
                    ps.setInt(3, note.getNoteId());
                    ps.executeUpdate();
                }
            }
            // kiểm tra xem có hình ảnh kèm theo để lưu không
            if (note.getPhotos() != null) {
                LinkedList<Photo> countPhotos = note.getPhotos();
                for (Photo item : countPhotos) {
                    query = "INSERT INTO Photo(`Data`,NoteId) VALUES(?,?);";
                    ps = Data.con.prepareStatement(query);
                    ps.setBytes(1, item.getData());
                    ps.setInt(2, note.getNoteId());
                    ps.executeUpdate();
                }
            }
//            JOptionPane.showMessageDialog(null, "update note successfully");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "update note failed");
        }
    }

    // xóa note
    public static void deleteNote(Note note) {

        try {
            // xóa Content, TodoList, Photo nếu có
            String query = "DELETE FROM Photo WHERE NoteId = " + note.getNoteId() + ";";
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.executeUpdate();

            query = "DELETE FROM TodoList WHERE NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();

            query = "DELETE FROM Content WHERE NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();

            // xóa note
            query = "DELETE FROM Note WHERE Note.NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();

//            JOptionPane.showMessageDialog(null, "Delete Note Successfully");
        } catch (Exception e) {
            e.printStackTrace();// neu co loi thi in ra thong bao
            JOptionPane.showMessageDialog(null, "Delete Note Failed");
        }
    }

    // set password
    public static void setPassword(Note note, String pass) {
        try {
            String query = "UPDATE Note SET `Password` = ? WHERE note.NoteId = ?;";
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setString(1, pass);
            ps.setInt(2, note.getNoteId());
            ps.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Set Password Successfully", "", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // reset Password
    public static void resetPassword(Note note) {
        try {
            String query = "update Note set `Password` = ? where note.NoteId = ?;";
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setString(1, "");
            ps.setInt(2, note.getNoteId());
            ps.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Reset Password Successfully", "", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // thiết lập noteType cho note
    public static void setType(int typeId, int noteId) {
        String query = "UPDATE Note SET TypeId = ? WHERE NoteId = ?;";
        try {
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setInt(1, typeId);
            ps.setInt(2, noteId);
            ps.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Change Type Successfully", "", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Change Type Failed", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // get TypeId
    private static int getTypeId(String name) {
        String query = "SELECT TypeId FROM NoteType WHERE TypeName=?;";
        try {
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return rs.getInt("TypeId");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    // pin note
    public static void pinNote(Note note) {
        String query = "UPDATE note SET pin=? WHERE note.NoteId = ?;";
        try {
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setBoolean(1, !note.isPin());
            ps.setInt(2, note.getNoteId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(NoteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
