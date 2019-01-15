package com.consistel.telas;

import com.consistel.dal.ModuloConexao;
import java.awt.Color;
import java.sql.*;
import java.text.DateFormat;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaClientes extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DateFormat ddmmaa = DateFormat.getDateInstance(DateFormat.SHORT);

    public TelaClientes() {
        initComponents();
        //abaixo faz o text area Obs quebrar linha
        panObsCli.setWrapStyleWord(true);
        panObsCli.setLineWrap(true);
        conexao = ModuloConexao.conector();
        read();
        txtIdCli.setEditable(false);
        lblData.setVisible(false);
        lblData2.setVisible(false);

        //achar o comando pra tirar a permissao de editar a tabela
        //metodo pra impedir que cadastre 2 nomes iguais --> resolvido, agora é só tratar a exceção
        //colocar cidade como default nao adiantou nada, vai ter que ser como combo box
    }

    public void reset_campos() {

        txtNomeCli.setText(null);
        txtApelidoCli.setText(null);
        txtEmailCli.setText(null);
        txtCpfCli.setText(null);
        cboTipoPessoaCli.setSelectedItem("Física");
        txtRgCli.setText(null);
        txtTelCli.setText(null);
        txtCelCli.setText(null);
        panObsCli.setText(null);
        cboTipoEndCli.setSelectedItem("Casa");
        txtEndCli.setText(null);
        txtComplementoCli.setText(null);
        txtBairroCli.setText(null);
        cboUfCli.setSelectedItem("MS");
        cboCidadeCli.setSelectedItem("Campo Grande");
        txtCepCli.setText(null);
        lblData.setVisible(false);
        lblData2.setVisible(false);

    }

    public void create() {

        String sql = "insert into tbclientes (nomecli,fantasiacli,emailcli,cpfcli,tpessoacli,rgiecli,telcli,celcli,obscli,tipoendcli,endcli,complementocli,bairrocli,estadocli,cidadecli,cepcli) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeCli.getText());
            pst.setString(2, txtApelidoCli.getText());
            pst.setString(3, txtEmailCli.getText());
            pst.setString(4, txtCpfCli.getText());
            pst.setString(5, cboTipoPessoaCli.getSelectedItem().toString());
            pst.setString(6, txtRgCli.getText());
            pst.setString(7, txtTelCli.getText());
            pst.setString(8, txtCelCli.getText());
            pst.setString(9, panObsCli.getText());
            pst.setString(10, cboTipoEndCli.getSelectedItem().toString());
            pst.setString(11, txtEndCli.getText());
            pst.setString(12, txtComplementoCli.getText());
            pst.setString(13, txtBairroCli.getText());
            pst.setString(14, cboUfCli.getSelectedItem().toString());
            pst.setString(15, cboCidadeCli.getSelectedItem().toString());
            pst.setString(16, txtCepCli.getText());

            if (txtNomeCli.getText().isEmpty() || txtTelCli.getText().isEmpty() || txtEndCli.getText().isEmpty()) {

                lblTelCli.setForeground(Color.red);
                lblNomeCli.setForeground(Color.red);
                lblEndCli.setForeground(Color.red);
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso!");
                }
            }

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Este nome já esta sendo usado em outro cliente!");

        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }

    }

    public void read() {

        String sql = "select idcli as ID, nomecli as Nome, endcli as Endereço, telcli as Tel, celcli as Cel from tbclientes where nomecli like ? order by nomecli;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtBusca.getText() + "%");
            rs = pst.executeQuery();
            tblBuscaCli.setModel(DbUtils.resultSetToTableModel(rs));
            txtIdCli.setText(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void set_idfield() {

        String sql = "select idcli from tbclientes where nomecli = ?";

        int setar = tblBuscaCli.getSelectedRow();
        String nome = tblBuscaCli.getModel().getValueAt(setar, 1).toString();

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome);
            rs = pst.executeQuery();

            if (rs.next()) {
                txtIdCli.setText(rs.getString(1));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void set_fields() {

        String sql = "select * from tbclientes where idcli = ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdCli.getText());
            rs = pst.executeQuery();

            if (rs.next()) {

                txtNomeCli.setText(rs.getString(2));
                txtApelidoCli.setText(rs.getString(3));
                txtEmailCli.setText(rs.getString(4));
                txtCpfCli.setText(rs.getString(5));
                cboTipoPessoaCli.setSelectedItem(rs.getString(6));
                txtRgCli.setText(rs.getString(7));
                txtTelCli.setText(rs.getString(8));
                txtCelCli.setText(rs.getString(9));

                lblData.setText(ddmmaa.format(rs.getObject(10)));

                //lblData.setText(rs.getString(10));
                panObsCli.setText(rs.getString(11));
                cboTipoEndCli.setSelectedItem(rs.getString(12));
                txtEndCli.setText(rs.getString(13));
                txtComplementoCli.setText(rs.getString(14));
                txtBairroCli.setText(rs.getString(15));
                cboUfCli.setSelectedItem(rs.getString(16));
                cboCidadeCli.setSelectedItem(rs.getString(17));
                txtCepCli.setText(rs.getString(18));

                lblData.setVisible(true);
                lblData2.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void update() {

        String sql = "update tbclientes set nomecli = ?,fantasiacli = ?,emailcli = ?,cpfcli = ?,tpessoacli = ?,rgiecli = ?,telcli = ?,celcli = ?,obscli = ?,tipoendcli = ?,endcli = ?,complementocli = ?,bairrocli = ?,estadocli = ?,cidadecli = ?,cepcli = ? where idcli = ?;";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtNomeCli.getText());
            pst.setString(2, txtApelidoCli.getText());
            pst.setString(3, txtEmailCli.getText());
            pst.setString(4, txtCpfCli.getText());
            pst.setString(5, cboTipoPessoaCli.getSelectedItem().toString());
            pst.setString(6, txtRgCli.getText());
            pst.setString(7, txtTelCli.getText());
            pst.setString(8, txtCelCli.getText());
            pst.setString(9, panObsCli.getText());
            pst.setString(10, cboTipoEndCli.getSelectedItem().toString());
            pst.setString(11, txtEndCli.getText());
            pst.setString(12, txtComplementoCli.getText());
            pst.setString(13, txtBairroCli.getText());
            pst.setString(14, cboUfCli.getSelectedItem().toString());
            pst.setString(15, cboCidadeCli.getSelectedItem().toString());
            pst.setString(16, txtCepCli.getText());
            pst.setString(17, txtIdCli.getText());

            if (txtNomeCli.getText().isEmpty() || txtTelCli.getText().isEmpty() || txtEndCli.getText().isEmpty()) {

                lblTelCli.setForeground(Color.red);
                lblNomeCli.setForeground(Color.red);
                lblEndCli.setForeground(Color.red);
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                int editado = pst.executeUpdate();
                if (editado > 0) {
                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void delete() {

        int apagar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esse cliente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (apagar == JOptionPane.YES_OPTION) {

            String sql = "delete from tbclientes where idcli = ?;";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdCli.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {

                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!");

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtBusca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtIdCli = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lblNomeCli = new javax.swing.JLabel();
        txtNomeCli = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cboTipoPessoaCli = new javax.swing.JComboBox<>();
        lblTelCli = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblApelidoCli = new javax.swing.JLabel();
        txtApelidoCli = new javax.swing.JTextField();
        txtTelCli = new javax.swing.JTextField();
        txtCelCli = new javax.swing.JTextField();
        lblData2 = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        lblCpfCli = new javax.swing.JLabel();
        lblRgCli = new javax.swing.JLabel();
        txtCpfCli = new javax.swing.JTextField();
        txtRgCli = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        lblEndCli = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtEndCli = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cboTipoEndCli = new javax.swing.JComboBox<>();
        txtBairroCli = new javax.swing.JTextField();
        txtComplementoCli = new javax.swing.JTextField();
        txtCepCli = new javax.swing.JTextField();
        txtEmailCli = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        panObsCli = new javax.swing.JTextArea();
        cboCidadeCli = new javax.swing.JComboBox<>();
        cboUfCli = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscaCli = new javax.swing.JTable();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Clientes");
        setPreferredSize(new java.awt.Dimension(1349, 554));

        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaKeyReleased(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search.png"))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setName(""); // NOI18N

        jLabel2.setText("ID");

        lblNomeCli.setText("Nome");

        jLabel5.setText("Tipo de pessoa");

        cboTipoPessoaCli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Física", "Jurídica" }));
        cboTipoPessoaCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTipoPessoaCliActionPerformed(evt);
            }
        });

        lblTelCli.setText("Telefone");

        jLabel10.setText("Celular");

        lblApelidoCli.setText("Apelido");

        lblData2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblData2.setText("Data de cadastro");

        lblData.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblData.setText("01/01/00");

        lblCpfCli.setText("CPF");

        lblRgCli.setText("RG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblApelidoCli)
                            .addComponent(lblCpfCli))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCpfCli, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                            .addComponent(txtApelidoCli)
                            .addComponent(lblNomeCli, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNomeCli))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRgCli, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTelCli)
                            .addComponent(txtCelCli)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblRgCli)
                                    .addComponent(jLabel10)
                                    .addComponent(lblTelCli))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(138, 138, 138)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboTipoPessoaCli, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(112, 112, 112)
                                        .addComponent(lblData))
                                    .addComponent(lblData2)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(100, 100, 100)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblData2)
                            .addComponent(lblData))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNomeCli)
                            .addComponent(lblTelCli)))
                    .addComponent(cboTipoPessoaCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApelidoCli)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApelidoCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCelCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCpfCli)
                    .addComponent(lblRgCli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCpfCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRgCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lblEndCli.setText("Endereço");

        jLabel16.setText("Complemento");

        jLabel17.setText("Bairro");

        jLabel18.setText("Estado");

        jLabel19.setText("Cidade");

        jLabel20.setText("CEP");

        jLabel13.setText("Observação");

        jLabel14.setText("Tipo de endereço");

        jLabel7.setText("Email");

        cboTipoEndCli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Casa", "Apartamento", "Comércio", "Indústria" }));

        panObsCli.setColumns(20);
        panObsCli.setRows(5);
        jScrollPane3.setViewportView(panObsCli);

        cboCidadeCli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Campo Grande", "Bonito", "Aquidauana" }));

        cboUfCli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MS" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtCepCli, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(cboUfCli, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEndCli)
                            .addComponent(txtEndCli, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(txtComplementoCli, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(cboTipoEndCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBairroCli)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel17)
                                    .addComponent(cboCidadeCli, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtEmailCli, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel18)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboTipoEndCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmailCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEndCli)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEndCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBairroCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtComplementoCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCidadeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCepCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboUfCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblBuscaCli.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBuscaCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuscaCliMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBuscaCli);

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/edit32.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/delete32.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/save32.png"))); // NOI18N
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnUpdate, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnCreate, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(23, 23, 23)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        setBounds(0, 0, 1349, 554);
    }// </editor-fold>//GEN-END:initComponents

    private void cboTipoPessoaCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTipoPessoaCliActionPerformed

        String a = cboTipoPessoaCli.getSelectedItem().toString();
        if (a.equals("Jurídica")) {
            lblNomeCli.setText("Razão Social");
            lblApelidoCli.setText("Nome Fantasia");
            lblCpfCli.setText("CNPJ");
            lblRgCli.setText("IE");
        } else {
            lblNomeCli.setText("Nome");
            lblApelidoCli.setText("Apelido");
            lblCpfCli.setText("CPF");
            lblRgCli.setText("RG");

        }
    }//GEN-LAST:event_cboTipoPessoaCliActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        create();

    }//GEN-LAST:event_btnCreateActionPerformed

    private void txtBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaKeyReleased
        read();
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscaKeyReleased

    private void tblBuscaCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscaCliMouseClicked
        set_idfield();
        set_fields();
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBuscaCliMouseClicked

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        delete();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboCidadeCli;
    private javax.swing.JComboBox<String> cboTipoEndCli;
    private javax.swing.JComboBox<String> cboTipoPessoaCli;
    private javax.swing.JComboBox<String> cboUfCli;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblApelidoCli;
    private javax.swing.JLabel lblCpfCli;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblData2;
    private javax.swing.JLabel lblEndCli;
    private javax.swing.JLabel lblNomeCli;
    private javax.swing.JLabel lblRgCli;
    private javax.swing.JLabel lblTelCli;
    private javax.swing.JTextArea panObsCli;
    private javax.swing.JTable tblBuscaCli;
    private javax.swing.JTextField txtApelidoCli;
    private javax.swing.JTextField txtBairroCli;
    private javax.swing.JTextField txtBusca;
    private javax.swing.JTextField txtCelCli;
    private javax.swing.JTextField txtCepCli;
    private javax.swing.JTextField txtComplementoCli;
    private javax.swing.JTextField txtCpfCli;
    private javax.swing.JTextField txtEmailCli;
    private javax.swing.JTextField txtEndCli;
    private javax.swing.JTextField txtIdCli;
    private javax.swing.JTextField txtNomeCli;
    private javax.swing.JTextField txtRgCli;
    private javax.swing.JTextField txtTelCli;
    // End of variables declaration//GEN-END:variables
}
