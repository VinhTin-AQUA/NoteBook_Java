
package Components;

import Models.Note;
import Views.App;
import _utility.WrapEditorKit;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class NoteItem extends JTextPane{
     public NoteItem(Note _note) {
         init(_note);
     }
     
     private void init(Note _note) {
         // định dạng hiển thị ngày tháng
        DateFormat dateF = new SimpleDateFormat("E, dd-MM-yyyy");
        Font font = new Font("Arial", Font.BOLD, 18);
        // đặt kích thước cố định
        Dimension pre = new Dimension(218, 100);

        // styles
        this.setFont(font);
        if (_note.isPin() == true) {
            this.setBackground(Color.decode("#FDC3E9")); // màu của note được ghim
        } else {
            this.setBackground(Color.decode("#FFFFFF")); // màu của note chưa được ghim
        }
       
        this.setForeground(Color.decode("#333333"));
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.setMargin(new Insets(10, 10, 10, 10));
        this.setBorder(BorderFactory.createLineBorder(Color.decode("#DCD3CB"), 2));
        this.setSize(200, 100);

        // văn bản tự động xuống hàng
        this.setPreferredSize(pre);
        this.setEditorKit(new WrapEditorKit());
        this.setFocusable(false);
        
        
        // style từng đoạn văn bản
        StyledDocument doc = this.getStyledDocument();
        Style style = this.addStyle("mystyle", null);

        try {
            // style title
            StyleConstants.setForeground(style, Color.BLACK);

            // hiển thị title tối đa 20 ký tự trên mỗi item note
            if (_note.getTitle().length() > 20) {
                doc.insertString(doc.getLength(), _note.getTitle().substring(0, 20) + "...\n", style);
            } else {
                doc.insertString(doc.getLength(), _note.getTitle() + "\n", style);
            }
            // style date
            StyleConstants.setBold(style, false);
            StyleConstants.setFontSize(style, 12);
            doc.insertString(doc.getLength(), dateF.format(_note.getDateCreate()) + "\n", style);

            // style NoteType
            StyleConstants.setForeground(style, Color.RED);
            StyleConstants.setBold(style, false);
            StyleConstants.setFontSize(style, 14);
            doc.insertString(doc.getLength(), _note.getType().getTypeName(), style);
        } catch (BadLocationException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
}
