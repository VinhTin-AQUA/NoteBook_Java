
package DataConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Data {
    public static Connection con = null;

    public static void loadConnection() {
        String url = "jdbc:mysql://localhost:3306/notebook";
        String root = "root"; // tên connection
        String pass = "root"; // mật khẩu connection

        try {
            con = DriverManager.getConnection(url, root, pass);
            if(con != null) {
//                JOptionPane.showMessageDialog(null,  "Database has been successfully connected");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in database loading" + e);
        }
    }
}
