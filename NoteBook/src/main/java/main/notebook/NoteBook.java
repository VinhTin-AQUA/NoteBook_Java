package main.notebook;
//

import Components.TodoItem;
import DataConnection.Data;
import Views.App;
import _utility.WrapEditorKit;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class NoteBook {
//

    public static void main(String[] args) {
        Data.loadConnection();
        App a= new App();
        a.setVisible(true);
//
//        test t = new test();
//        t.setVisible(true);

//        JFrame frame = new JFrame("Testing");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JTextArea field = new JTextArea(1, 30);
//        field.setLineWrap(true);
//        field.setWrapStyleWord(true);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.weightx = 1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        TestPane pane = new TestPane();
//        pane.add(field, gbc);
//
//        frame.add(pane);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
    }
}


