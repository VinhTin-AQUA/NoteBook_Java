package Controllers;

import DataConnection.Data;
import Models.Note;
import Models.Photo;
import Models.TodoList;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class NoteController {

    // tạo note
    public static void createNote(Note note) {
        String query;
        try {
            // luu note
            query = "insert into Note(Title,DateCreate,`Password`, TypeId, pin) "
                    + "values(?, CURDATE(), ?,?,?);";
            PreparedStatement ps = Data.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, note.getTitle());
            ps.setString(2, note.getPassword());
            if (note.getType() != null) {
                ps.setInt(3, note.getType().getId());
            } else {
                ps.setInt(3, 1);
            }
            ps.setBoolean(4, note.isPin());
            ps.executeUpdate();

            // getGeneratedKeys() sẽ trả về một đối tượng ResultSet chứa ID của note vừa được thêm vào database
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);

                if (!note.getTodoList().isEmpty()) {
                    LinkedList<TodoList> countTodoList = note.getTodoList();

                    for (TodoList item : countTodoList) {
                        query = "insert into TodoList(TodoList.Item,TodoList.`check`,TodoList.NoteId) "
                                + "values (?,?,?);";
                        ps = Data.con.prepareStatement(query);
                        ps.setString(1, item.getItem());
                        ps.setBoolean(2, item.isCheck());
                        ps.setInt(3, generatedId);
                        ps.executeUpdate();
                    }
                }
                // kiểm tra có nội dung kèm theo thì lưu nội dung
                else if (note.getContent().getText().equals("") == false) {
                    query = "insert into Content(`Text`,NoteId) values (?, ?);";
                    ps = Data.con.prepareStatement(query);
                    ps.setBytes(1, note.getContent().getText());
                    ps.setInt(2, generatedId);
                    ps.executeUpdate();
                } // nếu k có nội dung thì kiểm tra xem co todo list không
                
                // kiểm tra xem có hình ảnh kèm theo để lưu không
                if (note.getPhotos() != null) {
                    LinkedList<Photo> countPhotos = note.getPhotos();
                    for (Photo item : countPhotos) {
                        query = "insert into Photo(`Data`,NoteId) values(?,?);";
                        ps = Data.con.prepareStatement(query);
                        ps.setBytes(1, item.getData());
                        ps.setInt(2, generatedId);
                        ps.executeUpdate();
                    }
                }
                JOptionPane.showMessageDialog(null, "create note successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "note creation failed");
        }
    }

    // cập nhật note
    public static void updateNote(Note note) {
        try {
            // lưu note
            String query = "Update Note set Title=?,DateCreate=curdate(),TypeId=?,pin=? "
                    + "where NoteId=?";
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setString(1, note.getTitle());
            if (note.getType() != null) {
                ps.setInt(2, note.getType().getId());
            } else {
                ps.setInt(2, 1);
            }
            ps.setBoolean(3, note.isPin());
            ps.setInt(4, note.getNoteId());
            ps.executeUpdate();

            // xóa hết content, photos, todoList cũ
            query = "delete from TodoList where NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            query = "delete from Content where NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            query = "delete from Photo where NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();

            // kiểm tra có nội dung kèm theo thì lưu nội dung
            if (note.getContent().getText() != null) {
                query = "insert into Content(`Text`,NoteId) values (?, ?);";
                ps = Data.con.prepareStatement(query);
                ps.setBytes(1, note.getContent().getText());
                ps.setInt(2, note.getNoteId());
                ps.executeUpdate();
            } // nếu k có nội dung thì kiểm tra xem co todo list không
            else if (note.getTodoList() != null) {
                LinkedList<TodoList> countTodoList = note.getTodoList();
                for (TodoList item : countTodoList) {
                    query = "insert into TodoList(TodoList.Item,TodoList.`check`,TodoList.NoteId) "
                            + "values (?,?,?);";
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
                    query = "insert into Photo(`Data`,NoteId) values(?,?);";
                    ps = Data.con.prepareStatement(query);
                    ps.setBytes(1, item.getData());
                    ps.setInt(2, note.getNoteId());
                    ps.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(null, "update note successfully");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "update note failed");
        }
    }
    
    // xóa note
    public static void deleteNote(Note note) {
        
        try {
            // xóa Content, TodoList, Photo nếu có
            String query = "delete from Photo where NoteId = " + note.getNoteId() + ";";
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            
            query = "delete from TodoList where NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            
            query = "delete from Content where NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            
            // xóa note
            query = "delete from Note where Note.NoteId = " + note.getNoteId() + ";";
            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Delete Note Successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Delete Note Failed");
        }
    }
    
    // set password
    public static void setPassword(Note note, String pass) {
        try {
            String query = "update Note set `Password` = ? where note.NoteId = ?;";
            PreparedStatement ps = Data.con.prepareStatement(query);
            ps.setString(1, pass);
            ps.setInt(2, note.getNoteId());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null,"Set Password Successfully", "",JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null,"Reset Password Successfully", "",JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}