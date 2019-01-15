package com.consistel.telas;

import com.consistel.dal.ModuloConexao;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.sql.*;
import com.consistel.dal.ModuloConexao;
import java.awt.Color;
import net.proteanit.sql.DbUtils;

public class TelaServicos extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DecimalFormat df = new DecimalFormat("#,###.00");
    

    public TelaServicos() {
        initComponents();
        conexao = ModuloConexao.conector();
        read();
        txtDescServicos.setWrapStyleWord(true);
        txtDescServicos.setLineWrap(true);
        
    }

    //quebrar linha
    //tratar exceção de nome unique
    public void reset_campos() {

        txtIdServicos.setText(null);
        txtNomeServicos.setText(null);
        txtDescServicos.setText(null);
        txtPrecoHoraServicos.setText("60,00");
        spinDuracaoServicos.setValue(1);
        txtPrecoServicos.setText(null);
        lblNomeServicos.setForeground(Color.black);
        lblPrecoServicos.setForeground(Color.black);

    }

    public void create() {

        String sql = "insert into tbservicos (nome_serv, desc_serv, horas_serv, preco_hora_serv, preco_serv) values (?,?,?,?,?);";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeServicos.getText());
            pst.setString(2, txtDescServicos.getText());
            pst.setString(3, spinDuracaoServicos.getValue().toString());
            pst.setString(4, txtPrecoHoraServicos.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(5, txtPrecoServicos.getText().replaceAll("\\.", "").replace(",", "."));

            if (txtNomeServicos.getText().isEmpty() || txtPrecoServicos.getText().isEmpty()) {

                lblNomeServicos.setForeground(Color.red);
                lblPrecoServicos.setForeground(Color.red);
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Serviço adicionado com sucesso!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void read() {

        String sql = "select id_serv as ID, nome_serv as Nome, preco_hora_serv as Hora, preco_serv as Preco from tbservicos where nome_serv like ? order by nome_serv;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtBuscaServicos.getText() + "%");
            rs = pst.executeQuery();
            tblBuscaServicos.setModel(DbUtils.resultSetToTableModel(rs));
            txtIdServicos.setText(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void set_idfield() {

        String sql = "select id_serv from tbservicos where nome_serv = ?";

        int setar = tblBuscaServicos.getSelectedRow();
        String nome = tblBuscaServicos.getModel().getValueAt(setar, 1).toString();

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome);
            rs = pst.executeQuery();

            if (rs.next()) {
                txtIdServicos.setText(rs.getString(1));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void set_fields() {

        set_idfield();
        String sql = "select * from tbservicos where id_serv = ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdServicos.getText());
            rs = pst.executeQuery();

            if (rs.next()) {

                txtNomeServicos.setText(rs.getString(2));
                txtDescServicos.setText(rs.getString(3));
                spinDuracaoServicos.setValue(Double.parseDouble(rs.getString(4)));

                double PrecoHoraServicos = Double.parseDouble(rs.getString(5));
                if (PrecoHoraServicos < 1) {
                    txtPrecoHoraServicos.setText("0" + df.format(PrecoHoraServicos));
                } else {
                    txtPrecoHoraServicos.setText(df.format(PrecoHoraServicos));
                }

                double PrecoServicos = Double.parseDouble(rs.getString(6));

                if (PrecoServicos < 1) {
                    txtPrecoServicos.setText("0" + df.format(PrecoServicos));
                } else {
                    txtPrecoServicos.setText(df.format(PrecoServicos));
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void update() {

        String sql = "update tbservicos set nome_serv = ?,desc_serv = ?,horas_serv = ?,preco_hora_serv = ?,preco_serv = ? where id_serv = ?;";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtNomeServicos.getText());
            pst.setString(2, txtDescServicos.getText());
            pst.setString(3, spinDuracaoServicos.getValue().toString());
            pst.setString(4, txtPrecoHoraServicos.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(5, txtPrecoServicos.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(6, txtIdServicos.getText());

            if (txtNomeServicos.getText().isEmpty() || txtPrecoServicos.getText().isEmpty()) {

                lblNomeServicos.setForeground(Color.red);
                lblPrecoServicos.setForeground(Color.red);
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                int editado = pst.executeUpdate();
                if (editado > 0) {
                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Dados do serviço alterados com sucesso!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void delete() {

        int apagar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esse serviço?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (apagar == JOptionPane.YES_OPTION) {

            String sql = "delete from tbservicos where id_serv = ?;";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdServicos.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {

                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Serviço excluído com sucesso!");

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

    }

    public void setar_hora() {

        try {

            String duracaotxt = spinDuracaoServicos.getValue().toString();
            double duracao = Double.parseDouble(duracaotxt);
            double preco_hora = Double.parseDouble(txtPrecoHoraServicos.getText().replaceAll("\\.", "").replace(",", "."));
            double resultado = duracao * preco_hora;
            String resultadotxt = df.format(resultado);

            if (resultado < 1) {
                resultadotxt = "0" + resultadotxt;
            }

            txtPrecoServicos.setText(resultadotxt);

        } catch (java.lang.NumberFormatException e) {
        }
    }

    public void setar_preco() {

        try {

            String duracaotxt = spinDuracaoServicos.getValue().toString();
            double duracao = Double.parseDouble(duracaotxt);
            double preco = Double.parseDouble(txtPrecoServicos.getText().replaceAll("\\.", "").replace(",", "."));
            double resultado = preco / duracao;
            String resultadotxt = df.format(resultado);

            if (resultado < 1) {
                resultadotxt = "0" + resultadotxt;
            }

            txtPrecoHoraServicos.setText(resultadotxt);

        } catch (java.lang.NumberFormatException e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscaServicos = new javax.swing.JTable();
        txtBuscaServicos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdServicos = new javax.swing.JTextField();
        lblNomeServicos = new javax.swing.JLabel();
        txtNomeServicos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPrecoServicos = new javax.swing.JTextField();
        lblPrecoServicos = new javax.swing.JLabel();
        txtPrecoHoraServicos = new javax.swing.JTextField();
        spinDuracaoServicos = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDescServicos = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Serviços");
        setPreferredSize(new java.awt.Dimension(1349, 554));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/delete32.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
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

        txtBuscaServicos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaServicosKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search.png"))); // NOI18N

        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/save32.png"))); // NOI18N
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/edit32.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtBuscaServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(86, 86, 86)
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnCreate)
                        .addComponent(btnUpdate)
                        .addComponent(btnDelete))
                    .addComponent(txtBuscaServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("ID");

        txtIdServicos.setEditable(false);

        lblNomeServicos.setText("Nome do Serviço");

        jLabel3.setText("Descrição");

        txtPrecoServicos.setToolTipText("");
        txtPrecoServicos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecoServicosKeyReleased(evt);
            }
        });

        lblPrecoServicos.setText("Preço do serviço");

        txtPrecoHoraServicos.setText("60,00");
        txtPrecoHoraServicos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecoHoraServicosKeyReleased(evt);
            }
        });

        spinDuracaoServicos.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(50.0f), Float.valueOf(0.1f)));
        spinDuracaoServicos.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinDuracaoServicosStateChanged(evt);
            }
        });

        jLabel7.setText("Duração em horas");

        jLabel6.setText("Preço por hora");

        jLabel10.setText("R$");

        jLabel12.setText("R$");

        txtDescServicos.setColumns(20);
        txtDescServicos.setRows(5);
        jScrollPane3.setViewportView(txtDescServicos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtIdServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spinDuracaoServicos))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrecoHoraServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel6)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrecoServicos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(lblPrecoServicos))))
                    .addComponent(lblNomeServicos))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(txtIdServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblNomeServicos)
                        .addGap(6, 6, 6)
                        .addComponent(txtNomeServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(6, 6, 6)
                        .addComponent(spinDuracaoServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecoHoraServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPrecoServicos)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecoServicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setBounds(0, 0, 1349, 554);
    }// </editor-fold>//GEN-END:initComponents

    private void tblBuscaServicosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscaServicosMouseClicked
        set_fields();
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBuscaServicosMouseClicked

    private void spinDuracaoServicosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinDuracaoServicosStateChanged
        setar_hora();
    }//GEN-LAST:event_spinDuracaoServicosStateChanged

    private void txtPrecoHoraServicosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoHoraServicosKeyReleased
        setar_hora();
    }//GEN-LAST:event_txtPrecoHoraServicosKeyReleased

    private void txtPrecoServicosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoServicosKeyReleased
        setar_preco();
    }//GEN-LAST:event_txtPrecoServicosKeyReleased

    private void txtBuscaServicosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaServicosKeyReleased
        read();
    }//GEN-LAST:event_txtBuscaServicosKeyReleased

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        create();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblNomeServicos;
    private javax.swing.JLabel lblPrecoServicos;
    private javax.swing.JSpinner spinDuracaoServicos;
    private javax.swing.JTable tblBuscaServicos;
    private javax.swing.JTextField txtBuscaServicos;
    private javax.swing.JTextArea txtDescServicos;
    private javax.swing.JTextField txtIdServicos;
    private javax.swing.JTextField txtNomeServicos;
    private javax.swing.JTextField txtPrecoHoraServicos;
    private javax.swing.JTextField txtPrecoServicos;
    // End of variables declaration//GEN-END:variables
}
