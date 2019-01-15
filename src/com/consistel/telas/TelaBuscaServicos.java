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

public class TelaBuscaServicos extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public TelaBuscaServicos() {
        initComponents();
        conexao = ModuloConexao.conector();
        txtBuscaServicos.setText(null);
        read();
    }

    public void read() {

        String sql = "select id_serv as ID, nome_serv as Nome, horas_serv as Horas, preco_serv as Preco from tbservicos where nome_serv like ? order by nome_serv;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtBuscaServicos.getText() + "%");
            rs = pst.executeQuery();
            tblBuscaServicos.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void adiciona_produto() {
        

        String sql = "insert into os_recebe_servico (id_os, id_serv, quantidade) values (?,?,?)";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtIdOs.getText());

            int setar = tblBuscaServicos.getSelectedRow();
            pst.setString(2, tblBuscaServicos.getModel().getValueAt(setar, 0).toString());
            pst.setString(3, spinQuantidadeServicos.getValue().toString());

            if (txtIdOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione um cliente!");
            } else {

                pst.executeUpdate();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /*
    public void table_read_services() {

        String sql = "select s.id_serv as ID, s.nome_serv as Nome, sum(ors.quantidade) as Quant, s.preco_serv as 'Preço Unit.', (s.preco_serv * sum(ors.quantidade)) as 'Preço Total' from tbos join os_recebe_servico ors on tbos.id_os = ors.id_os join tbservicos s on s.id_serv = ors.id_serv where tbos.id_os = ? group by s.id_serv;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdOs.getText());
            rs = pst.executeQuery();

            tblServicosOs.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscaServicos = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        txtBuscaServicos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        spinQuantidadeServicos = new javax.swing.JSpinner();
        btnAdicionarServico = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        tblBuscaServicos.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBuscaServicos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuscaServicosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBuscaServicos);

        jLabel23.setText("Servico");

        txtBuscaServicos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaServicosKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search.png"))); // NOI18N

        spinQuantidadeServicos.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));

        btnAdicionarServico.setText("Adicionar");
        btnAdicionarServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarServicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(spinQuantidadeServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdicionarServico))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtBuscaServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel23))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel11))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscaServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinQuantidadeServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionarServico))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscaServicosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaServicosKeyReleased
        read();
    }//GEN-LAST:event_txtBuscaServicosKeyReleased

    private void tblBuscaServicosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscaServicosMouseClicked


    }//GEN-LAST:event_tblBuscaServicosMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        txtBuscaServicos.setText(null);
        read();

    }//GEN-LAST:event_formWindowClosed

    private void btnAdicionarServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarServicoActionPerformed

        this.dispose();
        txtBuscaServicos.setText(null);
        adiciona_produto();
        os.table_read_services();
        spinQuantidadeServicos.setValue(1);
        //botar o metodo read sempre que fechar
    }//GEN-LAST:event_btnAdicionarServicoActionPerformed

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
            java.util.logging.Logger.getLogger(TelaBuscaServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaBuscaServicos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarServico;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner spinQuantidadeServicos;
    private javax.swing.JTable tblBuscaServicos;
    private javax.swing.JTextField txtBuscaServicos;
    // End of variables declaration//GEN-END:variables
}
