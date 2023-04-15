package Components;

import _utility.Ultility;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Dialog extends JOptionPane {

    private static final ImageIcon icon;
    
    static {
        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\question.png");
    }
    
    public Dialog() {
        super();
    }

    public static int YesNoCancel(String message, String title) {
        String[] options = {"Yes", "No", "Cancel"};
    
        return showOptionDialog(null, message,
                title, JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, icon, options, options[2]);
    }
}
