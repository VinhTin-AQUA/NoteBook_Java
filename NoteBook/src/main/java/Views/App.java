package Views;

import Controllers.NoteController;
import Controllers.NoteTypeController;
import Models.Content;
import Models.Note;
import Models.NoteType;
import Models.NoteTypeNote;
import Models.Photo;
import Models.TodoList;
import _utility.WrapEditorKit;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class App extends javax.swing.JFrame {

    private String path = System.getProperty("user.dir");
    private CardLayout cardLayout;

    private static int indexPhoto = -1; // điều khiền hiển thị hình ảnh qua lại khi soạn thảo
    private static boolean checkClickToggleBtn = false; // nếu bấm nút chuyển văn bản sang todoList thì set True

    private LinkedList<NoteTypeNote> noteTypeNotes; // danh sách noteType
    private JPopupMenu contxtMenuNoteType; // menu chuột phải của mỗi notetype item
    private JPopupMenu contxtMenuNote; // menu chuột phải của mỗi note
    private JPopupMenu contxtMenuPhoto; // menu chuột phải của mỗi note
    private JPopupMenu contxtMenuTodoItem; // menu chuột phải của mỗi note

    private NoteType noteType; // biến tạm lưu thông tin noteType cần xóa
    private JTextField textFieldTemp; // biến tạm để lưu đối tượng JTextField khi xử lý sự kiện chuột phải

    private String fileName = ""; // đường dẫn tuyệt đối của hình ảnh tải lên
    private String format = ""; // định dạng hình ảnh

    private Note note; // chứa note cần chỉnh sửa hoặc lưu

    // điều chỉnh hiển thị todoList và Content
    private JScrollPane scrollPane;
    private JTextPane textPane;
    private JPanel pane;
    private int todoItemSelect;
// ========================================================================================= khởi tạo

    public App() {
        initComponents();

        ImageIcon logo = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\notebook.png");
        this.setIconImage(logo.getImage());
        this.setSize(1200, 700);
        this.setTitle("NOTEBOOK");
        this.setLocationRelativeTo(null);
        combo.setBackground(Color.WHITE);
        combo.setOpaque(true);
        initComponents2();
        icon();
        initPopupMenu();
    }

    private void initComponents2() {
        path = path.replace("\\", "\\\\");
        cardLayout = (CardLayout) jPanel1.getLayout();

        note = new Note(-1, "", null, "", null, false, new Content(-1,
                new byte[]{}), new LinkedList<TodoList>(), new LinkedList<Photo>());
        loadNoteTypes(); // load noteType

        jTextPane2.setEditorKit(new WrapEditorKit());
        scrollPane = jScrollPane3;
        textPane = jTextPane2;
        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setSize(427, 427);
        pane.setBackground(Color.decode("#CCFFCC"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // không cho chỉnh sửa item
        combo.setEditable(false);
    }

    private void icon() {
        //path = path.replace("\\", "\\\\");
        ImageIcon icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\left.png");
        left.setIcon(icon);

        jToolBar1.setLayout(new GridLayout(1, 11, 10, 10));
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\save.png");
        jButton1.setIcon(icon);
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\image.png");
        jButton2.setIcon(icon);
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\tick.png");
        tick.setIcon(icon);

        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\pass.png");
        jButton6.setIcon(icon);
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\fill.png");
        jButton7.setIcon(icon);
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\bold.png");
        jButton8.setIcon(icon);
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\italic.png");
        jButton9.setIcon(icon);
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\underline.png");
        jButton10.setIcon(icon);

        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\add.png");
        newnode.setIcon(icon);
        newtype.setIcon(icon);

        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\pre.png");
        jButton3.setIcon(icon);
        icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\next.png");
        jButton4.setIcon(icon);
        // "C:\Users\tinho\Desktop\NoteBook_Java\NoteBook\src\main\java\icon\next.png"
        // "C:\Users\tinho\Desktop\NoteBook_Java\NoteBook\src\main\java\icon\pre.png"

    }

    // khởi tạp menu chuột phải
    private void initPopupMenu() {
        contxtMenuNoteType = new JPopupMenu(); // menu cua note typr
        contxtMenuNote = new JPopupMenu(); // menu cua note
        contxtMenuPhoto = new JPopupMenu(); // menu khi chon anh
        contxtMenuTodoItem = new JPopupMenu(); // menu khi lam viec voi todolist

        // menu chuột phải của notetype item
        JMenuItem reNameNoteType = new JMenuItem("Rename");
        reNameNoteType.addActionListener((ActionEvent e) -> {// đổi tên noteType
            textFieldTemp.setFocusable(true);
            textFieldTemp.selectAll(); // bôi đen toàn bộ văn bản
            textFieldTemp.requestFocusInWindow(); // con trỏ chuột tự động focus vào jtext field
        });
        JMenuItem deleteNoteType = new JMenuItem("Delete");// xóa noteType
        deleteNoteType.addActionListener((ActionEvent e) -> {
            boolean checkDelete = NoteTypeController.deleteNoteType(noteType);
            loadNoteTypes();
        });
        contxtMenuNoteType.add(reNameNoteType);
        contxtMenuNoteType.add(deleteNoteType);

        // menu chuột phải của mỗi note
        JMenuItem deleteNote = new JMenuItem("Delete"); // xóa note
        deleteNote.addActionListener((ActionEvent e) -> {
            NoteController.deleteNote(this.note);
            loadNoteTypes();
            resetNote();
            jPanel5.repaint();
            jPanel5.revalidate();
        });
        JMenuItem setPassword = new JMenuItem("Set Password"); // set passwod
        setPassword.addActionListener((ActionEvent e) -> {
            java.awt.event.ActionEvent c = null;
            jButton6ActionPerformed(c);
            loadNoteTypes();
            resetNote();
            jPanel5.repaint();
            jPanel5.revalidate();
        });

        JMenuItem deletePassword = new JMenuItem("Reset Password"); // xóa mật khẩu Password
        deletePassword.addActionListener((ActionEvent e) -> {
            String pass = enterPassword();
            if (pass.equals("")) {

            } else if (pass.equals(this.note.getPassword()) == true) {
                NoteController.resetPassword(note);
                loadNoteTypes();
                resetNote();
                jPanel5.repaint();
                jPanel5.revalidate();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Password", "Confirm Password", JOptionPane.WARNING_MESSAGE);
            }
        });
        contxtMenuNote.add(deleteNote);
        contxtMenuNote.add(setPassword);
        contxtMenuNote.add(deletePassword);

        // menu chuột phải của mỗi hình ảnh hiển thị trên label
        JMenuItem deletePhoto = new JMenuItem("Delete"); // xóa hình ảnh
        deletePhoto.addActionListener((ActionEvent e) -> {
            this.note.deletePhoto(indexPhoto);

            if (this.note.getPhotos().isEmpty()) {
                jLabel7.setIcon(null);
                jLabel7.setText("No Image");
                indexPhoto = -1;
            } else if (indexPhoto == 0 && this.note.getPhotos().isEmpty() == false) {
                Photo photo = this.note.getPhotos().get(indexPhoto);
                showPhotos(photo);
            } else if (indexPhoto <= (this.note.getPhotos().size() - 1)) {
                Photo photo = this.note.getPhotos().get(indexPhoto);
                showPhotos(photo);
            } else if (indexPhoto > (this.note.getPhotos().size() - 1)) {
                indexPhoto--;
                Photo photo = this.note.getPhotos().get(indexPhoto);
                showPhotos(photo);
            }
        });
        contxtMenuPhoto.add(deletePhoto);

        // menu chuột phải của mỗi Todo Item
        JMenuItem deleteTodoItem = new JMenuItem("Delete"); // xóa TodoItem
        deleteTodoItem.addActionListener((ActionEvent evt) -> {
            this.note.deleteTodoItem(todoItemSelect);
            showTodoList(this.note.getTodoList());
        });
        contxtMenuTodoItem.add(deleteTodoItem);
    }

    // hàm kiểm tra chứa ký tụ unicode
    private static boolean containsUnicode(String input) {
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 127) {
                return true;
            }
        }
        return false;
    }

// ====================================================================================== thiết lập
    // thiết lập của các item NoteType
    private void initItemNoteType(JTextField textField) {
        Font font = new Font("Dialog", Font.BOLD, 18);
        textField.setFocusable(false);
        // styles
        textField.setFont(font);
        textField.setSize(178, 60);
        textField.setBackground(Color.decode("#CCCCFF"));
        textField.setForeground(Color.decode("#333333"));
        textField.setSelectionColor(Color.decode("#66FFCC"));
        textField.setCursor(Cursor.getDefaultCursor());

//        textField.setName("-1");
        // đặt lích thước cố định
        Dimension pre = new Dimension(178, 60);
        textField.setPreferredSize(pre);
        textField.setMinimumSize(pre);
        textField.setMaximumSize(pre);

        // sự kiện chuột
        textField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { // click chuột
                jTextField1MouseClicked(evt);
            }
        });
        // sự kiện focus
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent evt) {
                // TODO Auto-generated method stub
            }

            @Override
            public void focusLost(FocusEvent evt) {
                unfocus(evt);
            }
        });
        // sự kiện nhấn phím
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
    }

    // clear text
    private void clearDetail() {
        jTextField1.setText("Title");
        jLabel7.setIcon(null);
        if (this.note.getPhotos().isEmpty() == false) {
            jLabel7.setText("");
        } else {
            jLabel7.setText("No Image");
        }
        indexPhoto = -1;
        tick.setSelected(false);

        textPane.setText("");
        scrollPane.setViewportView(textPane);
        jPanel17.removeAll();
        jPanel17.add(scrollPane);
        jPanel17.revalidate();
        jPanel17.repaint();
    }

    // khởi tạo lại note sau mỗi lần lưu
    private void resetNote() {
        note.setNoteId(-1);
        note.setTitle("");
        note.setPassword("");
        note.setType(null);
        note.setPin(false);
        note.deleteContent();
        note.clearTodoList();
        note.clearPhotos();
    }

    // chuyển văn bản sang mảng byte
    public byte[] toByteArray(StyledDocument doc, String text) {
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

    // thiết lập hiển thị todoList
    private void setTodoListView() {
        scrollPane.setViewportView(pane);
        jPanel17.removeAll();
        jPanel17.add(scrollPane);
        pane.repaint();
        pane.revalidate();
        jPanel17.revalidate();
        jPanel17.repaint();
    }

    // thiets lập hiển thị văn bản
    private void setContentView() {
        scrollPane.setViewportView(textPane);
        jPanel17.removeAll();
        jPanel17.add(scrollPane);
        jPanel17.revalidate();
        jPanel17.repaint();
    }

    // hiển thị todoList ra view
    private void showTodoList(LinkedList<TodoList> todos) {
        pane.removeAll();
        int i = 0;
        for (TodoList item : todos) {
            // lấy thông tin của từng todo gắn vào check box
            JCheckBox box = new JCheckBox();
            box.setText("<html><body style='width: " + (jTextPane2.getWidth() - 108) + "px;'><div style='overflow-wrap: break-word;'>" + item.getItem() + "</div></body></html>");

            // style
            box.setFont(new Font("Arial", Font.PLAIN, 18));
            box.setBackground(Color.decode("#CCFFCC"));

            // margin
            box.setMargin(new Insets(7, 0, 7, 10));

            // checked
            box.setSelected(item.isCheck());
            box.setName(Integer.toString(i));
            i++;

            // set icon khi selected và un-selected
            ImageIcon icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\un_check.png");
            box.setIcon(icon);
            icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\check.png");
            box.setSelectedIcon(icon);

            // tạo 1 item todo
            // nếu item được tích thì check = true và ngược lại
            box.addActionListener((ActionEvent e) -> {
                if (box.isSelected()) {
                    item.setCheck(true);
                } else {
                    item.setCheck(false);
                }
            });
            // xóa 1 item todo
            box.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) { // click chuột
                    todoItemMouseClicked(evt);
                }
            });
            pane.add(box);
        }
        // ô nhập thêm 1 item của todolist
        JTextField addTodo = new JTextField("Add");
        Dimension pre = new Dimension(900, 40);
        addTodo.setMaximumSize(pre);
        addTodo.setMaximumSize(pre);
        addTodo.setBorder(new EmptyBorder(10, 10, 10, 10));
        addTodo.setFont(new Font("Arial", Font.PLAIN, 18));

        // sự kiện bấm enter để tạo thêm 1 item của todo
        addTodo.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addTodoKeyPressed(evt);
            }
        });
        pane.add(addTodo);
        setTodoListView();
    }

    // hiển thị hình ảnh ra view
    private void showPhotos(Photo photo) {
        ImageIcon icon = new ImageIcon(photo.getData());
        Image imageTemp = icon.getImage();
        int w = imageTemp.getWidth(null);
        int h = imageTemp.getHeight(null);
        ImageIcon scaledIcon = null;
        if (w > h) {
            Image scaledImage = imageTemp.getScaledInstance(380, 380 * h / w, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(scaledImage);
        } else if (w < h) {
            Image scaledImage = imageTemp.getScaledInstance(380 * w / h, 380, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(scaledImage);
        } else {
            Image scaledImage = imageTemp.getScaledInstance(380, 380, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(scaledImage);
        }
        jLabel7.setIcon(scaledIcon);
    }

    // load văn bản từ mảng byte sang văn bản
    private void loadText(byte[] data) {
        try {
            if (data != null) {
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(in);
                StyledDocument doc = (StyledDocument) ois.readObject();
                textPane.setStyledDocument(doc);
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // reset văn bản trong JTExtPaine
    private void resetText() {

        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("My Style", null);

        StyleConstants.setForeground(style, Color.BLACK); // màu chữ
        doc.setCharacterAttributes(0, doc.getLength(), style, false);
        
        StyleConstants.setBackground(style, Color.WHITE); // hightlight
        doc.setCharacterAttributes(0, doc.getLength(), style, false);

        StyleConstants.setBold(style, false);// bold
        doc.setCharacterAttributes(0, doc.getLength(), style, false);
        
        StyleConstants.setItalic(style, false);// in nghiêng
        doc.setCharacterAttributes(0, doc.getLength(), style, false);
        
        StyleConstants.setUnderline(style, false);// gạch chân
        doc.setCharacterAttributes(0, doc.getLength(), style, false);
        
 
        System.out.println("vao");

    }

// ====================================================================================== sự kiện
    // unfocus textfield của NoteType Item
    private void unfocus(FocusEvent evt) {
        JTextField textField = (JTextField) evt.getSource();
        textField.setFocusable(false);
        textField.setCaretPosition(0); // bỏ bôi đen văn bản
    }

    // sự kiện chuột vào mỗi noteType
    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {
        JTextField jtextField = (JTextField) evt.getSource();
        textFieldTemp = jtextField;

        if (evt.getButton() == MouseEvent.BUTTON3) { // click chuot phai
            contxtMenuNoteType.show(evt.getComponent(), evt.getX(), evt.getY());
            jtextField.setFocusable(true);
            // trường hợp bấm nhiều lần new note mà không lưu vào database
            try {
                noteType = new NoteType(Integer.parseInt(jtextField.getName()), jtextField.getText());
            } catch (Exception e) {
                noteType = null;
            }
        }
        if (evt.getButton() == MouseEvent.BUTTON1) { // đổi tên note type
            jtextField.selectAll();
            jtextField.requestFocusInWindow();
            jtextField.setFocusable(true);
        }
    }

    // sự kiện nhấn phím enter của notetype
    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {
        JTextField jtextField = (JTextField) evt.getSource();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String notice = NoteTypeController.createNoteType(jtextField.getName(), jtextField.getText());
            loadNoteTypes();
            JOptionPane.showMessageDialog(null, notice);
            jtextField.setFocusable(false);
        }
    }

    // sự kiện chuột của mỗi Note
    private void noteEvent(Note note, java.awt.event.MouseEvent evt) {
        this.note = new Note(note);
        clearDetail();
        // show note
        if (evt.getButton() == MouseEvent.BUTTON1) { // click chuot trai để xem chi tiết
            // nếu có mật khẩu thì phải nhập mật khẩu mới được xem
            if (this.note.getPassword().equals("") == false) {
                String pass = enterPassword();
                if (pass.equals("")) {
                    // không làm gì
                } else if (this.note.getPassword().equals(pass) == true) { // nhập đúng mật khẩu
                    cardLayout.show(jPanel1, "text"); // hiển thị trang text
                    jTextField1.setText(note.getTitle()); // set title
                    if (note.getPhotos().isEmpty() == false) { // nếu có hình ảnh thì hiển thị
                        indexPhoto++;
                        showPhotos(note.getPhotos().get(indexPhoto));
                    }
                    if (note.getTodoList().isEmpty() == false) { // nếu có todolist thì hiển thị
                        tick.setSelected(true);
                        showTodoList(note.getTodoList());
                    } else if (note.getContent() != null) { // ngược lại hiển thị văn bản
                        tick.setSelected(false);
                        setContentView();
                        loadText(note.getContent().getText()); // load văn bản
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password", "", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                cardLayout.show(jPanel1, "text"); // hiển thị trang text
                jTextField1.setText(note.getTitle()); // set title
                if (note.getPhotos().isEmpty() == false) { // nếu có hình ảnh thì hiển thị
                    indexPhoto++;
                    showPhotos(note.getPhotos().get(indexPhoto));
                }
                if (note.getTodoList().isEmpty() == false) { // nếu có todolist thì hiển thị
                    tick.setSelected(true);
                    showTodoList(note.getTodoList());
                } else if (note.getContent() != null) { // ngược lại hiển thị văn bản
                    tick.setSelected(false);
                    setContentView();
                    loadText(note.getContent().getText()); // load văn bản
                }
            }
        }
        if (evt.getButton() == MouseEvent.BUTTON3) { // click chuột phải để xem Popup
            this.note = new Note(note);
            contxtMenuNote.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    // sự kiện click chuột của ô nhập thêm 1 todoLít: để xóa todo không cần thiết
    private void todoItemMouseClicked(java.awt.event.MouseEvent evt) {
        JCheckBox item = (JCheckBox) evt.getSource();
        if (evt.getButton() == MouseEvent.BUTTON3) { // click chuot phai
            contxtMenuTodoItem.show(evt.getComponent(), evt.getX(), evt.getY());
            todoItemSelect = Integer.parseInt(item.getName());
        }
    }

    // sự kiện nhấn phím của ô nhập thêm 1 todolist : bấm enter để thêm 1 todo
    private void addTodoKeyPressed(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:
        JTextField addTodo = (JTextField) evt.getSource();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            TodoList todo = new TodoList(-1, addTodo.getText(), false);
            this.note.addTodo(todo);
//            checkClickToggleBtn = true;
            textPane.setText(textPane.getText() + "\n" + addTodo.getText() + "\n");
            showTodoList(this.note.getTodoList());
        }
    }

// ======================================================================================  // action
    // render nhiều note-type ra view, load note vào comboBox
    private void loadNoteTypes() {
        noteTypeNotes = NoteTypeController.loadNoteTypes();
        JTextField textField;

        if (noteTypeNotes.size() >= 0) {
            jPanel19.removeAll(); // box note type
            jPanel3.removeAll(); // box ghi chu
            for (var noteTypeNote : noteTypeNotes) {

                // load noteType
                loadNotes(noteTypeNote);
                // TODO add your handling code here:
                textField = new JTextField(noteTypeNote.getNoteType().getTypeName()); // tạo JTextField
                initItemNoteType(textField);
                textField.setName(Integer.toString(noteTypeNote.getNoteType().getId())); // setName là id
                jPanel19.add(textField);
                
                // load note vào comboBox
                combo.addItem(noteTypeNote.getNoteType().getTypeName()); // name
            }
            // item mặc định
            combo.setSelectedItem(noteTypeNotes.get(0).getNoteType().getTypeName());
            // load lại panel
            jPanel19.repaint();
            jPanel19.revalidate();
        }
    }

    // load note
    private void loadNotes(NoteTypeNote noteTypeNote) {
        if (noteTypeNote != null) {
            LinkedList<Note> notes = noteTypeNote.getNotes(); // danh sách notes để hiển thị ra view
            for (Note _note : notes) {
                DateFormat dateF = new SimpleDateFormat("E, dd-MM-yyyy");

                JTextPane textPane = new JTextPane();
                Font font = new Font("Arial", Font.BOLD, 18);

                // styles
                textPane.setFont(font);
                textPane.setBackground(Color.decode("#FFCCCC"));
                textPane.setForeground(Color.decode("#333333"));
                textPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                textPane.setMargin(new Insets(10, 10, 10, 10));

                // đặt kích thước cố định
                Dimension pre = new Dimension(200, 100);
                textPane.setSize(200, 100);

                // văn bản tự động xuống hàng
                textPane.setPreferredSize(pre);
                textPane.setEditorKit(new WrapEditorKit());
//                textPane.setWrapStyleWord(true);
//                textPane.setLineWrap(true);
                textPane.setFocusable(false);

                // sự kiện click vào mỗi note để hiển thị nội dung
                textPane.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) { // click chuột
                        noteEvent(_note, evt);
                    }
                });

                // style từng đoạn văn bản
                StyledDocument doc = textPane.getStyledDocument();
                Style style = textPane.addStyle("mystyle", null);

                try {
                    // style title
                    StyleConstants.setForeground(style, Color.BLACK);
                    doc.insertString(doc.getLength(), _note.getTitle() + "\n", style);

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
                jPanel3.add(textPane);
            }
            jPanel3.revalidate();
        }
    }
// ======================================================================================  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        home = new javax.swing.JPanel();
        taskbar = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        newnode = new javax.swing.JButton();
        newtype = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        Text = new javax.swing.JPanel();
        Title = new javax.swing.JPanel();
        NOTEBOOK = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        task = new javax.swing.JPanel();
        left = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        tick = new javax.swing.JToggleButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        combo = new javax.swing.JComboBox<>();
        majorpage = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();

        jPanel13.setBackground(new java.awt.Color(253, 253, 244));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 634, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.CardLayout());

        home.setBackground(new java.awt.Color(255, 255, 255));
        home.setLayout(new java.awt.BorderLayout());

        taskbar.setBackground(new java.awt.Color(220, 211, 203));
        taskbar.setPreferredSize(new java.awt.Dimension(90, 90));
        taskbar.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(220, 211, 203));
        jPanel10.setPreferredSize(new java.awt.Dimension(150, 100));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel4.setText("NOTE");
        jLabel4.setName(""); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(75, 33));
        jPanel10.add(jLabel4, java.awt.BorderLayout.LINE_START);

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(97, 106, 107));
        jLabel5.setText("BO");
        jLabel5.setPreferredSize(new java.awt.Dimension(38, 33));
        jPanel10.add(jLabel5, java.awt.BorderLayout.CENTER);

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(148, 156, 159));
        jLabel6.setText("OK");
        jPanel10.add(jLabel6, java.awt.BorderLayout.LINE_END);

        taskbar.add(jPanel10, java.awt.BorderLayout.LINE_START);

        jPanel9.setBackground(new java.awt.Color(220, 211, 203));
        jPanel9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.setText("search");
        jTextField2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 2, new java.awt.Color(153, 153, 153)));
        jTextField2.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        jTextField2.setSelectionColor(new java.awt.Color(204, 255, 255));
        jPanel9.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 390, 50));

        taskbar.add(jPanel9, java.awt.BorderLayout.LINE_END);

        home.add(taskbar, java.awt.BorderLayout.PAGE_START);

        jPanel12.setBackground(new java.awt.Color(253, 253, 244));
        jPanel12.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(220, 211, 203)));
        jPanel12.setPreferredSize(new java.awt.Dimension(200, 413));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(253, 253, 244));
        jPanel16.setPreferredSize(new java.awt.Dimension(150, 120));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        newnode.setBackground(new java.awt.Color(253, 253, 244));
        newnode.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        newnode.setText("NEW NODE");
        newnode.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 2, 2, 1, new java.awt.Color(153, 153, 153)));
        newnode.setMargin(new java.awt.Insets(2, 8, 3, 14));
        newnode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newnodeActionPerformed(evt);
            }
        });
        jPanel16.add(newnode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 160, -1));

        newtype.setBackground(new java.awt.Color(253, 253, 244));
        newtype.setText("NEW CATEGORY");
        newtype.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 2, 2, 1, new java.awt.Color(153, 153, 153)));
        newtype.setMargin(new java.awt.Insets(2, 8, 3, 8));
        newtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newtypeActionPerformed(evt);
            }
        });
        jPanel16.add(newtype, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 160, -1));

        jPanel12.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jPanel19.setBackground(new java.awt.Color(253, 253, 244));
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane4.setViewportView(jPanel19);

        jPanel12.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        home.add(jPanel12, java.awt.BorderLayout.WEST);

        jPanel3.setBackground(new java.awt.Color(253, 253, 244));
        jPanel3.setPreferredSize(new java.awt.Dimension(882, 482));
        jPanel3.setLayout(new java.awt.GridLayout(0, 3, 10, 10));
        jScrollPane1.setViewportView(jPanel3);

        home.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(home, "home");

        Text.setLayout(new java.awt.BorderLayout());

        Title.setBackground(new java.awt.Color(220, 211, 203));
        Title.setPreferredSize(new java.awt.Dimension(855, 90));
        Title.setLayout(new java.awt.BorderLayout());

        NOTEBOOK.setBackground(new java.awt.Color(220, 211, 203));
        NOTEBOOK.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        NOTEBOOK.setMinimumSize(new java.awt.Dimension(145, 33));
        NOTEBOOK.setName(""); // NOI18N
        NOTEBOOK.setPreferredSize(new java.awt.Dimension(145, 90));
        NOTEBOOK.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(220, 211, 203));
        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel2.setText("NOTE");
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(71, 33));
        NOTEBOOK.add(jLabel2, java.awt.BorderLayout.LINE_START);

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(97, 106, 107));
        jLabel1.setText("BO");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        NOTEBOOK.add(jLabel1, java.awt.BorderLayout.CENTER);

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(148, 156, 159));
        jLabel3.setText("OK");
        NOTEBOOK.add(jLabel3, java.awt.BorderLayout.LINE_END);

        task.setBackground(new java.awt.Color(220, 211, 203));
        task.setPreferredSize(new java.awt.Dimension(150, 40));
        task.setLayout(new java.awt.BorderLayout());

        left.setBackground(new java.awt.Color(220, 211, 203));
        left.setBorder(null);
        left.setPreferredSize(new java.awt.Dimension(50, 23));
        left.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftActionPerformed(evt);
            }
        });
        task.add(left, java.awt.BorderLayout.LINE_START);

        NOTEBOOK.add(task, java.awt.BorderLayout.PAGE_END);

        Title.add(NOTEBOOK, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(new java.awt.Color(220, 211, 203));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(220, 211, 203));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 673, -1));

        jToolBar1.setBackground(new java.awt.Color(220, 211, 203));
        jToolBar1.setBorder(null);
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(1000, 65));

        jButton1.setBackground(new java.awt.Color(220, 211, 207));
        jButton1.setBorder(null);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setBackground(new java.awt.Color(220, 211, 207));
        jButton2.setBorder(null);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        tick.setBackground(new java.awt.Color(220, 211, 203));
        tick.setBorder(null);
        tick.setFocusable(false);
        tick.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tick.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tickActionPerformed(evt);
            }
        });
        jToolBar1.add(tick);

        jButton6.setBackground(new java.awt.Color(220, 211, 207));
        jButton6.setBorder(null);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton7.setBackground(new java.awt.Color(220, 211, 207));
        jButton7.setBorder(null);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        jButton8.setBackground(new java.awt.Color(220, 211, 207));
        jButton8.setBorder(null);
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton8);

        jButton9.setBackground(new java.awt.Color(220, 211, 207));
        jButton9.setBorder(null);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton9);

        jButton10.setBackground(new java.awt.Color(220, 211, 207));
        jButton10.setBorder(null);
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton10);

        combo.setEditable(true);
        combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        combo.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 1, 1, new java.awt.Color(220, 211, 203)));
        combo.setMinimumSize(new java.awt.Dimension(100, 22));
        combo.setPreferredSize(new java.awt.Dimension(200, 25));
        jToolBar1.add(combo);
        combo.getAccessibleContext().setAccessibleParent(combo);

        jPanel2.add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 750, 50));

        Title.add(jPanel2, java.awt.BorderLayout.CENTER);

        Text.add(Title, java.awt.BorderLayout.PAGE_START);

        majorpage.setBackground(new java.awt.Color(253, 253, 244));
        majorpage.setLayout(new java.awt.BorderLayout());

        jTextField1.setBackground(new java.awt.Color(253, 253, 244));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Title");
        jTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(253, 253, 244)));
        jTextField1.setCaretColor(new java.awt.Color(153, 153, 153));
        jTextField1.setPreferredSize(new java.awt.Dimension(64, 70));
        jTextField1.setSelectedTextColor(new java.awt.Color(255, 255, 255));
        jTextField1.setSelectionColor(new java.awt.Color(0, 204, 204));
        majorpage.add(jTextField1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(253, 253, 244));
        jPanel4.setPreferredSize(new java.awt.Dimension(115, 229));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
        );

        majorpage.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setBackground(new java.awt.Color(253, 253, 244));
        jPanel5.setPreferredSize(new java.awt.Dimension(115, 229));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
        );

        majorpage.add(jPanel5, java.awt.BorderLayout.LINE_START);

        jPanel6.setBackground(new java.awt.Color(253, 253, 244));
        jPanel6.setPreferredSize(new java.awt.Dimension(855, 80));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 905, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );

        majorpage.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jPanel18.setPreferredSize(new java.awt.Dimension(595, 335));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(253, 253, 244));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel7.setPreferredSize(new java.awt.Dimension(450, 335));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel7.add(jLabel7, java.awt.BorderLayout.CENTER);

        jPanel11.setBackground(new java.awt.Color(253, 253, 244));
        jPanel11.setFocusCycleRoot(true);
        jPanel11.setPreferredSize(new java.awt.Dimension(448, 40));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setBackground(new java.awt.Color(253, 253, 244));
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, 30, 30));

        jButton4.setBackground(new java.awt.Color(253, 253, 244));
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 7, 30, 30));

        jPanel7.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jPanel15.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel17.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTextPane2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jScrollPane3.setViewportView(jTextPane2);

        jPanel17.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel15, java.awt.BorderLayout.CENTER);

        majorpage.add(jPanel18, java.awt.BorderLayout.CENTER);

        Text.add(majorpage, java.awt.BorderLayout.CENTER);

        jPanel1.add(Text, "text");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 825, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // chuyển sang trang home
    private void leftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftActionPerformed
        cardLayout.show(jPanel1, "home");
        combo.removeAllItems();
        loadNoteTypes();
        clearDetail();
        resetNote();
        
    }//GEN-LAST:event_leftActionPerformed

    // chuyển sang trang text - bấm nút new Type
    private void newnodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newnodeActionPerformed
        cardLayout.show(jPanel1, "text");
        clearDetail();
        resetNote();
    }//GEN-LAST:event_newnodeActionPerformed

    // bấm nút new Type
    private void newtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newtypeActionPerformed
        JTextField textField = new JTextField("TypeName");
        initItemNoteType(textField);
        textField.setName("-1");
        jPanel19.add(textField);
        jPanel19.revalidate();
    }//GEN-LAST:event_newtypeActionPerformed

    // lưu note
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        note.setTitle(jTextField1.getText());
        note.setType(null);
        note.setPin(false);

        if (tick.isSelected() == true) { // lưu todoList nếu tạo note mới
            // lưu todoList
//            String curString = jTextPane1.getText(); // lấy văn bản mới nhất trong ô văn bản
//            String[] todoArray = curString.split("\n"); // chuyển sang mảng
//
//            // tạo todolist mới
//            LinkedList<TodoList> curTodos = new LinkedList<>();
//            for (String item : todoArray) {
//                if ("".equals(item)) {
//                    continue;
//                }
//                TodoList todo = new TodoList(-1, item.trim(), false);
//                curTodos.add(todo);
//            }
//            // nếu có sự chỉnh sửa thì mới cập nhật
//            if (checkClickToggleBtn == true) {
//                // xóa todolist cũ
//                this.note.clearTodoList();
//                // cộng vào todolist có sẵn
//                this.note.setTodoList(curTodos); // thêm vào cuối
//                checkClickToggleBtn = false;
//
//            }
//            // xóa Content
//            this.note.deleteContent();

        } else { // hoặc lưu văn bản lúc tạo mới
            try {
                StyledDocument doc = jTextPane2.getStyledDocument();
                String text = doc.getText(0, doc.getLength());

                Content content = new Content(-1, toByteArray(doc, text));
                note.setContent(content);
                // xóa todoList
                this.note.clearTodoList();
            } catch (BadLocationException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // kiiểm tra là tạo note mới hay cập nhật
        if (note.getNoteId() == -1) { // tạo note mới
            NoteController.createNote(note);
        } else if (note.getNoteId() >= 0) { // cập nhật note
            NoteController.updateNote(note);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // chọn hình ảnh
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser jfile = new JFileChooser();

        // đặt đường dẫn của file theo đường dẫn thư mục của dự án
        // khi mở choose file window thì đường dẫn thư mục của dự án là đường dẫn mặc định
        jfile.setCurrentDirectory(new File(path));

        // loại file đươc chọn
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Image", "jpg", "png", "jpeg");
        jfile.showOpenDialog(null); // show choose file window

        // đối tượng file được chọn
        File selectedFile = jfile.getSelectedFile();

        if (selectedFile != null) {
            fileName = selectedFile.getAbsolutePath();
            format = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            try {
                BufferedImage imageBufer = ImageIO.read(new File(fileName));
                byte[] imageInByte;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(imageBufer, format, baos);
                    baos.flush();
                    // lấy mảng byte[] của ảnh
                    imageInByte = baos.toByteArray();
                }
                // tạo ra đối tượng ImageIcon, resize và hiển thị
                ImageIcon icon = new ImageIcon(imageInByte);
                Image imageTemp = icon.getImage();
                int w = imageTemp.getWidth(null);
                int h = imageTemp.getHeight(null);
                if (w > h) {
                    Image scaledImage = imageTemp.getScaledInstance(380, 380 * h / w, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    jLabel7.setIcon(scaledIcon);
                } else if (w < h) {
                    Image scaledImage = imageTemp.getScaledInstance(380 * w / h, 380, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    jLabel7.setIcon(scaledIcon);
                } else {
                    Image scaledImage = imageTemp.getScaledInstance(380, 380, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    jLabel7.setIcon(scaledIcon);
                }
                jLabel7.setText("");

                // tạo đối tượng photo để thêm vào danh sách photo của note
                Photo photo = new Photo(-1, imageInByte);
                this.note.addPhoto(photo);
                indexPhoto++;
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // chuyển từ text sang todolist và ngược lại
    private void tickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tickActionPerformed
        if (tick.isSelected() == true) { // từ văn bản chuyển sang TodoList, mặc định lúc đầu là văn bản
            String curString = textPane.getText(); // lấy văn bản mới nhất trong ô văn bản
            String[] todoArray = curString.split("\n"); // chuyển sang mảng

            // tạo todolist mới k
            LinkedList<TodoList> curTodos = new LinkedList<>();
            for (String item : todoArray) {
                if (item.isEmpty()) {
                    continue;
                }
                TodoList todo = new TodoList(-1, item.trim(), false);
                curTodos.add(todo);
            }
            // xóa content
            this.note.deleteContent();

            // xóa todolist cũ
            this.note.clearTodoList();

            // cộng vào todolist có sẵn
            this.note.setTodoList(curTodos); // 
            showTodoList(this.note.getTodoList()); // hiển thị
            checkClickToggleBtn = true;
        } else { // đang ở todoList chuyển sang dạng văn bản
            String todosString = this.note.getTodoList().stream().map(todo -> todo.getItem())
                    .reduce("", (x1, x2) -> x1 + x2 + "\n"); // // tạo chuỗi từ todoList của Note
            textPane.setText(todosString);
            setContentView();
            resetText(); // reset lại văn bản
            // xóa todoList
            this.note.clearTodoList();
        }
    }//GEN-LAST:event_tickActionPerformed

    // chuyển hình ảnh sang trái
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (!this.note.getPhotos().isEmpty()) {
            indexPhoto--;
            if (indexPhoto <= 0) {
                indexPhoto = 0;
            }
            Photo photo = this.note.getPhotos().get(indexPhoto);
            showPhotos(photo);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    // chuyển hình ảnh sang phải
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (!this.note.getPhotos().isEmpty()) {
            indexPhoto++;
            if (indexPhoto >= this.note.getPhotos().size()) {
                indexPhoto = this.note.getPhotos().size() - 1;
            }
            Photo photo = this.note.getPhotos().get(indexPhoto);
            showPhotos(photo);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    // sự kiện chuột của label hiển thị hình ảnh
    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) {
            if (jLabel7.getIcon() != null) {
                contxtMenuPhoto.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jLabel7MouseClicked
// ==================================================================================================== 
    // nút set password
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String pass = "";
        if (this.note.getPassword().equals("")) { // chưa có mk thì đặt mk mới
            pass = setPassword();
        } else { // có mk r thì phải nhập lại mk cũ để xác nhận r mới nhập mk mới
            pass = changePassword();
        }
        if (pass.equals("")) {
            // không làm gì cả
        } else if (pass.equals(this.note.getPassword()) == true) {
            JOptionPane.showMessageDialog(null, "The new password and the old password are the same");
        } else {
            if (containsUnicode(pass) == true) { // kiểm tra mật khẩu có chứa ký tự unicode không
                JOptionPane.showMessageDialog(null, "Password can not contain unicode character", "Invalid password", JOptionPane.WARNING_MESSAGE);
            } else {
                NoteController.setPassword(note, pass);
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    // màu chữ
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // lấy màu 
        Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
        if (newColor != null) {
            StyledDocument doc = textPane.getStyledDocument();
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();
            Style style = textPane.addStyle("My Style", null);
            StyleConstants.setForeground(style, newColor);
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    // in đậm
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart();   // vị trí index đầu tiên của chuỗi được chọn
        int end = textPane.getSelectionEnd(); // vị trí index cuối cùng của chuỗi được chọn
        Style style = textPane.addStyle("My Style", null);
        boolean checkBold = false; // cờ hiệu kiểm tra văn bản được chọn có bold không
        // kiểm tra văn bản được chọn có ký tự được bôi đen không
        for (int i = start; i <= end; i++) {
            AttributeSet attr = doc.getCharacterElement(i).getAttributes();
            if (StyleConstants.isBold(attr)) {
                checkBold = true;
                break;
            }
        }
        if (checkBold == true) { // nếu văn bản được chọn có ký tự bôi đen thì bỏ bôi đen toàn bộ chuỗi được chọn
            StyleConstants.setBold(style, false);
            doc.setCharacterAttributes(start, end - start, style, false);
        } else { // ngược lại thì bôi đen
            StyleConstants.setBold(style, true);
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    // in nghiêng
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart(); // vị trí index đầu tiên của chuỗi được chọn
        int end = textPane.getSelectionEnd(); // vị trí index cuối cùng của chuỗi được chọn
        Style style = textPane.addStyle("My Style", null);
        boolean checkItalicized = false; // cờ hiệu kiểm tra văn bản được chọn có được in nghiêng không
        // kiểm tra văn bản được chọn có ksy tự được in nghiêng không
        for (int i = start; i <= end; i++) {
            AttributeSet attr = doc.getCharacterElement(i).getAttributes();
            if (StyleConstants.isItalic(attr)) {
                checkItalicized = true;
                break;
            }
        }
        if (checkItalicized == true) { // nếu chuỗi được chọn có in nghiêng thì bỏ nghiêng
            StyleConstants.setItalic(style, false);
            doc.setCharacterAttributes(start, end - start, style, false);
        } else { // ngược lại thì in nghiêng
            StyleConstants.setItalic(style, true);
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    // gạch chân
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart(); // vị trí index đầu tiên của chuỗi được chọn
        int end = textPane.getSelectionEnd(); // vị trí index cuối cùng của chuỗi được chọn
        Style style = textPane.addStyle("My Style", null);
        boolean checkUnderline = false; // cờ hiệu kiểm tra văn bản được chọn có được gạch chân không
        // kiểm tra văn bản được chọn có ksy tự được gạch chân không
        for (int i = start; i <= end; i++) {
            AttributeSet attr = doc.getCharacterElement(i).getAttributes();
            if (StyleConstants.isUnderline(attr)) {
                checkUnderline = true;
                break;
            }
        }
        if (checkUnderline == true) { // nếu trong chuỗi bôi đen đã có ký tự được gạch chân thì bỏ gạch chân 
            StyleConstants.setUnderline(style, false);
            doc.setCharacterAttributes(start, end - start, style, false);
        } else { // ngược lại gạch chân
            StyleConstants.setUnderline(style, true);
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    // hight light
    private void hightlight() {
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart();  // vị trí index đầu tiên của chuỗi được chọn
        int end = textPane.getSelectionEnd();  // vị trí index cuối cùng của chuỗi được chọn
        Style style = textPane.addStyle("My Style", null);
        boolean checkHightlight = false; // cờ hiệu kiểm tra văn bản được chọn có được gạch chân không
        // kiểm tra văn bản được chọn có ksy tự được hightlight không
        for (int i = start; i <= end; i++) {
            AttributeSet attr = doc.getCharacterElement(i).getAttributes();
            if (StyleConstants.getBackground(attr) == Color.YELLOW) {
                checkHightlight = true;
                break;
            }
        }
        if (checkHightlight == true) { // nếu chuỗi được chọn có hight thì un-hightlight
            StyleConstants.setBackground(style, Color.WHITE);
            doc.setCharacterAttributes(start, end - start, style, false);
        } else { // ngược lại thì hightlight
            StyleConstants.setBackground(style, Color.YELLOW);
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }

    // đặt mật khẩu đối với ghi chú chưa có mật khẩu
    private String setPassword() {
        JPasswordField newPassWord = new JPasswordField();
        JPasswordField confirmPassWord = new JPasswordField();
        Object[] message = {
            "New Password:", newPassWord,
            "Confirm Password", confirmPassWord
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Set Password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String value1 = newPassWord.getText();
            String value2 = confirmPassWord.getText();
            if (value1.equals(value2) == true) {
                return value2;
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Password", "", JOptionPane.WARNING_MESSAGE);
            }
        }
        return "";
    }

    // thay đổi mật khẩu
    private String changePassword() {
        JPasswordField currentPassWord = new JPasswordField();
        JPasswordField newPassWord = new JPasswordField();
        JPasswordField confirmPassWord = new JPasswordField();
        Object[] message = {
            "Current Password", currentPassWord,
            "New Password:", newPassWord,
            "Confirm Password", confirmPassWord
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String value1 = currentPassWord.getText();
            String value2 = newPassWord.getText();
            String value3 = confirmPassWord.getText();
            if (value2.equals(value3) == true) {
                return value3;
            } else if (value1.equals(this.note.getPassword()) == false) {
                JOptionPane.showMessageDialog(null, "Incorrect Password", "", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Confirm Password Failed", "", JOptionPane.WARNING_MESSAGE);
            }
        }
        return "";
    }

    // nhập mật khẩu để xem chi tiết
    private String enterPassword() {
        JPasswordField enterPassword = new JPasswordField();
        Object[] message = {
            "Enter Password:", enterPassword,};
        int option = JOptionPane.showConfirmDialog(null, message, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String value1 = enterPassword.getText();
            return value1;
        }
        return "";
    }
// ==================================================================================================== password

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NOTEBOOK;
    private javax.swing.JPanel Text;
    private javax.swing.JPanel Title;
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton left;
    private javax.swing.JPanel majorpage;
    private javax.swing.JButton newnode;
    private javax.swing.JButton newtype;
    private javax.swing.JPanel task;
    private javax.swing.JPanel taskbar;
    private javax.swing.JToggleButton tick;
    // End of variables declaration//GEN-END:variables

    private void setIcon(ImageIcon icon) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
