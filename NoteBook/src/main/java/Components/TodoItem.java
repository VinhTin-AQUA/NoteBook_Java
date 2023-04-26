package Components;

import _utility.Ultility;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TodoItem extends JPanel {
    
    private JTextArea text;
    private JCheckBox  box;
    private int idTodo;
    
    public TodoItem(int id, String text) {
        init();
        this.text.setText(text);
        this.idTodo = id;
        
    }
    
    private void init() {
        Dimension pre = new Dimension(680,200);
        this.setBackground(Color.decode("#FFFEFE"));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.setMinimumSize(pre);
        this.setMaximumSize(pre);
        
        text = new JTextArea(1,28);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(Color.WHITE);
        text.setMinimumSize(pre);
        text.setMaximumSize(pre);
        text.setFont(new Font("Arial",Font.PLAIN, 15));
   
        
        box = new JCheckBox();
        box.setFocusPainted(false);
        ImageIcon icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\un_check.png");
        box.setIcon(icon);
        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\check.png");
        box.setSelectedIcon(icon);
        this.add(box);
        this.add(text);
    }

    public JTextArea getTextArea() {
        return text;
    }

    public void setText(JTextArea text) {
        this.text = text;
    }

    public int getIdTodo() {
        return idTodo;
    }

    public void setIdTodo(int idTodo) {
        this.idTodo = idTodo;
    }

    public JCheckBox getBox() {
        return box;
    }

    public void setBox(JCheckBox box) {
        this.box = box;
    }
}
