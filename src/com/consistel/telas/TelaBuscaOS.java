package com.consistel.telas;

import com.consistel.dal.ModuloConexao;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.sql.*;
import com.consistel.dal.ModuloConexao;
import java.awt.Color;
import net.proteanit.sql.DbUtils;
import static com.consistel.telas.TelaOS.txtClienteOs;
import static com.consistel.telas.TelaOS.txtEnderecoClienteOs;
import static com.consistel.telas.TelaOS.*;
import static com.consistel.telas.TelaOS.btnBuscaClienteOs;
import static com.consistel.telas.TelaPrincipal.os;
import java.text.DateFormat;

public class TelaBuscaOS extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DecimalFormat df = new DecimalFormat("#,###.00");
    DateFormat ddmmaa = DateFormat.getDateInstance(DateFormat.SHORT);

    public TelaBuscaOS() {
        initComponents();
        conexao = ModuloConexao.conector();
        txtBuscaOs.setText(null);
        read();
    }

    public void read() {

        String sql = "select os.id_os as OS, os.data_os as Data, os.status_os as Status, c.nomecli as Cliente from tbos as os join tbclientes as c on os.idcli = c.idcli where c.nomecli like ? and status_financeiro_os = 'A Receber' order by os.data_os;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtBuscaOs.getText() + "%");
            rs = pst.executeQuery();
            tblBuscaOs.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void set_client_fields() {

        String sql = "select nomecli, endcli from tbclientes where nomecli = ?";

        try {

            pst = conexao.prepareStatement(sql);
            int setar = tblBuscaOs.getSelectedRow();
            pst.setString(1, tblBuscaOs.getModel().getValueAt(setar, 3).toString());
            rs = pst.executeQuery();

            if (rs.next()) {

                txtClienteOs.setText(rs.getString(1));
                txtEnderecoClienteOs.setText(rs.getString(2));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void set_filtro_busca() {
        String statusfinanceiro = cboStatusFinanceiroFechamentoOs.getSelectedItem().toString();
        String statusos = cboStatusOs.getSelectedItem().toString();

        if (statusos.equals("Todas")) {

            String sql = "select os.id_os as OS, os.data_os as Data, os.status_os as Status, c.nomecli as Cliente from tbos as os join tbclientes as c on os.idcli = c.idcli where c.nomecli like ? and status_financeiro_os = '" + statusfinanceiro + "' order by os.data_os;";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtBuscaOs.getText() + "%");
                rs = pst.executeQuery();
                tblBuscaOs.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        } else {

            String sql = "select os.id_os as OS, os.data_os as Data, os.status_os as Status, c.nomecli as Cliente from tbos as os join tbclientes as c on os.idcli = c.idcli where c.nomecli like ? and status_financeiro_os = '" + statusfinanceiro + "' and os.status_os ='" + statusos + "' order by os.data_os;";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtBuscaOs.getText() + "%");
                rs = pst.executeQuery();
                tblBuscaOs.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboStatusFinanceiroFechamentoOs = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscaOs = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        txtBuscaOs = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboStatusOs = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        cboStatusFinanceiroFechamentoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A Receber", "Pago" }));
        cboStatusFinanceiroFechamentoOs.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboStatusFinanceiroFechamentoOsItemStateChanged(evt);
            }
        });

        tblBuscaOs.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBuscaOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuscaOsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBuscaOs);

        jLabel23.setText("Cliente da OS");

        txtBuscaOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaOsKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search.png"))); // NOI18N

        cboStatusOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "A Enviar", "Enviado", "Não Aprovado", "Aprovado", "Autorizada", "Em andamento", "Aguardando peças", "Pendente", "Finalizada" }));
        cboStatusOs.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboStatusOsItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cboStatusFinanceiroFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(cboStatusOs, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuscaOs, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(53, 53, 53))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscaOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboStatusFinanceiroFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboStatusOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscaOsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaOsKeyReleased
        read();
    }//GEN-LAST:event_txtBuscaOsKeyReleased

    private void tblBuscaOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscaOsMouseClicked

        int setar = tblBuscaOs.getSelectedRow();
        String id = tblBuscaOs.getModel().getValueAt(setar, 0).toString();
        String nomecli = tblBuscaOs.getModel().getValueAt(setar, 3).toString();
        
        os.habilita_edicao();
        os.set_client_fields(nomecli);

        os.set_fields(id);
        os.table_read_products();
        os.table_read_services();
        this.dispose();
        txtBuscaOs.setText(null);
        btnBuscaClienteOs.setEnabled(false);

        os.atualiza_valores();

        //botar o metodo read sempre que fechar
    }//GEN-LAST:event_tblBuscaOsMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        txtBuscaOs.setText(null);
        read();

    }//GEN-LAST:event_formWindowClosed

    private void cboStatusFinanceiroFechamentoOsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboStatusFinanceiroFechamentoOsItemStateChanged
        set_filtro_busca();
    }//GEN-LAST:event_cboStatusFinanceiroFechamentoOsItemStateChanged

    private void cboStatusOsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboStatusOsItemStateChanged
        set_filtro_busca(); 
// TODO add your handling code here:
    }//GEN-LAST:event_cboStatusOsItemStateChanged

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
            java.util.logging.Logger.getLogger(TelaBuscaOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaBuscaOS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> cboStatusFinanceiroFechamentoOs;
    public static javax.swing.JComboBox<String> cboStatusOs;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblBuscaOs;
    private javax.swing.JTextField txtBuscaOs;
    // End of variables declaration//GEN-END:variables
}
