package com.consistel.telas;

import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.sql.*;
import com.consistel.dal.ModuloConexao;
import static com.consistel.telas.TelaOS.txtClienteOs;
import static com.consistel.telas.TelaOS.txtEnderecoClienteOs;
import static com.consistel.telas.TelaOS.txtIdOs;
import static com.consistel.telas.TelaPrincipal.os;
import java.text.DateFormat;
import net.proteanit.sql.DbUtils;


public class TelaBuscaClientes extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DecimalFormat df = new DecimalFormat("#,###.00");
    String idbuscacli = null;
    int id_telabuscacli = 0;
    DateFormat ddmmaa = DateFormat.getDateInstance(DateFormat.SHORT);
    String txtid_telabuscacli = null;

    public TelaBuscaClientes() {
        initComponents();
        conexao = ModuloConexao.conector();
        txtBuscaClientes.setText(null);
        read();
    }

    public void read() {

        String sql = "select idcli as ID, nomecli as Nome, endcli as Endereço, telcli as Tel from tbclientes where nomecli like ? order by idcli;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtBuscaClientes.getText() + "%");
            rs = pst.executeQuery();
            tblBuscaClientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void set_fields() {

        int setar = tblBuscaClientes.getSelectedRow();
        idbuscacli = tblBuscaClientes.getModel().getValueAt(setar, 0).toString();
        String nome = tblBuscaClientes.getModel().getValueAt(setar, 1).toString();
        String end = tblBuscaClientes.getModel().getValueAt(setar, 2).toString();

        txtClienteOs.setText(nome);
        txtEnderecoClienteOs.setText(end);

    }
    
    public void set_id_os() {

        String sql = "select id_os from tbos order by id_os desc";

        try {

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery()
                    ;

            if (rs.next()) {
                //System.out.println(rs.getString(1));

                int ultimo_id = Integer.parseInt(rs.getString(1));
                id_telabuscacli = ultimo_id + 1;
                txtid_telabuscacli = Integer.toString(id_telabuscacli);
                
            }

            //txtIdOs.setText(Integer.toString(id));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscaClientes = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        txtBuscaClientes = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        tblBuscaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblBuscaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuscaClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBuscaClientes);

        jLabel23.setText("Cliente");

        txtBuscaClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaClientesKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuscaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap(65, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscaClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaClientesKeyReleased
        read();
    }//GEN-LAST:event_txtBuscaClientesKeyReleased

    private void tblBuscaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscaClientesMouseClicked

        int setar = tblBuscaClientes.getSelectedRow();
        //String id = txtIdOs.getText();
        String nomecli = tblBuscaClientes.getModel().getValueAt(setar, 1).toString();
        idbuscacli = tblBuscaClientes.getModel().getValueAt(setar, 0).toString();
        
        

        if (txtIdOs.getText().isEmpty()) {

           
            os.set_id_os();
            set_id_os();
            os.create_os_buscacli(idbuscacli);
            //buscacli busca cliente
            

            os.set_fields(txtid_telabuscacli);
            os.set_client_fields(nomecli);
            

        } else {

            os.delete_os_buscacli();
            
            
            os.set_id_os();
            set_id_os();
            os.create_os_buscacli(idbuscacli);
           
             
            os.set_fields(txtid_telabuscacli);
            os.set_client_fields(nomecli);
        }

        this.dispose();
        txtBuscaClientes.setText(null);

        //botar o metodo read sempre que fechar
    }//GEN-LAST:event_tblBuscaClientesMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        txtBuscaClientes.setText(null);
        read();

    }//GEN-LAST:event_formWindowClosed

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
            java.util.logging.Logger.getLogger(TelaBuscaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaBuscaClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblBuscaClientes;
    private javax.swing.JTextField txtBuscaClientes;
    // End of variables declaration//GEN-END:variables
}
