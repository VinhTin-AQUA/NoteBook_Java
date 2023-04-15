package Controllers;

import DataConnection.Data;
import Models.Note;
import Models.NoteType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class NoteTypeController {

    // tạo note type hoặc cập nhật note type
    static public void createNoteType(String id, String typeName) {
        typeName = typeName.trim();
        id = id.trim();

        // không thể thao tác với Note Type mặc định có Id = 1
        if (id.equals("1") || typeName.equals("Other")) {
            JOptionPane.showMessageDialog(null, "Cannot change or create default NoteType", "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (typeName.equals("")) {
            return;
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
                e.printStackTrace();
            }
            return;
        }

        // nếu id truyền tới null => chưa tồn tại NoteType
        // => tạo notetype mới
        try {
            // kiểm tra tên note type này đã tồn tại chưa
            query = "SELECT * FROM NoteType WHERE TypeName='" + typeName + "';";
            ps = Data.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery(query);
            if (rs.next() == true) {
                JOptionPane.showMessageDialog(null, "Type Name already exist", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // nếu tên note type chưa tồn tại
            query = "INSERT INTO NoteType(TypeId,TypeName) VALUES(NULL,?);";
            ps = Data.con.prepareStatement(query);
            ps.setString(1, typeName);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load note-type
    static public LinkedList<NoteType> loadNoteTypes() {
        LinkedList<NoteType> noteTypes = new LinkedList<>();
        
        String query = "SELECT *"
                + " FROM NoteType;";

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
                noteTypes.add(type);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Load Faiilure");
        }
        return noteTypes;
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
                + "where TypeId = " + noteType.getId();

        PreparedStatement ps;
        try {
            // kiểm tra note type có note nào không, nếu có thì phải xóa các note đó trước
            ps = Data.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int noteId = rs.getInt("NoteId");
                NoteController.deleteNote(new Note(noteId, "", null, "", null, false,
                        null, null, null));
            }

            query = "DELETE FROM NoteType WHERE TypeId = " + noteType.getId();

            ps = Data.con.prepareStatement(query);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Xoa that bai");
        }
        return false;
    }
}
