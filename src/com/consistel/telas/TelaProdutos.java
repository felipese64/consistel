package com.consistel.telas;

import com.consistel.dal.ModuloConexao;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.sql.*;
import com.consistel.dal.ModuloConexao;
import java.awt.Color;
import net.proteanit.sql.DbUtils;

public class TelaProdutos extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public TelaProdutos() {
        initComponents();
        conexao = ModuloConexao.conector();
        read();
        txtDescProdutos.setWrapStyleWord(true);
        txtDescProdutos.setLineWrap(true);
    }

    //quebrar linha
    //tratar exceção de nome unique
    public void reset_campos() {

        txtIdProdutos.setText(null);
        txtNomeProdutos.setText(null);
        txtDescProdutos.setText(null);
        txtMarcaProdutos.setText(null);
        txtCustoProdutos.setText(null);
        spinMargemProdutos.setValue(50);
        txtPrecoProdutos.setText(null);
        cboUnidadeProdutos.setSelectedItem("Pç.");
        cboGrupoProdutos.setSelectedItem("Alarme");
        lblNomeProdutos.setForeground(Color.black);
        lblPrecoProdutos.setForeground(Color.black);

    }

    public void create() {

        String sql = "insert into tbprodutos (nome_prod, desc_prod, grupo_prod, marca_prod, custo_prod, margem_prod, preco_prod, unidade_prod) values (?,?,?,?,?,?,?,?);";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeProdutos.getText());
            pst.setString(2, txtDescProdutos.getText());
            pst.setString(3, cboGrupoProdutos.getSelectedItem().toString());
            pst.setString(4, txtMarcaProdutos.getText());
            pst.setString(5, txtCustoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(6, spinMargemProdutos.getValue().toString());
            pst.setString(7, txtPrecoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(8, cboUnidadeProdutos.getSelectedItem().toString());

            if (txtNomeProdutos.getText().isEmpty() || txtPrecoProdutos.getText().isEmpty()) {

                lblNomeProdutos.setForeground(Color.red);
                lblPrecoProdutos.setForeground(Color.red);
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void read() {

        String sql = "select id_prod as ID, nome_prod as Nome, custo_prod as Custo, preco_prod as Preco from tbProdutos where nome_prod like ? order by nome_prod;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtBuscaProdutos.getText() + "%");
            rs = pst.executeQuery();
            tblBuscaProdutos.setModel(DbUtils.resultSetToTableModel(rs));
            txtIdProdutos.setText(null);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void set_idfield() {

        String sql = "select id_prod from tbprodutos where nome_prod = ?";

        int setar = tblBuscaProdutos.getSelectedRow();
        String nome = tblBuscaProdutos.getModel().getValueAt(setar, 1).toString();

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome);
            rs = pst.executeQuery();

            if (rs.next()) {
                txtIdProdutos.setText(rs.getString(1));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void set_fields() {

        set_idfield();
        String sql = "select * from tbprodutos where id_prod = ?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdProdutos.getText());
            rs = pst.executeQuery();

            if (rs.next()) {

                txtNomeProdutos.setText(rs.getString(2));
                txtDescProdutos.setText(rs.getString(3));
                cboGrupoProdutos.setSelectedItem(rs.getString(4));
                txtMarcaProdutos.setText(rs.getString(5));
                //txtCustoProdutos.setText(df.format(Double.parseDouble(rs.getString(6))));

                double CustoProdutos = Double.parseDouble(rs.getString(6));
                if (CustoProdutos < 1) {
                    txtCustoProdutos.setText("0" + df.format(CustoProdutos));
                } else {
                    txtCustoProdutos.setText(df.format(CustoProdutos));
                }

                spinMargemProdutos.setValue(Integer.parseInt(rs.getString(7)));
                //txtPrecoProdutos.setText(df.format(Double.parseDouble(rs.getString(8))));

                double PrecoProdutos = Double.parseDouble(rs.getString(8));

                if (PrecoProdutos < 1) {
                    txtPrecoProdutos.setText("0" + df.format(PrecoProdutos));
                } else {
                    txtPrecoProdutos.setText(df.format(PrecoProdutos));
                }

                cboUnidadeProdutos.setSelectedItem(rs.getString(9));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void formacao_respreco() {

        try {

            double custo;
            if (txtCustoProdutos.getText().isEmpty()) {
                custo = 1;
            } else {
                custo = Double.parseDouble(txtCustoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            }

            double preco;
            if (txtPrecoProdutos.getText().isEmpty()) {
                preco = 1;
            } else {
                preco = Double.parseDouble(txtPrecoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            }

            String margem1 = spinMargemProdutos.getValue().toString();
            double margem = Double.parseDouble(margem1);
            double multiplicador = (margem / 100) + 1;
            //DecimalFormat df = new DecimalFormat("#,###.00");

            double resultadoCusto = preco / multiplicador;
            double resultadoPreco = custo * multiplicador;
            String resultadoPrecoF = df.format(resultadoPreco);
            String resultadoCustoF = df.format(resultadoCusto);

            if (resultadoCusto < 1) {
                resultadoCustoF = "0" + resultadoCustoF;
            }
            if (resultadoPreco < 1) {
                resultadoPrecoF = "0" + resultadoPrecoF;
            }

            if (txtCustoProdutos.getText().isEmpty() && txtPrecoProdutos.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha pelo menos um dos campos da formação de preço!");
            } else {

                txtPrecoProdutos.setText(resultadoPrecoF);
            }
        } catch (Exception e) {
        }

    }

    public void formacao_rescusto() {

        try {

            double custo;
            if (txtCustoProdutos.getText().isEmpty()) {
                custo = 1;
            } else {
                custo = Double.parseDouble(txtCustoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            }

            double preco;
            if (txtPrecoProdutos.getText().isEmpty()) {
                preco = 1;
            } else {
                preco = Double.parseDouble(txtPrecoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            }

            String margem1 = spinMargemProdutos.getValue().toString();
            double margem = Double.parseDouble(margem1);
            double multiplicador = (margem / 100) + 1;
            //DecimalFormat df = new DecimalFormat("#,###.00");

            double resultadoCusto = preco / multiplicador;
            double resultadoPreco = custo * multiplicador;

            String resultadoPrecoF = df.format(resultadoPreco);
            String resultadoCustoF = df.format(resultadoCusto);

            if (resultadoCusto < 1) {
                resultadoCustoF = "0" + resultadoCustoF;
            }
            if (resultadoPreco < 1) {
                resultadoPrecoF = "0" + resultadoPrecoF;
            }

            if (txtCustoProdutos.getText().isEmpty() && txtPrecoProdutos.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha pelo menos um dos campos da formação de preço!");
            } else {

                txtCustoProdutos.setText(resultadoCustoF);
            }
        } catch (Exception e) {
        }

    }

    public void update() {

        String sql = "update tbprodutos set nome_prod = ?,desc_prod = ?,grupo_prod = ?,marca_prod = ?,custo_prod = ?,margem_prod = ?,preco_prod = ?,unidade_prod = ? where id_prod = ?;";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtNomeProdutos.getText());
            pst.setString(2, txtDescProdutos.getText());
            pst.setString(3, cboGrupoProdutos.getSelectedItem().toString());
            pst.setString(4, txtMarcaProdutos.getText());
            pst.setString(5, txtCustoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(6, spinMargemProdutos.getValue().toString());
            pst.setString(7, txtPrecoProdutos.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(8, cboUnidadeProdutos.getSelectedItem().toString());
            pst.setString(9, txtIdProdutos.getText());

            if (txtNomeProdutos.getText().isEmpty() || txtPrecoProdutos.getText().isEmpty()) {

                lblNomeProdutos.setForeground(Color.red);
                lblPrecoProdutos.setForeground(Color.red);
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else {

                int editado = pst.executeUpdate();
                if (editado > 0) {
                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Dados do produto alterados com sucesso!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void delete() {

        int apagar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esse produto?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (apagar == JOptionPane.YES_OPTION) {

            String sql = "delete from tbprodutos where id_prod = ?;";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdProdutos.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {

                    reset_campos();
                    read();
                    JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBuscaProdutos = new javax.swing.JTable();
        txtBuscaProdutos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdProdutos = new javax.swing.JTextField();
        lblNomeProdutos = new javax.swing.JLabel();
        txtNomeProdutos = new javax.swing.JTextField();
        txtMarcaProdutos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPrecoProdutos = new javax.swing.JTextField();
        lblPrecoProdutos = new javax.swing.JLabel();
        txtCustoProdutos = new javax.swing.JTextField();
        spinMargemProdutos = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboGrupoProdutos = new javax.swing.JComboBox<>();
        cboUnidadeProdutos = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDescProdutos = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Produtos");
        setPreferredSize(new java.awt.Dimension(1349, 554));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/delete32.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
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
        tblBuscaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuscaProdutosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBuscaProdutos);

        txtBuscaProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaProdutosKeyReleased(evt);
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
                        .addComponent(txtBuscaProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(txtBuscaProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("ID");

        txtIdProdutos.setEditable(false);

        lblNomeProdutos.setText("Nome do Produto");

        jLabel5.setText("Marca");

        jLabel3.setText("Descrição");

        txtPrecoProdutos.setToolTipText("");
        txtPrecoProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecoProdutosKeyReleased(evt);
            }
        });

        lblPrecoProdutos.setText("Preço");

        txtCustoProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCustoProdutosKeyReleased(evt);
            }
        });

        spinMargemProdutos.setModel(new javax.swing.SpinnerNumberModel(50.0d, 0.0d, 500.0d, 1.0d));
        spinMargemProdutos.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinMargemProdutosStateChanged(evt);
            }
        });

        jLabel7.setText("Margem (%)");

        jLabel6.setText("Custo");

        cboGrupoProdutos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alarme", "Acessórios", "Automatizadores", "Cerca Elétrica", "CFTV", "Interfones", "PABX" }));

        cboUnidadeProdutos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pç.", "m", "Pct." }));

        jLabel9.setText("Unidade");

        jLabel4.setText("Grupo");

        jLabel10.setText("R$");

        jLabel12.setText("R$");

        txtDescProdutos.setColumns(20);
        txtDescProdutos.setRows(5);
        jScrollPane3.setViewportView(txtDescProdutos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(329, 329, 329)
                                .addComponent(jLabel4))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtIdProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(290, 290, 290)
                                .addComponent(cboGrupoProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblNomeProdutos)
                                .addGap(257, 257, 257)
                                .addComponent(jLabel6)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(cboUnidadeProdutos, 0, 112, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtMarcaProdutos)
                            .addComponent(txtNomeProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecoProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtCustoProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(spinMargemProdutos))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(311, 311, 311)
                        .addComponent(lblPrecoProdutos))
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane3))
                .addGap(22, 22, 22))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboGrupoProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboUnidadeProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNomeProdutos)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNomeProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCustoProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spinMargemProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(lblPrecoProdutos))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecoProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMarcaProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
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

    private void tblBuscaProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscaProdutosMouseClicked
        
        set_fields();
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBuscaProdutosMouseClicked

    private void spinMargemProdutosStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinMargemProdutosStateChanged
        formacao_respreco();
        // TODO add your handling code here:
    }//GEN-LAST:event_spinMargemProdutosStateChanged

    private void txtCustoProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustoProdutosKeyReleased
        formacao_respreco();
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustoProdutosKeyReleased

    private void txtPrecoProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoProdutosKeyReleased
        formacao_rescusto();
    }//GEN-LAST:event_txtPrecoProdutosKeyReleased

    private void txtBuscaProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaProdutosKeyReleased
        read();
    }//GEN-LAST:event_txtBuscaProdutosKeyReleased

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
    private javax.swing.JComboBox<String> cboGrupoProdutos;
    private javax.swing.JComboBox<String> cboUnidadeProdutos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblNomeProdutos;
    private javax.swing.JLabel lblPrecoProdutos;
    private javax.swing.JSpinner spinMargemProdutos;
    private javax.swing.JTable tblBuscaProdutos;
    private javax.swing.JTextField txtBuscaProdutos;
    private javax.swing.JTextField txtCustoProdutos;
    private javax.swing.JTextArea txtDescProdutos;
    private javax.swing.JTextField txtIdProdutos;
    private javax.swing.JTextField txtMarcaProdutos;
    private javax.swing.JTextField txtNomeProdutos;
    private javax.swing.JTextField txtPrecoProdutos;
    // End of variables declaration//GEN-END:variables
}
