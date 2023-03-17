
package Views;

public class text extends javax.swing.JFrame {

    public text() {
        initComponents();
        this.setTitle("NOTEBOOK");
        this.setLocationRelativeTo(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taskbar = new javax.swing.JPanel();
        find = new javax.swing.JPanel();
        seach = new javax.swing.JTextField();
        nametaskbar = new javax.swing.JPanel();
        note = new javax.swing.JLabel();
        ok = new javax.swing.JLabel();
        bo = new javax.swing.JLabel();
        chuatoolbar = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        save = new javax.swing.JButton();
        image = new javax.swing.JButton();
        bold = new javax.swing.JButton();
        checkbox = new javax.swing.JButton();
        main = new javax.swing.JPanel();
        chuatitle = new javax.swing.JPanel();
        title = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1300, 750));

        taskbar.setBackground(new java.awt.Color(220, 211, 203));
        taskbar.setPreferredSize(new java.awt.Dimension(807, 80));
        taskbar.setLayout(new java.awt.BorderLayout());

        find.setBackground(new java.awt.Color(220, 211, 203));
        find.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        find.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        find.setOpaque(false);
        find.setPreferredSize(new java.awt.Dimension(380, 80));
        find.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        seach.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        seach.setText("  searching");
        seach.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(153, 153, 153)));
        seach.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        seach.setDisabledTextColor(new java.awt.Color(102, 102, 102));
        seach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seachActionPerformed(evt);
            }
        });
<<<<<<< HEAD
        find.add(seach, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 17, 356, 45));
=======

        javax.swing.GroupLayout findLayout = new javax.swing.GroupLayout(find);
        find.setLayout(findLayout);
        findLayout.setHorizontalGroup(
            findLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(findLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(seach, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addContainerGap())
        );
        findLayout.setVerticalGroup(
            findLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(findLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(seach, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
>>>>>>> 11183ed820e4237ea2beccdf4032cbf49c10fee6

        taskbar.add(find, java.awt.BorderLayout.LINE_END);

        nametaskbar.setBackground(new java.awt.Color(220, 211, 203));
        nametaskbar.setPreferredSize(new java.awt.Dimension(130, 80));
        nametaskbar.setLayout(new java.awt.BorderLayout());

        note.setBackground(new java.awt.Color(220, 211, 203));
        note.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        note.setText("NOTE");
        note.setMinimumSize(new java.awt.Dimension(59, 26));
        note.setName(""); // NOI18N
        note.setOpaque(true);
        note.setPreferredSize(new java.awt.Dimension(57, 26));
        nametaskbar.add(note, java.awt.BorderLayout.LINE_START);
        note.getAccessibleContext().setAccessibleParent(note);

        ok.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        ok.setForeground(new java.awt.Color(148, 156, 159));
        ok.setText("OK");
        ok.setPreferredSize(new java.awt.Dimension(45, 26));
        nametaskbar.add(ok, java.awt.BorderLayout.LINE_END);

        bo.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        bo.setForeground(new java.awt.Color(97, 106, 107));
        bo.setText("BO");
        nametaskbar.add(bo, java.awt.BorderLayout.CENTER);

        taskbar.add(nametaskbar, java.awt.BorderLayout.LINE_START);

        chuatoolbar.setBackground(new java.awt.Color(220, 211, 203));
        chuatoolbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jToolBar2.setBackground(new java.awt.Color(220, 211, 203));
        jToolBar2.setRollover(true);

        save.setBackground(new java.awt.Color(220, 211, 203));
        save.setFocusable(false);
        save.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        save.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(save);
        save.getAccessibleContext().setAccessibleParent(save);

        image.setBackground(new java.awt.Color(220, 211, 203));
        image.setFocusable(false);
        image.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        image.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(image);

        bold.setBackground(new java.awt.Color(220, 211, 203));
        bold.setFocusable(false);
        bold.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bold.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(bold);

        checkbox.setBackground(new java.awt.Color(220, 211, 203));
        checkbox.setFocusable(false);
        checkbox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkbox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(checkbox);

        chuatoolbar.add(jToolBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 290, 40));

        taskbar.add(chuatoolbar, java.awt.BorderLayout.CENTER);

        getContentPane().add(taskbar, java.awt.BorderLayout.PAGE_START);
        taskbar.getAccessibleContext().setAccessibleParent(taskbar);

        main.setBackground(new java.awt.Color(253, 253, 244));
        main.setOpaque(false);
        main.setLayout(new java.awt.BorderLayout());

        chuatitle.setBackground(new java.awt.Color(253, 253, 244));
        chuatitle.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(253, 253, 244)));
        chuatitle.setOpaque(false);
        chuatitle.setPreferredSize(new java.awt.Dimension(807, 80));
        chuatitle.setLayout(new java.awt.BorderLayout());

        title.setBackground(new java.awt.Color(253, 253, 244));
        title.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        title.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        title.setText("TITLE");
        title.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(253, 253, 244)));
        title.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        title.setFocusTraversalPolicyProvider(true);
        title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleActionPerformed(evt);
            }
        });
        chuatitle.add(title, java.awt.BorderLayout.CENTER);
        title.getAccessibleContext().setAccessibleParent(title);

        main.add(chuatitle, java.awt.BorderLayout.PAGE_START);

        jPanel1.setBackground(new java.awt.Color(253, 253, 244));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(253, 253, 244)));
        jPanel1.setMinimumSize(new java.awt.Dimension(80, 100));
        jPanel1.setPreferredSize(new java.awt.Dimension(80, 299));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
        );

        main.add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(new java.awt.Color(253, 253, 244));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(253, 253, 244)));
        jPanel2.setPreferredSize(new java.awt.Dimension(80, 279));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
        );

        main.add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel3.setBackground(new java.awt.Color(253, 253, 244));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(253, 253, 244)));
        jPanel3.setPreferredSize(new java.awt.Dimension(862, 80));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 797, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        main.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setFocusTraversalPolicyProvider(true);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(220, 211, 203)));
        jScrollPane1.setViewportView(jTextArea1);

        main.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(main, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void seachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seachActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_seachActionPerformed

    private void titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new text().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bo;
    private javax.swing.JButton bold;
    private javax.swing.JButton checkbox;
    private javax.swing.JPanel chuatitle;
    private javax.swing.JPanel chuatoolbar;
    private javax.swing.JPanel find;
    private javax.swing.JButton image;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JPanel main;
    private javax.swing.JPanel nametaskbar;
    private javax.swing.JLabel note;
    private javax.swing.JLabel ok;
    private javax.swing.JButton save;
    private javax.swing.JTextField seach;
    private javax.swing.JPanel taskbar;
    private javax.swing.JTextField title;
    // End of variables declaration//GEN-END:variables
}
