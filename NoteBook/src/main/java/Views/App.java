package Views;

import Components.Dialog;
import Components.NoteItem;
import Components.NoteTypeItem;
import Components.TodoItem;
import Controllers.*;
import Models.*;
import ThemeColor.*;
import _utility.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.time.LocalDate;
import java.sql.Date;
import javax.swing.UIManager;

public class App extends javax.swing.JFrame {

    private CardLayout cardLayout;

    private int indexPhoto = -1; // điều khiền hiển thị hình ảnh qua lại khi soạn thảo

    // ==========================================================================================
//    private LinkedList<NoteTypeNote> noteTypeNotes; // danh sách noteType
    private LinkedList<NoteType> noteTypes;
    private NoteType noteType; // biến tạm lưu thông tin noteType cần xóa
    private LinkedList<Note> notes;
    private Note note; // chứa note cần chỉnh sửa hoặc lưu
    private LinkedList<Note> curNotes; // danh sách ghi chú hiện tại đang thao tác
    private LinkedList<Note> lastNotes; // lưu danh sách ghi chứ trước đó

    // ==========================================================================================
    private JPopupMenu contxtMenuNoteType; // menu chuột phải của mỗi notetype item
    private JPopupMenu contxtMenuNote; // menu chuột phải của mỗi note
    private JPopupMenu contxtMenuPhoto; // menu chuột phải của mỗi note
    private JPopupMenu contxtMenuTodoItem; // menu chuột phải của mỗi note
    private JMenu chooseType; // sub menu của choose Type
    // ==========================================================================================

    private JTextField textFieldTemp; // biến tạm để lưu đối tượng JTextField khi xử lý sự kiện chuột phải

    // ==========================================================================================
    private String fileName = ""; // đường dẫn tuyệt đối của hình ảnh tải lên
    private String format = ""; // định dạng hình ảnh
    // ==========================================================================================

    // điều chỉnh hiển thị todoList và Content
    private int todoItemSelect; // lưu chỉ sổ của item todolist cần xóa
    private int todoIndex; // lưu số thứ tự cuối cùng hiện tại của item todolist

    // ==========================================================================================
    // is sort A-Z
    private boolean sortNote;
    private boolean checkSave;
// ========================================================================================= khởi tạo

    public App() {
        initComponents();
        ImageIcon logo = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\notebook.png");
        this.setIconImage(logo.getImage());
        jPanel3.setPreferredSize(new Dimension(400, 500));
        this.setMinimumSize(new Dimension(1090, 800)); // kích thước tối thiểu của ứng dụng
        this.setSize(1320, 400);
        this.setTitle("NOTEBOOK");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        combo.setBackground(Color.WHITE);
        combo.setOpaque(true);
        combo1.setBackground(Color.WHITE);
        combo1.setOpaque(true);

        // đổi màu thanh trượt, đôi lúc phát sinh exceptions
        jScrollPane3.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.LIGHT_GRAY;
            }
        });

        initComponents2();

        icon();
        loadNoteTypeData(); // load notetypes
        loadNoteTypes(noteTypes);
        loadNoteData(); // load notes
        loadNotes(notes);
        initPopupMenu();
    }

    private void initComponents2() {
        cardLayout = (CardLayout) jPanel1.getLayout();
        sortNote = true;
        todoIndex = 1;
        checkSave = false;

        noteType = new NoteType(-1, "typename");
        note = new Note(-1, "", null, "", new NoteType(1, "DEFAULT"), false, new Content(-1,
                new byte[]{}), new LinkedList<TodoList>(), new LinkedList<Photo>());
        curNotes = new LinkedList<>();
        lastNotes = new LinkedList<>();
        // văn bản tự động xuống dòng khi hết hàng khi nhập văn bản
        jTextPane2.setEditorKit(new WrapEditorKit());

        // không cho chỉnh sửa item
        combo.setEditable(false);
        combo1.setEditable(false);

        // theme
        jCheckBox1.setSelected(ThemeController.getThemMode());
        jCheckBox1ActionPerformed(null);

        // mang hinh hien thi todolist
        jPanel30.setLayout(new WrapLayout());
    }

    private void loadNoteTypeData() {
        // load và sắp xếp noteType theo tên NoteType
        noteTypes = NoteTypeController.loadNoteTypes();
        Collections.sort(noteTypes, (c1, c2) -> {
            return c1.getTypeName().compareToIgnoreCase(c2.getTypeName());
        });
    }

    private void loadNoteData() {
        // load và sắp xếp Notes theo title
        notes = NoteController.loadNotes(noteTypes);
        curNotes.clear();
        curNotes.addAll(notes);
        lastNotes.clear();
        lastNotes.addAll(notes);
        Collections.sort(notes, (c1, c2) -> {
            return c1.getTitle().compareToIgnoreCase(c2.getTitle());
        });
    }

    private void loadLastNotes() {
        if (this.noteType.getId() == -1) {
            lastNotes.clear();
            lastNotes.addAll(notes);
        } else {
            lastNotes.clear();
            lastNotes.addAll(notes.stream().filter(note -> note.getType().getId() == this.noteType.getId())
                    .collect(Collectors.toCollection(LinkedList::new)));
        }
    }

    private void icon() {
        //path = path.replace("\\", "\\\\");
        ImageIcon icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\left.png");
        left.setIcon(icon);
        left1.setIcon(icon);

        jToolBar1.setLayout(new GridLayout(1, 11, 10, 10));

        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\save.png");
        jButton1.setIcon(icon);
        jButton12.setIcon(icon);

        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\image.png");
        jButton2.setIcon(icon);

        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\pass.png");
        jButton6.setIcon(icon);
        jButton14.setIcon(icon);

        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\fill.png");
        jButton7.setIcon(icon);

        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\bold.png");
        jButton8.setIcon(icon);

        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\italic.png");
        jButton9.setIcon(icon);

        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\underline.png");
        jButton10.setIcon(icon);
        
        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\hightlight.png");
        jButton13.setIcon(icon);
        jButton15.setIcon(icon);

      

        // chuyển xem hình ảnh 
        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\pre.png");
        jButton3.setIcon(icon);
        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\next.png");
        jButton4.setIcon(icon);

        // set icon khi selected và un-selected của nút dark mode
        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\un_check.png");
        jCheckBox1.setIcon(icon);
        icon = new ImageIcon(Ultility.path + "\\\\src\\\\main\\\\java\\\\icon\\\\check.png");
        jCheckBox1.setSelectedIcon(icon);
    }
//============================================================================ popup menu

    // khởi tạo menu chuột phải
    private void initPopupMenu() {
        contxtMenuNoteType = new JPopupMenu(); // menu cua note type
        contxtMenuNote = new JPopupMenu(); // menu cua note
        contxtMenuPhoto = new JPopupMenu(); // menu khi chon anh
        contxtMenuTodoItem = new JPopupMenu(); // menu khi lam viec voi todolist

        initPopupMenuNoteType();
        initPopupMenuNote();
        initPopupMenuPhoto();
        initPopupMenuTodo();
    }

    // menu chuột phait note type
    private void initPopupMenuNoteType() {
        JMenuItem reNameNoteType = new JMenuItem("Rename NoteType");
        reNameNoteType.addActionListener((ActionEvent e) -> {// đổi tên noteType
            textFieldTemp.setFocusable(true);
            textFieldTemp.selectAll(); // bôi đen toàn bộ văn bản
            textFieldTemp.requestFocusInWindow(); // con trỏ chuột tự động focus vào jtext field
        });
        JMenuItem deleteNoteType = new JMenuItem("Delete NoteType");// xóa noteType
        deleteNoteType.addActionListener((ActionEvent e) -> {
            NoteTypeController.deleteNoteType(noteType);
            loadNoteTypeData();
            loadNoteTypes(noteTypes);
            chooseType.removeAll();
            addChooseTypeItem();
        });
        JMenuItem addNote = new JMenuItem("Add Note");// thêm note văn bản
        addNote.addActionListener((ActionEvent e) -> {
            newnodeActionPerformed(null);
            combo.setSelectedItem(noteType.getTypeName());
        });
        JMenuItem addTodoList = new JMenuItem("Add TodoList");// thêm note văn bản
        addTodoList.addActionListener((ActionEvent e) -> {
            jButton5ActionPerformed(null);
            combo1.setSelectedItem(noteType.getTypeName());
        });
        contxtMenuNoteType.add(addNote);
        contxtMenuNoteType.add(addTodoList);
        contxtMenuNoteType.add(reNameNoteType);
        contxtMenuNoteType.add(deleteNoteType);
    }

    // menu chuột phải note
    private void initPopupMenuNote() {
        JMenuItem deleteNote = new JMenuItem("Delete"); // xóa note
        deleteNote.addActionListener((ActionEvent e) -> {
            NoteController.deleteNote(this.note);
            loadNoteData();
            loadNotes(notes);
            resetNote();
            jPanel5.repaint();
            jPanel5.revalidate();
        });
        JMenuItem setPassword = new JMenuItem("Set Password"); // set passwod
        setPassword.addActionListener((ActionEvent e) -> {
            java.awt.event.ActionEvent c = null;
            jButton6ActionPerformed(c);
            loadNoteData();
            loadLastNotes();
            loadNotes(lastNotes);
            resetNote();
            jPanel5.repaint();
            jPanel5.revalidate();
        });
        JMenuItem pin = new JMenuItem("Pin"); // pin note
        pin.addActionListener((e) -> {
            NoteController.pinNote(note);
            loadNoteData();
            loadLastNotes();
            loadNotes(lastNotes);
        });
        JMenuItem deletePassword = new JMenuItem("Reset Password"); // xóa mật khẩu Password
        deletePassword.addActionListener((ActionEvent e) -> {
            String pass = enterPassword();
            if (pass.equals("")) {

            } else if (pass.equals(this.note.getPassword()) == true) {
                NoteController.resetPassword(note);
                loadNoteData();
                loadLastNotes();
                loadNotes(lastNotes);
                resetNote();
                jPanel5.repaint();
                jPanel5.revalidate();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Password", "Confirm Password", JOptionPane.WARNING_MESSAGE);
            }
        });
        chooseType = new JMenu("Choose Type");
        addChooseTypeItem();
        contxtMenuNote.add(pin);
        contxtMenuNote.add(deleteNote);
        contxtMenuNote.add(setPassword);
        contxtMenuNote.add(deletePassword);
        contxtMenuNote.add(chooseType);
    }

    // menu chuột phải hình ảnh
    private void initPopupMenuPhoto() {
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
            jLabel11.setText(indexPhoto + 1 + "/" + note.getPhotos().size()); // hiển hình ảnh trên tổng số hình ảnh
        });
        JMenuItem downloadPhoto = new JMenuItem("Download"); // tải 1 hình ảnh
        downloadPhoto.addActionListener((ActionEvent e) -> {
            try {
                String randomFileName = UUID.randomUUID().toString(); // tên file ngẫu nhiên
                String downloadPath = System.getProperty("user.home") + "\\Downloads\\";// đường dẫn thư mục dowload
                FileOutputStream fos = new FileOutputStream(downloadPath + randomFileName + ".png");
                fos.write(this.note.getPhotos().get(indexPhoto).getData());
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        JMenuItem downLoadPhotos = new JMenuItem("Download All Photos"); // tải tất cả hình ảnh
        downLoadPhotos.addActionListener((ActionEvent e) -> {
            try {
                String downloadPath = System.getProperty("user.home") + "\\Downloads\\";
                FileOutputStream fos = null;
                for (var photo : this.note.getPhotos()) {
                    String randomFileName = UUID.randomUUID().toString(); // tên file ngẫu nhiên
                    fos = new FileOutputStream(downloadPath + randomFileName + ".png");
                    fos.write(photo.getData());
                    /*
                        nên để dòng lệnh này trong vòng lặp, sau khi lưu thì ta có thể xóa hình ảnh trong hệ thống
                        tránh lỗi: file is open in java.....
                     */
                    fos.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        contxtMenuPhoto.add(deletePhoto);
        contxtMenuPhoto.add(downloadPhoto);
        contxtMenuPhoto.add(downLoadPhotos);
    }

    // menu chuột phải todolist
    private void initPopupMenuTodo() {
        // menu chuột phải của mỗi Todo Item
        JMenuItem deleteTodoItem = new JMenuItem("Delete"); // xóa TodoItem
        deleteTodoItem.addActionListener((ActionEvent evt) -> {
            this.note.deleteTodoItem(todoItemSelect);
            todoIndex = 1;
            showTodoList(this.note.getTodoList());
        });
        contxtMenuTodoItem.add(deleteTodoItem);
    }

    // load tên notetype vào submenu chooseType để chọn loại note khi tạo note
    private void addChooseTypeItem() {
        if (noteTypes.size() >= 0) {
            for (var noteType : noteTypes) {
                JMenuItem item = new JMenuItem(noteType.getTypeName());
                item.addActionListener((ActionEvent e) -> {
                    NoteController.setType(noteType.getId(), note.getNoteId()); // lưu vào database
                    loadNoteData();
                    loadLastNotes();
                    loadNotes(lastNotes);
                });
                chooseType.add(item);
            }
        }
    }

// ====================================================================================== thiết lập
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
        jTextPane2.setText("");
    }

    // khởi tạo lại note sau mỗi lần lưu
    private void resetNote() {
        note.setNoteId(-1);
        note.setTitle("");
        note.setPassword("");
//        note.setType(null);
        note.setPin(false);
        note.deleteContent();
        note.clearTodoList();
        note.clearPhotos();
    }

    // thiết lập hiển thị todoList
    private void setTodoListView() {
        jPanel30.removeAll();
        jTextArea1.setText("");
    }

    // hiển thị todoList ra view
    private void showTodoList(LinkedList<TodoList> todos) {
        setTodoListView();
        jTextField5.setText(this.note.getTitle());
        combo1.setSelectedItem(this.note.getType().getTypeName());

        if (!todos.isEmpty()) {
            for (TodoList todo : todos) {
                TodoItem todoItem = new TodoItem(todo.getTodoListId(), todo.getItem());
                todoItem.getBox().setSelected(todo.isCheck());
                todoItem.getBox().setText(todoIndex + "");

                // sự kiện khi thay đổi giá trị item
                todoItem.getTextArea().getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        changeItem(e, todo, todoItem);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        changeItem(e, todo, todoItem);
                    }
                });

                // sự kiện check 
                todoItem.getBox().addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        changeItem(evt, todo);
                    }
                });

                // sự kiện chuột phải
                todoItem.getTextArea().addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        todoItemMouseClicked(evt, todoItem);
                    }
                });
                todoItem.getBox().addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        todoItemMouseClicked(evt, todoItem);
                    }
                });
                todoItem.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        todoItemMouseClicked(evt, todoItem);
                    }
                });
                jPanel30.add(todoItem);
                todoIndex++;
            }
        }
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
                jTextPane2.setStyledDocument(doc);
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // reset văn bản trong JTExtPaine
    private void resetText() {
        StyledDocument doc = jTextPane2.getStyledDocument();
        Style style = jTextPane2.addStyle("My Style", null);
        StyleConstants.setForeground(style, Color.BLACK); // màu chữ
        StyleConstants.setBackground(style, Color.WHITE); // hightlight
        StyleConstants.setBold(style, false);// bold
        StyleConstants.setItalic(style, false);// in nghiêng
        StyleConstants.setUnderline(style, false);// gạch chân
        doc.setCharacterAttributes(-1, jTextPane2.getText().length() + 1, style, false);
    }

// ====================================================================================== sự kiện
    // unfocus textfield của NoteType Item
    private void unfocus(FocusEvent evt) {
        JTextField textField = (JTextField) evt.getSource();
        textField.setFocusable(false);
        textField.setCaretPosition(0); // bỏ bôi đen văn bản
    }

    // sự kiện chuột vào mỗi noteType
    private void mouseNoteTypeEvent(java.awt.event.MouseEvent evt) {
        JTextField jtextField = (JTextField) evt.getSource();
        this.textFieldTemp = jtextField;
        try {
            noteType.setId(Integer.parseInt(jtextField.getName()));
            noteType.setTypeName(jtextField.getText());
        } catch (Exception e) {
            noteType = null;
        }
        if (evt.getButton() == MouseEvent.BUTTON3) { // click chuot phai -> hiển thị popupMenu
            contxtMenuNoteType.show(evt.getComponent(), evt.getX(), evt.getY());
            jtextField.setFocusable(true);
        }
        if (evt.getButton() == MouseEvent.BUTTON1) { // click chuot trai để hiển thị các note của notetype này

            lastNotes.clear();
            lastNotes.addAll(notes.stream().filter(note -> note.getType().getId() == this.noteType.getId())
                    .collect(Collectors.toCollection(LinkedList::new)));
            loadNotes(lastNotes);
        }
    }

    // sự kiện nhấn phím enter của notetype
    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {
        JTextField jtextField = (JTextField) evt.getSource();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { // enter
            NoteTypeController.createNoteType(jtextField.getName(), jtextField.getText());
            loadNoteTypeData();
            loadNoteTypes(noteTypes);
            jtextField.setFocusable(false); // tạo note type thành công thì không cho nhập chữ vào note type nữa
            // reload lại submenu sau khi thêm 1 note type
            chooseType.removeAll();
            addChooseTypeItem();
        }
    }

    // sự kiện chuột của mỗi Note
    private void noteEvent(Note note, java.awt.event.MouseEvent evt) {
        this.note = new Note(note);
//        this.note = note;
        clearDetail();
        // show note
        if (evt.getButton() == MouseEvent.BUTTON1) { // click chuot trai để xem chi tiết
            // nếu có mật khẩu thì phải nhập mật khẩu mới được xem
            if (this.note.getPassword().equals("") == false) {
                String pass = enterPassword();
                if (pass.equals("")) {
                    // không làm gì
                } else if (this.note.getPassword().equals(pass) == true) { // nhập đúng mật khẩu
                    if (note.getContent().getText() != null) { // hiển thị note chứa content
                        cardLayout.show(jPanel1, "text"); // hiển thị trang text
                        jTextField1.setText(note.getTitle()); // set title
                        loadText(note.getContent().getText()); // load văn bản
                        if (note.getPhotos().isEmpty() == false) { // nếu có hình ảnh thì hiển thị
                            indexPhoto++; // nếu có hình ảnh thì tăng lên chỉ số 0
                            showPhotos(note.getPhotos().get(indexPhoto));
                        }
                        jLabel11.setText(indexPhoto + 1 + "/" + note.getPhotos().size()); // hiển hình ảnh trên tổng số hình ảnh
                    } else if (note.getTodoList().isEmpty() == false) { // hiển thị note chứa todolist
                        cardLayout.show(jPanel1, "todolist");
                        setTodoListView();
                        showTodoList(note.getTodoList());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password", "", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (note.getContent().getText() != null) {  // hiển thị note chứa content
                    cardLayout.show(jPanel1, "text"); // hiển thị trang text
                    jTextField1.setText(note.getTitle()); // set title
                    loadText(note.getContent().getText()); // load văn bản
                    if (note.getPhotos().isEmpty() == false) { // nếu có hình ảnh thì hiển thị
                        indexPhoto++; // nếu có hình ảnh thì tăng lên chỉ số 0
                        showPhotos(note.getPhotos().get(indexPhoto));
                        jLabel11.setText(indexPhoto + 1 + "/" + note.getPhotos().size()); // hiển hình ảnh trên tổng số hình ảnh
                    }
                } else if (note.getTodoList().isEmpty() == false) { // hiển thị note chứa todolist
                    cardLayout.show(jPanel1, "todolist"); // hiển thị trang text
                    setTodoListView();
                    showTodoList(note.getTodoList());
                }
            }
        }
        if (evt.getButton() == MouseEvent.BUTTON3) { // click chuột phải để xem Popup
            contxtMenuNote.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    // sự kiện click chuột của ô nhập thêm 1 todoLít: để xóa todo không cần thiết
    private void todoItemMouseClicked(java.awt.event.MouseEvent evt, TodoItem item) {
        if (evt.getButton() == MouseEvent.BUTTON3) { // click chuot phai
            contxtMenuTodoItem.show(evt.getComponent(), evt.getX(), evt.getY());
            todoItemSelect = Integer.parseInt(item.getBox().getText()) - 1;
        }
    }

// ====================================================================================== 
    // thiết lập của các item NoteType
    private void initItemNoteType(NoteTypeItem noteTypeItem) {
        noteTypeItem.setName("-1");
        // sự kiện chuột
        noteTypeItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { // click chuột
                mouseNoteTypeEvent(evt);
            }
        });
        // sự kiện focus
        noteTypeItem.addFocusListener(new FocusListener() {
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
        noteTypeItem.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
    }

    // render nhiều note-type ra view
    private void loadNoteTypes(LinkedList<NoteType> _noteTypes) {
//        title để thực hiện chức năng tìm kiếm
//        JTextField textField;
        if (_noteTypes.size() >= 0) {

            jPanel19.removeAll(); // box note type
            combo.removeAllItems(); // xóa item củ của comboBox
            combo1.removeAllItems(); // xóa item củ của comboBox
            for (var noteType : _noteTypes) {
//                textField = new JTextField(noteType.getTypeName()); // tạo JTextField
//                initItemNoteType(textField);
                NoteTypeItem noteTypeItem = new NoteTypeItem(noteType.getTypeName());
                initItemNoteType(noteTypeItem);
                noteTypeItem.setName(Integer.toString(noteType.getId())); // setName là id
                jPanel19.add(noteTypeItem);
                // load Type vào comboBox
                combo.addItem(noteType.getTypeName()); // 
                combo1.addItem(noteType.getTypeName());
            }
            // load lại panel
            jPanel19.repaint();
            jPanel19.revalidate();
        }
    }

    // item note
    private void noteItem(Note _note) {
        NoteItem noteItem = new NoteItem(_note);

        // sự kiện click vào mỗi note để hiển thị nội dung
        noteItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { // click chuột
                combo.setSelectedItem(_note.getType().getTypeName());
                noteEvent(_note, evt);
            }
        });
        jPanel3.add(noteItem);
    }

    // load note
    private void loadNotes(LinkedList<Note> notes, String... searchTitle) {
        // sort = true: sort A-Z
        // sort = false: sort Z-A
        jPanel3.removeAll();
        if (notes.isEmpty() == false) {

            LinkedList<Note> temp = null;
            // danh sách notes được pin
            LinkedList<Note> pinnedNote = null;

            // danh sách notes chưa pin
            LinkedList<Note> unpinNote = null;

            if (searchTitle.length > 0) {
                temp = notes.stream().filter(note -> note.getTitle().toLowerCase().contains(searchTitle[0].toLowerCase()) == true)
                        .collect(Collectors.toCollection(LinkedList::new));
                pinnedNote = temp.stream().filter(note -> note.isPin() == true)
                        .collect(Collectors.toCollection(LinkedList::new));
                unpinNote = temp.stream().filter(note -> note.isPin() == false)
                        .collect(Collectors.toCollection(LinkedList::new));
            } else {
                pinnedNote = notes.stream().filter(note -> note.isPin() == true)
                        .collect(Collectors.toCollection(LinkedList::new));
                unpinNote = notes.stream().filter(note -> note.isPin() == false)
                        .collect(Collectors.toCollection(LinkedList::new));
            }

            // hiển thị các note được ghim trước
            for (Note _note : pinnedNote) {
                noteItem(_note);
            }

            // hiển thị các note không được ghim sau
            for (Note _note : unpinNote) {
                noteItem(_note);
            }
        }
        jPanel3.repaint();
        jPanel3.revalidate();
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
        jButton5 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel15 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        Text = new javax.swing.JPanel();
        Title = new javax.swing.JPanel();
        NOTEBOOK = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        task = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        left = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
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
        jLabel11 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        todolist = new javax.swing.JPanel();
        Title1 = new javax.swing.JPanel();
        NOTEBOOK1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        task1 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        left1 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        combo1 = new javax.swing.JComboBox<>();
        jPanel26 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel30 = new javax.swing.JPanel();

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

        jPanel10.setBackground(taskbar.getBackground());
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

        jPanel9.setBackground(taskbar.getBackground());
        jPanel9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.setText("search");
        jTextField2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 2, new java.awt.Color(153, 153, 153)));
        jTextField2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField2.setSelectionColor(new java.awt.Color(204, 255, 255));
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });
        jPanel9.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 330, 40));

        taskbar.add(jPanel9, java.awt.BorderLayout.LINE_END);

        home.add(taskbar, java.awt.BorderLayout.PAGE_START);

        jPanel12.setBackground(new java.awt.Color(253, 253, 244));
        jPanel12.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(220, 211, 203)));
        jPanel12.setPreferredSize(new java.awt.Dimension(200, 413));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(253, 253, 244));
        jPanel16.setPreferredSize(new java.awt.Dimension(150, 250));
        jPanel16.setRequestFocusEnabled(false);
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        newnode.setBackground(new java.awt.Color(253, 253, 244));
        newnode.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        newnode.setText("NEW NOTE");
        newnode.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 3, 3, 1, new java.awt.Color(153, 153, 153)));
        newnode.setFocusPainted(false);
        newnode.setMargin(new java.awt.Insets(2, 8, 3, 14));
        newnode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newnodeActionPerformed(evt);
            }
        });
        jPanel16.add(newnode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 170, 50));

        newtype.setBackground(new java.awt.Color(253, 253, 244));
        newtype.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        newtype.setText("NEW CATEGORY");
        newtype.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        newtype.setFocusPainted(false);
        newtype.setMargin(new java.awt.Insets(2, 8, 3, 8));
        newtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newtypeActionPerformed(evt);
            }
        });
        jPanel16.add(newtype, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 170, 40));

        jButton5.setBackground(new java.awt.Color(253, 253, 244));
        jButton5.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton5.setText("TODOLIST");
        jButton5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jButton5.setFocusPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, 170, 40));

        jButton20.setBackground(new java.awt.Color(253, 253, 244));
        jButton20.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jButton20.setText("ALL NOTES");
        jButton20.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        jButton20.setFocusPainted(false);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 170, 40));

        jPanel12.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(181, 280));

        jPanel19.setBackground(new java.awt.Color(253, 253, 244));
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane4.setViewportView(jPanel19);

        jPanel12.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        home.add(jPanel12, java.awt.BorderLayout.WEST);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setViewportView(jPanel3);

        jPanel3.setBackground(new java.awt.Color(253, 253, 244));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        jScrollPane1.setViewportView(jPanel3);

        home.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel14.setBackground(new java.awt.Color(253, 253, 244));
        jPanel14.setPreferredSize(new java.awt.Dimension(180, 485));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new java.awt.Color(253, 253, 244));
        jPanel21.setPreferredSize(new java.awt.Dimension(170, 300));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel21.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        jButton11.setBackground(new java.awt.Color(255, 254, 254));
        jButton11.setText("Search for Date");
        jButton11.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 2, 2, 1, new java.awt.Color(220, 211, 203)));
        jButton11.setFocusPainted(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 160, 30));

        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDateChooser1.setDateFormatString("dd-MM-yyy");
        jDateChooser1.setPreferredSize(new java.awt.Dimension(97, 28));
        jPanel23.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 30, 150, -1));

        jDateChooser2.setDateFormatString("dd-MM-yyy");
        jDateChooser2.setPreferredSize(new java.awt.Dimension(97, 28));
        jPanel23.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 75, 150, -1));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Date To");
        jPanel23.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 110, -1));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Date From");
        jPanel23.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 110, -1));

        jPanel21.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 160, 120));

        jToggleButton2.setBackground(new java.awt.Color(242, 242, 242));
        jToggleButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton2.setText("Date");
        jToggleButton2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.setOpaque(true);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jPanel21.add(jToggleButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 160, 30));

        jToggleButton3.setBackground(new java.awt.Color(242, 242, 242));
        jToggleButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jToggleButton3.setText("A-Z");
        jToggleButton3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        jToggleButton3.setFocusPainted(false);
        jToggleButton3.setOpaque(true);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jPanel21.add(jToggleButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 160, 30));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Sort");
        jPanel21.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 40, 30));

        jPanel14.add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel22.setBackground(new java.awt.Color(253, 253, 244));
        jPanel22.setMinimumSize(new java.awt.Dimension(131, 100));
        jPanel22.setPreferredSize(new java.awt.Dimension(131, 120));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jCheckBox1.setBackground(new java.awt.Color(220, 211, 203));
        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jCheckBox1.setText("DARK MODE");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        jCheckBox1.setFocusPainted(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel22.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 160, 30));

        jPanel14.add(jPanel22, java.awt.BorderLayout.PAGE_END);

        home.add(jPanel14, java.awt.BorderLayout.LINE_END);

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

        jLabel2.setBackground(NOTEBOOK.getBackground());
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

        task.setBackground(NOTEBOOK.getBackground());
        task.setPreferredSize(new java.awt.Dimension(150, 10));
        task.setLayout(new java.awt.BorderLayout());
        NOTEBOOK.add(task, java.awt.BorderLayout.PAGE_END);

        Title.add(NOTEBOOK, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(new java.awt.Color(220, 211, 203));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(220, 211, 203));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 673, -1));

        jToolBar1.setBackground(jPanel2.getBackground());
        jToolBar1.setBorder(null);
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(1000, 65));

        left.setBackground(task.getBackground());
        left.setBorder(null);
        left.setPreferredSize(new java.awt.Dimension(50, 23));
        left.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftActionPerformed(evt);
            }
        });
        jToolBar1.add(left);

        jButton1.setBackground(jToolBar1.getBackground());
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

        jButton2.setBackground(jToolBar1.getBackground());
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

        jButton6.setBackground(jToolBar1.getBackground());
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

        jButton7.setBackground(jToolBar1.getBackground());
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

        jButton8.setBackground(jToolBar1.getBackground());
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

        jButton9.setBackground(jToolBar1.getBackground());
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

        jButton10.setBackground(jToolBar1.getBackground());
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

        jButton15.setBackground(new java.awt.Color(220, 211, 203));
        jButton15.setBorder(null);
        jButton15.setFocusable(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton15);

        combo.setEditable(true);
        combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        combo.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 1, 1, new java.awt.Color(220, 211, 203)));
        combo.setMinimumSize(new java.awt.Dimension(100, 22));
        combo.setPreferredSize(new java.awt.Dimension(200, 25));
        jToolBar1.add(combo);
        combo.getAccessibleContext().setAccessibleParent(combo);

        jPanel2.add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 900, 40));

        Title.add(jPanel2, java.awt.BorderLayout.CENTER);

        Text.add(Title, java.awt.BorderLayout.PAGE_START);

        majorpage.setBackground(new java.awt.Color(253, 253, 244));
        majorpage.setLayout(new java.awt.BorderLayout());

        jTextField1.setBackground(new java.awt.Color(253, 253, 244));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(51, 51, 51));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Title");
        jTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(253, 253, 244)));
        jTextField1.setCaretColor(new java.awt.Color(153, 153, 153));
        jTextField1.setPreferredSize(new java.awt.Dimension(64, 70));
        jTextField1.setSelectionColor(new java.awt.Color(0, 204, 204));
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });
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
            .addGap(0, 335, Short.MAX_VALUE)
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
            .addGap(0, 335, Short.MAX_VALUE)
        );

        majorpage.add(jPanel5, java.awt.BorderLayout.LINE_START);

        jPanel6.setBackground(new java.awt.Color(253, 253, 244));
        jPanel6.setPreferredSize(new java.awt.Dimension(855, 80));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 855, Short.MAX_VALUE)
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

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
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

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setText("0/0");
        jPanel11.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 90, 20));

        jPanel7.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jPanel15.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel17.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTextPane2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jScrollPane3.setViewportView(jTextPane2);

        jPanel17.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel15, java.awt.BorderLayout.CENTER);

        majorpage.add(jPanel18, java.awt.BorderLayout.CENTER);

        Text.add(majorpage, java.awt.BorderLayout.CENTER);

        jPanel1.add(Text, "text");

        todolist.setBackground(new java.awt.Color(255, 255, 255));
        todolist.setPreferredSize(new java.awt.Dimension(855, 575));
        todolist.setLayout(new java.awt.BorderLayout());

        Title1.setBackground(new java.awt.Color(220, 211, 203));
        Title1.setPreferredSize(new java.awt.Dimension(855, 90));
        Title1.setLayout(new java.awt.BorderLayout());

        NOTEBOOK1.setBackground(new java.awt.Color(220, 211, 203));
        NOTEBOOK1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        NOTEBOOK1.setMinimumSize(new java.awt.Dimension(145, 33));
        NOTEBOOK1.setName(""); // NOI18N
        NOTEBOOK1.setPreferredSize(new java.awt.Dimension(145, 90));
        NOTEBOOK1.setLayout(new java.awt.BorderLayout());

        jLabel9.setBackground(NOTEBOOK.getBackground());
        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel9.setText("NOTE");
        jLabel9.setOpaque(true);
        jLabel9.setPreferredSize(new java.awt.Dimension(71, 33));
        NOTEBOOK1.add(jLabel9, java.awt.BorderLayout.LINE_START);

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(97, 106, 107));
        jLabel10.setText("BO");
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        NOTEBOOK1.add(jLabel10, java.awt.BorderLayout.CENTER);

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(148, 156, 159));
        jLabel12.setText("OK");
        NOTEBOOK1.add(jLabel12, java.awt.BorderLayout.LINE_END);

        task1.setBackground(NOTEBOOK.getBackground());
        task1.setPreferredSize(new java.awt.Dimension(150, 10));
        task1.setLayout(new java.awt.BorderLayout());
        NOTEBOOK1.add(task1, java.awt.BorderLayout.PAGE_END);

        Title1.add(NOTEBOOK1, java.awt.BorderLayout.WEST);

        jPanel24.setBackground(new java.awt.Color(220, 211, 203));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel25.setBackground(new java.awt.Color(220, 211, 203));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel24.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 673, -1));

        jToolBar2.setBackground(jPanel2.getBackground());
        jToolBar2.setBorder(null);
        jToolBar2.setRollover(true);
        jToolBar2.setPreferredSize(new java.awt.Dimension(1000, 65));

        left1.setBackground(task.getBackground());
        left1.setBorder(null);
        left1.setFocusPainted(false);
        left1.setPreferredSize(new java.awt.Dimension(50, 23));
        left1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                left1ActionPerformed(evt);
            }
        });
        jToolBar2.add(left1);

        jButton12.setBackground(jToolBar1.getBackground());
        jButton12.setBorder(null);
        jButton12.setFocusPainted(false);
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton12);

        jButton14.setBackground(jToolBar1.getBackground());
        jButton14.setBorder(null);
        jButton14.setFocusPainted(false);
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton14);

        jButton13.setBackground(new java.awt.Color(220, 211, 203));
        jButton13.setBorder(null);
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton13);

        combo1.setEditable(true);
        combo1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        combo1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 1, 1, new java.awt.Color(220, 211, 203)));
        combo1.setMinimumSize(new java.awt.Dimension(100, 22));
        combo1.setPreferredSize(new java.awt.Dimension(200, 25));
        combo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo1ActionPerformed(evt);
            }
        });
        jToolBar2.add(combo1);

        jPanel24.add(jToolBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 900, 40));

        Title1.add(jPanel24, java.awt.BorderLayout.CENTER);

        todolist.add(Title1, java.awt.BorderLayout.NORTH);

        jPanel26.setLayout(new java.awt.BorderLayout());

        jTextField5.setBackground(new java.awt.Color(255, 255, 204));
        jTextField5.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("Title");
        jTextField5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(253, 253, 244)));
        jTextField5.setPreferredSize(new java.awt.Dimension(60, 50));
        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField5FocusGained(evt);
            }
        });
        jPanel26.add(jTextField5, java.awt.BorderLayout.PAGE_START);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setLayout(new java.awt.BorderLayout());

        jPanel28.setBackground(new java.awt.Color(255, 204, 204));
        jPanel28.setMaximumSize(new java.awt.Dimension(200, 2147483647));
        jPanel28.setPreferredSize(new java.awt.Dimension(250, 435));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton19.setBackground(new java.awt.Color(255, 254, 254));
        jButton19.setText("Add Item");
        jButton19.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 51, 51)));
        jButton19.setFocusPainted(false);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel28.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 100, 30));

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Enter TodoList Item");
        jTextArea1.setBorder(new javax.swing.border.MatteBorder(null));
        jTextArea1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextArea1FocusGained(evt);
            }
        });
        jScrollPane6.setViewportView(jTextArea1);

        jPanel28.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 230, 120));

        jPanel27.add(jPanel28, java.awt.BorderLayout.WEST);

        jPanel29.setBackground(new java.awt.Color(255, 204, 204));
        jPanel29.setPreferredSize(new java.awt.Dimension(150, 435));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jPanel29, java.awt.BorderLayout.LINE_END);

        jScrollPane5.setPreferredSize(new java.awt.Dimension(735, 100));

        jPanel30.setBackground(new java.awt.Color(255, 153, 255));
        jPanel30.setLayout(new javax.swing.BoxLayout(jPanel30, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane5.setViewportView(jPanel30);

        jPanel27.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel26.add(jPanel27, java.awt.BorderLayout.CENTER);

        todolist.add(jPanel26, java.awt.BorderLayout.CENTER);

        jPanel1.add(todolist, "todolist");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 935, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // chuyển sang trang home
    private void leftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftActionPerformed
        if (checkSave == true) {
            cardLayout.show(jPanel1, "home");
            loadNoteData();
            loadNotes(notes);
            clearDetail();
            resetNote();
        } else {
            int option = Dialog.YesNoCancel("Do you want to save changes", "Save Note");
            if (option == 0) {
                jButton1ActionPerformed(null);
                cardLayout.show(jPanel1, "home");
                loadNoteData();
                loadNotes(notes);
                clearDetail();
                resetNote();
            } else if (option == 1) {
                cardLayout.show(jPanel1, "home");
                loadNoteData();
                loadNotes(notes);
                clearDetail();
                resetNote();
            }
        }
        checkSave = false;
    }//GEN-LAST:event_leftActionPerformed

    // chuyển sang trang text - bấm nút new note
    private void newnodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newnodeActionPerformed
        cardLayout.show(jPanel1, "text");
        clearDetail();
        resetNote();
        resetText();
        combo.setSelectedItem("DEFAULT");
        combo1.addItem(noteType.getTypeName());
    }//GEN-LAST:event_newnodeActionPerformed

    // bấm nút new Type
    private void newtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newtypeActionPerformed
        NoteTypeItem noteTypeItem = new NoteTypeItem("Type Name");
        initItemNoteType(noteTypeItem);
        jPanel19.add(noteTypeItem);
        jPanel19.revalidate();
    }//GEN-LAST:event_newtypeActionPerformed

    // chuẩn bị note để bắt đầu lưu
    private void prepareNote() {
        note.setPin(false);
        if (note.getTodoList().isEmpty() == false) { // lưu todoList nếu tạo note mới
            this.note.deleteContent();
        } else { // hoặc lưu văn bản lúc tạo mới
            try {
                StyledDocument doc = jTextPane2.getStyledDocument();
                String text = doc.getText(0, doc.getLength());
                Content content = new Content(-1, Ultility.toByteArray(doc, text));
                note.setContent(content);
            } catch (BadLocationException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // lưu note văn bản
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        checkSave = true;
        if ("".equals(jTextField1.getText())) {
            note.setTitle("NO TITLE");
        } else {
            note.setTitle(jTextField1.getText());
        }
        prepareNote();

        // kiiểm tra là tạo note mới hay cập nhật
        if (note.getNoteId() == -1) { // tạo note mới
            int noteId = NoteController.createNote(note, (String) combo.getSelectedItem());
            note.setNoteId(noteId); // khi chỉnh sửa chính note vừa tạo, sẽ cập nhật note đó chứ không tạo note mới
        } else if (note.getNoteId() >= 0) { // cập nhật note
            NoteController.updateNote(note, (String) combo.getSelectedItem());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // chọn hình ảnh
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser jfile = new JFileChooser();

        // đặt đường dẫn của file theo đường dẫn thư mục của dự án
        // khi bấm nút chọn hình ảnh thì sẽ mở thư mục download
        jfile.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Downloads\\"));

        // loại file đươc chọn
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Image", "jpg", "png", "jpeg");
        jfile.setFileFilter(filter); // chỉ hiển thị hình ảnh có phần mở rộng được định nghĩa ở trên
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
                jLabel11.setText(indexPhoto + 1 + "/" + note.getPhotos().size()); // hiển hình ảnh trên tổng số hình ảnh
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // chuyển hình ảnh sang trái
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (!this.note.getPhotos().isEmpty()) {
            indexPhoto--;
            if (indexPhoto <= 0) {
                indexPhoto = 0;
            }
            Photo photo = this.note.getPhotos().get(indexPhoto);
            jLabel11.setText(indexPhoto + 1 + "/" + note.getPhotos().size());
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
            jLabel11.setText(indexPhoto + 1 + "/" + note.getPhotos().size());
            showPhotos(photo);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    // sự kiện chuột phải của label hiển thị hình ảnh
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
            if (Ultility.containsUnicode(pass) == true) { // kiểm tra mật khẩu có chứa ký tự unicode không
                JOptionPane.showMessageDialog(null, "Password can not contain unicode character", "Invalid password", JOptionPane.WARNING_MESSAGE);
            } else {
                NoteController.setPassword(note, pass);
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    // set màu chữ
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // lấy màu 
        Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
        if (newColor != null) {
            StyledDocument doc = jTextPane2.getStyledDocument();
            int start = jTextPane2.getSelectionStart();
            int end = jTextPane2.getSelectionEnd();
            Style style = jTextPane2.addStyle("My Style", null);
            StyleConstants.setForeground(style, newColor);
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    // in đậm
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        StyledDocument doc = jTextPane2.getStyledDocument();
        int start = jTextPane2.getSelectionStart();   // vị trí index đầu tiên của chuỗi được chọn
        int end = jTextPane2.getSelectionEnd(); // vị trí index cuối cùng của chuỗi được chọn
        Style style = jTextPane2.addStyle("My Style", null);
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
        StyledDocument doc = jTextPane2.getStyledDocument();
        int start = jTextPane2.getSelectionStart(); // vị trí index đầu tiên của chuỗi được chọn
        int end = jTextPane2.getSelectionEnd(); // vị trí index cuối cùng của chuỗi được chọn
        Style style = jTextPane2.addStyle("My Style", null);
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
        StyledDocument doc = jTextPane2.getStyledDocument();
        int start = jTextPane2.getSelectionStart(); // vị trí index đầu tiên của chuỗi được chọn
        int end = jTextPane2.getSelectionEnd(); // vị trí index cuối cùng của chuỗi được chọn
        Style style = jTextPane2.addStyle("My Style", null);
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

    // ô tìm kiếm note theo title
    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { // enter
//            loadNoteTypes(noteTypeNotes, sort, jTextField2.getText());
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    // tự động bôi đen title khi focus
    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.selectAll(); // bôi đen toàn bộ văn bản
    }//GEN-LAST:event_jTextField1FocusGained

// =========================================================================================================
    // đổi theme
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        ThemeController.saveThemMode(jCheckBox1.isSelected());
        if (jCheckBox1.isSelected() == true) { // DARKMODE ON
            homeDark();
            textDark();
            todoDark();
        } else { // DARKMODE OFF 
            homeLight();
            textLight();
            todoLight();
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void homeDark() {
        // header - home
        taskbar.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jPanel10.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jPanel9.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));

        // body - home
        jPanel3.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel14.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel16.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel19.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel22.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel21.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jLabel15.setForeground(Color.decode(DarkMode.TEXT_COLOR.getRGB()));
        jToggleButton3.setForeground(Color.decode(DarkMode.TEXT_COLOR.getRGB()));
        jToggleButton3.setBackground(Color.decode(DarkMode.BUTTON.getRGB()));
         jToggleButton2.setBackground(Color.decode(DarkMode.BUTTON.getRGB()));
         jButton11.setBackground(Color.decode(DarkMode.BUTTON.getRGB()));
         jPanel23.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
         jPanel23.setBorder(BorderFactory.createLineBorder(Color.decode(DarkMode.BUTTON.getRGB())));
         jLabel15.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
         jLabel14.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
         jLabel13.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
         jLabel4.setForeground(Color.decode(DarkMode.WHITE.getRGB()));
         newnode.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
         jButton5.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
         newtype.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
         jButton20.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
       newnode.setBorder(BorderFactory.createMatteBorder(1, 2, 3, 1, Color.decode(DarkMode.BUTTON.getRGB())));
        jButton5.setBorder(BorderFactory.createLineBorder(Color.decode(DarkMode.BUTTON.getRGB())));
        newtype.setBorder(BorderFactory.createLineBorder(Color.decode(DarkMode.BUTTON.getRGB())));
        jButton20.setBorder(BorderFactory.createLineBorder(Color.decode(DarkMode.BUTTON.getRGB())));
        newnode.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
        jButton5.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
        newtype.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
        jButton20.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
        jTextField1.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
        task.setBackground(Color.decode(DarkMode.BUTTON.getRGB()));
       
    }

    private void homeLight() {
        // header - home
        taskbar.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jPanel10.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jPanel9.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jLabel4.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        
        // body - home
        
        jPanel3.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel14.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel16.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel19.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel22.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel21.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jLabel15.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        jToggleButton3.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        jToggleButton3.setBackground(Color.decode(LightMode.BUTTON.getRGB()));
        jToggleButton2.setBackground(Color.decode(LightMode.BUTTON.getRGB()));
          jPanel23.setBackground(Color.decode(LightMode.BUTTON.getRGB()));
         jPanel23.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
         jLabel14.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
         jLabel13.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
         
         newnode.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
         jButton5.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
         newtype.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
         jButton20.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
       newnode.setBorder(BorderFactory.createMatteBorder(1, 2, 3, 1, Color.LIGHT_GRAY));
        jButton5.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        newtype.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        jButton20.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        newnode.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        jButton5.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        newtype.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        jButton20.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        jButton11.setBackground(Color.decode(LightMode.SEARCH.getRGB()));
    }

    private void textDark() {
        // header - text
        NOTEBOOK.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
       task.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        left.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jLabel2.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jPanel2.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jToolBar1.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton1.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton2.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton6.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton7.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton8.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton9.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton10.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));

        // body - text
        jLabel2.setForeground(Color.decode(DarkMode.WHITE.getRGB()));
        jPanel4.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel6.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel5.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jTextField1.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jTextField1.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
         jButton15.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jButton15.setBorder(BorderFactory.createEmptyBorder());
    }

    private void textLight() {
        // header - text
        NOTEBOOK.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
       
        left.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jLabel2.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jPanel2.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jToolBar1.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton1.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton2.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton6.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton7.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton8.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton9.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton10.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));

        // body - text
        jPanel4.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel6.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel5.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jTextField1.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jTextField1.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
          task.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
          jLabel2.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
           jButton15.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton15.setBorder(BorderFactory.createEmptyBorder());
    }

    private void todoDark() {
        // header
        jPanel24.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jToolBar2.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
    
        jLabel12.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jLabel9.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        NOTEBOOK1.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        left1.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton12.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));
        jButton14.setBackground(Color.decode(DarkMode.HEADER_cOLOR.getRGB()));

        // body
        jTextField5.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jTextField5.setForeground(Color.decode(DarkMode.BUTTON.getRGB()));
        jTextField5.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode(DarkMode.BODY_cOLOR.getRGB())));
        task1.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel28.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel30.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jPanel30.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.decode(DarkMode.BUTTON.getRGB())));
        jPanel29.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jButton19.setBackground(Color.decode(DarkMode.BUTTON.getRGB()));
        jLabel9.setForeground(Color.decode(DarkMode.WHITE.getRGB()));
         jButton13.setBackground(Color.decode(DarkMode.BODY_cOLOR.getRGB()));
        jButton13.setBorder(BorderFactory.createEmptyBorder());
    }

    private void todoLight() {
// header
        jPanel24.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jToolBar2.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        task1.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jLabel12.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jLabel9.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        NOTEBOOK1.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
         left1.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton12.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton14.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jLabel9.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        // body
        jTextField5.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jTextField5.setForeground(Color.decode(LightMode.TEXT_COLOR.getRGB()));
        jTextField5.setBorder(BorderFactory.createEmptyBorder());
                
        jPanel28.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel30.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jPanel29.setBackground(Color.decode(LightMode.BODY_cOLOR.getRGB()));
        jButton19.setBackground(Color.decode(LightMode.SEARCH.getRGB()));
        jPanel30.setBorder(BorderFactory.createEmptyBorder());
         jButton13.setBackground(Color.decode(LightMode.HEADER_cOLOR.getRGB()));
        jButton13.setBorder(BorderFactory.createEmptyBorder());
    }
// =========================================================================================================

    // sắp xếp theo title
    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        if (jToggleButton3.isSelected() == true) { // xếp tăng dần
            Collections.sort(lastNotes, (n1, n2) -> {
                return n1.getTitle().compareToIgnoreCase(n2.getTitle());
            });
        } else { // xếp giảm dần
            Collections.sort(lastNotes, (n1, n2) -> {
                return -n1.getTitle().compareToIgnoreCase(n2.getTitle());
            });
        }
        curNotes.clear();
        curNotes.addAll(lastNotes);
        loadNotes(curNotes);
    }//GEN-LAST:event_jToggleButton3ActionPerformed

// =========================================================================================================
    // trở ra màng hình home
    private void left1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_left1ActionPerformed

        if (checkSave == true) {
            cardLayout.show(jPanel1, "home");
            loadNoteData();
            loadNotes(notes);

            clearDetail();
            resetNote();
        } else {
            int option = Dialog.YesNoCancel("Do you want to save changes", "Save Note");
            if (option == 0) {
                jButton12ActionPerformed(null);
                cardLayout.show(jPanel1, "home");
                loadNoteData();
                loadNotes(notes);
                clearDetail();
                resetNote();
            } else if (option == 1) {
                cardLayout.show(jPanel1, "home");
                loadNoteData();
                loadNotes(notes);
                clearDetail();
                resetNote();
            }
        }
        checkSave = false;
        todoIndex = 1;
    }//GEN-LAST:event_left1ActionPerformed

// =========================================================================================================
    // nút lưu todolist
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        checkSave = true;
        if ("".equals(jTextField5.getText())) {
            note.setTitle("NO TITLE");
        } else {
            note.setTitle(jTextField5.getText());
        }
        prepareNote();
        // kiiểm tra là tạo note mới hay cập nhật
        if (note.getNoteId() == -1) { // tạo note mới
            int noteId = NoteController.createNote(note, (String) combo1.getSelectedItem());
            note.setNoteId(noteId); // khi chỉnh sửa chính note vừa tạo, sẽ cập nhật note đó chứ không tạo note mới
        } else if (note.getNoteId() >= 0) { // cập nhật note
            NoteController.updateNote(note, (String) combo1.getSelectedItem());
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    // chuyển sang màng hình todolist - bấm nút new todolist
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        setTodoListView();
        cardLayout.show(jPanel1, "todolist");
        combo1.setSelectedItem("DEFAULT");
        resetNote();
    }//GEN-LAST:event_jButton5ActionPerformed

    // add item todo
    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        TodoItem todoItem = new TodoItem(-1, jTextArea1.getText());
        TodoList todo = new TodoList(-1, jTextArea1.getText(), false);
        todoItem.getBox().setText(todoIndex + ""); // số thứ tự của box
        // sự kiện khi thay đổi giá trị item
        todoItem.getTextArea().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeItem(e, todo, todoItem);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeItem(e, todo, todoItem);
            }
        });
//
//        // sự kiện check 
        todoItem.getBox().addActionListener((java.awt.event.ActionEvent evt1) -> {
            changeItem(evt1, todo);
        });

        // sự kiện chuột phải
        todoItem.getTextArea().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                todoItemMouseClicked(evt, todoItem);
            }
        });
        todoItem.getBox().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                todoItemMouseClicked(evt, todoItem);
            }
        });
        todoItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                todoItemMouseClicked(evt, todoItem);
            }
        });
        this.note.addTodo(todo);
        jPanel30.add(todoItem);
        todoIndex++;
        jPanel30.revalidate();
        jPanel30.repaint();
    }//GEN-LAST:event_jButton19ActionPerformed

    // title tự động bôi đen khi focus
    private void jTextField5FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusGained
        jTextField5.selectAll(); // bôi đen toàn bộ văn bản
    }//GEN-LAST:event_jTextField5FocusGained

    // chọn type khi soạn todo list
    private void combo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo1ActionPerformed
        if (this.note.getType() != null) {
            this.note.getType().setTypeName(combo1.getSelectedItem() + "");
        } else {
            JOptionPane.showMessageDialog(null, "type null");
        }
    }//GEN-LAST:event_combo1ActionPerformed

    // bôi đen toàn bộ văn bản khi focus
    private void jTextArea1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea1FocusGained
        jTextArea1.selectAll();
    }//GEN-LAST:event_jTextArea1FocusGained

    //  tìm kiếm theo title
    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        String searchString = jTextField2.getText();
        loadNotes(curNotes, searchString);
    }//GEN-LAST:event_jTextField2ActionPerformed

    // hiển thị all notes
    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        lastNotes.clear();
        lastNotes.addAll(notes);
        loadNotes(notes);
    }//GEN-LAST:event_jButton20ActionPerformed

    // search theo ngày tháng
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        Date date1 = null;
        Date date2 = null;
        final LocalDate localDate1;
        final LocalDate localDate2;

        if (jDateChooser1.getDate() == null && jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(null, "DateFrom and DateTo is null or Incorect");
            return;
        }

        if (jDateChooser1.getDate() != null && jDateChooser2.getDate() == null) { // lấy từ ngày này trở về sau
            date1 = new Date(jDateChooser1.getDate().getTime());
            localDate1 = date1.toLocalDate();

            LinkedList<Note> collect = lastNotes.stream().filter(note -> {
                return note.getDateCreate().toLocalDate().compareTo(localDate1) > 0;
            }).collect(Collectors.toCollection(LinkedList::new));

            curNotes.clear();
            curNotes.addAll(collect);
            loadNotes(curNotes);
        } else if (jDateChooser1.getDate() == null && jDateChooser2.getDate() != null) { // lấy từ ngày này trở về trước
            date2 = new Date(jDateChooser2.getDate().getTime());
            localDate2 = date2.toLocalDate();

            LinkedList<Note> collect = lastNotes.stream().filter(note -> {
                return note.getDateCreate().toLocalDate().compareTo(localDate2) < 0;
            }).collect(Collectors.toCollection(LinkedList::new));

            curNotes.clear();
            curNotes.addAll(collect);
            loadNotes(curNotes);
        } else { // lấy giữa 2 khoản thời gian này
            date1 = new Date(jDateChooser1.getDate().getTime());
            localDate1 = date1.toLocalDate();
            date2 = new Date(jDateChooser2.getDate().getTime());
            localDate2 = date2.toLocalDate();

            LinkedList<Note> collect = lastNotes.stream().filter(note -> {
                return (note.getDateCreate().toLocalDate().compareTo(localDate2) < 0
                        & note.getDateCreate().toLocalDate().compareTo(localDate1) > 0);
            }).collect(Collectors.toCollection(LinkedList::new));

            curNotes.clear();
            curNotes.addAll(collect);
            loadNotes(curNotes);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    // focus ô tìm kiếm thì bôi đen hết
    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        jTextField2.selectAll();
    }//GEN-LAST:event_jTextField2FocusGained

    // sắp xếp theo ngày tháng
    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        if (jToggleButton2.isSelected() == true) { // xếp tăng dần
            Collections.sort(lastNotes, (n1, n2) -> {
                return n1.getDateCreate().compareTo(n2.getDateCreate());
            });
        } else { // xếp giảm dần
            Collections.sort(lastNotes, (n1, n2) -> {
                return -n1.getDateCreate().compareTo(n2.getDateCreate());
            });
        }
        curNotes.clear();
        curNotes.addAll(lastNotes);
        loadNotes(curNotes);
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    // value item sẽ thay đổi khi nhập giá trị mới
    private void changeItem(DocumentEvent evt, TodoList todo, TodoItem item) {
        todo.setItem(item.getTextArea().getText());
       
    }

    // tick các item todolist
    private void changeItem(java.awt.event.ActionEvent evt, TodoList todo) {
        JCheckBox box = (JCheckBox) evt.getSource();
        todo.setCheck(box.isSelected());
       
        
    }

    // =========================================================================================================
    // hight light
    private void hightlight() {
//        StyledDocument doc = textPane.getStyledDocument();
//        int start = textPane.getSelectionStart();  // vị trí index đầu tiên của chuỗi được chọn
//        int end = textPane.getSelectionEnd();  // vị trí index cuối cùng của chuỗi được chọn
//        Style style = textPane.addStyle("My Style", null);
//        boolean checkHightlight = false; // cờ hiệu kiểm tra văn bản được chọn có được gạch chân không
//        // kiểm tra văn bản được chọn có ksy tự được hightlight không
//        for (int i = start; i <= end; i++) {
//            AttributeSet attr = doc.getCharacterElement(i).getAttributes();
//            if (StyleConstants.getBackground(attr) == Color.YELLOW) {
//                checkHightlight = true;
//                break;
//            }
//        }
//        if (checkHightlight == true) { // nếu chuỗi được chọn có hight thì un-hightlight
//            StyleConstants.setBackground(style, Color.WHITE);
//            doc.setCharacterAttributes(start, end - start, style, false);
//        } else { // ngược lại thì hightlight
//            StyleConstants.setBackground(style, Color.YELLOW);
//            doc.setCharacterAttributes(start, end - start, style, false);
//        }
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
    private javax.swing.JPanel NOTEBOOK1;
    private javax.swing.JPanel Text;
    private javax.swing.JPanel Title;
    private javax.swing.JPanel Title1;
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JComboBox<String> combo1;
    private javax.swing.JPanel home;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
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
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton left;
    private javax.swing.JButton left1;
    private javax.swing.JPanel majorpage;
    private javax.swing.JButton newnode;
    private javax.swing.JButton newtype;
    private javax.swing.JPanel task;
    private javax.swing.JPanel task1;
    private javax.swing.JPanel taskbar;
    private javax.swing.JPanel todolist;
    // End of variables declaration//GEN-END:variables

    private void setIcon(ImageIcon icon) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
