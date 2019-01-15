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

public class TelaBuscaProdutos extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public TelaBuscaProdutos() {
        initComponents();
        conexao = ModuloConexao.conector();
        txtBuscaProdutos.setText(null);
        read();
    }

    public void read() {

        String sql = "select id_prod as ID, nome_prod as Nome, custo_prod as Custo, preco_prod as Preco from tbProdutos where nome_prod like ? order by nome_prod;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtBuscaProdutos.getText() + "%");
            rs = pst.executeQuery();
            tblBuscaProdutos.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void adiciona_produto() {


            String sql = "insert into os_recebe_produto (id_os, id_prod, quantidade) values (?,?,?)";

            try {

                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtIdOs.getText());

                int setar = tblBuscaProdutos.getSelectedRow();
                pst.setString(2, tblBuscaProdutos.getModel().getValueAt(setar, 0).toString());
                pst.setString(3, spinQuantidadeProduto.getValue().toString());

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
    public void table_read_products() {

        String sql = "select p.id_prod as ID, p.nome_prod as Nome, p.unidade_prod as Unidade, sum(op.quantidade) as Quant, p.preco_prod as 'Preço Unit.', (p.preco_prod * sum(op.quantidade)) as 'Preço Total' from tbos join os_recebe_produto op on tbos.id_os = op.id_os join tbprodutos p on p.id_prod = op.id_prod where tbos.id_os = ? group by p.id_prod;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdOs.getText());
            rs = pst.executeQuery();

            tblProdutosOs.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
*/
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscaProdutos = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        txtBuscaProdutos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        spinQuantidadeProduto = new javax.swing.JSpinner();
        btnAdicionarProduto = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        tblBuscaProdutos.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBuscaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuscaProdutosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBuscaProdutos);

        jLabel23.setText("Produto");

        txtBuscaProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaProdutosKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search.png"))); // NOI18N

        spinQuantidadeProduto.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));

        btnAdicionarProduto.setText("Adicionar");
        btnAdicionarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarProdutoActionPerformed(evt);
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
                        .addComponent(spinQuantidadeProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAdicionarProduto))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtBuscaProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(txtBuscaProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinQuantidadeProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionarProduto))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscaProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaProdutosKeyReleased
        read();
    }//GEN-LAST:event_txtBuscaProdutosKeyReleased

    private void tblBuscaProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscaProdutosMouseClicked


    }//GEN-LAST:event_tblBuscaProdutosMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        txtBuscaProdutos.setText(null);
        read();

    }//GEN-LAST:event_formWindowClosed

    private void btnAdicionarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarProdutoActionPerformed

        this.dispose();
        txtBuscaProdutos.setText(null);
        adiciona_produto();
        os.table_read_products();
        spinQuantidadeProduto.setValue(1);
        //botar o metodo read sempre que fechar
    }//GEN-LAST:event_btnAdicionarProdutoActionPerformed

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
            java.util.logging.Logger.getLogger(TelaBuscaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaBuscaProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaBuscaProdutos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionarProduto;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner spinQuantidadeProduto;
    private javax.swing.JTable tblBuscaProdutos;
    private javax.swing.JTextField txtBuscaProdutos;
    // End of variables declaration//GEN-END:variables
}
