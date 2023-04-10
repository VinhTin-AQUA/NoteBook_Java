package Controllers;

import DataConnection.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThemeController {

    public static boolean getThemMode() {
        
        try {
            String query = "SELECT dark_mode FROM ThemeMode;";
            PreparedStatement ps;
            
            ps = Data.con.prepareStatement(query);
           
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getBoolean("dark_mode");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ThemeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static void saveThemMode(boolean darkMode ) {
        try {
            String query = "UPDATE ThemeMode SET dark_mode = ? WHERE id = 1;";
            PreparedStatement ps;
            
            ps = Data.con.prepareStatement(query);
            ps.setBoolean(1, darkMode);
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(ThemeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
