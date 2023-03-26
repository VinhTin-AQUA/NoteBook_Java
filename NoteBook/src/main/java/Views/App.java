/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author tinho
 */
public class App extends javax.swing.JFrame {
    private String path = System.getProperty("user.dir");
    private CardLayout cardLayout;
        
    public App(){
        
        initComponents();
        path = path.replace("\\", "\\\\");
        ImageIcon logo = new ImageIcon(path+"\\\\src\\\\main\\\\java\\\\icon\\\\notebook.png");
                this.setIconImage(logo.getImage());
        
        this.setSize(1200,700);
        this.setTitle("NOTEBOOK");
        this.setLocationRelativeTo(null);
        icon(); 
        combo.setBackground(Color.WHITE);
        combo.setOpaque(true);
        initComponents2();
        
    }
    
    private void initComponents2() {
        cardLayout = (CardLayout) jPanel1.getLayout();
    }
    
    private void icon() {
        //path = path.replace("\\", "\\\\");
          ImageIcon icon = new ImageIcon(path + "\\\\src\\\\main\\\\java\\\\icon\\\\left.png");
          left.setIcon(icon);
    
          
          jToolBar1.setLayout(new GridLayout(1,11,10,10));
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\save.png");
          jButton1.setIcon(icon);
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\image.png");
          jButton2.setIcon(icon);
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\tick.png");
          tick.setIcon(icon);
          
          
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\pass.png");
          jButton6.setIcon(icon);
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\fill.png");
          jButton7.setIcon(icon);
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\bold.png");
          jButton8.setIcon(icon);
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\italic.png");
          jButton9.setIcon(icon);
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\underline.png");
          jButton10.setIcon(icon);
          
          icon = new ImageIcon(path +"\\\\src\\\\main\\\\java\\\\icon\\\\add.png");
          newnode.setIcon(icon);
          newtype.setIcon(icon);
           
          
          
         
          
    }

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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        newnode = new javax.swing.JButton();
        newtype = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
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
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jPanel9.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 390, 50));

        taskbar.add(jPanel9, java.awt.BorderLayout.LINE_END);

        home.add(taskbar, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jPanel11.setBackground(new java.awt.Color(253, 253, 244));
        jPanel11.setLayout(new java.awt.BorderLayout());

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
        jPanel16.add(newtype, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 160, -1));

        jPanel12.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jPanel11.add(jPanel12, java.awt.BorderLayout.LINE_START);

        jPanel14.setBackground(new java.awt.Color(253, 253, 244));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 723, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        jPanel11.add(jPanel14, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(jPanel11);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );

        home.add(jPanel3, java.awt.BorderLayout.CENTER);

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
        jToolBar1.add(jButton2);

        tick.setBackground(new java.awt.Color(220, 211, 203));
        tick.setBorder(null);
        tick.setFocusable(false);
        tick.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tick.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(tick);

        jButton6.setBackground(new java.awt.Color(220, 211, 207));
        jButton6.setBorder(null);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton6);

        jButton7.setBackground(new java.awt.Color(220, 211, 207));
        jButton7.setBorder(null);
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton7);

        jButton8.setBackground(new java.awt.Color(220, 211, 207));
        jButton8.setBorder(null);
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton8);

        jButton9.setBackground(new java.awt.Color(220, 211, 207));
        jButton9.setBorder(null);
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton9);

        jButton10.setBackground(new java.awt.Color(220, 211, 207));
        jButton10.setBorder(null);
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setPreferredSize(new java.awt.Dimension(65, 65));
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton10);

        combo.setEditable(true);
        combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4", "Tín ngu", "Tín ngáo", "Tín zịt", "Tín chicken", " " }));
        combo.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 1, 1, new java.awt.Color(220, 211, 203)));
        combo.setMinimumSize(new java.awt.Dimension(10, 22));
        combo.setOpaque(true);
        combo.setPreferredSize(new java.awt.Dimension(200, 25));
        combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboActionPerformed(evt);
            }
        });
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
        jTextField1.setPreferredSize(new java.awt.Dimension(64, 70));
        jTextField1.setSelectionColor(new java.awt.Color(253, 253, 244));
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
            .addGap(0, 910, Short.MAX_VALUE)
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

        jLabel7.setText("jLabel7");
        jLabel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel7.add(jLabel7, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel17.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    // chuyển sang màng hình text
    private void newnodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newnodeActionPerformed
        cardLayout.show(jPanel1, "text");
    }//GEN-LAST:event_newnodeActionPerformed

    private void leftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftActionPerformed
        cardLayout.show(jPanel1, "home");
    }//GEN-LAST:event_leftActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboActionPerformed

    /**
     * @param args the command line arguments
     */
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
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
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
