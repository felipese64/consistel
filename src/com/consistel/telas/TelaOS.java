package com.consistel.telas;

import com.consistel.dal.ModuloConexao;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.sql.*;
import com.consistel.dal.ModuloConexao;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class TelaOS extends javax.swing.JInternalFrame {

    public TelaBuscaProdutos buscaprod = new TelaBuscaProdutos();
    public TelaBuscaServicos buscaserv = new TelaBuscaServicos();

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    DateFormat ddmmaa = DateFormat.getDateInstance(DateFormat.SHORT);
    boolean campos_alterados = false;
    String condicao_cheque = "0";
    String condicao_cartao = "0";
    public int id = 0;
    int ultimo_id = 0;
    DecimalFormat df = new DecimalFormat("#,###.00");
    double somaprodutos = 0;
    double somaservicos = 0;
    SimpleDateFormat dataFechamento = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));

    //botar timezone
    TimeZone tz = new SimpleTimeZone(Calendar.ZONE_OFFSET, "GMT-04:00");
    Calendar ca = GregorianCalendar.getInstance(tz);

    public TelaOS() {
        initComponents();
        conexao = ModuloConexao.conector();
        rbtnOS.setSelected(true);
        rbtnFechamentoPgAvista.setSelected(true);
        spnParcelasPgFechamentoOs.setVisible(false);
        lblParcelas.setVisible(false);
        reset_prod_serv_tables();

    }

    public void reset_fields() {
        //reseta só a primeira parte da tela OS, nao reseta nada nas abas
        //limpa_produtos();
        //table_read_products();
        reset_prod_serv_tables();
        habilita_edicao();

        txtIdOs.setText(null);
        txtDataAberturaOs.setText(null);
        txtIdOs.setText(null);
        txtClienteOs.setText(null);
        txtEnderecoClienteOs.setText(null);
        rbtnOS.setSelected(true);
        cboAtendimentoOs.setSelectedIndex(0);
        cboStatusOs.setSelectedIndex(0);
        cboTecnicoOs.setSelectedIndex(0);
        txtRelatoOs.setText(null);
        txtSolucaoOs.setText(null);
        TxtFormasPg1.setText(null);
        TxtFormasPg2.setText(null);

        lblValTotParcialOs.setText(null);
        txtDescontoOs.setText(null);
        lblValTotFinalOs.setText(null);
        lblValProdutosOs.setText(null);
        lblValServicosOs.setText(null);
        spinDescontoFormaPgAvistaOs.setValue(5);
        spinQuantParcelasFormaPgPrazoOs.setValue(3);
        rbtnFormaPgCredito.setSelected(true);
        rbtnFormaPgCheque.setSelected(true);
        cboStatusFinanceiroFechamentoOs.setSelectedIndex(0);
        txtDataFechamentoOs.setText(null);
        rbtnFechamentoPgAvista.setSelected(true);

        cboTipoPagamentoFechamentoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Dinheiro", "Débito", "Cheque", "Depósito"}));
        spnParcelasPgFechamentoOs.setVisible(false);
        lblParcelas.setVisible(false);

        cboTipoPagamentoFechamentoOs.setSelectedItem("Dinheiro");

        cboStatusOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Autorizada", "Em andamento", "Aguardando peças", "Pendente", "Finalizada"}));
        btnBuscaClienteOs.setEnabled(true);

    }

    public void set_id_os() {

        String sql = "select id_os from tbos order by id_os desc";

        try {

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                //System.out.println(rs.getString(1));

                ultimo_id = Integer.parseInt(rs.getString(1));
                id = ultimo_id + 1;

            }

            txtIdOs.setText(Integer.toString(id));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void create_os_buscacli(String idcli) {

        String sql = "insert into tbos (id_os, atendimento_os, tipo_os, status_os, relato_os, solucao_os, tecnico, condicoes_avista_os, condicoes_aprazo_os, status_financeiro_os, idcli) values (?,?,?,?,?,?,?,?,?,?,?)";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, Integer.toString(id));
            pst.setString(2, cboAtendimentoOs.getSelectedItem().toString());

            if (rbtnOS.isSelected()) {
                pst.setString(3, "Ordem de Serviço");
            } else {
                pst.setString(3, "Orçamento");
            }

            pst.setString(4, cboStatusOs.getSelectedItem().toString());
            pst.setString(5, txtRelatoOs.getText());
            pst.setString(6, txtSolucaoOs.getText());
            pst.setString(7, cboTecnicoOs.getSelectedItem().toString());
            pst.setString(8, spinDescontoFormaPgAvistaOs.getValue().toString());
            pst.setString(9, spinQuantParcelasFormaPgPrazoOs.getValue().toString());
            pst.setString(10, cboStatusFinanceiroFechamentoOs.getSelectedItem().toString());
            pst.setString(11, idcli);

            int create = pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void set_fields(String id) {

        String sql = "select * from tbos where id_os = ?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, id);

            rs = pst.executeQuery();

            if (rs.next()) {

                txtIdOs.setText(rs.getString(1));
                txtDataAberturaOs.setText(ddmmaa.format(rs.getObject(2)));

                cboAtendimentoOs.setSelectedItem(rs.getString(3));

                if (rs.getString(4).equals("Ordem de Serviço")) {
                    rbtnOS.setSelected(true);
                } else {
                    rbtnOrc.setSelected(true);
                }

                cboStatusOs.setSelectedItem(rs.getString(5));
                txtRelatoOs.setText(rs.getString(6));
                txtSolucaoOs.setText(rs.getString(7));
                cboTecnicoOs.setSelectedItem(rs.getString(8));

                if (cboStatusOs.getSelectedItem().equals("Finalizada")) {
                   //txtDataFechamentoOs.setText(null);
                    cancela_edicao();
                   txtDataFechamentoOs.setText(ddmmaa.format(rs.getObject(9)));
                } else {

                    //txtDataFechamentoOs.setText(ddmmaa.format(rs.getObject(9)));
                    
                }

                txtComentariosFechamentoOs.setText(rs.getString(10));
                lblValServicosOs.setText(rs.getString(11));
                lblValProdutosOs.setText(rs.getString(12));
                txtDescontoOs.setText(rs.getString(13));
                lblValTotParcialOs.setText(rs.getString(14));
                lblValTotFinalOs.setText(rs.getString(15));

                spinQuantParcelasFormaPgPrazoOs.setValue(Integer.parseInt(rs.getString(17)));

                if (rs.getString(18).equals("1")) {

                    rbtnFormaPgCheque.setSelected(true);
                } else {
                    rbtnFormaPgCheque.setSelected(false);
                }

                if (rs.getString(19).equals("1")) {
                    rbtnFormaPgCredito.setSelected(true);
                } else {
                    rbtnFormaPgCredito.setSelected(false);
                }

                if (!(rs.getString(20).isEmpty())) {
                    cboTipoPagamentoFechamentoOs.setSelectedItem(rs.getString(18));
                }

                if (rs.getString(21).equals("Á Vista")) {
                    rbtnFechamentoPgAvista.setSelected(true);
                } else {
                    rbtnFechamentoPgPrazo.setSelected(true);
                }

                //if (!(rs.getString(22).isEmpty())) {
                //System.out.println(rs.getString(22).isEmpty());
                //System.out.println(rs.getString(22));
                cboStatusFinanceiroFechamentoOs.setSelectedItem(rs.getString(22));
                //}
                spinDescontoFormaPgAvistaOs.setValue(Integer.parseInt(rs.getString(16)));
            }

        } /*catch (NullPointerException e) {
            

        } */ catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }

        /* 
        sql = "select nomecli, endcli from tbclientes where nomecli = ?";
        
        try {
            
            pst = conexao.prepareStatement(sql);
            int setar = tblBuscaOs.getSelectedRow();
            pst.setString(1, tblBuscaOs.getModel().getValueAt(setar, 3).toString());
            rs = pst.executeQuery();
            
            if (rs.next()) {
                
                txtClienteOs.setText(rs.getString(1));
                txtEnderecoClienteOs.setText(rs.getString(1));
            }
            
            
            
            
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
         */
    }

    public void set_client_fields(String nomecli) {

        String sql = "select nomecli, endcli from tbclientes where nomecli = ?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, nomecli);
            rs = pst.executeQuery();

            if (rs.next()) {

                txtClienteOs.setText(rs.getString(1));
                txtEnderecoClienteOs.setText(rs.getString(2));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void delete_os_buscacli() {

        String sql = "delete from tbos where id_os =?";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdOs.getText());
            int apagado = pst.executeUpdate();
            if (apagado > 0) {

                //telacli_reset_fields();
                reset_fields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void create_os(String idcli) {

        String sql = "insert into tbos (atendimento_os, tipo_os, status_os, relato_os, solucao_os, tecnico, condicoes_avista_os, condicoes_aprazo_os, status_financeiro_os, idcli) values (?,?,?,?,?,?,?,?,?,?)";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboAtendimentoOs.getSelectedItem().toString());

            if (rbtnOS.isSelected()) {
                pst.setString(2, "Ordem de Serviço");
            } else {
                pst.setString(2, "Orçamento");
            }

            pst.setString(3, cboStatusOs.getSelectedItem().toString());
            pst.setString(4, txtRelatoOs.getText());
            pst.setString(5, txtSolucaoOs.getText());
            pst.setString(6, cboTecnicoOs.getSelectedItem().toString());
            pst.setString(7, spinDescontoFormaPgAvistaOs.getValue().toString());
            pst.setString(8, spinQuantParcelasFormaPgPrazoOs.getValue().toString());
            pst.setString(9, cboStatusFinanceiroFechamentoOs.getSelectedItem().toString());
            pst.setString(10, idcli);

            if (txtClienteOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione um cliente!");
            } else {

                int create = pst.executeUpdate();

                if (create > 0) {

                    reset_fields();
                    JOptionPane.showMessageDialog(null, "OS criada com sucesso!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void delete_os() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta OS?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            limpa_produtos();
            limpa_servicos();
            table_read_products();

            String sql = "delete from tbos where id_os =?";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdOs.getText());

                int apagado = pst.executeUpdate();
                if (apagado > 0) {

                    reset_fields();
                    JOptionPane.showMessageDialog(null, "OS excluída com sucesso!");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void delete_os_closeframe() {

        int confirma = JOptionPane.showConfirmDialog(null, "Deseja salvar a OS em andamento?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.NO_OPTION) {

            limpa_produtos();
            limpa_servicos();
            table_read_products();
            String sql = "delete from tbos where id_os =?";

            try {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtIdOs.getText());

                int apagado = pst.executeUpdate();
                if (apagado > 0) {

                    reset_fields();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void update_os() {

        String sql = "update tbos set atendimento_os = ?, tipo_os = ?, status_os = ?, relato_os = ?, solucao_os = ?, tecnico = ?, valorservicos_os = ?, valorprodutos_os = ?, valordesconto_os = ?, valorparcial_os = ?, valortotal_os = ?, condicoes_avista_os = ?, condicoes_aprazo_os = ?, condicao_cheque = ?, condicao_cartao = ?, status_financeiro_os = ? where id_os = ?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, cboAtendimentoOs.getSelectedItem().toString());

            if (rbtnOS.isSelected()) {
                pst.setString(2, "Ordem de Serviço");
            } else {
                pst.setString(2, "Orçamento");
            }

            pst.setString(3, cboStatusOs.getSelectedItem().toString());
            pst.setString(4, txtRelatoOs.getText());
            pst.setString(5, txtSolucaoOs.getText());
            pst.setString(6, cboTecnicoOs.getSelectedItem().toString());
            pst.setString(7, lblValServicosOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(8, lblValProdutosOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(9, txtDescontoOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(10, lblValTotParcialOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(11, lblValTotFinalOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(12, spinDescontoFormaPgAvistaOs.getValue().toString());
            pst.setString(13, spinQuantParcelasFormaPgPrazoOs.getValue().toString());

            if (rbtnFormaPgCredito.isSelected()) {
                condicao_cartao = "1";
            }
            if (rbtnFormaPgCheque.isSelected()) {
                condicao_cheque = "1";
            }

            pst.setString(14, condicao_cheque);
            pst.setString(15, condicao_cartao);

            pst.setString(16, cboStatusFinanceiroFechamentoOs.getSelectedItem().toString());
            pst.setString(17, txtIdOs.getText());

            if (txtIdOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione uma OS");
            } else {

                int update = pst.executeUpdate();

                if (update > 0) {

                    reset_fields();
                    JOptionPane.showMessageDialog(null, "OS salva com sucesso!");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    
        public void update_os_nomsg() {

        String sql = "update tbos set atendimento_os = ?, tipo_os = ?, status_os = ?, relato_os = ?, solucao_os = ?, tecnico = ?, valorservicos_os = ?, valorprodutos_os = ?, valordesconto_os = ?, valorparcial_os = ?, valortotal_os = ?, condicoes_avista_os = ?, condicoes_aprazo_os = ?, condicao_cheque = ?, condicao_cartao = ?, status_financeiro_os = ? where id_os = ?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, cboAtendimentoOs.getSelectedItem().toString());

            if (rbtnOS.isSelected()) {
                pst.setString(2, "Ordem de Serviço");
            } else {
                pst.setString(2, "Orçamento");
            }

            pst.setString(3, cboStatusOs.getSelectedItem().toString());
            pst.setString(4, txtRelatoOs.getText());
            pst.setString(5, txtSolucaoOs.getText());
            pst.setString(6, cboTecnicoOs.getSelectedItem().toString());
            pst.setString(7, lblValServicosOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(8, lblValProdutosOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(9, txtDescontoOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(10, lblValTotParcialOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(11, lblValTotFinalOs.getText().replaceAll("\\.", "").replace(",", "."));
            pst.setString(12, spinDescontoFormaPgAvistaOs.getValue().toString());
            pst.setString(13, spinQuantParcelasFormaPgPrazoOs.getValue().toString());

            if (rbtnFormaPgCredito.isSelected()) {
                condicao_cartao = "1";
            }
            if (rbtnFormaPgCheque.isSelected()) {
                condicao_cheque = "1";
            }

            pst.setString(14, condicao_cheque);
            pst.setString(15, condicao_cartao);

            pst.setString(16, cboStatusFinanceiroFechamentoOs.getSelectedItem().toString());
            pst.setString(17, txtIdOs.getText());

            if (txtIdOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione uma OS");
            } else {

                pst.executeUpdate();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public static String convertDate(Date dtConsulta) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("pt", "BR"));
            return formatter.format(dtConsulta);
        } catch (Exception e) {
            return null;
        }
    }

    public void update_data_fechamento() {

        String sql = "update tbos set fechamento_os = ? where id_os = ?";
        //java.util.Date data = new java.util.Date();
        //java.sql.Date dataSql = new java.sql.Date(data.getTime())

        dataFechamento.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        //TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        java.util.Date data = new java.util.Date();
        java.sql.Date dataSql = new java.sql.Date(data.getTime());

        //data.setDate(data.getDate() + 1);
        String datateste = "2018-08-25";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setTimestamp(1, new java.sql.Timestamp(data.getTime()));
            //System.out.println(dataFechamento.format(data));

            pst.setString(2, txtIdOs.getText());

            if (txtIdOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione uma OS");
            } else {

                pst.executeUpdate();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void salva_alteracoes_os() {

        int confirma = JOptionPane.showConfirmDialog(null, "Deseja salvar as alterações?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "update tbos set atendimento_os = ?, tipo_os = ?, status_os = ?, relato_os = ?, solucao_os = ?, tecnico = ?, condicoes_avista_os = ?, condicoes_aprazo_os = ?, condicao_cheque = ?, condicao_cartao = ?, status_financeiro_os = ? where id_os = ?";

            try {

                pst = conexao.prepareStatement(sql);

                pst.setString(1, cboAtendimentoOs.getSelectedItem().toString());

                if (rbtnOS.isSelected()) {
                    pst.setString(2, "Ordem de Serviço");
                } else {
                    pst.setString(2, "Orçamento");
                }

                pst.setString(3, cboStatusOs.getSelectedItem().toString());
                pst.setString(4, txtRelatoOs.getText());
                pst.setString(5, txtSolucaoOs.getText());
                pst.setString(6, cboTecnicoOs.getSelectedItem().toString());
                /*
                pst.setString(7, lblValServicosOs.getText().replaceAll("\\.", "").replace(",", "."));
                pst.setString(8, lblValProdutosOs.getText().replaceAll("\\.", "").replace(",", "."));
                pst.setString(9, txtDescontoOs.getText().replaceAll("\\.", "").replace(",", "."));
                pst.setString(10, lblValTotParcialOs.getText().replaceAll("\\.", "").replace(",", "."));
                pst.setString(11, lblValTotFinalOs.getText().replaceAll("\\.", "").replace(",", "."));
                 */
                pst.setString(7, spinDescontoFormaPgAvistaOs.getValue().toString());
                pst.setString(8, spinQuantParcelasFormaPgPrazoOs.getValue().toString());

                if (rbtnFormaPgCredito.isSelected()) {
                    condicao_cartao = "1";
                }
                if (rbtnFormaPgCheque.isSelected()) {
                    condicao_cheque = "1";
                }

                pst.setString(9, condicao_cheque);
                pst.setString(10, condicao_cartao);

                pst.setString(11, cboStatusFinanceiroFechamentoOs.getSelectedItem().toString());
                pst.setString(12, txtIdOs.getText());

                if (txtIdOs.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione uma OS");
                } else {

                    int update = pst.executeUpdate();

                    if (update > 0) {

                        //reset_fields ();
                        JOptionPane.showMessageDialog(null, "OS salva com sucesso!");
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }

    public void check_os() {

        String sql = "select atendimento_os, tipo_os, status_os, relato_os, solucao_os, tecnico, valorservicos_os, valorprodutos_os, valordesconto_os, valorparcial_os, valortotal_os, condicoes_avista_os, condicoes_aprazo_os, condicao_cheque, condicao_cartao, status_financeiro_os from tbos where id_os = ?";

        String atendimento_os = null;
        String tipo_os = null;
        String status_os = null;
        String relato_os = null;
        String solucao_os = null;
        String tecnico = null;
        String condicoes_avista_os = null;
        String condicoes_aprazo_os = null;
        String status_financeiro_os = null;
        String condicao_cheque1 = "0";
        String condicao_cartao1 = "0";
        String check_cartao = "0";
        String check_cheque = "0";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdOs.getText());
            rs = pst.executeQuery();

            if (rs.next()) {

                atendimento_os = rs.getString(1);
                tipo_os = rs.getString(2);
                status_os = rs.getString(3);
                relato_os = rs.getString(4);
                solucao_os = rs.getString(5);
                tecnico = rs.getString(6);

                condicoes_avista_os = rs.getString(12);
                condicoes_aprazo_os = rs.getString(13);
                condicao_cheque1 = rs.getString(14);
                condicao_cartao1 = rs.getString(15);
                status_financeiro_os = rs.getString(16);

            }

        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }

        try {

            boolean check1 = atendimento_os.equals(cboAtendimentoOs.getSelectedItem().toString());

            boolean check2;
            if (rbtnOS.isSelected()) {
                check2 = tipo_os.equals("Ordem de Serviço");
            } else {
                check2 = tipo_os.equals("Orçamento");
            }

            boolean check3 = status_os.equals(cboStatusOs.getSelectedItem().toString());
            boolean check4 = relato_os.equals(txtRelatoOs.getText());
            boolean check5 = solucao_os.equals(txtSolucaoOs.getText());
            boolean check6 = tecnico.equals(cboTecnicoOs.getSelectedItem().toString());

            /*
            Double get_valorservicos_os = Double.parseDouble(lblValServicosOs.getText());
            Double get_valorprodutos_os = Double.parseDouble(lblValProdutosOs.getText());
            Double get_valordesconto_os = Double.parseDouble(spinDescontoFormaPgAvistaOs.getValue().toString());
            Double get_valorparcial_os = Double.parseDouble(lblValTotParcialOs.getText());
            Double get_valortotal_os = Double.parseDouble(lblValTotFinalOs.getText());
             */
            if (rbtnFormaPgCredito.isSelected()) {
                check_cartao = "1";
            }
            if (rbtnFormaPgCheque.isSelected()) {
                check_cheque = "1";
            }

            boolean check12 = condicoes_avista_os.equals(spinDescontoFormaPgAvistaOs.getValue().toString());
            boolean check13 = condicoes_aprazo_os.equals(spinQuantParcelasFormaPgPrazoOs.getValue().toString());
            boolean check14 = condicao_cheque1.equals(check_cheque);
            boolean check15 = condicao_cartao1.equals(check_cartao);
            boolean check16 = status_financeiro_os.equals(cboStatusFinanceiroFechamentoOs.getSelectedItem().toString());

            if (check1 && check2 && check3 && check4 && check5 && check6 && check12 && check13 && check14 && check15 && check16) {
                campos_alterados = false;
            } else {
                campos_alterados = true;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

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
        atualiza_valores();
    }

    public void deleta_produto() {

        String sql = "delete from os_recebe_produto where id_os =? and id_prod = ?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtIdOs.getText());

            int setar = tblProdutosOs.getSelectedRow();
            pst.setString(2, tblProdutosOs.getModel().getValueAt(setar, 0).toString());

            if (txtIdOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione um cliente!");
            } else {

                pst.executeUpdate();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void deleta_servico() {

        String sql = "delete from os_recebe_servico where id_os =? and id_serv = ?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtIdOs.getText());

            int setar = tblServicosOs.getSelectedRow();
            pst.setString(2, tblServicosOs.getModel().getValueAt(setar, 0).toString());

            if (txtIdOs.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione um cliente!");
            } else {

                pst.executeUpdate();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

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
        atualiza_valores();
    }

    public void limpa_produtos() {

        String sql = "delete from os_recebe_produto where id_os =?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtIdOs.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void limpa_servicos() {

        String sql = "delete from os_recebe_servico where id_os =?";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtIdOs.getText());

            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void reset_prod_serv_tables() {

        tblProdutosOs.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String[]{
                    "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));

        tblServicosOs.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String[]{
                    "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));

    }

    public void somatot_servicos() {

        String sql = "select sum(s.preco_serv * ors.quantidade) as 'Preço Total' from tbos join os_recebe_servico ors on tbos.id_os = ors.id_os join tbservicos s on s.id_serv = ors.id_serv where tbos.id_os =?  group by tbos.id_os;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdOs.getText());
            rs = pst.executeQuery();

            if (rs.next()) {

                somaservicos = rs.getDouble(1);
                lblValServicosOs.setText(df.format(somaservicos));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void somatot_produtos() {

        String sql = "select sum(p.preco_prod * op.quantidade) as 'Preço Total' from tbos join os_recebe_produto op on tbos.id_os = op.id_os join tbprodutos p on p.id_prod = op.id_prod where tbos.id_os = ? group by tbos.id_os;";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtIdOs.getText());
            rs = pst.executeQuery();

            if (rs.next()) {

                somaprodutos = rs.getDouble(1);

                lblValProdutosOs.setText(df.format(somaprodutos));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void atualiza_valores() {

        somatot_produtos();
        somatot_servicos();

        double totparc = somaservicos + somaprodutos;

        if (totparc > 1) {
            lblValTotParcialOs.setText(df.format(totparc));
        } else {
            lblValTotParcialOs.setText("0" + df.format(totparc));
        }

        String descontoporctxt = spinDescontoFormaPgAvistaOs.getValue().toString();
        double descontoporc = Double.parseDouble(descontoporctxt);

        //System.out.println(descontoporc);
        double desconto = (descontoporc / 100) * totparc;
        //System.out.println(desconto);

        if (desconto > 1) {
            txtDescontoOs.setText(df.format(desconto));
        } else {
            txtDescontoOs.setText("0" + df.format(desconto));
        }

        double totfinal = totparc - desconto;
        //System.out.println(totfinal);

        //System.out.println(totfinal);
        if (totfinal > 1) {
            lblValTotFinalOs.setText(df.format(totfinal));
        } else {
            lblValTotFinalOs.setText("0" + df.format(totfinal));
        }

        //System.out.println(df.format(totfinal));
        somaprodutos = 0;
        somaservicos = 0;
        totparc = 0;
        descontoporc = 0;
        desconto = 0;
        totfinal = 0;

    }

    public void string_formas_pg() {

        try {

            String tiravirgula = lblValTotFinalOs.getText().replaceAll("\\.", "").replace(",", ".");
            String tiravirgula_totparcial = lblValTotParcialOs.getText().replaceAll("\\.", "").replace(",", ".");
            Double referencia = 0.01;

            if (!lblValTotFinalOs.getText().isEmpty()) {

                Double conversao = Double.parseDouble(tiravirgula);

                if (conversao > referencia) {

                    Double parcela = Double.parseDouble(tiravirgula_totparcial);
                    String quantidade_parcelas = spinQuantParcelasFormaPgPrazoOs.getValue().toString();
                    Double quantidade_parcelas_d = Double.parseDouble(quantidade_parcelas);
                    Double valor_parcela = parcela / quantidade_parcelas_d;
                    String valor_parcela_s = (df.format(valor_parcela));
                    int valortotalint = (int) Math.round(conversao);

                    String formaspg1 = "Á vista R$ " + lblValTotFinalOs.getText();
                    String formaspg2 = "ou em " + spinQuantParcelasFormaPgPrazoOs.getValue().toString() + "x de R$ " + valor_parcela_s;

                    //System.out.println(formaspg);
                    TxtFormasPg1.setText(formaspg1);
                    TxtFormasPg2.setText(formaspg2);

                }
            }
        } catch (Exception e) {
        }
    }

    public void cancela_edicao() {

        btnEncerrarOs.setEnabled(false);
        rbtnFechamentoPgAvista.setEnabled(false);
        rbtnFechamentoPgPrazo.setEnabled(false);
        cboStatusFinanceiroFechamentoOs.setEnabled(false);
        cboTipoPagamentoFechamentoOs.setEnabled(false);
        spnParcelasPgFechamentoOs.setEnabled(false);
        txtComentariosFechamentoOs.setEditable(false);
        //txtDescontoOs.setEditable(false);
        spinDescontoFormaPgAvistaOs.setEnabled(false);
        spinQuantParcelasFormaPgPrazoOs.setEnabled(false);
        rbtnFormaPgCredito.setEnabled(false);
        rbtnFormaPgCheque.setEnabled(false);
        //btnAddAssistenciaOs.setEnabled(false);
        //btnApagaAssistenciaOs.setEnabled(false);
        txtRelatoOs.setEditable(false);
        txtSolucaoOs.setEditable(false);
        btnAddProdutosOs.setEnabled(false);
        btnApagaProdutosOs.setEnabled(false);
        btnAddServicosOs.setEnabled(false);
        btnApagaServicosOs.setEnabled(false);
        btnEditaOs.setEnabled(false);
        cboStatusOs.setEnabled(false);
        cboTecnicoOs.setEnabled(false);
        cboAtendimentoOs.setEnabled(false);
        rbtnOrc.setEnabled(false);
        rbtnOS.setEnabled(false);

    }

    public void habilita_edicao() {

        btnEncerrarOs.setEnabled(true);
        rbtnFechamentoPgAvista.setEnabled(true);
        rbtnFechamentoPgPrazo.setEnabled(true);
        cboStatusFinanceiroFechamentoOs.setEnabled(true);
        cboTipoPagamentoFechamentoOs.setEnabled(true);
        spnParcelasPgFechamentoOs.setEnabled(true);
        txtComentariosFechamentoOs.setEditable(true);
        //txtDescontoOs.setEditable(true);
        spinDescontoFormaPgAvistaOs.setEnabled(true);
        spinQuantParcelasFormaPgPrazoOs.setEnabled(true);
        rbtnFormaPgCredito.setEnabled(true);
        rbtnFormaPgCheque.setEnabled(true);
        //btnAddAssistenciaOs.setEnabled(true);
        //btnApagaAssistenciaOs.setEnabled(true);
        txtRelatoOs.setEditable(true);
        txtSolucaoOs.setEditable(true);
        btnAddProdutosOs.setEnabled(true);
        btnApagaProdutosOs.setEnabled(true);
        btnAddServicosOs.setEnabled(true);
        btnApagaServicosOs.setEnabled(true);
        btnEditaOs.setEnabled(true);
        cboStatusOs.setEnabled(true);
        cboTecnicoOs.setEnabled(true);
        cboAtendimentoOs.setEnabled(true);
        rbtnOrc.setEnabled(true);
        rbtnOS.setEnabled(true);

    }

    @SuppressWarnings("unchecked")
    public void imprime_os() {
       

        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão da OS?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            try {

                HashMap filtro = new HashMap();
                filtro.put("os", Integer.parseInt(txtIdOs.getText()));

                JasperPrint print = JasperFillManager.fillReport("C:\\Users\\Consistel\\Documents\\NetBeansProjects\\consistel\\src\\MyReports\\os.jasper", filtro, conexao);

                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

    }
    
    @SuppressWarnings("unchecked")
     public void imprime_orc() {
         

        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão do Orçamento?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            try {

                HashMap filtro = new HashMap();
                filtro.put("os", Integer.parseInt(txtIdOs.getText()));

                JasperPrint print = JasperFillManager.fillReport("C:\\Users\\Consistel\\Documents\\NetBeansProjects\\consistel\\src\\MyReports\\orcamento.jasper", filtro, conexao);

                JasperViewer.viewReport(print, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panItensOs = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProdutosOs = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblServicosOs = new javax.swing.JTable();
        btnAddServicosOs = new javax.swing.JButton();
        btnApagaServicosOs = new javax.swing.JButton();
        btnAddProdutosOs = new javax.swing.JButton();
        btnApagaProdutosOs = new javax.swing.JButton();
        panInfoOs = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRelatoOs = new javax.swing.JTextArea();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSolucaoOs = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        panFinanceiroOs = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblValProdutosOs = new javax.swing.JLabel();
        lblValServicosOs = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lblValTotParcialOs = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtDescontoOs = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblValTotFinalOs = new javax.swing.JLabel();
        TxtFormasPg1 = new javax.swing.JLabel();
        TxtFormasPg2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        spinDescontoFormaPgAvistaOs = new javax.swing.JSpinner();
        spinQuantParcelasFormaPgPrazoOs = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        rbtnFormaPgCredito = new javax.swing.JRadioButton();
        rbtnFormaPgCheque = new javax.swing.JRadioButton();
        panFechamentoOs = new javax.swing.JPanel();
        cboStatusFinanceiroFechamentoOs = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cboTipoPagamentoFechamentoOs = new javax.swing.JComboBox<>();
        txtDataFechamentoOs = new javax.swing.JTextField();
        btnEncerrarOs = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtComentariosFechamentoOs = new javax.swing.JTextArea();
        rbtnFechamentoPgAvista = new javax.swing.JRadioButton();
        rbtnFechamentoPgPrazo = new javax.swing.JRadioButton();
        lblParcelas = new javax.swing.JLabel();
        spnParcelasPgFechamentoOs = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        cboStatusOs = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtDataAberturaOs = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtIdOs = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cboAtendimentoOs = new javax.swing.JComboBox<>();
        btnBuscaClienteOs = new javax.swing.JButton();
        txtClienteOs = new javax.swing.JTextField();
        txtEnderecoClienteOs = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cboTecnicoOs = new javax.swing.JComboBox<>();
        btnApagaOs = new javax.swing.JButton();
        btnBuscaOs = new javax.swing.JButton();
        btnEditaOs = new javax.swing.JButton();
        btnImprimeOs = new javax.swing.JButton();
        rbtnOS = new javax.swing.JRadioButton();
        rbtnOrc = new javax.swing.JRadioButton();
        btnLimparCampos = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(600, 456));

        panItensOs.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel23.setText("Produtos");

        tblProdutosOs.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblProdutosOs);

        jLabel24.setText("Serviços");

        tblServicosOs.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tblServicosOs);

        btnAddServicosOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/add32.png"))); // NOI18N
        btnAddServicosOs.setToolTipText("");
        btnAddServicosOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddServicosOsActionPerformed(evt);
            }
        });

        btnApagaServicosOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/delete32.png"))); // NOI18N
        btnApagaServicosOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagaServicosOsActionPerformed(evt);
            }
        });

        btnAddProdutosOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/add32.png"))); // NOI18N
        btnAddProdutosOs.setToolTipText("");
        btnAddProdutosOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProdutosOsActionPerformed(evt);
            }
        });

        btnApagaProdutosOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/delete32.png"))); // NOI18N
        btnApagaProdutosOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagaProdutosOsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panItensOsLayout = new javax.swing.GroupLayout(panItensOs);
        panItensOs.setLayout(panItensOsLayout);
        panItensOsLayout.setHorizontalGroup(
            panItensOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panItensOsLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panItensOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panItensOsLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(406, 406, 406)
                        .addComponent(btnAddServicosOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApagaServicosOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panItensOsLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddProdutosOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApagaProdutosOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panItensOsLayout.setVerticalGroup(
            panItensOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panItensOsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panItensOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23)
                    .addGroup(panItensOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAddProdutosOs)
                        .addComponent(btnApagaProdutosOs)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(panItensOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panItensOsLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel24))
                    .addComponent(btnApagaServicosOs)
                    .addComponent(btnAddServicosOs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("Itens", panItensOs);

        panInfoOs.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtRelatoOs.setColumns(20);
        txtRelatoOs.setLineWrap(true);
        txtRelatoOs.setRows(5);
        jScrollPane1.setViewportView(txtRelatoOs);

        jLabel35.setText("Solução");

        txtSolucaoOs.setColumns(20);
        txtSolucaoOs.setLineWrap(true);
        txtSolucaoOs.setRows(5);
        jScrollPane4.setViewportView(txtSolucaoOs);

        jLabel4.setText("Relato");

        javax.swing.GroupLayout panInfoOsLayout = new javax.swing.GroupLayout(panInfoOs);
        panInfoOs.setLayout(panInfoOsLayout);
        panInfoOsLayout.setHorizontalGroup(
            panInfoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInfoOsLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(panInfoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(panInfoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel35))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        panInfoOsLayout.setVerticalGroup(
            panInfoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInfoOsLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Informações", panInfoOs);

        panFinanceiroOs.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Totais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel9.setText("Produtos");

        jLabel10.setText("Serviços");

        jLabel12.setText("R$");

        jLabel20.setText("R$");

        lblValProdutosOs.setText("0");

        lblValServicosOs.setText("0");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel26.setText("Total Parcial");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel27.setText("R$");

        lblValTotParcialOs.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblValTotParcialOs.setText("0");

        jLabel29.setText("_________________________");

        jLabel30.setText("Desconto (R$)");

        txtDescontoOs.setEditable(false);
        txtDescontoOs.setText("0");

        jLabel31.setText("_________________________");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel32.setText("Total Final");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel33.setText("R$");

        lblValTotFinalOs.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblValTotFinalOs.setText("0");
        lblValTotFinalOs.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                lblValTotFinalOsPropertyChange(evt);
            }
        });

        TxtFormasPg1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        TxtFormasPg2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel26))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblValServicosOs))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblValProdutosOs))
                                    .addComponent(txtDescontoOs, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblValTotParcialOs)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblValTotFinalOs))
                            .addComponent(jLabel31)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtFormasPg1)
                            .addComponent(TxtFormasPg2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(lblValProdutosOs))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel20)
                    .addComponent(lblValServicosOs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addGap(7, 7, 7)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(lblValTotParcialOs))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtDescontoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(lblValTotFinalOs))
                .addGap(50, 50, 50)
                .addComponent(TxtFormasPg1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtFormasPg2)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel15.setText("Desconto á Vista (%)");

        jLabel16.setText("Á Prazo em até");

        spinDescontoFormaPgAvistaOs.setModel(new javax.swing.SpinnerNumberModel(5, 0, 50, 1));
        spinDescontoFormaPgAvistaOs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinDescontoFormaPgAvistaOsStateChanged(evt);
            }
        });

        spinQuantParcelasFormaPgPrazoOs.setModel(new javax.swing.SpinnerNumberModel(3, 1, 12, 1));
        spinQuantParcelasFormaPgPrazoOs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinQuantParcelasFormaPgPrazoOsStateChanged(evt);
            }
        });

        jLabel13.setText("vezes");

        rbtnFormaPgCredito.setSelected(true);
        rbtnFormaPgCredito.setText("Cartão de crédito");

        rbtnFormaPgCheque.setSelected(true);
        rbtnFormaPgCheque.setText("Cheque");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinQuantParcelasFormaPgPrazoOs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addGap(16, 16, 16))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtnFormaPgCheque)
                            .addComponent(rbtnFormaPgCredito))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinDescontoFormaPgAvistaOs)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(spinDescontoFormaPgAvistaOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(spinQuantParcelasFormaPgPrazoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnFormaPgCredito)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnFormaPgCheque)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panFinanceiroOsLayout = new javax.swing.GroupLayout(panFinanceiroOs);
        panFinanceiroOs.setLayout(panFinanceiroOsLayout);
        panFinanceiroOsLayout.setHorizontalGroup(
            panFinanceiroOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFinanceiroOsLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        panFinanceiroOsLayout.setVerticalGroup(
            panFinanceiroOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFinanceiroOsLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(panFinanceiroOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Financeiro", panFinanceiroOs);

        panFechamentoOs.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        cboStatusFinanceiroFechamentoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A Receber", "Pago" }));

        jLabel14.setText("Status financeiro");

        jLabel7.setText("Data de fechamento");

        jLabel8.setText("Comentarios fechamento");

        jLabel11.setText("Tipo de pagamento");

        cboTipoPagamentoFechamentoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Débito", "Cheque", "Depósito" }));

        txtDataFechamentoOs.setEditable(false);

        btnEncerrarOs.setText("Encerrar OS");
        btnEncerrarOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncerrarOsActionPerformed(evt);
            }
        });

        txtComentariosFechamentoOs.setColumns(20);
        txtComentariosFechamentoOs.setLineWrap(true);
        txtComentariosFechamentoOs.setRows(5);
        jScrollPane5.setViewportView(txtComentariosFechamentoOs);

        buttonGroup2.add(rbtnFechamentoPgAvista);
        rbtnFechamentoPgAvista.setText("Á Vista");
        rbtnFechamentoPgAvista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnFechamentoPgAvistaActionPerformed(evt);
            }
        });

        buttonGroup2.add(rbtnFechamentoPgPrazo);
        rbtnFechamentoPgPrazo.setText("A Prazo");
        rbtnFechamentoPgPrazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnFechamentoPgPrazoActionPerformed(evt);
            }
        });

        lblParcelas.setText("Parcelas");

        spnParcelasPgFechamentoOs.setModel(new javax.swing.SpinnerNumberModel(3, 1, 12, 1));

        javax.swing.GroupLayout panFechamentoOsLayout = new javax.swing.GroupLayout(panFechamentoOs);
        panFechamentoOs.setLayout(panFechamentoOsLayout);
        panFechamentoOsLayout.setHorizontalGroup(
            panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFechamentoOsLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panFechamentoOsLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(rbtnFechamentoPgAvista)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbtnFechamentoPgPrazo))
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panFechamentoOsLayout.createSequentialGroup()
                        .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panFechamentoOsLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDataFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panFechamentoOsLayout.createSequentialGroup()
                                .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(cboStatusFinanceiroFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboTipoPagamentoFechamentoOs, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEncerrarOs)
                            .addComponent(spnParcelasPgFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblParcelas))))
                .addGap(67, 67, 67))
        );
        panFechamentoOsLayout.setVerticalGroup(
            panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFechamentoOsLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDataFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEncerrarOs))
                .addGap(18, 18, 18)
                .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtnFechamentoPgAvista)
                    .addComponent(rbtnFechamentoPgPrazo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panFechamentoOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panFechamentoOsLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboTipoPagamentoFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panFechamentoOsLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboStatusFinanceiroFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFechamentoOsLayout.createSequentialGroup()
                        .addComponent(lblParcelas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spnParcelasPgFechamentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
        );

        jTabbedPane1.addTab("Fechamento", panFechamentoOs);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setText("Status");

        cboStatusOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Autorizada", "Em andamento", "Aguardando peças", "Pendente", "Finalizada" }));

        jLabel18.setText("Cliente");

        jLabel19.setText("Endereço");

        txtDataAberturaOs.setEditable(false);

        jLabel21.setText("Nº da OS");

        jLabel1.setText("Data de abertura");

        txtIdOs.setEditable(false);

        jLabel2.setText("Atendimento");

        cboAtendimentoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Externo", "Interno" }));

        btnBuscaClienteOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search16.png"))); // NOI18N
        btnBuscaClienteOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaClienteOsActionPerformed(evt);
            }
        });

        txtClienteOs.setEditable(false);

        txtEnderecoClienteOs.setEditable(false);

        jLabel6.setText("Técnico");

        cboTecnicoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Felipe Simões", "Paulo Santos", "Rafael Moreira" }));

        btnApagaOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/delete32.png"))); // NOI18N
        btnApagaOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagaOsActionPerformed(evt);
            }
        });

        btnBuscaOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/search.png"))); // NOI18N
        btnBuscaOs.setToolTipText("");
        btnBuscaOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaOsActionPerformed(evt);
            }
        });

        btnEditaOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/save32.png"))); // NOI18N
        btnEditaOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditaOsActionPerformed(evt);
            }
        });

        btnImprimeOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/consistel/icones/print32.png"))); // NOI18N
        btnImprimeOs.setToolTipText("");
        btnImprimeOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimeOsActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnOS);
        rbtnOS.setText("Ordem de Serviço");
        rbtnOS.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtnOSItemStateChanged(evt);
            }
        });
        rbtnOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnOSActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnOrc);
        rbtnOrc.setText("Orçamento");
        rbtnOrc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtnOrcItemStateChanged(evt);
            }
        });
        rbtnOrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnOrcActionPerformed(evt);
            }
        });

        btnLimparCampos.setText("Limpar campos");
        btnLimparCampos.setToolTipText("");
        btnLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCamposActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(rbtnOS)
                        .addGap(18, 18, 18)
                        .addComponent(rbtnOrc))
                    .addComponent(txtEnderecoClienteOs, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtClienteOs, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscaClienteOs, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(txtIdOs, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtDataAberturaOs, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboAtendimentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel6)
                    .addComponent(cboTecnicoOs, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboStatusOs, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnBuscaOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditaOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApagaOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImprimeOs, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbtnOS)
                            .addComponent(rbtnOrc))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboAtendimentoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel21)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtIdOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtDataAberturaOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscaOs)
                            .addComponent(btnEditaOs)
                            .addComponent(btnApagaOs)
                            .addComponent(btnImprimeOs))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimparCampos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnBuscaClienteOs, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtClienteOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEnderecoClienteOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboStatusOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTecnicoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 50, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1349, 554);
    }// </editor-fold>//GEN-END:initComponents

    private void rbtnOrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnOrcActionPerformed

        //cboStatusOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"A Enviar", "Enviado", "Não Aprovado", "Aprovado"}));

    }//GEN-LAST:event_rbtnOrcActionPerformed

    private void rbtnOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnOSActionPerformed

        //cboStatusOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Autorizada", "Em andamento", "Aguardando peças", "Pendente", "Finalizada"}));

    }//GEN-LAST:event_rbtnOSActionPerformed

    private void btnBuscaClienteOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaClienteOsActionPerformed

        if (txtIdOs.getText().isEmpty()) {

            TelaBuscaClientes buscacli = new TelaBuscaClientes();
            buscacli.setVisible(true);

        } else {

            //delete_os_buscacli();
            TelaBuscaClientes buscacli = new TelaBuscaClientes();
            buscacli.setVisible(true);

        }

    }//GEN-LAST:event_btnBuscaClienteOsActionPerformed

    private void btnImprimeOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimeOsActionPerformed

        if (rbtnOS.isSelected()) {
            
            update_os_nomsg();
            imprime_os();
            
        } else {
            
            update_os_nomsg();
            imprime_orc();
            
        }
    }//GEN-LAST:event_btnImprimeOsActionPerformed

    private void btnBuscaOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaOsActionPerformed

        TelaBuscaOS busca_os = new TelaBuscaOS();
        busca_os.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscaOsActionPerformed

    private void btnApagaOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagaOsActionPerformed

        if (txtIdOs.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Selecione uma OS!");

        } else {

            delete_os();
        }


    }//GEN-LAST:event_btnApagaOsActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed

        reset_fields();
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

        if ((btnBuscaClienteOs.isEnabled()) && (!txtIdOs.getText().isEmpty())) {

            delete_os_closeframe();

        } else {

            if (!txtIdOs.getText().isEmpty()) {

                check_os();
                //System.out.println(campos_alterados);
                if (campos_alterados) {

                    salva_alteracoes_os();

                }

            }
        }


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCamposActionPerformed

        if ((btnBuscaClienteOs.isEnabled()) && (!txtIdOs.getText().isEmpty())) {

            delete_os_closeframe();

        } else {

            if (!txtIdOs.getText().isEmpty()) {

                check_os();

                //System.out.println(campos_alterados);
                if (campos_alterados) {

                    salva_alteracoes_os();

                }

            }
        }

        reset_fields();
    }//GEN-LAST:event_btnLimparCamposActionPerformed

    private void btnEditaOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditaOsActionPerformed

        update_os();
    }//GEN-LAST:event_btnEditaOsActionPerformed

    private void rbtnOrcItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtnOrcItemStateChanged

        cboStatusOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"A Enviar", "Enviado", "Não Aprovado", "Aprovado"}));
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnOrcItemStateChanged

    private void rbtnOSItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtnOSItemStateChanged
        // TODO add your handling code here:
        cboStatusOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Autorizada", "Em andamento", "Aguardando peças", "Pendente", "Finalizada"}));
        btnBuscaClienteOs.setEnabled(true);
    }//GEN-LAST:event_rbtnOSItemStateChanged

    private void rbtnFechamentoPgPrazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnFechamentoPgPrazoActionPerformed

        cboTipoPagamentoFechamentoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Crédito", "Cheque"}));
        spnParcelasPgFechamentoOs.setVisible(true);
        lblParcelas.setVisible(true);
    }//GEN-LAST:event_rbtnFechamentoPgPrazoActionPerformed

    private void rbtnFechamentoPgAvistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnFechamentoPgAvistaActionPerformed

        cboTipoPagamentoFechamentoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Dinheiro", "Débito", "Cheque", "Depósito"}));
        spnParcelasPgFechamentoOs.setVisible(false);
        lblParcelas.setVisible(false);
    }//GEN-LAST:event_rbtnFechamentoPgAvistaActionPerformed

    private void btnEncerrarOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncerrarOsActionPerformed

        rbtnOS.setSelected(true);
        cboStatusOs.setSelectedItem("Finalizada");
        cboStatusFinanceiroFechamentoOs.setSelectedItem("Pago");

        update_data_fechamento();
        update_os();
    }//GEN-LAST:event_btnEncerrarOsActionPerformed

    private void spinQuantParcelasFormaPgPrazoOsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinQuantParcelasFormaPgPrazoOsStateChanged
        string_formas_pg();
        // TODO add your handling code here:
    }//GEN-LAST:event_spinQuantParcelasFormaPgPrazoOsStateChanged

    private void spinDescontoFormaPgAvistaOsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinDescontoFormaPgAvistaOsStateChanged
        atualiza_valores();
    }//GEN-LAST:event_spinDescontoFormaPgAvistaOsStateChanged

    private void lblValTotFinalOsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_lblValTotFinalOsPropertyChange

        string_formas_pg();
        // TODO add your handling code here:
    }//GEN-LAST:event_lblValTotFinalOsPropertyChange

    private void btnApagaProdutosOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagaProdutosOsActionPerformed
        deleta_produto();
        table_read_products();
    }//GEN-LAST:event_btnApagaProdutosOsActionPerformed

    private void btnAddProdutosOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProdutosOsActionPerformed

        buscaprod.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddProdutosOsActionPerformed

    private void btnApagaServicosOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagaServicosOsActionPerformed

        deleta_servico();
        table_read_services();
    }//GEN-LAST:event_btnApagaServicosOsActionPerformed

    private void btnAddServicosOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddServicosOsActionPerformed
        buscaserv.setVisible(true);
    }//GEN-LAST:event_btnAddServicosOsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TxtFormasPg1;
    private javax.swing.JLabel TxtFormasPg2;
    private javax.swing.JButton btnAddProdutosOs;
    private javax.swing.JButton btnAddServicosOs;
    private javax.swing.JButton btnApagaOs;
    private javax.swing.JButton btnApagaProdutosOs;
    private javax.swing.JButton btnApagaServicosOs;
    public static javax.swing.JButton btnBuscaClienteOs;
    private javax.swing.JButton btnBuscaOs;
    private javax.swing.JButton btnEditaOs;
    private javax.swing.JButton btnEncerrarOs;
    private javax.swing.JButton btnImprimeOs;
    private javax.swing.JButton btnLimparCampos;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    public static javax.swing.JComboBox<String> cboAtendimentoOs;
    private javax.swing.JComboBox<String> cboStatusFinanceiroFechamentoOs;
    public static javax.swing.JComboBox<String> cboStatusOs;
    public static javax.swing.JComboBox<String> cboTecnicoOs;
    private javax.swing.JComboBox<String> cboTipoPagamentoFechamentoOs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblParcelas;
    private javax.swing.JLabel lblValProdutosOs;
    private javax.swing.JLabel lblValServicosOs;
    private javax.swing.JLabel lblValTotFinalOs;
    private javax.swing.JLabel lblValTotParcialOs;
    private javax.swing.JPanel panFechamentoOs;
    private javax.swing.JPanel panFinanceiroOs;
    private javax.swing.JPanel panInfoOs;
    private javax.swing.JPanel panItensOs;
    private javax.swing.JRadioButton rbtnFechamentoPgAvista;
    private javax.swing.JRadioButton rbtnFechamentoPgPrazo;
    private javax.swing.JRadioButton rbtnFormaPgCheque;
    private javax.swing.JRadioButton rbtnFormaPgCredito;
    public static javax.swing.JRadioButton rbtnOS;
    public static javax.swing.JRadioButton rbtnOrc;
    private javax.swing.JSpinner spinDescontoFormaPgAvistaOs;
    private javax.swing.JSpinner spinQuantParcelasFormaPgPrazoOs;
    private javax.swing.JSpinner spnParcelasPgFechamentoOs;
    private javax.swing.JTable tblProdutosOs;
    private javax.swing.JTable tblServicosOs;
    public static javax.swing.JTextField txtClienteOs;
    private javax.swing.JTextArea txtComentariosFechamentoOs;
    public static javax.swing.JTextField txtDataAberturaOs;
    private javax.swing.JTextField txtDataFechamentoOs;
    private javax.swing.JTextField txtDescontoOs;
    public static javax.swing.JTextField txtEnderecoClienteOs;
    public static javax.swing.JTextField txtIdOs;
    private javax.swing.JTextArea txtRelatoOs;
    private javax.swing.JTextArea txtSolucaoOs;
    // End of variables declaration//GEN-END:variables
}
