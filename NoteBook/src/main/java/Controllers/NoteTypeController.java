package Controllers;

import DataConnection.Data;
import Models.Content;
import Models.Note;
import Models.NoteType;
import Models.NoteTypeNote;
import Models.Photo;
import Models.TodoList;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class NoteTypeController {

    // tạo note type hoặc cập nhật note type
    static public String createNoteType(String id, String typeName) {
        typeName = typeName.trim();
        id = id.trim();

        // không thể thao tác với Note Type mặc định có Id = 1
        if (id.equals("1") || typeName.equals("Other")) {
            return "Cannot change or create default NoteType";
        }
        
        if(typeName.equals("")) {
            return "Type Name cannot NULL";
        }

        String query = "";
        PreparedStatement ps = null;

        // nếu id truền tới != "-1", có nghĩa id đã tồn tại => noteType đã tồn tại
        // => chỉ đổi tên
        if (id.equals("-1") == false) {
            int typeId = Integer.parseInt(id);// id mang kieu chuoi, truyen toi thi set lai kieu int, do id trong databases kieu int

            try {
                query = "UPDATE NoteType SET TypeName = ? WHERE TypeId = ?";
                ps = Data.con.prepareStatement(query);

                ps.setString(1, typeName);
                ps.setInt(2, typeId);
                ps.executeUpdate();

            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, "" + e);
                return "Error" + e;

            }
            return "Change TypeName Successful";
        }

        // nếu id truyền tới null => chưa tồn tại NoteType
        // => tạo notetype mới
        try {
            // kiểm tra tên note type này đã tồn tại chưa
            query = "select * from NoteType where TypeName='" + typeName + "';";
            ps = Data.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery(query);
            if (rs.next()) {
                return "Type Name already exist";
            }

            // nếu tên note type chưa tồn tại
            query = "insert into NoteType(TypeId,TypeName) values(null,?);";
            ps = Data.con.prepareStatement(query);
            ps.setString(1, typeName);

            ps.executeUpdate();
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "" + e);
            e.printStackTrace();
            return "Error" + e;
        }
        return "Create Note Type successfull";
    }

    // load note-type, khi load thì load luôn các note của mỗi type
    static public LinkedList<NoteTypeNote> loadNoteTypes() {
        LinkedList<NoteTypeNote> listNoteTypeNote = new LinkedList<>();
        LinkedList<Note> notes = new LinkedList<>();
        String query = "select NoteType.TypeId,"
                + " NoteType.TypeName"
                + " from NoteType;";

        try {
            PreparedStatement ps = Data.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            NoteType type = null;

            int typeIdNoteType;
            String typeName;

            while (rs.next()) {
                typeIdNoteType = rs.getInt("TypeId");
                typeName = rs.getString("TypeName");
                type = new NoteType(typeIdNoteType, typeName);
                notes = loadNote(type);
                NoteTypeNote noteTypeNote = new NoteTypeNote(type, notes);
                listNoteTypeNote.add(noteTypeNote);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Load Faiilure");
        }
        return listNoteTypeNote;
    }

    // load danh sách notes của 1 NoteType
    private static LinkedList<Note> loadNote(NoteType noteType) {
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
            String query = "select * from note where note.TypeId = " + noteType.getId();
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

            for (Note note : notes) {
                // kiểm tra Note này có content không
                query = "select * from Content where Content.NoteId = " + note.getNoteId();
                ps = Data.con.prepareStatement(query);
                rs = ps.executeQuery();
                if (rs.next()) {
                    content.setContentId(rs.getInt("ContentId"));
                    content.setText(rs.getBytes("Text"));
                }

                // kiểm tra note có chứa hình ảnh không
                query = "select * from Photo where Photo.NoteId = " + note.getNoteId();
                ps = Data.con.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    photoId = rs.getInt("PhotoId");
                    data = rs.getBytes("Data");
                    Photo photo = new Photo(photoId, data);
                    photos.add(photo);
                }

                // kiểm tra có todolist không
                query = "select * from TodoList where TodoList.NoteId = " + note.getNoteId();
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

    // xóa notetype
    public static boolean deleteNoteType(NoteType noteType) {

        if (noteType == null) {
            return false;
        }

        if (noteType.getId() == 1) {
            JOptionPane.showMessageDialog(null, "Cannot delete default NoteType");
            return false;
        }

        String query = "select * "
                + "from Note "
                + "left outer join NoteType on NoteType.TypeId = Note.TypeId "
                + "where NoteType.TypeId = " + noteType.getId();

        PreparedStatement ps;
        try {
            // kiểm tra note type có note nào không, nếu có thì phải xóa các note đó trước
            ps = Data.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                query = "delete from Note where TypeId = " + noteType.getId();
                ps = Data.con.prepareStatement(query);
                ps.executeUpdate();
            }

            query = "delete from NoteType where TypeId = " + noteType.getId();

            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Xoa thanh cong");
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Xoa that bai");
        }
        return false;
    }
}
