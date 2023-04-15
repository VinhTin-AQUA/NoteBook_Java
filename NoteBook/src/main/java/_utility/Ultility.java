
package _utility;

import Views.App;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.StyledDocument;


public class Ultility {
    
    public static String path = System.getProperty("user.dir");
    
    static{path = path.replace("\\", "\\\\");}
    
    // hàm kiểm tra chứa ký tụ unicode khi nhập mật khẩu
    public static boolean containsUnicode(String input) {
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 127) {
                return true;
            }
        }
        return false;
    }
    
    // chuyển văn bản sang mảng byte
    public static byte[] toByteArray(StyledDocument doc, String text) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(out);
            oos.writeObject(doc);
            byte[] data = out.toByteArray();
//            SaveTextColor.saveTextColor(data);
            return data;
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
