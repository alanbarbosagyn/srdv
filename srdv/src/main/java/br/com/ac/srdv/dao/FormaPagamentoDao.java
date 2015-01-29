package br.com.ac.srdv.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.ac.srdv.modelo.FormaPagamento;
import br.com.ac.srdv.util.Conexao;
import br.com.ac.srdv.util.Resultado;

public class FormaPagamentoDao {

	private Conexao con;

	public FormaPagamentoDao() {
		con = Conexao.getConexaoAtual();
	}

	public void createTable() {
		con = Conexao.getConexaoAtual();
		try {
			con.executar("CREATE TABLE RDV_FormaPagamento (id integer primary key)");
		} catch (Exception e) {
		}
		try {
			con.executar("ALTER TABLE RDV_FormaPagamento ADD COLUMN formaPagamento varchar(50)");
		} catch (Exception e) {
		}

	}

	public List<FormaPagamento> getTodasFormaPagamentos() throws Exception {

		Resultado resultado = con.consultar("SELECT * FROM RDV_FormaPagamento");

		List<FormaPagamento> lista = new ArrayList<FormaPagamento>();

		while (resultado.next())
			lista.add(parse(resultado));

		resultado.close();
		return lista;
	}

	private FormaPagamento parse(Resultado resultado) throws Exception {
		FormaPagamento forma = new FormaPagamento();
		forma.setId(resultado.getInt("id"));
		forma.setFormaPagamento(resultado.getString("formaPagamento"));
		return forma;
	}

}
