
package Components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;

public class NoteTypeItem extends JTextField{
    public NoteTypeItem(String name){
        super(name);
        init();
    }
    
    private void init() {
        Font font = new Font("Dialog", Font.BOLD, 18);
        this.setFocusable(false);
        // styles
        this.setFont(font);
        this.setSize(178, 60);
        this.setBackground(Color.decode("#FFFFFF"));
        this.setForeground(Color.decode("#333333"));
        this.setSelectionColor(Color.decode("#DCD3CB"));
        this.setCursor(Cursor.getDefaultCursor());

        this.setName("-1");
        // đặt lích thước cố định
        Dimension pre = new Dimension(178, 60);
        this.setPreferredSize(pre);
        this.setMinimumSize(pre);
        this.setMaximumSize(pre);
    }
}
